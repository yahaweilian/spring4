package spittr.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import spittr.data.SpitterRepository;
import spittr.service.impl.SpitterUserService;

/**
 * web安全控制
 * @author ynding
 * @version 2018/12/26
 *
 */
@Configuration
//@EnableWebMvcSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	SpitterRepository spitterRepository;
	
	/* 
	 * 用户存储
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		/*--启用内存用户存储--*/
		auth.inMemoryAuthentication()
		//roles()方法所给定的值都会添加一个“ROLE_”前缀， 并将其作为权限授予给用户。
		.withUser("ynding").password("111111").authorities("ROLE_USER").and()
		//和上面的写法是等价的
		.withUser("admin").password("password").roles("USER","ADMIN");
		
		/*--基于数据库表认证 ---*/
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(
				"select username, password, true" + 
		        "from Spitter where username = ?")
		.authoritiesByUsernameQuery(
				"select username, 'ROLE_USER' from Spitter where username=?")
		//转码器
		//用户在登录时输入的密码会按照相同的算法进行转码， 然后再与数据库中已经转码过的密码进行对比。
		//这个对比是在PasswordEncoder的matches()方法中进行的。
		.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
		
		/*--基于LDAP进行认证---*/
		//略
		
		
		/*--自定义服务--*/
		//auth.userDetailsService(new SpitterUserService(spitterRepository));
		
	}
	
	/* 
	 * 
	 */
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		//任何请求会跳到登录界面
		http
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin()
		  //.loginPage("/login")//登录页
		.and()
		.httpBasic();//启用HTTP Basic认证
		//springSecurityFilterChain FIlTER会拦截请求，这个会配置请求跳转的接口，不配置的话，意思是拦截之后没做任何处理。 
		
		//http.csrf().disable();//禁用security的csrf
	}
	
}
