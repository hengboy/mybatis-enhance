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
package com.gitee.hengboy.mybatis.enhance.provider.insert;

import com.gitee.hengboy.mybatis.enhance.common.enums.PlaceholderEnum;
import com.gitee.hengboy.mybatis.enhance.common.helper.StringHelper;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilter;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilterBuilder;
import com.gitee.hengboy.mybatis.enhance.common.mapping.MappingTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.mapper.CrudMapper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：下午3:10
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class OrmInsertCollectionProvider extends BaseProvider {
    /**
     * 构造函数初始化变量
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类型
     * @param method      MappedStatement对应的方法实例
     */
    public OrmInsertCollectionProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 通过Collection形式插入多条数据
     *
     * @param statement MappedStatement对象
     * @see CrudMapper#insertCollection(Collection)
     */
    public void insertCollection(MappedStatement statement) {

        // 获取数据表的结构
        TableStruct tableStruct = getTableStruct();
        // 获取列映射过滤对象
        ColumnMappingFilter columnMapping =
                ColumnMappingFilterBuilder
                        .builder()
                        .columnStructList(getColumnStruct())
                        .mappingType(MappingTypeEnum.INSERT)
                        .build()
                        .filter();

        // 使用xml形式重新装载SqlSource
        StringBuffer sqlBuffer = new StringBuffer("<script>");
        sqlBuffer.append(PlaceholderEnum.INSERT_INTO.getValue());
        sqlBuffer.append(tableStruct.getTableName());
        sqlBuffer.append(PlaceholderEnum.SPLIT_PREFIX.getValue());
        sqlBuffer.append(StringHelper.contact(PlaceholderEnum.SPLIT.getValue(), columnMapping.getColumnNames()));
        sqlBuffer.append(PlaceholderEnum.VALUES.getValue());
        sqlBuffer.append("<foreach collection=\"collection\" item=\"bean\" separator=\",\"> ");
        sqlBuffer.append(PlaceholderEnum.SPLIT_PREFIX.getValue());
        sqlBuffer.append(StringHelper.contact(PlaceholderEnum.SPLIT.getValue(), columnMapping.getColumnValues()));
        sqlBuffer.append(PlaceholderEnum.SPLIT_SUFFIX.getValue());
        sqlBuffer.append("</foreach>");
        sqlBuffer.append("</script>");

        // 重新装载sql
        reloadSqlSource(statement, sqlBuffer.toString());

        // 获取主键结构
        ColumnStruct idColumn = columnMapping.getIdColumnStruct();

        // 重新装载主键生成策略
        reloadKeyGenerator(statement, columnMapping.getKeyGenerator(), idColumn.getFieldName(), idColumn.getColumnName());
    }
}
