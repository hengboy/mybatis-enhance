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
package com.gitee.hengboy.mybatis.enhance.page;

import com.gitee.hengboy.mybatis.enhance.sort.Sort;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * 分页对象
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：下午5:20
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@Data
@Builder
public class Pageable<T> {
    /**
     * 每页的条数
     */
    private Integer limit;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 查询开始位置
     */
    private Long offset;
    /**
     * 排序对象
     */
    private Sort sort;

    /**
     * 获取开始位置
     *
     * @return （当前页码 - 1） * 每页查询条数
     */
    public Long getOffset() {
        // 如果并未传递当前页码，使用默认值1
        if (ObjectUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        // 计算出开始的位置
        Long offset = Long.valueOf(String.valueOf((currentPage - 1) * limit));
        // 如果开始位置为0，则不返回开始位置
        return offset > 0 ? offset : null;
    }
}
