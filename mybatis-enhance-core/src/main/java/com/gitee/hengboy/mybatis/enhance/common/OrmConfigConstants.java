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
package com.gitee.hengboy.mybatis.enhance.common;

/**
 * 持久化框架配置常量设置
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/4/12
 * Time：下午1:34
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public interface OrmConfigConstants {
    /**
     * 单独参数的前缀
     */
    String PARAMETER_PREFIX = "#{";
    /**
     * 实体类型的参数后缀
     */
    String PARAMETER_SUFFIX = "}";
    /**
     * 参数实体名称
     * 配置所示：@Param(name="bean")
     */
    String BEAN_PARAMETER_NAME = "bean";
    /**
     * 集合参数名称
     * 配置所示：@Param(name="collection")
     */
    String COLLECTION_PARAMETER_NAME = "collection";
    /**
     * List参数名称
     * 配置所示：@Param(name="list")
     */
    String LIST_PARAMETER_NAME = "list";
    /**
     * 数组参数名称
     * 配置所示：@Param(name="array")
     */
    String ARRAY_PARAMETER_NAME = "array";
    /**
     * 实体类型的参数前缀
     */
    String BEAN_PARAMETER_PREFIX = PARAMETER_PREFIX + BEAN_PARAMETER_NAME + ".";
    /**
     * 单独使用主键时的参数名称
     */
    String PK_PARAMETER = "id";

    /**
     * 分页参数名称
     * 配置所示：@Param(name="pageable")
     */
    String PAGEABLE_PARAMETER_NAME = "pageable";
    /**
     * 每页限制的参数名称:limit
     */
    String PAGEABLE_PARAMETER_LIMIT_NAME = "limit";
    /**
     * 分页参数limit内容：pageable.limit
     */
    String PAGEABLE_PARAMETER_LIMIT_CONTENT = PAGEABLE_PARAMETER_NAME + "." + PAGEABLE_PARAMETER_LIMIT_NAME;
    /**
     * 分页参数：#{pageable.limit}
     */
    String PAGEABLE_PARAMETER_LIMIT = PARAMETER_PREFIX + PAGEABLE_PARAMETER_LIMIT_CONTENT + PARAMETER_SUFFIX;

    /**
     * 查询开始的参数名称
     */
    String PAGEABLE_PARAMETER_OFFSET_NAME = "offset";

    /**
     * 分页offset参数内容：pageable.offset
     */
    String PAGEABLE_PARAMETER_OFFSET_CONTENT = PAGEABLE_PARAMETER_NAME + "." + PAGEABLE_PARAMETER_OFFSET_NAME;
    /**
     * 分页参数：#{pageable.offset}
     */
    String PAGEABLE_PARAMETER_OFFSET = PARAMETER_PREFIX + PAGEABLE_PARAMETER_OFFSET_CONTENT + PARAMETER_SUFFIX;

    /**
     * 排序名称
     */
    String SORT_NAME = PAGEABLE_PARAMETER_NAME + ".sort";
    /**
     * 分页对象内的排序列名
     */
    String SORT_COLUMN_NAME = SORT_NAME + ".column";
    /**
     * 分页对象内的排序方式
     */
    String SORT_AWAY = SORT_NAME + ".away";
    /**
     * 分页内排序语句生成
     */
    String PAGEABLE_ORDER_BY = "${" + SORT_NAME + ".sorter}";

}
