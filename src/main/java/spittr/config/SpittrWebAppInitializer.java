package spittr.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 对应web.xml配置文件
 *
 */
public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* 
	 * 此方法返回的带有@Configuration注解的类将用来定义DispatcherServlet应用上下文中的bean
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}

	/* 
	 * 返回的带有@Configuration注解的类将用来配置ContextLoaderListener创建的应用上下文中的bean
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};//指定配置类
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};//将DispatcherServlet 映射到"/"p
	}

}
