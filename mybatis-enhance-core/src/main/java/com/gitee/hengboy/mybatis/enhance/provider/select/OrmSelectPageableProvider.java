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
package com.gitee.hengboy.mybatis.enhance.provider.select;

import com.gitee.hengboy.mybatis.enhance.common.helper.sql.MapperXmlMySqlHelper;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilter;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilterBuilder;
import com.gitee.hengboy.mybatis.enhance.common.mapping.MappingTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * 分页查询SQL提供者实体类
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：下午4:58
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class OrmSelectPageableProvider extends BaseProvider {
    /**
     * 构造函数初始化全局变量
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类型
     * @param method      MappedStatement对应的方法实例
     */
    public OrmSelectPageableProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 重新装载分页查询的SqlSource
     *
     * @param statement MappedStatement对象实例
     */
    public void selectByPageable(MappedStatement statement) {
        // 获取数据表结构对象
        TableStruct tableStruct = getTableStruct();
        // 获取过滤的映射对象
        ColumnMappingFilter mappingFilter = ColumnMappingFilterBuilder
                .builder()
                .mappingType(MappingTypeEnum.SELECT)
                .columnStructList(getColumnStruct())
                .build()
                .filter();

        // 生成分页查询的sql
        String sql = MapperXmlMySqlHelper.script(
                // 生成查询信息
                MapperXmlMySqlHelper.select(tableStruct.getTableName(), mappingFilter.getColumnNames()),
                // 生成分页信息
                MapperXmlMySqlHelper.pageable(),
                // 生成排序信息
                MapperXmlMySqlHelper.sort()
        );
        // 重新装载SqlSource
        reloadSqlSource(statement, sql);
        // 重新装载返回类型
        reloadEntityResultType(statement);
    }
}
