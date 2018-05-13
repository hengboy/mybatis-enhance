### 欢迎使用MyBatis Enhance

[MENU]


[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.gitee.hengboy/mybatis-enhance-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.gitee.hengboy/mybatis-enhance-spring-boot-starter) 

#### MyBatis Enhance是什么？
`Enhance`是对于原生的`MyBatis`的增强编写，不影响任何原生的使用，使用后完全替代`mybatis-core`、`mybatis-spring`以及`mybatis-spring-boot-starter`，可以使用`SpringBoot`配置文件的形式进行配置相关的内容，尽可能强大的方便快速的集成`MyBatis`。

除此之外还提供了`单表基础数据`的`CRUD`操作以及部分`批量数据`的操作，可以不再使用`MyBatis`提供的自动生成的方式对单个数据表进行数据操作，当然如果你想使用也是可以的。

`Enhance`还规划了多个数据表之间的动态查询方式，这种方式可以让你体验到你在使用`Java代码`编写`SQL语句`，极大方便的关联、聚合、多表查询字段等常用数据动作（`1.0.2.RELEASE`暂未支持）。
#### 使用环境
目前`SpringBoot`的发展趋势已经势如破竹，为了更方便的使用，所以`Enhance`暂时只允许在集成了`SpringBoot`框架的项目中使用。
- `SpringBoot1.x`以上版本
- `JDK 1.6`以上版本
#### 安装
安装比较简单，在引用`Enhance`的项目中你还需要添加你使用的`数据库驱动`以及`数据源`，`Enhance`并不会限制这一点，由于`Enhance`相关的`jar`已经上传到`Maven Center Repository`所以我们只需要添加依赖即可，开发工具会自动下载相关的依赖包。
- 使用`Maven`构建工具时，复制下面的内容到`pom.xml`配置文件内
```
<dependency>
    <groupId>com.gitee.hengboy</groupId>
    <artifactId>mybatis-enhance-spring-boot-starter</artifactId>
    <version>1.0.2.RELEASE</version>
</dependency>
```
- 如果你是用的`Gradle`构建工具，那么复制下面的内容到你的`build.gradle`
```
compile group: 'com.gitee.hengboy', name: 'mybatis-enhance-spring-boot-starter', version: '1.0.2.RELEASE'
```
#### 该怎么使用呢？
##### 实体的创建
根据对应数据库内的表来创建实体，`Enhance`采用的是`Spring Data JPA`的形式来管理实体类，并且已经预先提供的一些`Annotation`，`数据实体(Entity)`对应数据库内的`数据表(Table)`，下面是一个简单的实体代码：
```
/**
 * 用户数据实体
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/5/13
 * Time：8:53
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
Data
@Table(name = "test_user_info")
public class UserInfoEntity implements Serializable {
    /**
     * 用户编号
     */
    @Id(generatorType = KeyGeneratorTypeEnum.AUTO)
    @Column(name = "TUI_ID")
    private Integer userId;
    /**
     * 用户名
     */
    @Column(name = "TUI_NAME")
    private String userName;
    /**
     * 年龄
     */
    @Column(name = "tui_age")
    private Integer age;
    /**
     * 地址
     */
    @Column(name = "tui_address")
    private String address;
}
```
我采用了跟`Spring Data JPA`相同命名方式的注解，这样也方便大家在使用`Enhance`时可以快速的转换注解的使用。
##### Mapper的创建
创建`Mapper`跟我们使用原生`MyBatis`创建方式一样，不过使用`Enhance`后不需要添加`@Mapper`注解，你创建的`Mapper`只需要继承`EnhanceMapper<T,PK>`接口就可以被扫描到，并且同时可以获取内部提供的`CRUD`方法！！！
如下所示：
```
/**
 * 用户基本信息数据接口
 *
 * @author：于起宇 <br/>
 * ===============================
 * Created with IDEA.
 * Date：2018/5/13
 * Time：9:00
 * 简书：http://www.jianshu.com/u/092df3f77bca
 * ================================
 */
public interface UserInfoMapper extends EnhanceMapper<UserInfoEntity, Integer> {
}
```
`EnhanceMapper`需要两个泛型，第一个是实体类的类型，第二个则是实体类主键的类型，这样方便我们在传参或者返回值时做到统一，否则还需要进行`Object`类型的转换，那样不仅麻烦还会提高运行成本，详细介绍请阅读 [使用文档](https://gitee.com/hengboy/mybatis-enhance/blob/master/README.md)
##### 暂时内置的方法
```
// 统计数据
Long countAll() throws EnhanceFrameworkException;
// 清空数据
void deleteAll() throws EnhanceFrameworkException;
// 根据主键数组删除指定数据
void deleteArray(Id... ids) throws EnhanceFrameworkException;
// 根据自定义sql删除数据
void deleteBySql(String sql, Map<String, Object> params) throws EnhanceFrameworkException;
// 根据主键集合删除指定数据
void deleteCollection(Collection<Id> collection) throws EnhanceFrameworkException;
// 删除一条数据
void deleteOne(Id id) throws EnhanceFrameworkException;
// 数据保存
void insert(T t) throws EnhanceFrameworkException;
// 保存数组内的所有数据
void insertArray(T... array) throws EnhanceFrameworkException;
// 保存集合内的所有数据
void insertCollection(Collection<T> collection) throws EnhanceFrameworkException;
// 查询全部数据
List<T> selectAll() throws EnhanceFrameworkException;
// 根据主键数组查询指定数据
List<T> selectArray(Id... ids) throws EnhanceFrameworkException;
// 分页查询数据
List<T> selectByPageable(Pageable pageable) throws EnhanceFrameworkException;
// 自定义sql查询数据
List<Map> selectBySql(String sql, Map<String, Object> params) throws EnhanceFrameworkException;
// 根据主键集合查询指定数据
List<T> selectCollection(Collection<Id> ids) throws EnhanceFrameworkException;
// 根据主键查询单条数据
T selectOne(Id id) throws EnhanceFrameworkException;
// 根据主键更新数据实体
void update(T t) throws EnhanceFrameworkException;
// 自定义sql更新数据
void updateBySql(String sql, Map<String, Object> params) throws EnhanceFrameworkException;
```
以上是`1.0.2.RELEASE`版本提供的内置方法列表，都是在平时开发中比较常用到对单表数据操作的方法。
##### 方法命名规则的使用
`方法命名规则`是`Spring Data JPA`中的提供的一种数据操作的方式，主要适用于`查询`、`统计`、`删除`等数据操作动作，其主要原理是根据方法的名称来自动生成`SQL`，使用正则表达式来进行方法匹配。
###### 方法规则查询
方法规则查询简单示例如下所示：
```
public interface UserInfoMapper extends EnhanceMapper<UserInfoEntity, Integer> {
    /**
     * 只根据一个字段查询
     *
     * @param name 查询条件的值
     * @return
     */
    UserInfoEntity findByUserName(@Param("userName") String name);

    /**
     * 可以根据多个查询条件进行查询
     * 中间使用And进行连接
     *
     * @param name 第一个查询条件的值
     * @param age  第二个查询条件的值
     * @return
     */
    UserInfoEntity findByUserNameAndAge(@Param("userName") String name, @Param("age") Integer age);
}
```
###### 方法规则统计
方法规则统计简单示例如下所示：
```
public interface UserInfoMapper extends EnhanceMapper<UserInfoEntity, Integer> {
    /**
     * 只根据一个字段统计数据
     *
     * @param name 统计条件的值
     * @return
     */
    Long countByUserName(@Param("userName") String name);
    /**
     * 根据多个条件进行统计数据
     *
     * @param name 第一个统计条件的值
     * @param age  第二个统计条件的值
     * @return
     */
    Long countByUserNameAndAge(@Param("userName") String name, @Param("age") Integer age);
}    
```
###### 方法规则删除
方法规则删除简单示例如下所示：
```
public interface UserInfoMapper extends EnhanceMapper<UserInfoEntity, Integer> {
    /**
     * 只根据一个字段删除
     *
     * @param name 查询条件的值
     */
    void removeByUserName(@Param("userName") String name);

    /**
     * 根据多个条件进行删除数据
     * 中间使用And进行连接
     *
     * @param name 第一个删除条件的值
     * @param id   第二个删除条件的值
     */
    void removeByUserNameAndUserId(@Param("userName") String name, @Param("userId") String id);
}   
```
当然`方法命名规则`不仅上面那一点的方便，详细介绍请阅读 [使用文档](https://gitee.com/hengboy/mybatis-enhance/blob/master/README.md)
#### 敬请期待Maven自动化代码生成插件
作为目前开发的环境，不管你是产品项目的研发还是外包项目的研发都应该做到快速实现功能，针对`MyBatis Enhance`框架来说，我单独配套编写了一个`代码生成工具`，能够把`创建实体`、`Mapper`等必须需要类的自动化创建以及可以通过`freemarker`模版来完成自定义的实体类创建，从而实现代码上的真正的快速开发，提高编码效率！！！

#### 敬请期待DSL动态查询
`Enhance`内部默认支持了`单表`的数据操作方法，但是我们平时在项目中多表查询是最普遍的，我结合了`QueryDSL`动态查询框架的优点将部分的基础实现迁移到了`Enhance`框架内，极大的方便了多表联合查询以及动态返回任意`数据实体(Entity)`、`数据映射实体（DTO）`等。