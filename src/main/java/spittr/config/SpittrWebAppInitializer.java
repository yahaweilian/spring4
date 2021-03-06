package spittr.config;


import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 对应web.xml配置文件
 *
 */

public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* 
	 * 此方法返回的带有@Configuration注解的类将用来定义ContextLoaderListener应用上下文中的bean
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// 添加一个 ContextLoaderListener 来加载 SecurityConfig类
		return new Class<?>[]{RootConfig.class,SecurityConfig.class};
	}

	/* 
	 * 返回的带有@Configuration注解的类将用来配置DispatcherServlet创建的应用上下文中的bean
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};//指定配置类
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/","*.service"};//将DispatcherServlet 映射到"/"
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {//启用multipart请求,和配置
		//将上传文件的临时存储目录设置在“/tmp/spittr/uploads”中
		//假设我们想限制文件的大小不超过2MB， 整个请求不超过4MB， 而且所有的文件都要写到磁盘中
		//上传文件的最大容量（以字节为单位） 。 默认是没有限制的。
		//整个multipart请求的最大容量（以字节为单位） ， 不会关心有多少个part以及每个part的大小。 默认是没有限制的。
		//在上传的过程中， 如果文件大小达到了一个指定最大容量（以字节为单位） ， 将会写入到临时文件路径中。 默认值为0， 也就是所有上传
		//的文件都会写入到磁盘上。
		registration.setMultipartConfig(new MultipartConfigElement("/tmp/spittr/uploads",
				2097152,4194304,0));
		registration.setInitParameter("spring.profiles.active", "dev");//Servlet设置默认的profile
	} 
	
	/*
	 * 如果你只是注册Filter， 并且该Filter只会映射到DispatcherServlet上的话，可以使用如下方式。
	 * 在这里它只返回了一个Filter， 但它实际上可以返回任意数量的Filter。
	 * 在这里没有必要声明它的映射路径， getServletFilters()方法返回的所有Filter都会映射到DispatcherServlet上。
	 * 当然你也可以使用自定义Servlet的方式来自定义Filter
	 */
    @Override 
	protected Filter[] getServletFilters(){ //使用此filter之后，请求无效，页面无反映。初步判断DispatcherServlet 拦截请求 是使用或经过filter
		//return new Filter[] {new MyFilter()};//坑
    	CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        //加了这个有很大不同，首次打开页面，会有登陆控制，和控制台具体sql语句显示，以及错误提示。后面就和以前一样了。看样只在首次有作用
        //首次加载'/',会自动定位到'/login'
        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");
        return new Filter[] {characterEncodingFilter, securityFilterChain};
//        return new Filter[] {characterEncodingFilter};
	}
}
