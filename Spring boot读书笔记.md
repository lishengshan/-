# Spring boot读书笔记

[Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/reference/htmlsingle/#using-boot-configuration-classes)

[Spring Boot中文文档](https://blog.csdn.net/qq_35242910/article/details/76039911)

## Part Ⅰ

###spring boot 参考指南 1-7

## Part Ⅱ 入门指南

###8-9 系统要求及支持

###10.检查当前java 安装

```
$ java -version
```

maven安装，gradle安装 spring boot CLI 安装  Groovy

###11.开始之前检查安装Java和maven

```
$java -version

$mvn -v
```

###11.1 创建POM

pom.xml

```
<?xml version="1.0" encoding="UTF-8"?> <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"> <modelVersion>4.0.0</modelVersion> <groupId>com.example</groupId> <artifactId>myproject</artifactId> <version>0.0.1-SNAPSHOT</version> <parent> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-parent</artifactId> <version>1.5.2.RELEASE</version> </parent> <!-- Additional lines to be added here... --> </project> 
```

Spring Boot提供了一些“启动器(Starters)”，可以方便地将jar添加到类路径中。我们的示例应用程序已经在POM的父部分使用了spring-boot-starter-parent。spring-boot-starter-parent是一个特殊启动器，提供一些Maven的默认值。它还提供依赖管理 [dependency-management](http://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/reference/htmlsingle/#using-boot-dependency-management) 标签，以便您可以省略子模块依赖关系的版本标签。 



查看依赖关系

```
$ mvn dependency:tree 
```

###11.3 编写代码

**@RestController** ：告诉Spring将生成的字符串直接返回给调用者。

**@RequestMapping**： 提供“路由”信息。 告诉Spring，任何具有路径“/”的HTTP请求都应映射到home方法。

**@EnableAutoConfiguration**:告诉 Spring Boot 根据您添加的jar依赖关系来“猜(guess)”你将如何配置Spring。 

###11.5 创建可执行的jar

​      创建一个完全自包含的可执行jar文件，我们可以在生产环境中运行。 可执行的jar（有时称为“fat jars”）是包含编译的类以及代码运行所需要的所有jar包依赖的归档(archives)。 

$ mvn package 

$ java -jar target/myproject-0.0.1-SNAPSHOT.jar  

ctrl+c正常退出应用程序。 

##Part III. 使用 Spring Boot

##### 继承启动器parent

要将项目配置为继承spring-boot-starter-parent，只需设置标签如下：

```
<parent>

	<groupId>org.springframework.boot</groupId> 

	<artifactId>spring-boot-starter-parent</artifactId>

 	<version>1.5.2.RELEASE</version> 

</parent>
```

要升级到另一个 Spring Data 版本序列，需要将以下内容添加到您的pom.xml中。 

```
<properties>
	<spring-data-releasetrain.version>Fowler-SR2</spring-data-releasetrain.version> 
</properties> 
```

###### 更改java版本

​      spring-boot-starter-parent选择相当保守的Java兼容性版本。 如果要遵循我们的建议并使用更高版本的Java版本，可以添加java.version属性： 

```
<properties> <java.version>1.8</java.version> </properties> 
```

##### 使用Spring Boot Maven插件、

​      Spring Boot包括一个Maven插件，可以将项目打包成可执行jar。 如果要使用它，请将插件添加到部分： 

```
<build> 
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId> 
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins> 
</build> 
```

##14.构建代码

###### 不要使用“default”包

​        当类不包括包声明时，它被认为是在“默认包”中。 通常不鼓励使用“默认包”，并应该避免使用。 对于使用@ComponentScan，@EntityScan或@SpringBootApplication注解的Spring Boot应用程序，可能会有一些特殊的问题，因为每个jar的每个类都将被读取。 

##15.配置类

​        Spring Boot支持基于Java的配置。虽然可以使用XML配置用SpringApplication.run()，但我们通常建议您的主source是@Configuration类。 通常，定义main方法的类也是作为主要的@Configuration一个很好的选择。

不需要将所有的@Configuration放在一个类中。 @Import注解可用于导入其他配置类。 

##16.禁用指定的自动配置

​       如果您发现正在使用一些不需要的自动配置类，可以使用@EnableAutoConfiguration的exclude属性来禁用它们。  

```
import org.springframework.boot.autoconfigure.*; 
import org.springframework.boot.autoconfigure.jdbc.*; 
import org.springframework.context.annotation.*; @Configuration 

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) 
public class MyConfiguration { 

} 
```

##17.Spring Beans 和 依赖注入

​        可以自由使用任何标准的Spring Framework技术来定义您的bean及其依赖注入关系。 为了简单起见，使用@ComponentScan搜索bean，结合@Autowired构造函数(constructor)注入效果很好。

@ComponentScan而不使用任何参数。 所有应用程序组件（@Component，@Service,@Repository，@Controller等）将自动注册为Spring Bean。

我们可以使用构造函数注入获取RiskAssessor bean。

<!--如果一个bean 只有一个构造函数，则可以省略@Autowired--> 

```
package com.example.service; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class DatabaseAccountService implements AccountService {
	
	private final RiskAssessor riskAssessor;
   
    @Autowired
    public DatabaseAccountService(RiskAssessor riskAssessor) {
    this.riskAssessor =  riskAssessor; 
    } // ...
 } 
```

##18.使用@SpringBootApplication注解

​       许多Spring Boot开发人员总是使用@Configuration，@EnableAutoConfiguration和@ComponentScan来标注它们的主类。 由于这些注解经常一起使用（特别是如果您遵循之前说的[最佳实践](http://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/reference/htmlsingle/#using-boot-structuring-your-code)），Spring Boot提供了一个方便的@SpringBootApplication注解作为这三个的替代方法。

@SpringBootApplication注解相当于使用@Configuration，@EnableAutoConfiguration和@ComponentScan和他们的默认属性。

##### 自定义Banner

​        可以通过在您的类路径中添加一个 banner.txt 文件，或者将banner.location设置到banner文件的位置来更改启动时打印的banner。 如果文件有一些不常用的编码，你可以设置banner.charset（默认为UTF-8）。除了文本文件，您还可以将banner.gif，banner.jpg或banner.png图像文件添加到您的类路径中，或者设置一个banner.image.location属性。 图像将被转换成ASCII艺术表现，并打印在任何文字banner上方。 

##### 端口号占用

```
*************************** 
APPLICATION FAILED TO START
***************************
```

## 23. SpringApplication

```
public static void main(String[] args) { 
SpringApplication.run(MySpringConfiguration.class, args); 
} 
```

