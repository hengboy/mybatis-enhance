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
package com.gitee.hengboy.mybatis.enhance.provider.named;

import com.gitee.hengboy.mybatis.enhance.common.helper.sql.MapperXmlMySqlHelper;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilter;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilterBuilder;
import com.gitee.hengboy.mybatis.enhance.common.mapping.MappingTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * 方法规则查询Provider
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/17
 * Time：下午10:53
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class MethodNamedFindProvider extends BaseProvider {
    /**
     * 实例化全局变量
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类类型
     * @param method 方法对象
     */
    public MethodNamedFindProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 方法规则查询方式的构建方法
     * 所有匹配方法规则查询的方法都执行该方法重载SqlSource
     *
     * @param statement MappedStatement对象实例
     */
    public void build(MappedStatement statement) {
        TableStruct tableStruct = getTableStruct();
        // 获取列映射过滤对象
        ColumnMappingFilter mappingFilter = ColumnMappingFilterBuilder
                .builder()
                .columnStructList(getColumnStruct())
                .mappingType(MappingTypeEnum.SELECT)
                .build()
                .filter();
        // 生成查询的sql
        String sql = MapperXmlMySqlHelper.script(
                MapperXmlMySqlHelper.select(tableStruct.getTableName(), mappingFilter.getColumnNames()),
                MapperXmlMySqlHelper.where(
                        MapperXmlMySqlHelper.named(getMethod().getName(), getColumnStructMap())
                )
        );
        // 重载SqlSource
        reloadSqlSource(statement, sql);
        // 重载返回的类型
        reloadEntityResultType(statement);
    }
}
