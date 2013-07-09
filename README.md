## Demo for nutz


### -1.不是前言的前言

**这是什么**

这是个采用了 [nutz] 做的 demo 项目。

**目的是什么**

~~既然你诚心诚意地问了，我们就大慈大悲地告诉你。为了防止世界被破坏，为了维护世界的和平。贯彻爱与真实的罪恶，可爱又迷人的反派码农。我们是穿梭在银河的屌丝！白洞、白色的明天等着我们！就是这样,喵～~~

为了尽可能的展示 nutz 的功能。

**我需要做什么**

* 继续阅读此文，并跟着一起动手实现。

下面就通过创建一个简单的微博系统来开始我们的 nutz 学习之旅吧。


### 0.准备工作

1. 从 [每日构建地址] 下载 nutz 的最新的每日构建 jar 包
1. 从 [Maven Repository] 下载「commons-dbcp-1.4」、「commons-pool-1.6」、「log4j-1.2.16」、「mysql-connector-java-5.1.25」这4个 jar 包
1. 打开 eclipse，创建一个动态 web 工程，我们把该工程起名为「nutz-weibo」
1. 把之前下载回来的 jar 包放入「/WebContent/WEB-INF/lib」文件夹中
1. 把该工程添加到 tomcat 服务器中

如果做过其他 web 方面的开发的话，相信你很快就把上面的步骤都给做完。


### 1.搭建框架

首先我们在生成一个名为「app.controllers」的 包 ，并在该 包 下生成一个名为「ApplicationController」的类，这是我们的主模块类。

接下来把「/WebContent/WEB-INF/web.xml」文件给修改成如下内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
  <display-name>nutz-weibo</display-name>
  <filter>
    <filter-name>nutz-weibo</filter-name>
    <filter-class>org.nutz.mvc.NutFilter</filter-class>
    <init-param>
      <param-name>modules</param-name>
      <param-value>app.controllers.ApplicationController</param-value>
    </init-param>
  </filter>

  <filter-mapping>
      <filter-name>nutz-weibo</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
</web-app>
```

TIPS：
* 需要注意的是 L8，这里的`param-name`的值就固定为`modules`
* L9 就是我们的主模块类

让我们来验证一下环境是否搭建成功。把「ApplicationController」类改为如下

```java
package app.controllers;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

public class ApplicationController {

    @At("/home/index")
    @Ok("raw")
    public static String index() {
        return "Hello Nutz.";
    }
}
```

接着在浏览器地址栏中输入「http://localhost:8080/nutz-weibo/home/index」，这时你应该在画面中看到「Hello Nutz.」

是的，这代表我们已经把环境给搭建完毕了。让我们休息一下，继续下一部分的学习。

本节内容请参考 [nutz 手册] 中「7 Mvc 手册」的相关部分。


### 3.通过 ioc 方式管理数据库连接

生成一个叫「conf」的源文件夹（Source Folder），在该文件中创建一个叫做「dao.js」的 javascript 文件，内容如下

```javascript
var ioc = {
	dataSource : {
		type : "org.apache.commons.dbcp.BasicDataSource",
		events : {
			depose : 'close'
		},
		fields : {
			driverClassName : 'com.mysql.jdbc.Driver',
			url : 'jdbc:mysql://127.0.0.1:3306/nutz_weibo',
			username : 'root',
			password : 'root123456'
		}
	},
	/* 定义NutDao */
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		fields : {
			dataSource : {
				refer : 'dataSource'
			}
		}
	}
};
```

TIPS：
* L9、10、11行是你的 mysql 数据库的信息，请根据你的实际情况修改这部分。
* 最好同时添加 log4j 用的配置文件让控制台信息更友好些。

到这里有关数据库的 ioc 配置已经完成了，接下来让我们实际的通过数据库查询来验证一下 ioc 注入的正确。

需要注意的是，既然已经用来 ioc 来管理数据库连接，就不需要再在程序中有「new Dao()」这样的处理了，这样很不科学好不好啊。

本节内容请参考 [nutz 手册] 中「5 Ioc 手册」的相关部分。


### 4.创建 weibo 的一览页面（part1--生成model

在 Mysql 中创建名为「nutz_weibo」的数据库，之后创建一个名为「weibos」的表，建表语句如下

```sql
CREATE TABLE `weibos` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`content` TEXT NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
	PRIMARY KEY (`id`)
)
```

接下来创建一个「Weibo」的 POJO ，放在「app.models」包下面

```java
@Table(value = "weibos")
public class Weibo {

    @Id
    private int id;

    @Column
    private String content;

    @Column("created_at")
    private Timestamp createdAt;

    @Column("updated_at")
    private Timestamp updatedAt;

    //getter、setter方法省略
}
```

出现的各注解的意思：
* @Table：表示该 POJO 与哪个表进行关联
* @Id：声明一个整数型的主键（primary key）
* @Column：表示该属性与表中哪个字段关联

到此，POJO 与表的关联就完成了。

本节内容请参考 [nutz 手册] 中「4 Dao 手册」的相关部分。


### 4.创建 weibo 的一览页面（part2--生成controller
在「app.controllers」包下创建一个「WeiboController」的类

```java
@IocBean(fields = "dao")
@At("/weibo")
public class WeiboController extends IdEntityService<Weibo> {

    @At
    @GET
    @Ok("jsp:jsp.weibo.index")
    public List<?> index() {
        return dao().query(Weibo.class, null);
    }
}
```

一如既往的解释下注解的意思
* @IocBean：还记得之前生成的、用来 ioc 用的「dao.js」里面有一个「dao」的变量么，这里就会通过 ioc 方式生成了一个 dao ，再也不用手动管理数据库连接之类的东东了
* @At：这里出现了两次，一次是在类上并且有值，表示这个类里面的所有的 public 方法都是跟 URL 有映射关系，并且默认映射关系的前缀是「/weibo」；一个是在方法中且无值，表示该方法属于入口函数，并且该方法名就是直接跟 URL 有映射关系
* @GET：该方法只对应 get 请求，使用 post 请求的话即使是同一个 URL 也是无法映射到该入口函数的哦
* @Ok：入口函数正常执行后，返回的视图

因为这个 controller 只是单独对 Weibo 这个 POJO 类进行操作，为了方便处理直接继承了一个叫「IdEntityService」的类。这个是 nutz 提供的内置的服务类，内带了一些好用的方法。

还没结束，让我们继续在之前生成的主模块「ApplicationController」上添加一些东西

```java
@IocBy(type = ComboIocProvider.class, args = {"*org.nutz.ioc.loader.json.JsonLoader",
                                              "dao.js",
                                              "*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
                                              "app"})
@Modules({WeiboController.class})
public class ApplicationController {
```

一下就来这么多些东西是不是有些不知所措呢
* @IocBy：ioc 容器的获取方式。这里使用「ComboIocProvider」类实现了 Combo 方式一次性设置多个，方便快捷
* @Modules：指定子模块。如果没写的话系统是不会知道哪个是子模块的哦，也就不知道怎么跟 URL 映射了

那么让我们在浏览器地址栏中输入「http://localhost:8080/nutz-weibo/weibo/index」，看看有什么事情发生

。。。
。。。。。。

是的，出现了 404 错误，那是因为我们还没创建用来显示结果的视图，继续下一步。

本节内容请参考 [nutz 手册] 中「7 Mvc 手册」的相关部分。


### 4.创建weibo的一览页面（part3--生成view
之前「index」方法指定的成功返回视图是「jsp:jsp.weibo.index」，这个值的意思是，返回的为jsp视图（冒号前内容），该jsp文件的位置位于「/WebContent/WEB-INF/jsp/weibo/index.jsp」（也就是在 web.xml 文件同级目录下开始生成视图的路径以及文件）。赶紧创建该「index.jsp」

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<%@ page import="java.util.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Weibo List</title>
  </head>
  <body>
    <h3>Weibo List</h3>
    <% List<Weibo> posts = (List<Weibo>)request.getAttribute("obj"); %>
    <table>
      <tr>
        <th>Id</th>
        <th>Content</th>
        <th>Created At</th>
      </tr>
      <% for (Weibo weibo : posts) { %>
      <tr>
        <td><%= weibo.getId() %></td>
        <td><%= weibo.getContent() %></td>
        <td><%= weibo.getCreatedAt() %></td>
      </tr>
      <% } %>
    </table>
  </body>
</html>
```

TIPS：
* 因为从入口函数返回的对象在 request 中固定叫做「obj」，因此在视图中取得的话用「request.getAttribute("obj");」语句

然后再在浏览器地址栏中输入「http://localhost:8080/nutz-weibo/weibo/index」，是不是出现了一个什么内容都没有的 index 页面呢。

同学，恭喜你，你已经解锁了成就「weibo 一览页面功能完成」。

不过这光秃秃的也没啥好看的，我们可以通过数据库 GUI 工具之类的玩意往表中插入些数据来看看一览页面的效果如何，是不是感觉很有成就感呢。

不过现在才是功能的冰山一角，下一节则是让我们个通过画面往表中插入数据的功能。


### 5.通过画面创建一条 weibo

创建一条weibo我们需要
1）显示登录 weibo 视图的入口函数，以及登录该 weibo 的入口函数
2）一个输入 weibo 的视图

让我们先来生成一个名叫「create」的 JSP（生成路径与之前的「index」相同）

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>New Weibo</title>
  </head>
  <body>
    <h3>new weibo</h3>
    <form action="../weibo/save" method="post">
      <p>
        <label for="content">content</label>
        <input type="text" name="weibo.content" id="weibo.content" />
      </p>
      <input type="submit" name="submit" value="create" />
    </form>
  </body>
</html>
```

页面很简单，就一个 form，里面有个「input[type=text]」的输入框，以及一个「input[type=submit][value=create]」 submit 按钮。

接下来回到「WeiboController」类里添加个显示该视图的入口函数

```java
    @At
    @GET
    @Ok("jsp:jsp.weibo.create")
    public void create() {

    }
```

浏览器地址栏中输入「http://localhost:8080/nutz-weibo/weibo/create」，是不是 weibo 输入画面就出现了呢。

但是当你想点击 submit 按钮试图往表中插入一个记录的话，会无情的返回一个 404 错误，这是因为我们还没有处理提交该 form 指定的 action 的方法，继续在「WeiboController」类添加个「save」的方法吧

```java
    @At("/save")
    @POST
    @Ok("redirect:/weibo/index")
    public void save(@Param("::weibo.") Weibo weibo) {
        dao().insert(weibo);
    }
```

继续讲解这里出现的高级玩意
* @Ok("redirect:/weibo/index")：「redirect」表示内部重定向，会再发起一个 URL 请求找到该 URL 对应的入口函数进行处理
* @POST：该方法只对应 post 请求，也就是说直接通过浏览器地址栏来输入 URL 的话是不会进到该入口函数的哦
* @Param("::weibo.")：在画面的 form 里「input[type=text]」的名字是「weibo.content」，这时我们可以直接在对应的入口函数的参数里面使用「@Param("::前缀.")」这样的前缀表单方式一次性生成出一个对应的对象出来。需要注意的是，只支持「类名.属性名」这样的类 EL 表达式的方式，「类名_属性名」、「类名-属性名」之类的则是不支持的

让我们再回到输入画面输入我们的第一条 weibo ，点下 submit 按钮，这时候跳转到的「weibo/index」页面上是不是看到了刚才输入的内容呢。

本节内容请参考 [nutz 手册] 中「7 Mvc 手册」的相关部分。

### 6.显示一条 weibo

根据主键查询是个很简单的操作，直接在「WeiboController」类添加个「show」的方法

```java
    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Weibo weibo = fetch(id);
        return weibo;
    }
```

注解内容解释
* @At("/?/show")：这里的「@At」注解的值出现了个「?」这个符号，这表示的是一个占位符，其值就是传入方法中的第一个参数

TIPS：
* L5 的「fetch(id)」这个通过主键检索的方法是「IdEntityService」这个内置服务类自带的方法

为了把内容表示出来我们还需要一个名叫「show」的 JSP

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>show weibo</title>
  </head>
  <body>
    <h3>show post</h3>
      <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
      <p>
        <label for="content">content</label><br />
        <%= weibo.getContent() %>
      </p>
  </body>
</html>
```

很简单的画面是不是，直接得到入口函数中返回的对象并表示出来。

为了更方便操作些稍微修改一下「index」页面

```jap
<td><a href='../weibo/<%= weibo.getId() %>/show'><%= weibo.getId() %></a></td>
```

当点击画面上的「ID」link 的时候，这是会迁移到 show 画面，并显示出该 ID 对应的 weibo 的内容

如果没出意外，在画面上出现的将是该 weibo 的内容。

不过，之前测试的时候输入了无意义的内容，怎么能修改它呢？


### 7.修改 weibo 的内容

既然能创建也要能修改对吧，那么怎么做呢。

跟上一节一样，首先还是在「WeiboController」类添加个「edit」的入口函数

```java
    @At("/?/edit")
    @GET
    @Ok("jsp:jsp.weibo.edit")
    public Weibo edit(int id) {
        Weibo weibo = fetch(id);
        return weibo;
    }
```

注意观察的你可能已经发现了，这不就是跟「show」方法的处理几乎一样么。是的，除了 URL 映射跟返回的视图不一样外他们内部处理现在来看是一样的，而之所以这么做是因为个人风格原因。。。

接着生成一个名叫「edit」的 JSP

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit Weibo</title>
  </head>
  <body>
    <h3>edit weibo</h3>
    <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
    <form action="../../weibo/<%= weibo.getId() %>/update" method="post">
      <input type="hidden" name="weibo.id" id="weibo.id" value='<%= weibo.getId() %>' />
      <p>
        <label for="content">content</label>
        <textarea name="weibo.content" id="weibo.content"><%= weibo.getContent() %></textarea>
      </p>
      <input type="submit" name="submit" value="update" />
    </form>
  </body>
</html>
```

然后给「show」画面添加一个 link 让画面能跳转过来

```jsp
<a href='../../weibo/<%= weibo.getId() %>/edit'>Edit this weibo</a>
```

画面是有了，不过还没有进行更新的处理，继续在「WeiboController」类添加个「update」的入口函数

```java
    @At("/?/update")
    @POST
    @Ok(">>:/weibo/${obj.id}/show")
    public Weibo update(@Param("::weibo.") Weibo weibo) {
        weibo.setUpdatedAt(new Timestamp(Times.now().getTime()));
        dao().updateIgnoreNull(weibo);
        return weibo;
    }
```

注解解释
* @Ok(">>:/weibo/${obj.id}/show")：内部重定向到「show」画面。「>>」是「redirect」的另外一种写法；而「${obj.id}」则表示使用该方法的返回对象的某一个属性来填充

TIPS：
* 因为这里传入的 weibo 对象里面的「createdAt」的值是「null」，因此需要用到「dao().updateIgnoreNull()」这个方法在更新的时候忽略值为「null」的属性，不然的话就会把原来的值给更新为「null」了

最后，在「edit」画面点「update」按钮看看效果。


### 8.删除一条 weibo

不好，刚才写的内容太无节操，需要把它给删掉，怎么做呢

赶紧在「WeiboController」类添加个「destroy」方法

```java
    @At("/?/destroy")
    @POST
    @Ok(">>:/weibo/index")
    public void destroy(@Param("id") int id) {
        dao().delete(fetch(id));
    }
```

注解解释
* @Param("id")：因为是个 post 请求的方法，自然需要安全一些，这里的「id」的意思表示是 http 传过来的、名为「id」的变量的值，即使「@At」中出现了「?」这个占位符，也不会使用该值

接着在「show」画面添加一个删除的按钮

```jsp
      <a href='../../weibo/<%= weibo.getId() %>/edit'>Edit this weibo</a>
      <form action="../../weibo/<%= weibo.getId() %>/destroy" method="post">
        <input type="hidden" name="id" id="id" value='<%= weibo.getId() %>' />
        <input type="submit" name="submit" value="delete this weibo" />
      </form>
```

因为「destroy」方法只对应 post 请求，因此需要在画面通过 form 来发起 post 提交。不想使用 form 来发起提交的话，也就剩下了用 Ajax 来发起了。

然后我们只需要点下该按钮，谁还能说我无节操来着。


### 9.给某条 weibo 添加 comment（part1--生成model

当正好看到某无节操的 weibo 正适合你口味的时候，总会忍不住的给它来个评论（comment）的吧，我们来实现你的需求。

跟 weibo 一样，先建一个「comments」表

```sql
CREATE TABLE `comments` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`content` TEXT NOT NULL,
	`weibo_id` INT(10) NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
	PRIMARY KEY (`id`)
)
```

接下来再来一个对应的 POJO 类

```java
@Table(value = "comments")
public class Comment {

    @Id
    private int id;

    @Column
    private String content;

    @Column("weibo_id")
    private int weiboId;

    @Column("created_at")
    private Timestamp createdAt;

    @Column("updated_at")
    private Timestamp updatedAt;

    //getter、setter方法省略
}
```

在实际情况中，一条 weibo 是对应着多个 comment ，也就是一对多的关系。因此不仅需要在 comment 类里面有个跟 weibo 进行关联的属性「weiboId」外，还需要给 weibo 添加个跟 comment 进行关联的属性

```java
    @Many(target = Comment.class, field = "weiboId")
    private Comment[] comments;

    //getter、setter方法省略
```

一如既往的注解解释
* @Many(target = Comment.class, field = "weiboId")：「target」的值写对应的实体类；「field」的值则是关联字段，写的是对应的实体类的属性名（注意，这不是表的字段名）


### 9.给某条 weibo 添加 comment（part2--修改 controller

基本上都是在现实具体的 weibo 的时候，同时显示其所有的 comment ，因此我们这里修改的是 WeiboController 类中「show」这个方法

```java
    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Weibo weibo = fetch(id);
        dao().fetchLinks(weibo, "comments");
        return weibo;
    }
```

在这里 L6 添加的「fetchLinks」方法，表示查找该 weibo 下所有的 comment，并放在「comments」这个属性中。

其实这里更简单的写法

```java
return dao().fetchLinks(fetch(id), "comments");
```


### 9.给某条 weibo 添加 comment（part3--修改 view

数据已经准备好，只剩下修改「show」JSP 了

```jsp
  <body>
    <h3>show weibo</h3>
      <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
      <p>
        <label for="content">content</label><br />
        <%= weibo.getContent() %>
      </p>
      <a href='../../weibo/<%= weibo.getId() %>/edit'>Edit this weibo</a>
      <form action="../../weibo/<%= weibo.getId() %>/destroy" method="post">
        <input type="hidden" name="weibo.id" id="weibo.id" value='<%= weibo.getId() %>' />
        <input type="submit" name="submit" value="delete this weibo" />
      </form>
    <hr />
    <h4>comment list</h4>
    <table>
      <tr>
        <th>Id</th>
        <th>Content</th>
      </tr>
      <% for (Comment comment : weibo.getComments()) { %>
      <tr>
        <td><%= comment.getId() %></td>
        <td><%= comment.getContent() %></td>
      </tr>
      <% } %>
    </table>
  </body>
```

也就是添加个循环来表示而已，很简单的。

用数据库 GUI 之类的工具往 comments 表里面登录几条数据试试看看是不是在画面中出现了你同样没有节操的评论呢。记得「weibo_id」这个字段的值跟画面上表示的 weibo 的「id」是同一个值。


### 10.通过画面添加 comment

继续修改「show」JSP 

```jsp
    <h4>comment list</h4>
    <table>
      <tr>
        <th>Id</th>
        <th>Content</th>
      </tr>
      <% for (Comment comment : weibo.getComments()) { %>
      <tr>
        <td><%= comment.getId() %></td>
        <td><%= comment.getContent() %></td>
      </tr>
      <% } %>
    </table>
    <hr />
    <h4>add comment</h4>
    <form action="../../weibo/<%= weibo.getId() %>/comment/save" method="post">
      <input type="hidden" name="comment.weiboId" id="comment.weiboId" value='<%= weibo.getId() %>' />
      <p>
        <label for="comment.content">content</label>
        <input type="text" name="comment.content" id="comment.content" />
      </p>
      <input type="submit" name="submit" value="submit" />
    </form>
```

跟创建 weibo 一样，需要先添加个 form ，里面有个「input[type=text][name=comment.content]」的输入框用来输入 comment 的内容，而那个「input[type=hidden]」则是为了保存住需要关联的 weibo 的「id」这个属性。

因为操作的是另一个 POJO 对象，按照习惯会生成一个新的 Controller 类（子模块）来对它进行操作

```java
@IocBean(fields = "dao")
@At("/weibo/?")
public class CommentController extends IdEntityService<Comment> {

    @At("/comment/save")
    @POST
    @Ok(">>:/weibo/${obj.weiboId}/show")
    public Comment save(@Param("::comment.") Comment comment) {
        Comment createdComment = dao().insert(comment);
        return createdComment;
    }
}
```

这里都没啥新鲜玩意，基本跟「WeiboController」类一样。

不过这还没做完，还需要在「ApplicationController」类中改一个地方

```java
@Modules({WeiboController.class, CommentController.class})
public class ApplicationController {
```

在「Modules」注解里面把刚才新添加的「CommentController」给写上，让它被容器管理。

不过这也有些麻烦，要是有 N 个Controller 的话就需要把那 N 个类都加到上面，显然不太智能。所以这里可以改成让它自动扫描

```java
@Modules(scanPackage = true)
public class ApplicationController {
```

「scanPackage = true」只能用在主函数上，表示自动扫描所有的子模块并加载，也就把所有的 Controller 类给加载进来。

再次回到画面，在 comment 中输入评论并点提交按钮，是不是画面中已经出来了呢。


### 11.删除一条 weibo，Again（part1

在之前的删除 weibo 的处理的时候我们是直接是通过主键把这条 weibo 给删掉，但是在增加了 comments 这样的一对多的时候，也需要同时删除这条 weibo 下的所有 comments 才行，以免数据库中出现冗余数据

```java
    @At("/?/destroy")
    @POST
    @Ok(">>:/weibo/index")
    public void destroy(@Param("id") int id) {
        Weibo weibo = dao().fetchLinks(fetch(id), "comments");
        dao().deleteWith(weibo, "comments");
    }
```

原来的一条语句现在变成了两条，这是为什么呢
* L5：不仅得到该 id 对应的 weibo，还能得到该 weibo 下所有的 comments
* L6：通过「deleteWith」方法同时删除 weibo 以及其拥有的 comments


快来试试效果如何。


### 11.删除一条 weibo，Again（part2

接下来换另一种做法

```java
    @At("/?/destroy")
    @POST
    @Ok(">>:/weibo/index")
    public void destroy(@Param("id") final int id) {
        Trans.exec(new Atom(){
            public void run() {
                delete(id);
                dao().clear(Comment.class, Cnd.where("weibo_id", "=", id));
            }
        });
    }
```

。。。我了个去，这种方法不但代码没减少，还一下多出那么多东西，作者乃不是在玩我吧。

>解释 start
>
>这里使用的是手动启动事务处理的方式
>
>解释 end

从 L5 到 L10 是 nutz 中事务的标准写法，实际的逻辑处理则是在「run()」方法里面进行处理。


### 11.删除一条 weibo，Again（part3

有些同学想手动操作数据库，应该怎么做呢

```java
    @At("/?/destroy")
    @POST
    @Ok(">>:/weibo/index")
    public void destroy(@Param("id") final int id) {
        Trans.exec(new Atom() {
            public void run() {
                Sql deleteWeibo = Sqls.create("DELETE FROM weibos WHERE id=@id");
                deleteWeibo.params().set("id", id);
                dao().execute(deleteWeibo);

                Sql deleteComments = Sqls.create("DELETE FROM $table WHERE weibo_id=@weibo_id");
                deleteComments.vars().set("table", "comments");
                deleteComments.params().set("weibo_id", id);
                dao().execute(deleteComments);
            }
        });
    }
```

以上做法，在 nutz 中叫做使用「自定义 SQL」方式。

* 首先是设置检索条件。其实原理是跟 PreparedStatement 一样来着只不过在 nutz 中用的是具名替换的方式，即用「@xxx」来代替了「index」。
* L7--L9 是删除 weibo，首先通过「Sqls.create(String)」方法生成出一个 Sql 对象，接下来往里面填充「id」的值，最后通过「dao().execute」方法执行该sql。
* 而删除 comment 的操作是在L11--L14，跟上面进行比较发现只是在 L12 多了一个填充表名的处理。


### 12.进击的自定义 SQL（part1--使用手动回调的方式

nutz 中，「自定义 SQL」的方式可以完成所有通过 SQL 来跟数据库进行交互的操作。之前我们已经展示了无返回值时是如何使用「自定义 SQL」的过程，下面让我们修改「show」方法来看看使用了「自定义 SQL」时如何取得检索结果

```java
    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Sql sql = Sqls.create("SELECT weibos.* FROM weibos WHERE id=@id");
        sql.params().set("id", id);
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                Weibo weibo = new Weibo();
                while (rs.next()) {
                    weibo.setId(rs.getInt("id"));
                    weibo.setContent(rs.getString("content"));
                    weibo.setCreatedAt(rs.getTimestamp("created_at"));
                    weibo.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
                return weibo;
            }
        });
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
    }
```

执行有返回值的「自定义 SQL」最重要的处理就是在如 L7--L18 所示的「sql.setCallback()」方法里，而所谓的重要也就只是赋值并返回罢了。


### 12.进击的自定义 SQL（part2--使用 nutz 内置回调的方式

要是从「ResultSet」里取得很多字段（比如50个）的时候，手动回调的方式来实现的话你需要一项一项的来赋值这绝对的是个苦差。

如果要我来做的话我大概会用反射的方式来进行赋值处理，虽然有些麻烦但至少免去了写各种 setter 语句。

不过，在 nutz 里面内置了好几款回调方式，任亲挑选，总有一款适合亲哦。

所以之前的做法我们可以换成这种方式

```java
    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Sql sql = Sqls.create("SELECT weibos.* FROM weibos WHERE id=@id");
        sql.params().set("id", id);
        sql.setCallback(Sqls.callback.entity());
        sql.setEntity(dao().getEntity(Weibo.class));
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
    }
```

之前在「sql.setCallback()」方法里面写的那么一大坨内容被一句话「Sqls.callback.entity()」给代替了，这个就是 nutz 自带的回调方法，表示获取一个对象。

而接下来的「sql.setEntity()」则是设置一个表与其对应的对象关联关系的那么个实体（在 nutz 中这东西叫做 Entity）。没设置这东西的话会无情的抛出异常的哦。

不过，以上代码还可以更简便些

```java
    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Sql sql = Sqls.fetchEntity("SELECT weibos.* FROM weibos WHERE id=@id")
                .setEntity(dao().getEntity(Weibo.class));
        sql.params().set("id", id);
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
    }
```

「Sqls.fetchEntity()」就是「Sqls.create().setCallback(Sqls.callback.entity())」的简写。

有关 nutz 内置回调方面的内容请参照「Sqls.java」代码。


### 13. 出现的错误哪有这么可爱

让我们来考虑一下两种
* 数据库里面只有10条 weibo 的数据，然后在地址栏输入「http://localhost:8080/nutz-weibo/weibo/100/show」
* 直接在地址栏输入「http://localhost:8080/nutz-weibo/weibo/hello/show」

第一种情况通常做法就是跳转到 404 页面，第二种则是跳转到一个错误页面。那么，应该如何处理呢。

花开两枝各表一朵。

对于第一种情况，因为查不到数据的时候得到的对象是 null， 可以直接在「@Ok」里面判断返回的对象来实现渲染不同的页面，

```java
    @At("/?/show")
    @GET
    @Ok("jsp:${obj != null ? 'jsp.weibo.show' : '404'}")
    public Weibo show(int id) {
        Sql sql = Sqls.fetchEntity("SELECT weibos.* FROM weibos WHERE id=@id")
                .setEntity(dao().getEntity(Weibo.class));
        sql.params().set("id", id);
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
    }
```

关键点是在第三行，首先判断返回对象，存在了跟原来一样渲染「show」视图，不存在的话返回「404」视图。
注意，这里的「404」视图是自己定义的一个页面，而不是服务器自带的那个 404 页面，因此需要直接在「WEB-INF」目录下创建个「404.jsp」（注意路径为什么不是在 jsp 文件夹下），里面随便写些东西比如

```jsp
<h3>Hi man. Here is 404 page.</h3>
```

可能你想说, nutz 不是自带这样的「@Ok("http:404")」方法来实现 404 页面的显示么。

各位观众，不好意思，这种写法在这样带分支判断的里面是行不通。原因在于，这里的「@Ok」注解里面已经写上了使用 jsp 视图进行渲染（再次提醒一下，冒号前面的意思是采用哪种视图模板进行渲染），而 http 是「HTTP 返回码视图」，无法做到返回两种不同的视图，是不是感到有些遗憾呢。

下面说一下第二种情况，因为我们方法参数写的是 int 类型，而通过 url 传过来的是个 String 类型的变量，铁定是出类型转换错误的异常了。在这里我们使用「@Fail」注解来实现错误页面。

```java
    @At("/?/show")
    @GET
    @Ok("jsp:${obj != null ? 'jsp.weibo.show' : '404'}")
    @Fail("jsp:503")
    public Weibo show(int id) {
        Sql sql = Sqls.fetchEntity("SELECT weibos.* FROM weibos WHERE id=@id")
                .setEntity(dao().getEntity(Weibo.class));
        sql.params().set("id", id);
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
    }
```

这里需要解释一下的是，「@Fail」只是用来处理程序异常（Exception）用的，如果是想用来处理逻辑验证错误的话这个注解真管不了。

跟「404.jsp」一样还需要弄个「503.jsp」页面，

```jsp
<h3>Hi man. Here is 503 page.</h3>
```

而直接在主函数上加上这个注解的话，所有的入口函数就会自动的带上「@Fail」的处理

```java
@Fail("jsp:503")
public class ApplicationController {
```


### 14. 冲出亚洲，走向世界--进行 i18n 处理

为了方便你进行全球化扩展，nutz 已经内置了一个 i18n 的处理。

首先，需要在「conf/msg」下创建个名为「zh-CN」的文件夹，文件夹则是根据是 java.util.Locale 的来进行定义，比如中文是「zh-CN」,日语是「ja_JP」。接着在这个文件夹里面生成个「msg.properties」的配置文件。

之后就是往该文件里面写上 i18n 的内容了，每个文件对应的是该 Locale 用的表示内容，

```property
# model
weibo.id=微博ID
weibo.content=微博内容
weibo.created_at=创建时间
comment.id=评论ID
comment.content=评论内容

# view
weibo.view.index.label.weibo_list=微博一览
weibo.view.show.label.show_weibo=微博信息
weibo.view.show.button.edit_weibo=编辑该微博
weibo.view.show.button.delete_weibo=删除该微博
weibo.view.index.label.comment_list=评论一览
weibo.view.show.label.add_comment=添加评论
weibo.view.show.button.add_comment=添加
weibo.view.edit.label.edit_weibo=编辑微博
weibo.view.edit.button.edit_weibo=更新
weibo.view.create.label.create_weibo=创建微博
weibo.view.create.button.create_weibo=创建
```

虽然已经完成了翻译的内容，不过要让这些文件加载到环境里面的话，还需要在主模块加上个「@Localization」注解指定一下配置文件所在目录，并且该目录下所有的配置文件都是 i18n 用的配置文件。

```java
@Localization("msg")
public class ApplicationController {
```

在 nutz 中，会自动的把这些文件加载到系统，之后在画面上直接从 request 中通过「msg」这个 key 取得内容

```jsp
<% Map<String, String> message = ((Map<String,String>)request.getAttribute("msg")); %>
<h3><%= message.get("weibo.view.index.label.weibo_list") %></h3>
```

单单一个语言的话看不出 i18n 的好处，接下来我们在「en_US」再添加一个「msg.properties」文件后，来看看系统中如何实现切换语言的处理。

其实很简单，就新加个入口函数就行，比如在主模块中添加那么段代码

```java
    @At("/weibo/change/?")
    @Ok(">>:/weibo/index")
    public void changeLocal(String lang) {
        lang = Lang.list("zh_CN", "en_US").contains(lang)? lang : "en_US";
        Mvcs.setLocalizationKey(lang);
    }
```

还需要在画面上加上切换语言用的 link，比如直接在「index」页面添加

```jsp
    <a href='../weibo/change/zh_CN'>中文</a>
    <a href='../weibo/change/en_US'>English</a>
```

之后，就能切换语言玩了。

有的同学还想问，我想让系统有个默认的现实语言的话肿么破，很简单，修改一下「@Localization」注解就行

```java
@Localization(value="msg", defaultLocalizationKey="en_US")
```

「defaultLocalizationKey」的值就是系统默认语言的值了。

需要注意的是，这里只是进行了语言的切换，并没有把系统的 Locale 给变动。如果程序中有需要根据不同的 Locale 进行不同处理的话还是需要自己来做这方面的处理。

>天外迷音 start
>
>一直都觉得在配置文件方面，使用「yaml」会比「properties」来得跟好些，因为「yaml」会呈现出一种层次关系，而「properties」则是个 Hashtable 的实现。在很久很久以前 nutz 曾经是自带了个 yaml 的解析来着，后来因为某种未名原因从 master 主线上给删掉了。
>
>天外迷音 end


### 99. 不是结尾的结尾

基本上通过这个小小的东西认识到了 nutz 的绝大部分内容。其实还有些东西比如像什么 aop 、自定义视图之类的都没用到，因此遇到问题的时候，还需要翻看 [nutz 手册] 来查找相关内容。

接下来你可以给这个项目添加些功能，比如说用户登录、编辑・删除权限（知道为什么在第 7 步的时候「show」方法跟「edit」方法拆开来处理了么，就是为了方便判断啊）之类的。

~~我们还会回来的。。。~~

祝你玩得开心


[nutz]: https://github.com/nutzam/nutz
[每日构建地址]: https://oss.sonatype.org/content/repositories/snapshots/org/nutz/nutz/
[Maven Repository]:http://mvnrepository.com/
[nutz 手册]:http://nutzam.github.io/nutz/
[如何配置 web.xml]:http://nutzam.github.io/nutz/mvc/web_xml.html
