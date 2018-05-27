# spring核心读书笔记

[spring中文文档参考](https://blog.csdn.net/tangtong1/article/details/51960382)

##7.1 spring的IoC简介和bean的简介

**org.springframework.beans**和**org.springframework.context**两个包是Spring IoC容器的基础  

**ApplicationContext**是**BeanFactory**的子接口 

##7.2 容器概述

**org.springframework.context.ApplicationContext**代表了Spring的IoC容器 

**ClassPathXmlApplicationContext**或者**FileSystemXmlApplicationContext**可以创建**ApplicationContext**接口的实例 

**@Configuration**一般被用来初始化配置，有两种方法可以使带有@Configuration的类被初始化，一为让把类所在包的路径纳入scanBasePackages，这样就进入了Spring的扫描范围；还有一种方法就是在spring.factories中用org.springframework.boot.autoconfigure.EnableAutoConfiguration=类的全路径名,这样在项目启动的时候SpringFactoriesLoader会初始化spring.factories（包括引入的jar包中的）中配置的类。在spring.factories配置的好处就是，如果我们想开发一个jar把供其他人使用，那么我们就在自己工程的spring.factories中配置@@Configuration类，这样只要其他人在他们项目的POM中加入了我们开发的jar包作为依赖，在他们项目启动的时候就会初始化我们开发的工程中的类。 

**@Bean** [bean的应用](https://www.cnblogs.com/feiyu127/p/7700090.html)

 **@Import**

**@DepensOn** 

### 7.2.2 实例化容器

```
ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"services.xml", "daos.xml"});
```

 XML形式的**<bean/>**定义。 

#### 组合基于xml的配置元数据

```
<beans> <import resource="services.xml"/> <import resource="resources/messageSource.xml"/> <import resource="/resources/themeSource.xml"/> <bean id="bean1" class="..."/> <bean id="bean2" class="..."/> </beans> 
```

### 7.2.3 使用容器

```
// create and configure beans 
ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"services.xml", "daos.xml"}); 
// retrieve configured instance 
PetStoreService service = context.getBean("petStore", PetStoreService.class); 
// use configured instance 
List<String> userList = service.getUsernameList(); 
```

##7.3 Bean概述

**表 7.1. bean定义**

| 属性             | 参考                                                         |
| ---------------- | ------------------------------------------------------------ |
| class            | [7.3.2 实例化bean](https://blog.csdn.net/tangtong1/article/details/51960382#instantiating-bean) |
| name             | [7.3.1 命名bean](https://blog.csdn.net/tangtong1/article/details/51960382#beans-beanname) |
| scope            | [7.5 bean的作用域](https://blog.csdn.net/tangtong1/article/details/51960382#bean-scopes) |
| 构造方法的参数   | [7.4.1 依赖注入](https://blog.csdn.net/tangtong1/article/details/51960382#beans-factory-collaborators) |
| 属性             | [7.4.1 依赖注入](https://blog.csdn.net/tangtong1/article/details/51960382#beans-factory-collaborators) |
| 自动装配的模式   | [7.4.5 自动装配合作者](https://blog.csdn.net/tangtong1/article/details/51960382#aotowiring-collaborators) |
| 延迟初始化的模式 | [7.4.4 延迟初始化bean](https://blog.csdn.net/tangtong1/article/details/51960382#beans-factory-lazy-init) |
| 初始化的模式     | [初始化回调](https://blog.csdn.net/tangtong1/article/details/51960382#beans-factory-lifecycle-initializingbean) |
| 销毁方法         | [销毁回调](https://blog.csdn.net/tangtong1/article/details/51960382#beans-factory-lifecycle-disposablebean) |

#### 7.3.1 命名bean

每一个bean都有一个或多个标识符。这些标识符在托管bean的容器中必须唯一。一个bean通常只有一个标识符，但如果需要更多标识符，可以通过别名来实现。 

id的唯一性，name可以用来设置别名，bean的id并不是必须的，但如果要应用这个bean，比如通过ref元素或[Service定位器](https://blog.csdn.net/tangtong1/article/details/51960382#beans-servicelocator)查找，那么必须指定一个名字。

bean命名约定：驼峰式。

在bean定义的地方指定所有的别名并不足够，有时候希望在其它地方为bean指定一个别名。在XML配置中，可以使用**<alias/>**元素指定别名。 

例：三个名字引用相同的对象

```
<alias name="subsystemA-dataSource" alias="subsystemB-dataSource"/> 
<alias name="subsystemA-dataSource" alias="myApp-dataSource" /> 
```

#### 7.3.2 实例化bean

如果在**com.example**包中有一个类叫**Foo**，并且**Foo**类中有一个**static**嵌套类叫**Bar**，那么bean定义中的**class**属性的值应该是**com.example.Foo$Bar** 

构造方法实例化

静态工厂

非静态工厂

##7.4依赖

#####基于构造方法的依赖注入

如果没有参数类型潜在的歧义，不需要在**<construct-arg/>**中显式地指定参数的索引或类型。 

如有歧义: <constructor-arg/>  中可以通过name，index,type,等来解决构造方法法中多个参数的问题

*当使用简单类型时， Spring无法判断值的类型，所以没有类型就没办法匹配 。所以需要显示只等类型或索引*

如果没有开启调试标记（或不想）编译，可以使用JDK的注解[@ConstructorProperties](http://download.oracle.com/javase/6/docs/api/java/beans/ConstructorProperties.html)显式地指定参数的名字。 

```
@ConstructorProperties({"years", "ultimateAnswer"}) 
public ExampleBean(int years, String ultimateAnswer) { 
	this.years = years;
	this.ultimateAnswer = ultimateAnswer;
} 
```

#####基于setter方法的依赖注入

```
<bean id="exampleBean" class="examples.ExampleBean"> <!-- setter injection using the nested ref element --> <property name="beanOne"> <ref bean="anotherExampleBean"/> </property> <!-- setter injection using the neater ref attribute --> <property name="beanTwo" ref="yetAnotherBean"/> <property name="integerProperty" value="1"/> </bean> <bean id="anotherExampleBean" class="examples.AnotherBean"/> <bean id="yetAnotherBean" class="examples.YetAnotherBean"/> 
```

Spring容器在创建时会验证每个bean的配置。但是，bean的属性本身直到bean实际被创建时才会设置 

因为可以混合使用基于构造方法和基于setter方法的依赖注入，所以使用构造方法注入强制依赖并使用setter方法或配置方法注入可选依赖是一个不错的原则。注意，在setter方法上使用[@Required](https://blog.csdn.net/tangtong1/article/details/51960382#beans-required-annotation)注解可以使属性变成必需的依赖。  

如果主要使用构造方法注入，很有可能创建一个无法解决的循环依赖场景。  

#### 7.4.2依赖于配置详解

在XML配置中，可以使用<property/>或<constructor-arg/>的属性达到这个目的。 

PropertyEditor最早用于Swing编程中，在Spring中主要被用于xml内value的转换和mvc中参数值得转换。 

idref元素可以把容器中另一个bean的id（字符串值-不是引用）传递给<constructor-arg/>或<property/>元素。 使用idref标签时容器会在部署时验证引用的命名的bean是否实际存在 

可以使用ref让一个bean的指定属性引用另一个bean（合作者）。被引用的bean就是这个bean的一个依赖，并且会在此属性设置前被初始化 

通过parent属性可以指定对当前容器的父容器中的bean的引用 ,而且目标bean必须在当前容器的父容器中。 

内部bean:只能被他的外部bean调用，且随着外部bean一起创建，只有一种边界情况，可以从自定义作用域中接收销毁方法的回调 。

在<list/>, <set/>, <map/>和<props/>元素中，可以分别设置Java集合类型List, Set, Map和Properties的属性和参数； 集合合并 props元素的merge=true ，子集合继承父集合的元素，并重写key相同的值；list是有序的；不能合并不同的集合类型 ；

强类型集合：

```
  public void setAccounts(Map<String, Float> accounts) { 
  this.accounts = accounts;
  }   

  <beans>
 	 <bean id="foo" class="x.y.Foo">
     	 <property name="accounts"> 
  			<map>
  				<entry key="one" value="9.99"/> 
  				<entry key="two" value="2.75"/> 
 		 		<entry key="six" value="3.99"/>
 			</map>
 	 	</property>
 	 </bean>
  </beans> 
```

```
<map>
  	<entry key="one" value="9.99"/> 
  	<entry key="two" value="2.75"/> 
  	<entry key="six" value="3.99"/>
  </map>
```

当foo的accounts属性准备注入的时候，会通过反射获得强类型Map<String, Float>的泛型信息。然后Spring的类型转换机制识别到value的类型为Float，并把String类型的值9.99， 2.75和3.99转换成实际的Float类型 。

<null/>元素处理null值。 

p命名空间不是定义在XSD文件中的，它存在于Spring的核心包中。 

<property name="email" value="foo@bar.com"/> 等价于 p:email="foo@bar.com"/> 

 p:spouse-ref="jane"  -ref表明这不是直接的值而是对另一个对象的引用 

c命名空间于p命名空间类似，配置构造方法的参数 

使用参数的索引，索引符号需要以 _ 开头作为XML属性名，而不能以数字开头（  

<bean id="foo" class="x.y.Foo" c:_0-ref="bar" c:_1-ref="baz"/> 

#### 7.4.3 使用depends-on

depends-on="manager,accountDao" ：属性表示对单例bean的依赖 。

#### 7.4.4 延迟初始化的bean

lazy-init="true" ：延迟初始化 

default-lazy-init="true" ：类计级别的延迟初始化

#### 7.4.5 自动装配合作者

**表7.2 自动装配的模式**

| 模式        | 解释                                                         |
| ----------- | ------------------------------------------------------------ |
| no          | 默认地没有自动自动装配。bean的引用必须通过ref元素定义。对于大型部署，不推荐更改默认设置，因为显式地指定合作者能够更好地控制且更清晰。在一定程度上，这也记录了系统的结构。 |
| byName      | 按属性名称自动装配。Spring为待装配的属性寻找同名的bean。例如，如果一个bean被设置为按属性名称自动装配，且它包含一个属性叫master（亦即，它有setMaster(…)方法），Spring会找到一个名叫master的bean并把它设置到这个属性中。 |
| byType      | 按属性的类型自动装配，如果这个属性的类型在容器中只存在一个bean。如果多于一个，则会抛出异常，这意味着不能为那个bean使用按类型装配。如果一个都没有，则什么事都不会发生，这个属性不会被装配。 |
| constructor | 与按类型装配类似，只不过用于构造方法的参数。如果这个构造方法的参数类型在容器中不存在明确的一个bean，将会抛出异常。 |

##### 避免bean自动装配

**utowire-candidate**属性为false 

#### 7.4.6 方法注入





### 7.5 bean的作用域

**表 7.3. bean的作用域**

| 作用域                    | 描述                                                         |
| ------------------------- | ------------------------------------------------------------ |
| singleton（单例）         | 默认值，每个spring的IoC容器中只保留一份bean定义对应的实例。  |
| prototype（原型）         | 一份bean定义对应多个实例。                                   |
| request（请求）           | 依赖于Http请求的生命周期，也就是说，每个Http请求都有它自己实例。这只在web应用上下文中有效。 |
| session（会话）           | 依赖于Http会话的生命周期。这只在web应用上下文中有效。        |
| globalSession（全局会话） | 依赖于全局Http会话的生命周期。典型地仅当使用在Portlet上下文中有效。这只在web应用上下文中有效。 |
| application（应用）       | 依赖于**ServletContext**的生命周期。这只在web应用上下文中有效。 |
| websocket                 | 依赖于**WebSocket**的生命周期。这只在web应用上下文中有效。   |

#### 7.5.1 单例作用域

一个单例bean仅共享同一个实例，并且所有的请求都只返回同一个实例。 scope="singleton" 

#### 7.5.2 原型作用域

非单例的原型作用域的bean将会导致每次请求时都创建一个新的实例 。scope="prototype" 

spring不会管理一个原型bean的完整生命周期 ，客户端代码必须自己清除原型作用域的对象并释放其占用的资源。 

#### 7.5.3 依赖于原型bean的单例bean



#### 7.5.4 请求、会话、全局会话、应用及WebSocket作用域

为了支持**请求、会话、全局会话、应用及WebSocket作用域**，一些小小的初始化配置是必须的 

请求作用域：scope="request" 

会话作用域：scope="session" 

全局会话作用域：scope="globalSession" 

应用作用域：scope="application" 

##### 有作用域的bean作为依赖项

原型对象上使用 **<aop:scoped-proxy/>** 在共享的代理对象上每次的方法调用都会创建一个新的目标实例。 

#### 自定义作用域

### 7.9 基于注解的容器配置

```
xmlns:context="http://www.springframework.org/schema/context"  

<context:annotation-config/> 
```

#### 7.9.1 @Required

**@Autowired**： setter方法  构造方法  具有任意名字和多个参数的方法  字段 数组字段   

集合类型   Map也可以被自动装配，只要key的类型是**String**就可以 

需要数组或list中的元素按顺序排列的话：使用**@Order**注解或标准的**@Priority**注解。 

@Autowired 默认地，如果没有候选的bean则自动装配会失败。 @Autowired(required=false) 改变这种行为 

#### 7.9.3 使用@Primary微调基于注解的自动装配

多个候选者就使用标记了**@Primary**注解的那个依赖。 

#### 7.9.4 使用限定符微调基于注解的自动装配

 @Qualifier("main") ：带有限定符“main”的bean会被装配到拥有同样值的构造方法参数上。 

也可创建自定义的限定符注解 

```
@Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME)
@Qualifier public 
@interface Genre { 
String value();
} 
```

```
@Autowired 

@Genre("Action") 

private MovieCatalog actionCatalog; 
```

```
 <qualifier type="Genre" value="Action"/> 
```

#### 7.9.7 @Resource

 @Resource(name="myMovieFinder") 

### 7.10 类路径扫描及管理的组件



#### 7.10.1 @Component及其扩展注解

**@Repository**注解是一种用于标识存储类 

 spinrg提供了很多模板注解：@Component、@Service和@Controller。@Component是用于spring管理组件的泛型模板。@Repository、@Service和@Controller是@Component的特例用于更清晰的定义，例如，在持久化、服务和表现层。你可以将你的组件类都声明为@Component，但是通过@Repository、@Service和@Controller会更加的方便出来并与切面相互结合。例如，这些策略可以成为目标的切入点。而且这些在spirng框架的后续版本中会有特殊的功能。如果你在服务层纠结使用@Component还是@Service，那很明显@Service是更好的选择。同样，在开始的时候，@Repository支持持久层的异常translation。

#### 7.10.2 元注解​     

   所有spring提供的注解都可以作为你代码中的元注解。一个元注解可以很简单的运用在另一个注解上。例如，@Service注解就在元注解@Component上。

​       元注解也可以合并用于创建组合注解。例如，spring mvc中的@RestController注解就是@Controller和@ResponseBody的组合。

#### 7.10.3自动检测类并注册bean定义

​      spring可以自动探测模板类并且使用ApplicationContext来注册相应的bean定义。

​       需要被自动探测的类需要注册相应的bean，你需要在@Configuration类上添加@ComponentScan注解，然后基本包路径就是为两个类的通用父包。

​      使用<context:component-scan>暗示着允许<context:annotation-config>的功能。所以当使用<context:component-scan>时可以忽略<context:annotation-config>。

​      用于扫描的包路径必须要以相对路径来表示。

​       AutowiredAnnotationBeanPostProcessor和CommonAnnotationBeanPostProcessor都隐含的包含了当你使用组件扫描元素时。这就意味着这两个组件已经被扫描注入了，不需要在xml中提供bean的配置。

​       正常spring组件中的@Bean方法和用@Configuration修饰的spring类处理方式不同。区别在于@Component修饰的类不会使用CGLIB来加强以实现对方法和属性的调用。cglib代理意味着在@Configuration的类中调用方法和属性，创建的bean元数据指向组合object。这样的方法不再被java声明调用，而是通过容器用于提供通常的生命周期管理和spring的bean的代理通过调用@Bean方法执行其他的bean。相对的，使用普通的@Component类可以调用@Bean方法，而不再使用特殊的cglbi处理或其他假设的应用。

​       像@Autowired注解一样，@Inject也可以修饰field级别、方法级别和构造器参数级别。更进一步，你可以定义你自己的注入点作为Provider，允许依赖访问小范围或延迟访问其他bean通过Provider.get()方法。  

#### 7.11.1 使用@Inject和@Named依赖注入

​       像@Autowired注解一样，@Inject也可以修饰field级别、方法级别和构造器参数级别。更进一步，你可以定义你自己的注入点作为Provider，允许依赖访问小范围或延迟访问其他bean通过Provider.get()方法。

 

### 7.12 基于Java的容器配置

​           @Bean注解用于指定配置和初始化由spring的ioc容器管理的实例。@Bean注解和beans元素中的bean元素有相同的作用。你可以在@Component中使用@Bean注解方法，然而，最常用的是@Configuration修饰bean。

​        当一个@Bean的方法定义在一个没有@Configuration修饰的类中时，他们将会以简化的模式被处理。例如，一个定义在@Component中的bean方法或一个普通的类将会被简化处理。

​       只有@Configuration修饰的类中的@Bean方法才会以full模式的形式被处理。这样可以避免相同的@Bean方法偶尔的被调用多次，也可以减少在lite模式下问题的追踪。

​       spring的AnnotationConfigApplicationContext是在spring3.0中加入的。这个ApplicationContext的实现允许只有@Configuration修饰的类作为输入，也允许普通的@Component类和使用JSR330元数据的注解类。

​       JSR 330 ，提供了一种可重用的、可维护、可测试的方式来获取Java对象。也称为Dependency Injection 。

​       当@Configuration修饰的类作为输入，@Configuration类本身会注册为一个bean的定义，并且所有该类定义的@Bean方法也会注册为bean的定义。

​        默认情况下，使用java配置的bean定义有一个公共的关闭方法在销毁时被回调。如果有公共的销毁回调方法，但是你不希望在容器关闭时调用，那么可以使用@Bean(destroyMethod="")来使得你的bean不调用默认关闭方法。

```
@Configuration
public class AppConfig {
    
    @Bean
    public Foo foo() {
        return new Foo(bar());
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }

}
```

​       这种方法定义内部bean依赖只使用与@Configuration类中的@Bean方法。你不可以在普通的@Component类中定义内部bean依赖。

