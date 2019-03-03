package spittr.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import spittr.data.SpitterRepository;
import spittr.service.impl.UserDetailsServiceImpl;

/**
 * web安全控制,springSecurityFilterChain 会拦截请求，请求经过如下配置的筛选，才可通过
 * @author ynding
 * @version 2018/12/26
 *
 */
@Configuration
//@EnableWebMvcSecurity
@EnableWebSecurity//当我们在任意一个类上添加了一个注解@EnableWebSecurity，就可以创建一个名为 springSecurityFilterChain 的Filter
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	SpitterRepository spitterRepository;
	
	@Autowired
	private UserDetailsServiceImpl userDetailService;
	
	/* 
	 * 用户存储
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		/*--启用内存用户存储--*/
		/*auth.inMemoryAuthentication()
		//roles()方法所给定的值都会添加一个“ROLE_”前缀， 并将其作为权限授予给用户。
		.withUser("ynding").password("222222").authorities("ROLE_USER").and()
		.withUser("sj-wany").password("111111").authorities("ROLE_USER").and()
		//和上面的写法是等价的
		.withUser("admin").password("password").roles("USER","ADMIN");*/
		
		/*--基于数据库表认证 ，可能不适用h2数据库---*/
		/*auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(
				"select username, password, true " + 
		        "from Spitter where username = ?")
		.authoritiesByUsernameQuery(
				"select username, 'ROLE_USER' from Spitter where username = ?")
		//转码器
		//用户在登录时输入的密码会按照相同的算法进行转码， 然后再与数据库中已经转码过的密码进行对比。
		//这个对比是在PasswordEncoder的matches()方法中进行的。
		.passwordEncoder(new StandardPasswordEncoder("53cr3t"));*/
		
		/*--基于LDAP进行认证---*/
		//略
		
		
		/*--自定义服务--*/
		auth.userDetailsService(userDetailService);
		
	}
	
	/* 
	 * springSecurityFilterChain FIlTER会拦截请求，这个会配置请求跳转的接口，不配置的话，意思是拦截之后没做任何处理，
	 * 以上的用户存储配置都会没什么作用。
	 */
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		//任何请求会跳到登录界面
		http
		.authorizeRequests()
		    // 所有用户均可访问的资源  //不加下面的静态资源使用Thymeleaf 会报错
		    .antMatchers("/resources/**", "/","/**/register").permitAll() 
		    // ROLE_USER的权限才能访问的资源
		    .antMatchers("/spitter/**").hasRole("SPITTER")
		    // 任何尚未匹配的URL只需要验证用户即可访问
			.anyRequest().authenticated()
			.and()
		.formLogin()
		    .loginPage("/spitter/loginForm")//登录页
		    .loginProcessingUrl("/spitter/login")//登录提交的处理Url
		    .defaultSuccessUrl("/spitter/login") //登陆成功的url，这里去到个人首页，get
//		    .passwordParameter("password")//form表单用户名参数名
//            .usernameParameter("username") //form表单密码参数名
//            .successForwardUrl("/spitter/login")  //登录成功跳转地址,都是get，post请求不行
//		    .failureForwardUrl("/spitter/login")//登陆失败进行转发
            .permitAll()////允许所有用户都有权限访问登录页面
		.and()
//		.rememberMe()//这个功能是通过在cookie中存储一个token完成的
//		    .tokenValiditySeconds(2419200)//四周内有效
//		    .key("spittrKey")//存储在cookie中的token包含用户名、 密码、 过期时间和一个私钥——在写入cookie前都进行了MD5哈希。 默认情况下， 私钥的名
		    // 为SpringSecured， 但在这里我们将其设置为spitterKey， 使它专门用于Spittr应用
//		.and()
		.httpBasic();//启用HTTP Basic认证
		
		http.csrf().disable();//禁用security的csrf
	}
	
}
