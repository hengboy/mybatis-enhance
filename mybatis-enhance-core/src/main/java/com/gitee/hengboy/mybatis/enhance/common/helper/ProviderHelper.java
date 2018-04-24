/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 恒宇少年
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gitee.hengboy.mybatis.enhance.common.helper;

import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import com.gitee.hengboy.mybatis.enhance.named.helper.NamedMethodHelper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import com.gitee.hengboy.mybatis.enhance.provider.named.MethodNamedCountProvider;
import com.gitee.hengboy.mybatis.enhance.provider.named.MethodNamedDeleteProvider;
import com.gitee.hengboy.mybatis.enhance.provider.named.MethodNamedFindProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * Provider工具类
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/8
 * Time：下午3:50
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class ProviderHelper {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ProviderHelper.class);

    /**
     * 根据Method反射方法对象获取该方法上配置的Provider注解并且实例化该Provider返回
     *
     * @param statementId MapperStatement的id值
     * @return
     * @throws EnhanceFrameworkException
     */
    public static BaseProvider getMethodProvider(String statementId) throws EnhanceFrameworkException {
        // 获取statementId对应的方法名称
        String methodName = StatementHelper.getMethodName(statementId);
        // 找到MappedStatementId对应的Mapper
        Class<?> mapperClass = StatementHelper.getMapperClass(statementId);
        // 获取实体类的类型
        Class<?> entityClass = EntityHelper.getEntityClass(mapperClass);
        // 获取Mapper内对应的方法
        Method method = MapperHelper.getMethod(methodName, mapperClass);

        // 查找Method上配置的@XxxProvider注解
        // 如果方法匹配规则查询的命名方式，获取对应的Provider
        Class<?> providerClass = NamedMethodHelper.isMatchNamed(methodName) ? findNamedProvider(methodName) : findProviderClass(method);

        // 并未配置@XxxProvider注解
        if (ObjectUtils.isEmpty(providerClass)) {
            throw new EnhanceFrameworkException("接口方法：" + mapperClass.getName() + "." + method.getName() + "，并未配置@XxxProvider注解，无法进行生成Provider具体类型的实例.");
        }

        // 构造函数初始化BaseProvider子类实例
        BaseProvider provider;
        // 构造函数初始化XxxProvider实例
        try {
            provider = (BaseProvider) providerClass.getConstructor(Class.class, Class.class, Method.class).newInstance(mapperClass, entityClass, method);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EnhanceFrameworkException("实例化：" + providerClass.getName() + " , 失败.");
        }
        return provider;
    }

    /**
     * 查找Method对应配置的@SelectProvider/@UpdateProvider等内的type类型全限定名
     *
     * @param method Method反射实例
     * @return
     */
    public static Class<?> findProviderClass(Method method) {
        Class<?> providerClass = null;
        // 查询方法
        if (method.isAnnotationPresent(SelectProvider.class)) {
            SelectProvider selectProvider = method.getAnnotation(SelectProvider.class);
            providerClass = selectProvider.type();
        }
        // 更新方法
        else if (method.isAnnotationPresent(UpdateProvider.class)) {
            UpdateProvider updateProvider = method.getAnnotation(UpdateProvider.class);
            providerClass = updateProvider.type();
        }
        // 删除方法
        else if (method.isAnnotationPresent(DeleteProvider.class)) {
            DeleteProvider deleteProvider = method.getAnnotation(DeleteProvider.class);
            providerClass = deleteProvider.type();
        }
        // 插入方法
        else if (method.isAnnotationPresent(InsertProvider.class)) {
            InsertProvider insertProvider = method.getAnnotation(InsertProvider.class);
            providerClass = insertProvider.type();
        }
        return providerClass;
    }

    /**
     * 根据方法名称规则匹配的方法名称
     * 获取不同的Provider
     *
     * @param methodName 方法名称
     * @return
     */
    public static Class<?> findNamedProvider(String methodName) {
        // 方法规则 - findByXxx
        if (NamedMethodHelper.isMatchFindNamed(methodName)) {
            return MethodNamedFindProvider.class;
        }
        // 方法规则 - countByXxx
        else if (NamedMethodHelper.isMatchCountNamed(methodName)) {
            return MethodNamedCountProvider.class;
        }
        // 方法规则 - deleteByXxx
        else if (NamedMethodHelper.isMatchDeleteNamed(methodName)) {
            return MethodNamedDeleteProvider.class;
        }
        return null;
    }
}
