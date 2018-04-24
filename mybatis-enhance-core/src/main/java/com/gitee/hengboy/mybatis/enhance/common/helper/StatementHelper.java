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

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
public final class StatementHelper {

    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(StatementHelper.class);

    /**
     * MappedStatement缓存
     * key：MappedStatement的id
     * value：MappedStatement实例
     */
    public static final Hashtable<String, MappedStatement> STATEMENT_CACHE = new Hashtable();
    /**
     * 缓存执行重新装载后的MappedStatement的Id
     * 防止重复装载
     */
    public static final List<String> STATEMENT_SQL_RELOAD_CACHE = new ArrayList();
    /**
     * MappedStatement的Id分隔符
     */
    private static String SPLIT = ".";
    /**
     * Mapper的接口类型缓存
     * key：Mapper类型，如:UserMapper.class
     * value：
     */
    private static Cache MAPPER_CLASS_CACHE = new SoftCache(new PerpetualCache("MAPPER_CLASS_CACHE"));

    /**
     * 根据MappedStatementId获取方法的名称
     *
     * @param msId MappedStatementId
     * @return
     */
    public static String getMethodName(String msId) {
        if (msId.indexOf(SPLIT) == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        }
        return msId.substring(msId.lastIndexOf(SPLIT) + 1);
    }

    /**
     * 根据MappedStatement的Id获取到对应Mapper的类型
     * 如：userMapper.insert，可以获取到userMapper对应的Class类型
     *
     * @param msId MappedStatement的Id
     * @return
     */
    public static Class<?> getMapperClass(String msId) {
        if (msId.indexOf(SPLIT) == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        }
        String mapperClassStr = msId.substring(0, msId.lastIndexOf(SPLIT));
        //由于一个接口中的每个方法都会进行下面的操作，因此缓存
        Class<?> mapperClass = (Class<?>) MAPPER_CLASS_CACHE.getObject(mapperClassStr);
        if (mapperClass != null) {
            return mapperClass;
        }
        ClassLoader[] classLoader = getClassLoaders();

        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                try {
                    mapperClass = Class.forName(mapperClassStr, true, cl);
                    if (mapperClass != null) {
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mapperClass == null) {
            throw new RuntimeException("class loaders failed to locate the class " + mapperClassStr);
        }
        MAPPER_CLASS_CACHE.putObject(mapperClassStr, mapperClass);
        return mapperClass;
    }

    /**
     * 获取当前线程的ClassLoader
     *
     * @return
     */
    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{Thread.currentThread().getContextClassLoader(), StatementHelper.class.getClassLoader()};
    }

}
