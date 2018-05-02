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

import com.gitee.hengboy.mybatis.enhance.common.annotation.Table;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 数据库表、列信息工具类
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/7
 * Time：上午11:31
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public final class TableHelper {
    /**
     * 数据库表信息缓存
     * key：数据实体类型，如：UserEntity.class
     * value：TableStruct对象实例
     */
    private static Cache TABLE_CACHE = new SoftCache(new PerpetualCache("TABLE_CACHE"));
    /**
     * 数据库列信息缓存
     * key：数据实体类型，如：UserEntity.class
     * value：List<ColumnStruct>集合
     */
    private static Cache COLUMN_CACHE = new SoftCache(new PerpetualCache("COLUMN_CACHE"));

    /**
     * 根据Mapper类型获取数据表结构信息
     *
     * @param entityClass Mapper类型名称
     * @return 表结构映射工具类
     * @throws EnhanceFrameworkException 增强框架异常
     */
    public static TableStruct getTableStruct(Class<?> entityClass) throws EnhanceFrameworkException {
        // 从缓存内获取TableStruct
        Object object = TABLE_CACHE.getObject(entityClass.getName());
        // 缓存内存在直接返回
        if (!ObjectUtils.isEmpty(object)) {
            return (TableStruct) object;
        }
        // 读取TableStruct信息
        TableStruct tableStruct = loadTableStruct(entityClass);
        // 写入到缓存内
        TABLE_CACHE.putObject(entityClass.getName(), tableStruct);
        return tableStruct;
    }

    /**
     * 根据数据库表名称获取表内所有的列映射结构信息
     *
     * @param entityClass 实体类型
     * @return 实体类内的所有列映射集合
     * @throws EnhanceFrameworkException 增强框架异常
     */
    public static List<ColumnStruct> getColumnStruct(Class<?> entityClass) throws EnhanceFrameworkException {
        // 获取实体对应的列信息
        Object object = COLUMN_CACHE.getObject(entityClass.getName());
        if (!ObjectUtils.isEmpty(object)) {
            return (List<ColumnStruct>) object;
        }
        // 获取表结构
        TableStruct tableStruct = getTableStruct(entityClass);
        // 加载该表的所有列字段并且设置主键的信息到TableStruct
        List<ColumnStruct> columns = ColumnHelper.loadColumnStruct(tableStruct, entityClass);
        // 将表内的所有列信息写入到缓存内
        COLUMN_CACHE.putObject(entityClass.getName(), columns);
        return columns;
    }

    /**
     * 加载数据表的缓存结构信息
     *
     * @param entityClass 实体类型
     * @return 表格映射对象
     * @throws EnhanceFrameworkException 增强框架异常
     */
    static TableStruct loadTableStruct(Class<?> entityClass) throws EnhanceFrameworkException {

        // 不存在时，创建并初始化信息
        TableStruct tableStruct = new TableStruct();
        // 设置表名
        tableStruct.setTableName(TableHelper.getTableName(entityClass).toUpperCase());
        // 加载该表的所有列字段并且设置主键的信息到TableStruct
        List<ColumnStruct> columns = ColumnHelper.loadColumnStruct(tableStruct, entityClass);
        // 将表内的所有列信息写入到缓存内
        COLUMN_CACHE.putObject(entityClass.getName(), columns);
        return tableStruct;
    }

    /**
     * 获取表名称
     * 默认使用实体类驼峰命名
     * 如果存在@Table注解，使用该注解的name属性作为表名
     *
     * @param entityClass 数据实体类类型
     * @return 表名
     */
    public static String getTableName(Class<?> entityClass) {
        //非空处理
        if (entityClass == null) {
            throw new EnhanceFrameworkException("The parameter：[entityClass] is null，getTableName Faild!");
        }
        // 表名，默认使用类名驼峰命名
        String tableName = NameDefineHelper.camelToUnderline(entityClass.getSimpleName());
        //获取注解对象
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null && !StringUtils.isEmpty(table.name())) {
            tableName = table.name();
        }
        return tableName;
    }
}
