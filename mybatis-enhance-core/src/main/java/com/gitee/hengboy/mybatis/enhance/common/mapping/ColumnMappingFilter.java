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
package com.gitee.hengboy.mybatis.enhance.common.mapping;

import com.gitee.hengboy.mybatis.enhance.common.ConfigConstants;
import com.gitee.hengboy.mybatis.enhance.common.enums.KeyGeneratorTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import com.gitee.hengboy.mybatis.enhance.key.generator.UUIDKeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 列映射工具类
 * 获取数据实体内的列名称、列值映射列表
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：上午9:40
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class ColumnMappingFilter {
    /**
     * 列映射过滤的构建者对象
     */
    private ColumnMappingFilterBuilder mappingFilterBuilder;

    /**
     * 主键列映射对象
     */
    private ColumnStruct idColumnStruct;

    /**
     * 构造函数初始化构建者对象
     *
     * @param mappingFilterBuilder 列映射过滤构建者对象
     */
    public ColumnMappingFilter(ColumnMappingFilterBuilder mappingFilterBuilder) {
        this.mappingFilterBuilder = mappingFilterBuilder;
    }

    /**
     * 获取主键列结构映射对象
     *
     * @return
     */
    public ColumnStruct getIdColumnStruct() {
        for (ColumnStruct columnStruct : mappingFilterBuilder.getColumnStructList()) {
            if (columnStruct.isPk()) {
                this.idColumnStruct = columnStruct;
                return columnStruct;
            }
        }
        throw new EnhanceFrameworkException("并未查询到配置了@Id的字段.");
    }

    /**
     * 获取主键生成策略
     * <p>
     * 如果实体类内不设置主键生成策略，默认使用mybatis内的NoKeyGenerator
     *
     * @return
     * @see KeyGeneratorTypeEnum 根据枚举判断处理返回自定义的主键生成策略
     * @see org.apache.ibatis.executor.keygen.NoKeyGenerator
     */
    public KeyGenerator getKeyGenerator() {
        if (ObjectUtils.isEmpty(idColumnStruct)) {
            getIdColumnStruct();
        }
        // 获取@Id内配置的generatorType枚举
        KeyGeneratorTypeEnum keyGeneratorTypeEnum = idColumnStruct.getGeneratorType();
        if (ObjectUtils.isEmpty(keyGeneratorTypeEnum)) {
            throw new EnhanceFrameworkException("并未查询到@Id内配置的generatorType主键策略.");
        }

        KeyGenerator keyGenerator;
        switch (keyGeneratorTypeEnum) {
            // 使用UUID主键生成策略
            case UUID:
                keyGenerator = new UUIDKeyGenerator();
                break;
            // 默认不使用主键生成策略
            default:
                keyGenerator = new NoKeyGenerator();
                break;
        }
        return keyGenerator;
    }

    /**
     * 获取映射列名称列表
     *
     * @return 列名数组
     */
    public String[] getColumnNames() {

        List<String> columnNames = new ArrayList();
        for (ColumnStruct columnStruct : mappingFilterBuilder.getColumnStructList()) {
            // 是否添加到集合的标识为true时执行添加
            if (isAddToList(columnStruct)) {
                columnNames.add(columnStruct.getColumnName());
            }
        }
        return columnNames.toArray(new String[columnNames.size()]);
    }

    /**
     * 获取映射实体的列值映射
     * 如：#{bean.xxx}
     *
     * @return
     */
    public String[] getColumnValues() {

        List<String> columnValues = new ArrayList();
        for (ColumnStruct columnStruct : mappingFilterBuilder.getColumnStructList()) {
            // 是否添加到集合的标识为true时执行添加
            if (isAddToList(columnStruct)) {
                columnValues.add(ConfigConstants.BEAN_PARAMETER_PREFIX + columnStruct.getFieldName() + ConfigConstants.PARAMETER_SUFFIX);
            }
        }
        return columnValues.toArray(new String[columnValues.size()]);
    }

    /**
     * 判断列是否可以添加到返回的列名以及列值的数组内
     *
     * @param columnStruct 列映射结构对象
     * @return
     */
    private boolean isAddToList(ColumnStruct columnStruct) {
        boolean isAddToList = false;
        // 根据传递的过滤映射类型枚举判断
        switch (mappingFilterBuilder.getMappingType()) {
            // 查询类型
            case SELECT:
                isAddToList = selectIsAddToList(columnStruct);
                break;
            // 添加类型
            case INSERT:
                isAddToList = insertIsAddToList(columnStruct);
                break;
            // 删除类型
            case DELETE:
                isAddToList = deleteIsAddToList(columnStruct);
                break;
            // 更新类型
            case UPDATE:
                isAddToList = updateIsAddToList(columnStruct);
                break;
        }
        return isAddToList;
    }

    /**
     * 映射类型为查询时
     * 1. 排除mapping = false
     *
     * @param columnStruct 列结构对象
     * @return
     */
    private boolean selectIsAddToList(ColumnStruct columnStruct) {
        return columnStruct.isMapping();
    }

    /**
     * 映射类型为添加时
     * 1. 排除insertable = false
     * 2. 排除mapping = false
     * 3. 排除主键生成策略为KeyGeneratorTypeEnum.AUTO
     *
     * @param columnStruct 列结构对象
     * @return
     */
    private boolean insertIsAddToList(ColumnStruct columnStruct) {
        if (!columnStruct.isMapping() || !columnStruct.isInsertable()) {
            return false;
        }
        // 获取当前列的主键生成策略
        KeyGeneratorTypeEnum keyGeneratorTypeEnum = columnStruct.getGeneratorType();
        if (!ObjectUtils.isEmpty(keyGeneratorTypeEnum)) {
            switch (keyGeneratorTypeEnum) {
                // 自动生成的主键策略
                // 不参与SQL生成
                case AUTO:
                    return false;
            }
        }
        return true;
    }

    /**
     * 映射类型为更新时
     * 1. 过滤mapping = false
     * 2. 过滤updateable = false
     * 3. 过滤主键，isPk = true
     *
     * @param columnStruct 列映射结构对象
     * @return
     */
    private boolean updateIsAddToList(ColumnStruct columnStruct) {
        if (!columnStruct.isMapping() || !columnStruct.isUpdateable() || columnStruct.isPk()) {
            return false;
        }
        return true;
    }

    /**
     * 映射类型为删除时
     * 1.过滤mapping = false
     * 2.过滤主键，isPk = true
     *
     * @param columnStruct 列结构对象
     * @return
     */
    private boolean deleteIsAddToList(ColumnStruct columnStruct) {
        if (!columnStruct.isMapping() || !columnStruct.isPk()) {
            return false;
        }
        return true;
    }
}
