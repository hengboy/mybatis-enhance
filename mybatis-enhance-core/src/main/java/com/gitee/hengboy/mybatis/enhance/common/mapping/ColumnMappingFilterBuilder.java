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

import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * ColumnMappingFilter类的构建类
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/13
 * Time：上午7:52
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@Data
@Builder
public class ColumnMappingFilterBuilder {
    /**
     * 表内列的映射对象集合
     */
    private List<ColumnStruct> columnStructList;
    /**
     * 映射类型枚举
     */
    private MappingTypeEnum mappingType;

    /**
     * 过滤方法，实例化ColumnMappingFilter对象实例
     *
     * @return 列映射过滤对象
     */
    public ColumnMappingFilter filter() {
        // 检查参数有效性
        checkParam();
        return new ColumnMappingFilter(this);
    }


    /**
     * 检查设置的参数有效性
     * insertable、updateable参数必须有一个存在
     */
    private void checkParam() {
        /**
         * 检查列映射集合是否传递
         */
        if (ObjectUtils.isEmpty(getColumnStructList())) {
            throw new EnhanceFrameworkException("必须传递列映射集合列表.");
        }
        /**
         * 检查映射枚举是否传递
         */
        if (ObjectUtils.isEmpty(mappingType)) {
            throw new EnhanceFrameworkException("必须传递映射类型枚举：MappingTypeEnum的值.");
        }
    }
}
