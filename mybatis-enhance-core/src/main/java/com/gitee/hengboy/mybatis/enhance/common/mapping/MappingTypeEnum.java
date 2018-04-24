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

/**
 * 映射类型枚举定义
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/13
 * Time：上午10:56
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public enum MappingTypeEnum {
    /**
     * 添加数据的映射类型
     * 1.排除insertable=false的列
     * 2.排除mapping=false的列
     */
    INSERT,
    /**
     * 更新数据的映射类型
     * 1.排除updateable=false的列
     * 2.排除mapping=false的列
     * 3.排除主键列
     */
    UPDATE,
    /**
     * 查询数据的映射类型
     * 1.排除mapping=false的列
     */
    SELECT,
    /**
     * 删除数据的映射类型
     * 1.排除mapping=false的列
     * 2.排除主键列
     */
    DELETE
}
