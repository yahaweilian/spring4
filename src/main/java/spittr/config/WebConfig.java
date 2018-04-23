package spittr.config;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import spittr.web.HomeController;

/**
 * @author yding
 * DispatcherServlet配置
 */
@Configuration
@EnableWebMvc//启用Spring MVC
@ComponentScan(basePackageClasses = HomeController.class,
includeFilters = @ComponentScan.Filter(Controller.class),
useDefaultFilters = false)
public class WebConfig extends WebMvcConfigurerAdapter{

	@Bean
	public MessageSource messageSource(){//国际化
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasename("messages");//设置在根类路径下
		
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:///messages");//设置在类路径下
//		messageSource.setBasename("file:///etc/spittr/messages");
		messageSource.setCacheSeconds(10);
		return messageSource;
	}
	
	@Bean
	@Order(2)
	public ViewResolver viewResolver(){//配置 jsp视图解析器
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(2);
		resolver.setExposeContextBeansAsAttributes(true);
		resolver.setViewClass(
				org.springframework.web.servlet.view.JstlView.class);//将视图解析为JstlView
		return resolver;
	}
	
	/*----------------------Tiles--------------------------------------------*/
	@Bean
	public TilesConfigurer tilesConfigurer(){//tile页面布局设置
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions(new String[]{
				"/WEB-INF/**/*tiles.xml"
		});
		tiles.setCheckRefresh(true);//启动刷新功能
		return tiles;
	}
	@Bean
	@Order(1)
	public ViewResolver tilesViewResolver(){//将逻辑视图名解析为Tile定义
		TilesViewResolver viewResolver = new TilesViewResolver();//这里要引用tiles3的包，而非tiles2的包，否则会报错：java.lang.ClassNotFoundException: org.apache.tiles.TilesApplicationContext
		viewResolver.setOrder(1);
//		viewResolver.setCache(false);//开发时不启用缓存，改动即可生效
		return viewResolver;
	}
	/*------------------------------------------------------------------------------*/
	/*-------------------------------thymeleaf---------------------------------------*/
	@Bean
	@Order(3)
	public ViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine){//thymeleaf视图解析器
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setOrder(3);
//		viewResolver.setViewNames(new String[]{"*html"});
//		viewResolver.setCache(false);//开发时不启用缓存，改动即可生效
		return viewResolver;
	}
	@Bean
	public SpringTemplateEngine templateEngine(){//模板引擎
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}
	@Bean
	public SpringResourceTemplateResolver templateResolver(){//模板解析器
		SpringResourceTemplateResolver  templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	/*-----------------------------------------------------------------------------*/
	/*------------------------------上传图片的配置-------------------------------------*/
	/*@Bean
	public MultipartResolver multipartResolver() throws IOException{//配置multipart解析器
		return new StandardServletMultipartResolver();
	}*/
	/**
	 * 如果我们需要将应用部署到非Servlet 3.0的容器中， 那么就得需要替代的方案
	 */
	@Bean
	public MultipartResolver multipartResolver() throws IOException{
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//		Resource uploadTempDir = new FileSystemResource("uploads/temps");
//		multipartResolver.setUploadTempDir(uploadTempDir);
		
		multipartResolver.setMaxUploadSize(2097152);
		return multipartResolver;
	}
	
	/*值得一提的是， 如果在编写控制器方法的时候， 通过Part参数的形式接受文件上传， 那么就没有必要配置MultipartResolver了。 只有使
	用MultipartFile的时候， 我们才需要MultipartResolver。*/
	/*--------------------------------------------------------------------------------*/
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		//配置静态资源的处理
		configurer.enable();
	}
	
}
