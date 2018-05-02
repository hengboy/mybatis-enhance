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

import com.gitee.hengboy.mybatis.enhance.common.annotation.Column;
import com.gitee.hengboy.mybatis.enhance.common.annotation.Id;
import com.gitee.hengboy.mybatis.enhance.common.enums.KeyGeneratorTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * columnStruct工具类
 *
 * @author yuqiyu
 * ===============================
 * Created with IntelliJ IDEA.
 * User：于起宇
 * Date：2017/8/14
 * Time：10:24
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class ColumnHelper {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ColumnHelper.class);

    /**
     * 获取列名
     *
     * @param field 字段对象
     * @return 获取列名
     */
    public static String getColumnName(Field field) {
        /**
         * 获取字段配置的@Column注解
         */
        Column column = field.getAnnotation(Column.class);
        /**
         * 默认列名为字段名称
         */
        String columnName = field.getName();
        /**
         * 存在配置的列名时使用配置内容
         */
        if (column != null && !StringUtils.isEmpty(column.name())) {
            columnName = column.name();
        }
        return columnName;
    }

    /**
     * 验证字段是否为主键字段
     *
     * @param field 字段对象
     * @return true：该字段为主键
     */
    public static boolean getIsPk(Field field) {
        /**
         * 获取字段配置的@Id注解
         */
        Id id = field.getAnnotation(Id.class);
        return id != null;
    }

    /**
     * 获取主键生成枚举类型
     *
     * @param field 字段
     * @return 获取主键生成策略
     */
    public static KeyGeneratorTypeEnum getGeneratorType(Field field) {
        if (getIsPk(field)) {
            Id id = field.getAnnotation(Id.class);
            return id.generatorType();
        }
        return null;
    }

    /**
     * 获取配置的是否映射
     *
     * @param field 字段对象
     * @return true：字段参与映射
     */
    public static boolean getMapping(Field field) {
        /**
         * 获取字段配置的@Column注解
         */
        Column column = field.getAnnotation(Column.class);
        return column.mapping();
    }

    /**
     * 验证是否insertable
     *
     * @param field 字段对象
     * @return true：获取参与插入映射
     */
    public static boolean getInsertable(Field field) {
        /**
         * 获取字段配置的@Column注解
         */
        Column column = field.getAnnotation(Column.class);
        return column.insertable();
    }

    /**
     * 验证是否updateable
     *
     * @param field 字段对象
     * @return true：参与更新映射
     */
    public static boolean getUpdateable(Field field) {
        /**
         * 获取字段配置的@Column注解
         */
        Column column = field.getAnnotation(Column.class);
        return column.updateable();
    }

    /**
     * 获取columnStruct映射信息
     *
     * @param field 字段对象
     * @return true：获取列映射对象
     */
    public static ColumnStruct getStruct(Field field) {
        /**
         * 构建ColumnStruct实例并返回
         */
        ColumnStruct struct = new ColumnStruct();
        //列名，采取大写方式
        struct.setColumnName(getColumnName(field).toUpperCase());
        struct.setFieldName(field.getName());
        struct.setPk(getIsPk(field));
        struct.setMapping(getMapping(field));
        struct.setInsertable(getInsertable(field));
        struct.setUpdateable(getUpdateable(field));
        struct.setGeneratorType(getGeneratorType(field));
        struct.setJavaType(field.getType());
        return struct;
    }

    /**
     * 从实体类中获取ColumnStruct集合
     *
     * @param entityClass 实体类型
     * @return 获取表内的列映射集合
     */
    static List<ColumnStruct> loadColumnStruct(TableStruct tableStruct, Class<?> entityClass) {
        List<ColumnStruct> structs = new ArrayList();
        /**
         * 读取实体类内 & 父类定义的所有字段
         */
        List<Field> fields = ReflectionHelper.getAllFields(entityClass);
        for (Field field : fields) {
            //仅仅遍历private修饰的字段进行映射
            if (!Modifier.isPrivate(field.getModifiers())) {
                logger.warn("load column struct：{}，faild，reason：{}", entityClass.getName() + "." + field.getName(), "only load private field!");
                continue;
            }
            //获取映射对象基本信息
            ColumnStruct struct = ColumnHelper.getStruct(field);
            /**
             * 如果是主键，将主键名称 & 主键字段名称设置到tableStruct内
             *
             */
            if (struct.isPk()) {
                tableStruct.setIdName(struct.getColumnName());
                tableStruct.setIdFieldName(field.getName());
            }
            structs.add(struct);
        }
        return structs;
    }
}
