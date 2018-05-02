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

import com.gitee.hengboy.mybatis.enhance.mapper.EnhanceMapper;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 实体类工具类
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/8
 * Time：下午10:51
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class EntityHelper {
    /**
     * 缓存实体，由于一个接口需要重新装载多个SqlSource，每一个SqlSource都会去获取实体的类型
     * 缓存起来，第一次后读取从缓存内读取
     * key：Mapper类型全限定名
     * value：实体类型
     */
    private static Cache ENTITY_CLASS_CACHE = new SoftCache(new PerpetualCache("ENTITY_CLASS_CACHE"));

    /**
     * 根据Mapper实例对象提取泛型实体类型
     *
     * @param mapperClass Mapper实例对象类型
     * @return 实体的类型
     */
    public static Class<?> getEntityClass(Class<?> mapperClass) {
        // 从缓存内获取实体的类型
        Class<?> modelClass = getCacheEntityClass(mapperClass);
        if (!ObjectUtils.isEmpty(modelClass)) {
            return modelClass;
        }
        // 从Mapper泛型参数获取实体类型
        // 将实体类型写入到缓存
        return getMapperEntityClass(mapperClass);
    }

    /**
     * 从缓存内获取实体的类型
     *
     * @param mapperClass Mapper类型
     * @return 实体类型
     */
    private static Class<?> getCacheEntityClass(Class<?> mapperClass) {
        return (Class<?>) ENTITY_CLASS_CACHE.getObject(mapperClass.getName());
    }

    /**
     * 从Mapper泛型参数获取实体的类型并写入缓存内
     *
     * @param mapperClass Mapper类型
     * @return Mapper类型
     */
    private static Class<?> getMapperEntityClass(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        ParameterizedType target = null;
        for (Type type : types) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(EnhanceMapper.class)) {
                target = (ParameterizedType) type;
                break;
            }
        }
        Type[] parameters = target.getActualTypeArguments();
        Class<?> modelClass = (Class<?>) parameters[0];
        // 写入到缓存
        ENTITY_CLASS_CACHE.putObject(mapperClass.getName(), modelClass);
        return modelClass;
    }
}
