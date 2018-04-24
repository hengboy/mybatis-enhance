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
package com.gitee.hengboy.mybatis.enhance.common.enums;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：恒宇少年
 * Date：2017/8/13
 * Time：16:09
 * 码云：http://git.oschina.net/jnyqy
 * ========================
 */
public enum PlaceholderEnum {
    EQ(" = "),
    NEQ(" <> "),
    ALL(" * "),
    LT(" < "),
    GT(" > "),
    LET(" <= "),
    GET(" >= "),
    LIKE(" LIKE "),
    LIMIT(" LIMIT "),
    SPLIT(" , "),
    AS("AS"),
    IN(" IN "),
    AND(" AND "),
    OR(" OR "),
    SET(" SET "),
    IS_NOT_NULL(" IS NOT NULL "),
    IS_NULL(" IS NULL "),
    IS_EMPTY(" = '' "),
    IS_NOT_EMPTY(" <> '' "),
    INSERT_INTO("INSERT INTO "),
    DELETE_FROM("DELETE FROM "),
    SELECT("SELECT "),
    COUNT("COUNT"),
    FROM(" FROM "),
    ORDER_BY("ORDER BY "),
    UPDATE("UPDATE "),
    VALUES(" ) VALUES "),
    SPLIT_PREFIX(" ( "),
    SPLIT_SUFFIX(" ) "),
    WHERE(" WHERE ");
    private String value;

    PlaceholderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
