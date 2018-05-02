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
package com.gitee.hengboy.mybatis.enhance.provider.delete;

import com.gitee.hengboy.mybatis.enhance.common.helper.sql.MapperXmlMySqlHelper;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.mapper.CrudMapper;
import com.gitee.hengboy.mybatis.enhance.provider.base.BaseProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 删除语句自动生成sql提供类
 * 提供给MyBatis :: @DeleteProvider注解使用
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/7
 * Time：上午10:43
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class OrmDeleteProvider extends BaseProvider {
    /**
     * 构造函数初始化全局变量
     *
     * @param mapperClass Mapper类型
     * @param entityClass 实体类型
     * @param method      MappedStatement对应的方法实例
     */
    public OrmDeleteProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        super(mapperClass, entityClass, method);
    }

    /**
     * 生成根据主键删除的sql语句
     * 示例：userMapper.deleteOne(7);
     * @param statement MappedStatement对象实例
     * @see CrudMapper#deleteOne(Serializable)
     */
    public void deleteOne(MappedStatement statement) {
        // 获取数据表的结构
        TableStruct tableStruct = getTableStruct();

        // 生成根据主键删除的sql
        String sql = MapperXmlMySqlHelper.script(
                MapperXmlMySqlHelper.delete(tableStruct.getTableName()),
                MapperXmlMySqlHelper.where(
                        getSinglePkWhere()
                )
        );
        // 执行重新SqlSource
        reloadSqlSource(statement, sql);
    }

}
