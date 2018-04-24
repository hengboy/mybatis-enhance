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
package com.gitee.hengboy.mybatis.enhance.provider.update;


import com.gitee.hengboy.mybatis.enhance.common.enums.PlaceholderEnum;
import com.gitee.hengboy.mybatis.enhance.common.helper.sql.MapperXmlMySqlHelper;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilter;
import com.gitee.hengboy.mybatis.enhance.common.mapping.ColumnMappingFilterBuilder;
import com.gitee.hengboy.mybatis.enhance.common.mapping.MappingTypeEnum;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.mapper.CrudMapper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
public final class OrmUpdateProvider extends BaseProvider {
    /**
     * 构造函数初始化全局变量
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类型
     * @param method      MappedStatement对应的方法实例
     */
    public OrmUpdateProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 更新对象sql生成
     *
     * @return
     * @see CrudMapper#update(Object)
     */
    public void update(MappedStatement statement) {
        // 获取数据表结构
        TableStruct tableStruct = getTableStruct();

        // 生成更新语句sql
        String sql = MapperXmlMySqlHelper.script(
                MapperXmlMySqlHelper.update(tableStruct.getTableName()),
                MapperXmlMySqlHelper.set(getSets()),
                MapperXmlMySqlHelper.where(getBeanPkWhere())
        );
        // 重新装载SqlSource
        reloadSqlSource(statement, sql);
    }

    /**
     * 获取set格式的字符串数组集合
     * 如：xxx = #{bean.zzz}
     *
     * @return
     */
    private String[] getSets() {
        List<String> sets = new ArrayList();
        ColumnMappingFilter mappingFilter = ColumnMappingFilterBuilder
                .builder()
                .columnStructList(getColumnStruct())
                .mappingType(MappingTypeEnum.UPDATE)
                .build()
                .filter();
        /*
         * 遍历处理set字符串
         */
        for (int i = 0; i < mappingFilter.getColumnNames().length; i++) {
            sets.add(mappingFilter.getColumnNames()[i] + PlaceholderEnum.EQ.getValue() + mappingFilter.getColumnValues()[i]);
        }
        return sets.toArray(new String[mappingFilter.getColumnNames().length]);
    }
}
