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
 * 自动生成主键枚举
 * 类型解释：
 * AUTO -- 自动生成，构建insert column时排除，针对mysql数据库
 * SNOWFLAKE -- 采用Twitter分布式生成ID机制
 * UUID -- 采用UUID形式生成
 * DIY -- 用户自定义
 * ===============================
 * Created with IntelliJ IDEA.
 * User：于起宇
 * Date：2017/8/14
 * Time：15:00
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public enum KeyGeneratorTypeEnum {
    AUTO,
    DIY,
    SNOWFLAKE,
    UUID
}
