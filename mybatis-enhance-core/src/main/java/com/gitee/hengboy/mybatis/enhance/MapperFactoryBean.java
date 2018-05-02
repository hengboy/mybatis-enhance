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
package com.gitee.hengboy.mybatis.enhance;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import java.util.ArrayList;

/**
 * Mapper工厂类
 *
 * @author：于起宇
 * ===============================
 * Created with IDEA.
 * Date：2018/4/6
 * Time：下午5:01
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(MapperFactoryBean.class);
    /**
     * mapper类型
     */
    private Class<T> mapperInterface;

    private boolean addToConfig = true;
    /**
     * MappedStatement装载类实例
     */
    private MappedStatementSupport mappedStatementSupport;

    /**
     * 无参数构造函数
     */
    public MapperFactoryBean() {
    }

    /**
     * 该构造函数必须存在，否则会抛出构造函数参数不匹配的异常信息
     *
     * @param mapperInterface mapper接口的类型
     */
    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * 检查mapper的配置
     */
    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = this.getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                // 将mapper添加到configuration配置内
                configuration.addMapper(this.mapperInterface);
            } catch (Exception var6) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", var6);
                throw new IllegalArgumentException(var6);
            } finally {
                ErrorContext.instance().reset();
            }
        }
        // Mapper集合存在该接口实例
        if (configuration.hasMapper(this.mapperInterface)) {

            // 内置的方法重载SqlSource
            logger.debug("Start initializing the MappedStatement list within >> {} << .", mapperInterface.getName());
            mappedStatementSupport.support(new ArrayList(configuration.getMappedStatements()));
            logger.debug("All the MappedStatement SqlSource loading in >> {} << is completed.", mapperInterface.getName());

            // 处理Named方法规则方法的MappedStatement以及重载SqlSource
            logger.debug("Start initializing the Named Method MappedStatement list within >> {} << .", mapperInterface.getName());
            mappedStatementSupport.namedSupport(configuration, this.mapperInterface);
            logger.debug("All the Named Method MappedStatement SqlSource loading in >> {} << is completed.", mapperInterface.getName());
        }

    }

    @Override
    public T getObject() throws Exception {
        return this.getSqlSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return this.mapperInterface;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public boolean isAddToConfig() {
        return this.addToConfig;
    }

    public MappedStatementSupport getMappedStatementSupport() {
        return mappedStatementSupport;
    }

    public void setMappedStatementSupport(MappedStatementSupport mappedStatementSupport) {
        this.mappedStatementSupport = mappedStatementSupport;
    }
}
