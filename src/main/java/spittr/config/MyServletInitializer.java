package spittr.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;

/**
 *  如果我们想往Web容器中注册其他组件的话， 只需创建一个新的初始化器就可以了。
 *  最简单的方式就是实现Spring的WebApplicationInitializer接口
 *
 */
public class MyServletInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		Dynamic myServlet = servletContext.addServlet("myServlet", MyServlet.class);//注册Servlet
		myServlet.addMapping("/custom/**");//映射Servlet

//		javax.servlet.FilterRegistration.Dynamic filter = 
//				servletContext.addFilter("myFilter", MyFilter.class);
//		filter.addMappingForUrlPatterns(null, false, "/custom/*");
	}

}
