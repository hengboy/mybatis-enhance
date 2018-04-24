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

import com.gitee.hengboy.mybatis.enhance.exception.OrmCoreFrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Mapper接口工具类
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/7
 * Time：上午11:21
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public final class MapperHelper {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(MapperHelper.class);

    /**
     * 获取Mapper内的方法反射实例
     *
     * @param methodName  方法名称
     * @param mapperClass Mapper类型
     * @return
     */
    public static Method getMethod(String methodName, Class<?> mapperClass) {
        if (StringUtils.isEmpty(methodName) || ObjectUtils.isEmpty(mapperClass)) {
            throw new OrmCoreFrameworkException("无法获取方法反射实例，请检查传递的参数.");
        }
        // 获取Mapper内的所有方法以及继承的所有接口
        Method[] methods = mapperClass.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new OrmCoreFrameworkException("接口：" + mapperClass.getName() + "内并未找到定义的方法：" + methodName);
    }
}
