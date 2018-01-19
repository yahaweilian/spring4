package spittr.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/**
 * @author Administrator
 * DispatcherServlet配置
 */
@Configuration
@EnableWebMvc//启用Spring MVC
@ComponentScan("spittr.web")
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
	public ViewResolver viewResolver(){//配置 jsp视图解析器
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		resolver.setViewClass(
				org.springframework.web.servlet.view.JstlView.class);//将视图解析为JstlView
		return resolver;
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer(){//tile页面布局设置
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions(new String[]{
				"/WEB-INF/**/tiles.xml"
		});
		tiles.setCheckRefresh(true);//启动刷新功能
		return tiles;
	}
	@Bean
	public ViewResolver tilesViewResolver(){//将逻辑视图名解析为Tile定义
		return new TilesViewResolver();
	}
	
	/*-------------------------------thymeleaf---------------------------------------*/
	@Bean
	public ViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine){//thymeleaf视图解析器
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		return viewResolver;
	}
	@Bean
	public TemplateEngine templateEngine(){//模板引擎
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}
	@Bean
	public SpringResourceTemplateResolver templateResolver(){//模板解析器
		SpringResourceTemplateResolver  templateResolver = new SpringResourceTemplateResolver ();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}
	/*-----------------------------------------------------------------------------*/
	
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		//配置静态资源的处理
		configurer.enable();
	}
}
