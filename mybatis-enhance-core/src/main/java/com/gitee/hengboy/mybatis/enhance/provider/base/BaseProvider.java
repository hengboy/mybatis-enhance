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
package com.gitee.hengboy.mybatis.enhance.provider.base;

import com.gitee.hengboy.mybatis.enhance.common.ConfigConstants;
import com.gitee.hengboy.mybatis.enhance.common.enums.PlaceholderEnum;
import com.gitee.hengboy.mybatis.enhance.common.helper.StatementHelper;
import com.gitee.hengboy.mybatis.enhance.common.helper.TableHelper;
import com.gitee.hengboy.mybatis.enhance.common.struct.ColumnStruct;
import com.gitee.hengboy.mybatis.enhance.common.struct.TableStruct;
import com.gitee.hengboy.mybatis.enhance.exception.EnhanceFrameworkException;
import lombok.Data;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供者Provider的基础类型
 * 提供了操作实体的类型以及接口的类型
 *
 * @author：于起宇 ===============================
 * Created with IDEA.
 * Date：2018/4/8
 * Time：上午11:14
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
@Data
public class BaseProvider {
    /**
     * 方法规则查询的方法名称
     * 对应MethodNamedXxxProvider内的build方法
     * 用于方法规则查询时重载SqlSource时调用的方法
     */
    private final String NAMED_PROVIDER_METHOD_NAME = "build";

    /**
     * MyBaits内部Xml节点解析的语言驱动
     */
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    /**
     * Mapper接口的类型
     */
    private Class<?> mapperClass;
    /**
     * Mapper接口泛型实体类的类型
     */
    private Class<?> entityClass;
    /**
     * MappedStatement对应的方法实例
     */
    private Method method;
    /**
     * Entity对应数据表结构实例
     */
    private TableStruct tableStruct;
    /**
     * 表内列结构的List集合
     */
    private List<ColumnStruct> columnStruct;
    /**
     * 表内列结构的Map集合
     */
    private Map<String, ColumnStruct> columnStructMap;

    /**
     * 构造函数
     *
     * @param mapperClass Mapper类型
     * @param entityClass Mapper泛型实体类类型
     * @param method      MappedStatement对应的Method实例
     */
    public BaseProvider(Class<?> mapperClass, Class<?> entityClass, Method method) {
        this.method = method;
        this.mapperClass = mapperClass;
        this.entityClass = entityClass;
        // 从缓存中读取Table的结构信息
        this.tableStruct = TableHelper.getTableStruct(entityClass);
        // 从缓存中读取Table内所有列结构信息
        this.columnStruct = TableHelper.getColumnStruct(entityClass);
    }

    /**
     * 该方法用于配置CrudMapper、EnhanceMapper、SqlMapper、PageAndSortMapper
     * 内@SelectProvider、@DeleteProvider、@UpdateProvider、@InsertProvider等注解的method属性
     * 目的：具体生成的SQL对应关系如下所示（使用方法名称对应）：
     * CrudMapper::insert(T t)方法具体的生成SQL对应OrmInsertProvider::insert(MappedStatement statement)
     * 生成SQL的参数别名如下：
     * 实体类型参数：@Param("bean") T t，对应SQL格式：#{bean.xxx_id}、
     * 主键类型参数：@Param("id") Id id，对应SQL格式：{#id}
     *
     * @return 默认的sql字符串
     */
    public String empty() {
        return "this is empty sql";
    }

    /**
     * 执行创建Sql的具体方法
     * 如：OrmSelectAllProvider::selectOne(MappedStatement ms)
     *
     * @param ms MappedStatement对象实例
     */
    public void invokeProviderMethod(MappedStatement ms) {
        // 调用Provider对应的方法并返回Sql
        String methodName = StatementHelper.getMethodName(ms.getId());
        invokeProviderMethod(methodName, ms);
    }

    /**
     * 执行NamedProvider内的方法
     * 实现重载SqlSource
     *
     * @param ms MappedStatement对象实例
     */
    public void invokeNamedProviderMethod(MappedStatement ms) {
        invokeProviderMethod(NAMED_PROVIDER_METHOD_NAME, ms);
    }

    /**
     * 根据Provider内方法名称执行对应的方法
     *
     * @param methodName 方法名称
     * @param ms         MappedStatement对象实例
     */
    private void invokeProviderMethod(String methodName, MappedStatement ms) {
        try {
            Method method = this.getClass().getMethod(methodName, MappedStatement.class);
            // 执行方法 & 生成SqlSource & 重新装载SqlSource
            method.invoke(this, ms);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EnhanceFrameworkException("MappedStatement：[" + ms.getId() + "]，执行重载SqlSource失败.");
        }
    }

    /**
     * 重新装载MappedStatement的SqlSource内容
     * sql字符串如果以script开头，那么表示内容是mybatis可以识别的xml方式字符串
     *
     * @param statement MappedStatement实例
     * @param sql       构建mybatis可以识别的sql（普通sql、xml方式sql）
     */
    public void reloadSqlSource(MappedStatement statement, String sql) {
        // 根据语言驱动创建SqlSource对象
        SqlSource sqlSource = languageDriver.createSqlSource(statement.getConfiguration(), sql, null);
        // 重新装载SqlSource
        reloadSqlSource(statement, sqlSource);
    }

    /**
     * 重新装载MappedStatement的SqlSource内容
     *
     * @param statement MappedStatement实例
     * @param sqlSource MappedStatement内的SqlSource实例
     */
    protected void reloadSqlSource(MappedStatement statement, SqlSource sqlSource) {
        MetaObject msObject = SystemMetaObject.forObject(statement);
        msObject.setValue("sqlSource", sqlSource);
    }

    /**
     * 重新装载返回值类型
     * 根据Mapper泛型的实体类型进行设置返回ResultMap
     * 获取实体内字段对应列表的映射集合ResultMapping
     * 将ResultMapping写入到ResultMap集合内
     * 将ResultMap设置到MappedStatement对象内
     *
     * @param statement MappedStatement实例对象
     */
    protected void reloadEntityResultType(MappedStatement statement) {
        List<ResultMapping> resultMappings = new ArrayList();
        for (ColumnStruct column : columnStruct) {
            ResultMapping.Builder builder = new ResultMapping.Builder(statement.getConfiguration(), column.getFieldName(), column.getColumnName(), column.getJavaType());
            resultMappings.add(builder.build());
        }
        ResultMap.Builder builder = new ResultMap.Builder(statement.getConfiguration(), statement.getId() + "-Inline", this.entityClass, resultMappings, true);
        ResultMap resultMap = builder.build();
        MetaObject metaObject = SystemMetaObject.forObject(statement);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(Arrays.asList(resultMap)));
    }

    /**
     * 重新装载返回值类型
     * 根据传入的基本数据类型作为返回值
     *
     * @param statement   MappedStatement对象实例
     * @param singleClass 基本数据类型，如：Long.class/Integer.class
     */
    protected void reloadSingleResultType(MappedStatement statement, Class<?> singleClass) {
        ResultMap.Builder builder = new ResultMap.Builder(statement.getConfiguration(), statement.getId() + "-Inline", singleClass, new ArrayList(), true);
        ResultMap resultMap = builder.build();
        MetaObject metaObject = SystemMetaObject.forObject(statement);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(Arrays.asList(resultMap)));
    }

    /**
     * 重新装载主键生成策略
     * 如果实体类内不设置主键生成策略，默认使用mybatis内的NoKeyGenerator
     * 设置实体内主键的field名称到MappedStatement的keyProperties
     * 设置实体内主键的列名到MappedStatement内的keyColumns
     * 设置主键生成策略到MappedStatement内的keyGenerator
     *
     * @param statement     MappedStatement实例对象
     * @param keyColumnName 主键列名
     * @param keyFieldName  主键对应的Field名称
     * @param keyGenerator  生成策略
     * @see org.apache.ibatis.executor.keygen.NoKeyGenerator
     */
    protected void reloadKeyGenerator(MappedStatement statement, KeyGenerator keyGenerator, String keyFieldName, String keyColumnName) {
        if (!ObjectUtils.isEmpty(keyGenerator)) {
            MetaObject metaObject = SystemMetaObject.forObject(statement);
            metaObject.setValue("keyProperties", new String[]{keyFieldName});
            metaObject.setValue("keyColumns", new String[]{keyColumnName});
            metaObject.setValue("keyGenerator", keyGenerator);
        }
    }

    /**
     * 获取主键查询条件的字符串
     * 如：id = #{bean.id}
     *
     * @return 查询条件字符串
     */
    protected String getBeanPkWhere() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(tableStruct.getIdName());
        buffer.append(PlaceholderEnum.EQ.getValue());
        buffer.append(ConfigConstants.BEAN_PARAMETER_PREFIX);
        buffer.append(tableStruct.getIdFieldName());
        buffer.append(ConfigConstants.PARAMETER_SUFFIX);
        return buffer.toString();
    }

    /**
     * 获取主键条件的字符串
     * 如：xxId = #{id}
     *
     * @return 查询条件字符串
     */
    protected String getSinglePkWhere() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(tableStruct.getIdName());
        buffer.append(PlaceholderEnum.EQ.getValue());
        buffer.append(ConfigConstants.PARAMETER_PREFIX);
        buffer.append(ConfigConstants.PK_PARAMETER);
        buffer.append(ConfigConstants.PARAMETER_SUFFIX);
        return buffer.toString();
    }

    /**
     * 将ColumnStruct集合转换为Map类型的ColumnStruct
     *
     * @return 返回的Map集合的ColumnStruct
     */
    protected Map<String, ColumnStruct> getColumnStructMap() {
        // 初始化ColumnStructMap集合
        columnStructMap = new ConcurrentHashMap(columnStruct.size());
        for (ColumnStruct struct : columnStruct) {
            columnStructMap.put(struct.getFieldName(), struct);
        }
        return columnStructMap;
    }
}
