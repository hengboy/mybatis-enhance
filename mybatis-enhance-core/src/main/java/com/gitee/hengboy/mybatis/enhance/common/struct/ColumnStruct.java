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
package com.gitee.hengboy.mybatis.enhance.common.struct;

import com.gitee.hengboy.mybatis.enhance.common.enums.KeyGeneratorTypeEnum;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * 列与字段的结构映射
 * 每一个字段对应一个列信息
 * 包含实体配置的注解配置
 *
 * @author yuqiyu
 * ========================
 * Created with IntelliJ IDEA.
 * User：恒宇少年
 * Date：2017/8/13
 * Time：18:26
 * 码云：http://git.oschina.net/jnyqy
 * ========================
 */
@Data
public class ColumnStruct
        implements Serializable {

    /**
     * 列名
     */
    private String columnName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * Java数据类型
     */
    private Class<?> javaType;
    /**
     * 数据库数据类型
     */
    private JdbcType jdbcType;

    /**
     * 生成方式
     * 仅主键可用
     */
    private KeyGeneratorTypeEnum generatorType;

    /**
     * 是否映射
     * true：映射
     * false：不映射
     */
    private boolean mapping = true;
    /**
     * 是否为主键
     * true：是主键
     * false：不是主键
     */
    private boolean isPk = false;
    /**
     * 是否参与插入
     * true：参与插入
     * false：不参与插入
     */
    private boolean insertable = true;
    /**
     * 是否参与更新
     * true：参与更新
     * false：不参与更新
     */
    private boolean updateable = true;

}
