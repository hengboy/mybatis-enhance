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
package com.gitee.hengboy.mybatis.enhance.common.helper;

import org.springframework.util.ObjectUtils;

/**
 * 字符串处理工具类
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：下午1:49
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class StringHelper {
    /**
     * 追加字符串
     *
     * @param split 分隔符
     * @param array 字符串数组
     * @return 连接后的字符串
     */
    public static String contact(String split, String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            throw new RuntimeException("无法处理空列表的字符串");
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i] + (i == array.length - 1 ? "" : split));
        }
        return buffer.toString();
    }
}
