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
package com.gitee.hengboy.mybatis.enhance.named.helper;

import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.exception.OrmCoreFrameworkException;
import com.gitee.hengboy.mybatis.enhance.named.OrPart;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命名规则工具类
 * 用于方法规则命名方法检测、处理等
 *
 * @author yuqiyu
 * ========================
 * Created with IntelliJ IDEA.
 * User：恒宇少年
 * Date：2018/1/14
 * Time：下午2:49
 * 码云：http://git.oschina.net/jnyqy
 * ========================
 */
public final class NamedMethodHelper {
    /**
     * 关键字模板正则表达式
     */
    private static final String KEYWORD_TEMPLATE = "(%s)(?=(\\p{Lu}|\\P{InBASIC_LATIN}))";
    /**
     * 匹配查询方法名称正则表达式
     */
    private static final String QUERY_PATTERN = "find";
    /**
     * 匹配统计方法名称正则表达式
     */
    private static final String COUNT_PATTERN = "count";
    /**
     * 匹配删除方法名称正则表达式
     */
    private static final String DELETE_PATTERN = "remove";
    /**
     * 匹配所有方法前缀模板
     */
    public static final Pattern PREFIX_TEMPLATE = Pattern.compile(
            "^(" + QUERY_PATTERN + "|" + COUNT_PATTERN + "|" + DELETE_PATTERN + ")((\\p{Lu}.*?))??By");
    /**
     * 方法名称分割占位符，And
     */
    public static final String PLACEHOLDER_AND = "And";
    /**
     * 方法名称分割占位符，Or
     */
    public static final String PLACEHOLDER_OR = "Or";

    /**
     * 是否匹配名称规则
     *
     * @param methodName 方法名称
     * @return true：匹配方法名称规则，false：不匹配名称规则
     */
    public static boolean isMatchNamed(String methodName) {
        return matches(methodName, PREFIX_TEMPLATE);
    }

    /**
     * 是否匹配查询方法名称规则
     *
     * @param methodName 方法名称
     * @return true：匹配，false：不匹配
     */
    public static boolean isMatchFindNamed(String methodName) {
        return matches(methodName, Pattern.compile(QUERY_PATTERN));
    }

    /**
     * 是否匹配删除方法名字规则
     *
     * @param methodName 方法名称
     * @return true：匹配，false：不匹配
     */
    public static boolean isMatchDeleteNamed(String methodName) {
        return matches(methodName, Pattern.compile(DELETE_PATTERN));
    }

    /**
     * 是否匹配统计方法名字规则
     *
     * @param methodName 方法名称
     * @return true：匹配，false：不匹配
     */
    public static boolean isMatchCountNamed(String methodName) {
        return matches(methodName, Pattern.compile(COUNT_PATTERN));
    }

    /**
     * 验证是否匹配指定正则
     *
     * @param subject 验证字符串内容
     * @param pattern 正则表达式
     * @return
     */
    private static boolean matches(String subject, Pattern pattern) {
        return subject == null ? false : pattern.matcher(subject).find();
    }

    /**
     * 分割方法名称
     *
     * @param text    需要分割的字符串
     * @param keyword 分割关键字
     * @return 分割后的字符串数组
     */
    public static String[] split(String text, String keyword) {
        Pattern pattern = Pattern.compile(String.format(KEYWORD_TEMPLATE, keyword));
        return pattern.split(text);
    }

    /**
     * 构造方法名称规则条件sql语句
     * 如：findAllByNameAndAge(String name,Integer age) ->
     * where u_name = #{name} and u_age = #{age}
     * <p>
     * - 处理or拼接条件
     * - 处理or内每个元素的and拼接条件
     *
     * @param methodName 名称规则对应的方法名称
     * @param mappings   操作实体类字段与列映射集合
     * @return
     */
    public static List<OrPart> getNamedWhereSql(String methodName, Map<String, ColumnStruct> mappings) {
        // or sql 语句集合，每一个 or 内的所有and作为list内的一个项
        List<OrPart> orPartList = new ArrayList();

        // 创建正则匹配对象
        Matcher matcher = NamedMethodHelper.PREFIX_TEMPLATE.matcher(methodName);
        if (!matcher.find()) {
            return null;
        }
        // 获取排序PREFIX_TEMPLATE内容的方法名称
        String subjects = methodName.substring(matcher.group().length());

        // 正则分割出所有Or条件
        String[] orParts = NamedMethodHelper.split(subjects, NamedMethodHelper.PLACEHOLDER_OR);
        for (String orPart : orParts) {
            if (!StringUtils.isEmpty(orPart)) {
                // or项实体
                OrPart part = new OrPart();

                // 正则分割出每个orPart内的And条件
                String[] andParts = NamedMethodHelper.split(orPart, NamedMethodHelper.PLACEHOLDER_AND);
                for (String andPart : andParts) {
                    // 拼接sql
                    StringBuffer buffer = new StringBuffer();
                    /*
                     *格式化拼接字段首写字母为小写，如：UserId -> userId
                     */
                    andPart = andPart.substring(0, 1).toLowerCase() + andPart.substring(1);
                    /**
                     * 映射字段关系
                     */
                    ColumnStruct struct = mappings.get(andPart);
                    if (ObjectUtils.isEmpty(struct)) {
                        throw new OrmCoreFrameworkException("未找到【" + andPart + "】字段，请检查Mapper泛型实体类内是否创建该字段!");
                    }
                    buffer.append(struct.getColumnName());
                    buffer.append(" = ");
                    buffer.append(" #{" + andPart + "} ");
                    // 将or sql添加到返回list集合内
                    String orSql = buffer.toString();
                    if (!StringUtils.isEmpty(orSql)) {
                        part.getPredicates().add(orSql);
                    }
                }
                // 将or part放入or part集合内
                orPartList.add(part);
            }
        }
        return orPartList;
    }
}
