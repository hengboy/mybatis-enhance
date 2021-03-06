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
package com.gitee.hengboy.mybatis.enhance.mapper.select;

import com.gitee.hengboy.mybatis.enhance.common.annotation.ProviderMapper;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import com.gitee.hengboy.mybatis.enhance.provider.select.OrmSelectCollectionProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/14
 * Time：下午2:30
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@ProviderMapper
public interface SelectCollectionMapper<T, Id extends Serializable> {
    /**
     * 根据主键的集合查询
     *
     * @param ids 主键编号集合
     * @return T类型的查询集合
     * @throws EnhanceFrameworkException 增强框架异常
     */
    @SelectProvider(type = OrmSelectCollectionProvider.class, method = "empty")
    List<T> selectCollection(@Param("collection") Collection<Id> ids) throws EnhanceFrameworkException;
}
