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
package com.gitee.hengboy.mybatis.enhance;

import com.gitee.hengboy.mybatis.enhance.common.helper.ProviderHelper;
import com.gitee.hengboy.mybatis.enhance.common.helper.StatementHelper;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import com.gitee.hengboy.mybatis.enhance.named.helper.NamedMethodHelper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * MappedStatement装载类
 * 装载完成后重新装载SqlSource
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/7
 * Time：下午12:40
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class MappedStatementSupport {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(MappedStatementSupport.class);

    /**
     * 如果configuration内存在了该mapper
     * 1. 读取出所有的MappedStatement
     * 2. 遍历MappedStatement，通过id找到Mapper的类型以及对应Provider的类型以及方法
     * 3. 重新设置MappedStatement的SQLSource
     */
    public void support(Collection<Object> statements) {
        // 读取出当前mapper内的所有ms
        for (Object statementObject : statements) {
            // 类型不匹配跳过
            if (!(statementObject instanceof MappedStatement)) {
                continue;
            }
            MappedStatement statement = (MappedStatement) statementObject;
            // 添加到缓存内
            StatementHelper.STATEMENT_CACHE.put(statement.getId(), statement);
        }
        // 执行Statement重新装载方法
        // 该方法只会处理并未装载过的方法
        initStatementMapper();
    }


    /**
     * 处理MapperClass内的匹配NamedMethod的MappedStatement以及SqlSource
     *
     * @param configuration MyBatis配置对象
     * @param mapperClass   Mapper的类型
     */
    public void namedSupport(Configuration configuration, Class<?> mapperClass) {
        if (ObjectUtils.isEmpty(mapperClass)) {
            throw new EnhanceFrameworkException("处理NamedMethod MappedStatement时，参数[mapperClass]为必填项.");
        }
        // 获取Mapper内定义的方法不包含上级接口
        Method[] methods = mapperClass.getDeclaredMethods();

        // 遍历Mapper内定义的每一个方法
        for (Method method : methods) {
            // MappedStatement Id
            String statementId = mapperClass.getName() + "." + method.getName();
            // 如果匹配方法规则查询
            // 执行创建MappedStatement对象
            // 调用对应的Provider，执行重载SqlSource
            if (!configuration.hasStatement(statementId) && NamedMethodHelper.isMatchNamed(method.getName())) {
                logger.debug("Invoke Named Reload ： {} SqlSource.", statementId);
                MappedStatement statement = new MappedStatement.Builder(configuration, statementId, new DynamicSqlSource(configuration, new TextSqlNode("this is empty.")), getSqlCommandType(method.getName())).build();
                // 将MappedStatement添加到Configuration
                configuration.addMappedStatement(statement);
                // 会自动根据方法规则匹配对应的Provider
                BaseProvider provider = ProviderHelper.getMethodProvider(statementId);
                provider.invokeNamedProviderMethod(statement);
            }
        }
    }

    /**
     * 执行重新装载sqlSource
     */
    public void initStatementMapper() {
        Set<String> statementIds = StatementHelper.STATEMENT_CACHE.keySet();
        for (String statementId : statementIds) {
            // 根据编号获取MappedStatementId
            MappedStatement ms = getStatement(statementId);
            // 并未执行过装载
            // SQLSource是ProviderSqlSource类型
            if (!StatementHelper.STATEMENT_SQL_RELOAD_CACHE.contains(statementId)
                    && ms.getSqlSource() instanceof ProviderSqlSource) {
                logger.debug("Invoke Reload ： {} SqlSource.", ms.getId());
                /*
                 * 获取MappedStatementId对应的方法配置的@XxxProvider信息
                 * 初始化Provider实例对象
                 */
                BaseProvider provider = ProviderHelper.getMethodProvider(statementId);
                /*
                 *  1.执行MappedStatementId对应的方法，如：xxx.xxx.xxx.deleteOne，就会执行@DeleteProvider配置type类型内的delete方法
                 *  2.设置该方法的返回值
                 *  3.重新装载SqlSource
                 */
                provider.invokeProviderMethod(ms);
                // 写入到已加载的缓存集合内
                StatementHelper.STATEMENT_SQL_RELOAD_CACHE.add(statementId);
            }
        }
    }

    /**
     * 获取MappedStatement实例
     *
     * @param statementId statementId编号
     * @return
     */
    public static MappedStatement getStatement(String statementId) {
        return StatementHelper.STATEMENT_CACHE.get(statementId);
    }

    /**
     * 根据方法的名称获取SqlCommandType枚举类型
     *
     * @param methodName 符合方法规则的方法名称
     * @return
     */
    private static SqlCommandType getSqlCommandType(String methodName) {
        // 查询类型
        // 统计类型
        if (NamedMethodHelper.isMatchFindNamed(methodName) || NamedMethodHelper.isMatchCountNamed(methodName)) {
            return SqlCommandType.SELECT;
        }
        // 删除类型
        if (NamedMethodHelper.isMatchDeleteNamed(methodName)) {
            return SqlCommandType.DELETE;
        }
        return SqlCommandType.UNKNOWN;
    }
}
