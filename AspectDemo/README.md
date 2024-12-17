# AspectJ介绍
org.aspectj.aspectjweaver 并不是 Spring 的核心依赖包，而是 AspectJ 框架的一个组成部分。它是用于支持 AspectJ 编程模型的库，通常在 Spring AOP 中使用 AspectJ 风格的切面编程时需要引入。

## 相关背景：

- Spring AOP：Spring 提供的面向切面编程的支持，主要基于代理模式。Spring AOP 可以与 Java 动态代理（JDK代理）或 CGLIB 代理一起使用。
- AspectJ：AspectJ 是一个功能更强大的面向切面编程框架，它支持编译时和加载时切面编织，可以与 Spring 一起使用来提供更多高级的切面编程功能（如切点表达式支持、更加灵活的切面定义等）。
org.aspectj.aspectjweaver 的作用：
- org.aspectj.aspectjweaver 是 AspectJ 的核心包之一，负责在运行时将切面织入到代码中（即"编织"切面）。它通常在 Spring 配置了 AspectJ 风格的切面时使用。
- 当你使用 @Aspect 和 @Around 等注解时，Spring AOP 会自动将它与 AspectJ 集成，允许你在 Spring 应用中使用 AspectJ 提供的更强大功能。

## 需要引入 org.aspectj.aspectjweaver 的情况：
1. 使用 AspectJ 注解（如 @Aspect）时： 如果你在 Spring 中使用 @Aspect 来定义切面（如你提供的 LoginInterceptor），那么需要在项目中引入 aspectjweaver 依赖，确保能够使用 AspectJ 的功能。

2. 使用 AspectJ 编织时： 如果你希望 Spring 使用 AspectJ 编织（例如在应用启动时或编译时将切面代码织入目标类），则需要引入 org.aspectj.aspectjweaver。

示例 Maven 依赖：
如果你使用的是 Maven，并且需要使用 AspectJ 的功能，可以通过以下方式引入依赖：
```xml
<dependency>
<groupId>org.aspectj</groupId>
<artifactId>aspectjweaver</artifactId>
<version>1.9.x</version>  <!-- 请根据需要选择版本 -->
</dependency>
```

## 与 Spring 的关系：
- Spring AOP 默认使用基于 JDK 动态代理或 CGLIB 代理的方式来实现切面功能。如果只使用 Spring AOP，通常不需要引入 aspectjweaver。
- 只有当你需要更强大的 AspectJ 功能，或者需要在 Spring 中使用 AspectJ 注解时，才需要引入 aspectjweaver。
- 
总结：org.aspectj.aspectjweaver 不是 Spring 的依赖，而是 **AspectJ** 的一个核心依赖包。如果你在 Spring 项目中使用 AspectJ 风格的切面（例如 @Aspect 注解），则需要引入该依赖。

# 自定义用户登录校验拦截器
> 面向切面编程（AOP） 来实现一个自定义的登录拦截器，能够拦截特定的请求并进行权限检查。代码通过 Spring AOP 来切入服务层方法，并在方法执行前进行一系列的验证，确保请求是合法的。以下是你代码的详细解析：

核心部分说明：
1. @Aspect 注解：
```java
@Aspect
```
@Aspect 注解标识了该类是一个切面类，它是 Spring AOP 的核心注解之一，用于定义横切关注点。在本例中，LoginInterceptor 类是一个切面，它将拦截特定的请求并进行权限检查。

2. @Component 注解：
```java
@Component
```
@Component 注解使得该切面类成为 Spring 容器管理的 Bean，从而可以被自动注入并启用。

3. @Pointcut 注解：
```java
@Pointcut("within(com.example.aspectdemo.controller.*)")
public void pointCut() {

}
```
这里定义了一个切点（pointCut），它表示在 com.example.aspectdemo.controller 包下的所有方法执行时，都会触发该切面。在切点表达式中，还排除了被 @IgnoreLogin 注解标记的方法，意味着这些方法不需要进行登录验证。

4. @Around 注解：
```java
@Around("pointCut()")
public Object trackInfo(ProceedingJoinPoint joinPoint) throws Throwable {
// ... 权限验证逻辑 ...
}
```
@Around 注解表示环绕通知，在目标方法执行之前和之后都可以加入自己的逻辑。在 trackInfo 方法中，我们可以访问到目标方法的所有参数，并且可以决定是否执行目标方法（joinPoint.proceed()）。

- ProceedingJoinPoint 是 JoinPoint 的子类，提供了一个 proceed() 方法，用来继续执行目标方法，或者中止目标方法的执行。

- 在 trackInfo 方法内，首先检查请求中的 Open-ApiKey 和 X-Service-ID 头，如果存在某些特定条件，则放行该请求。如果请求中没有这些标头，则继续进行 Session 校验。

5. 自定义的权限验证： 代码中通过检查 HTTP 请求中的各种头部信息（如 Open-ApiKey、ALADDIN_AUTH、official-website）来决定是否放行请求，或者是否需要登录验证。

- 如果请求中的 Open-ApiKey 是有效的，系统会根据该 API Key 查询缓存中的凭证，判断凭证是否有效。
- 如果是 td-worker 请求，直接放行。
- 如果请求中包含有效的 YzhToken，且请求方法是 downloadMinioFile，会进行用户信息校验。
- 如果请求中包含 official-website 头部，且该头部为 true，会根据接口方法名决定是否放行。
- 最后，检查 Session 中的用户信息（KeycloakConstants.CURRENT_USER），如果没有用户信息，则抛出 NeedLoginException 异常，表示需要登录。
## 总结：
这段代码实现了面向切面编程（AOP）。它通过切面拦截请求，并在执行目标方法之前进行权限检查。具体使用了以下 Spring AOP 的核心概念：
- 切面（Aspect）
- 切点（Pointcut）
- 通知（Advice）（如 @Around 通知）
这段代码是 Spring AOP 的一个典型应用，能够有效地将权限验证逻辑从业务代码中解耦，提高代码的可维护性和扩展性。

# 测试
```bash
curl --location 'http://localhost:8080/index' \
--header 'Open-ApiKey: 123456'
```