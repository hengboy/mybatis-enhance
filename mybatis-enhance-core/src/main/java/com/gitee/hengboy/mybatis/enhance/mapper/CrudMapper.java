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
package com.gitee.hengboy.mybatis.enhance.mapper;

import com.gitee.hengboy.mybatis.enhance.common.annotation.ProviderMapper;
import com.gitee.hengboy.mybatis.enhance.mapper.count.CountAllMapper;
import com.gitee.hengboy.mybatis.enhance.mapper.delete.DeleteMapper;
import com.gitee.hengboy.mybatis.enhance.mapper.insert.InsertMapper;
import com.gitee.hengboy.mybatis.enhance.mapper.select.SelectMapper;
import com.gitee.hengboy.mybatis.enhance.mapper.update.UpdateMapper;

import java.io.Serializable;

/**
 * 提供单个对象的CRUD操作方法
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/6
 * Time：下午2:17
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@ProviderMapper
public interface CrudMapper<T, Id extends Serializable>
        extends
        InsertMapper<T, Id>,
        DeleteMapper<T, Id>,
        UpdateMapper<T, Id>,
        SelectMapper<T, Id>,
        CountAllMapper<T, Id>,
        SqlMapper<T, Id> {

}
