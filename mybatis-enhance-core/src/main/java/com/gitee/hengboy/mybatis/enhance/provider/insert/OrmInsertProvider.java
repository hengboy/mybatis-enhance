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

import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilter;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilterBuilder;
import com.gitee.hengboy.mybatis.enhance.common.mapping.MappingTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.mapper.CrudMapper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * 持久化数据Sql构建实体类
 * 提供给MyBatis :: @InsertProvider注解使用
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/6
 * Time：下午2:36
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class OrmInsertProvider extends BaseProvider {
    /**
     * 继承构造函数
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类类型
     * @param method      MappedStatement对应的方法实例
     */
    public OrmInsertProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 生成插入单个实体的sql
     *
     * @param statement MappedStatement实例
     * @return
     * @see CrudMapper#insert(Object)
     */
    public void insert(MappedStatement statement) {
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

        // 构建sql
        String sql = new SQL()
                .INSERT_INTO(tableStruct.getTableName())
                .INTO_COLUMNS(columnMapping.getColumnNames())
                .INTO_VALUES(columnMapping.getColumnValues())
                .toString();

        // 获取主键结构
        ColumnStruct idColumn = columnMapping.getIdColumnStruct();

        // 重新装载sql
        reloadSqlSource(statement, sql);

        // 重新装载主键生成策略
        reloadKeyGenerator(statement, columnMapping.getKeyGenerator(), idColumn.getFieldName(), idColumn.getColumnName());
    }
}
