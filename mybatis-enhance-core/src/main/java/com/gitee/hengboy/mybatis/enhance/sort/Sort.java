/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2018 恒宇少年
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gitee.hengboy.mybatis.enhance.sort;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 排序对象
 * 存放排序字段以及排序方式
 *
 * @author yuqiyu
 * ===============================
 * Created with IntelliJ IDEA.
 * User：于起宇
 * Date：2017/8/12
 * Time：11:25
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@Data
@Builder
public class Sort implements Serializable {
    /**
     * 排序枚举类型
     */
    private String away;
    /**
     * 排序字段
     */
    private String column;
    /**
     * 格式化后的排序
     */
    private String sorter;

    /**
     * 获取未被格式化的字符串
     *
     * @return 获取排序的字符串
     */
    public String getSorter() {
        return column + " " + away;
    }
}
