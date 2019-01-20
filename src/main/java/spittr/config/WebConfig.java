package spittr.config;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.swing.JScrollBar;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.h2.command.dml.SelectOrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import spittr.controller.HomeController;
import spittr.service.AlertService;
import spittr.service.SpitterService;

/**
 * DispatcherServlet配置，对应spring-mvc.xml,以及其他servlet配置
 * @author yding 
 */
@Configuration
@EnableWebMvc // 启用Spring MVC
@ComponentScan(basePackageClasses = HomeController.class, includeFilters = @ComponentScan.Filter(Controller.class), useDefaultFilters = false)
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public MessageSource messageSource() {// 国际化信息、错误信息
		// ResourceBundleMessageSource messageSource = new
		// ResourceBundleMessageSource();
		// messageSource.setBasename("messages");//设置在根类路径下
		
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:///ENG_messages");// 设置在类路径下
		// messageSource.setBasenames("classpath:///messages","classpath:///ValidationMessages");//验证信息文件默认ValidationMessages
		// messageSource.setBasename("file:///etc/spittr/messages");
		messageSource.setCacheSeconds(10);
		return messageSource;
	}

	/*
	 * @Bean public ValidatorFactory validatorFactory(MessageSource
	 * messageSource){//这里注释掉使用默认 LocalValidatorFactoryBean validatorFactory =
	 * new LocalValidatorFactoryBean();
	 * validatorFactory.setProviderClass(HibernateValidator.class);
	 * validatorFactory.setValidationMessageSource(messageSource); return
	 * validatorFactory; }
	 */

	@Bean
//	 @Order(1)
	public ViewResolver viewResolver() {// 配置 jsp视图解析器
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(2);
		resolver.setExposeContextBeansAsAttributes(true);
		resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);// 将视图解析为JstlView
		return resolver;
	}

	/*----------------------Tiles--------------------------------------------*/
	@Bean
	public TilesConfigurer tilesConfigurer() {// tile页面布局设置
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions(new String[] { "/WEB-INF/**/*tiles.xml" });
		tiles.setCheckRefresh(true);// 启动刷新功能
		return tiles;
	}

	@Bean
//	 @Order(1)
	public ViewResolver tilesViewResolver() {// 将逻辑视图名解析为Tile定义
		TilesViewResolver viewResolver = new TilesViewResolver();// 这里要引用tiles3的包，而非tiles2的包，否则会报错：java.lang.ClassNotFoundException:
																	// org.apache.tiles.TilesApplicationContext
		viewResolver.setOrder(1);
		viewResolver.setCache(false);// 开发时不启用缓存，改动即可生效
		return viewResolver;
	}

	/*------------------------------------------------------------------------------*/
	/*-------------------------------thymeleaf---------------------------------------*/
	@Bean
//	 @Order(3)
	public ViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {// thymeleaf视图解析器
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setOrder(3);
		// viewResolver.setViewNames(new String[]{"*html"});
		viewResolver.setCache(false);// 开发时不启用缓存，改动即可生效
		return viewResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(Set<ITemplateResolver> resolvers) {// 模板引擎
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setTemplateResolvers(resolvers);
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {// 模板解析器
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(2);
		return templateResolver;
	}

	@Bean
	public ClassLoaderTemplateResolver emailTemplateResolver() {// 发送带有html内容邮件所用
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("mail/");
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setOrder(1);//优先选择此模板解析器
		return resolver;
	}
	/*-----------------------------------------------------------------------------*/
	/*---------------------------REST----------------------------------------------*/
	
	/*@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
		//ContentNegotiationConfigurer中的一些方法对应于ContentNegotiationManager的Setter方法
		configurer.defaultContentType(MediaType.TEXT_HTML);//设置了默认的内容类型为HTML
	}
	@Bean
	public ViewResolver cnViewResolver(ContentNegotiationManager cnm){
		//不建议使用
		ContentNegotiatingViewResolver cnvr = new ContentNegotiatingViewResolver();
		cnvr.setContentNegotiationManager(cnm);
		return cnvr;
	}*/
	
	
	
	
	/*------------------------------上传图片的配置-------------------------------------*/
	/*
	 * @Bean public MultipartResolver multipartResolver() throws
	 * IOException{//配置multipart解析器 return new
	 * StandardServletMultipartResolver(); }
	 */
	/**
	 * 如果我们需要将应用部署到非Servlet 3.0的容器中， 那么就得需要替代的方案
	 */
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		// Resource uploadTempDir = new FileSystemResource("uploads/temps");
		// multipartResolver.setUploadTempDir(uploadTempDir);

		multipartResolver.setMaxUploadSize(2097152);
		return multipartResolver;
	}

	/*
	 * 值得一提的是， 如果在编写控制器方法的时候， 通过Part参数的形式接受文件上传， 那么就没有必要配置MultipartResolver了。
	 * 只有使 用MultipartFile的时候， 我们才需要MultipartResolver。
	 */
	/*--------------------------------------------------------------------------------*/

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// 配置静态资源的处理
		configurer.enable();
	}

	/*
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry
	 * registry) {
	 * registry.addResourceHandler("/resources/**").addResourceLocations(
	 * "/resources/"); }
	 */

	/*------------------------------远程服务-----------------------------------/
	/**
	 * ①导出Hessian服务(Hessian服务端配置)
	 * 1.HessianServiceExporter是一个Spring MVC控制器， 它可以接收Hessian请求， 并把这些请求转换成对POJO的调用从而将POJO导出为一个Hessian服务
	 * 2.与RmiServiceExporter不同的是， 我们不需要设置serviceName属性。 在RMI中， serviceName属性用来在RMI注册表中注册一个服
	 *  务。 而Hessian没有注册表， 因此也就没必要为Hessian服务进行命名
	 * 3.Hessian 基于HTTP。需要配置web.xml中的DispatcherServlet，和URL处理器
	 * @param spitterService
	 * @return
	 */
	@Bean
	public HessianServiceExporter hessianExportedSpitterService(SpitterService spitterService) {
		HessianServiceExporter exporter = new HessianServiceExporter();
		exporter.setService(spitterService);
		exporter.setServiceInterface(SpitterService.class);
		return exporter;
	}

	@Bean // URL映射处理
	public HandlerMapping hessianMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		Properties mappings = new Properties();
		// spitter.service”的请求最终将被hessianSpitterServicebean所处理
		mappings.setProperty("/spitter.service", "hessianExportedSpitterService");// 类似Controller与url请求的映射
		mapping.setMappings(mappings);
		return mapping;
	}

	/**
	 * Hessian(客户端配置)
	 * 生成了一个spitterService的Bean,可以在程序中Autowired注入来使用
	 * 
	 * @return
	 */
	/*
	 * @Bean public HessianProxyFactoryBean spitterService(){
	 * HessianProxyFactoryBean proxy = new HessianProxyFactoryBean();
	 * proxy.setServiceUrl("http://localhost:8080/spring4/spitter.service");
	 * proxy.setServiceInterface(SpitterService.class); return proxy; }
	 */

	/*
	 * 因为Hessian和Burlap都是基于HTTP的， 它们都解决了RMI所头疼的防火墙渗透问题。 但是当传递过来的RPC消息中包含序列化对象时，
	 * RMI 就完胜Hessian和Burlap了。 因为Hessian和Burlap都采用了私有的序列化机制，
	 * 而RMI使用的是Java本身的序列化机制。 如果我们的数据模型 非常复杂， Hessian/Burlap的序列化模型就可能 无法胜任了。
	 */
	/*
	 * 我们还有一个两全其美的解决方案。 让我们看一下Spring的HTTP invoker，
	 * 它基于HTTP提供了RPC（像Hessian/Burlap一样） ， 同时又使用了 Java的对象序列化机制（像RMI一样） 。
	 */
	/**
	 * ②HttpInvoker导出Bean(服务端配置)
	 * 
	 * @param service
	 * @return
	 */
	@Bean
	public HttpInvokerServiceExporter httpExportedSpitterService(SpitterService service) {
		HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setService(service);
		exporter.setServiceInterface(SpitterService.class);
		return exporter;
	}

	@Bean
	public HandlerMapping httpInvokerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		Properties mappings = new Properties();
		mappings.setProperty("/spitter2.service", "httpExportedSpitterService");
		mapping.setMappings(mappings);
		return mapping;
	}
	/**
	 * HttpInvoker客户端配置 
	 * 要记住HTTP invoker有一个重大的限制： 它只是一个Spring框架所提供的远程调用解决方案。
	 * 这意味着客户端和服务端必须都是Spring应用。 并且， 至少目前而言， 也隐含表明客户端和服务端必须是基于Java的。 另外，
	 * 因为使用了Java的序列化机制， 客户端和服务端必须使用相同版 本的类（与RMI类似）
	 * 
	 * @return
	 */
	/*
	 * @Bean public HttpInvokerProxyFactoryBean spitterService(){
	 * HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
	 * proxy.setServiceUrl("http://localhost:8080/spring4/spitter.service");
	 * proxy.setServiceInterface(SpitterService.class); return proxy; }
	 */
	/*-----------------------------------------------------------------------------------*/
	
	/**
	 * ③导出基于JMS的服务
	 * @param alertService
	 * @return
	 */
	@Bean
	@JmsListener(destination = "spitter.alert.queue")//配置JMS监听器容器 //TODO
	public JmsInvokerServiceExporter alertServiceExporter(AlertService alertService){
		JmsInvokerServiceExporter exporter = new JmsInvokerServiceExporter();
		exporter.setService(alertService);
		exporter.setServiceInterface(AlertService.class);
		return exporter;
	}
	@Bean
	public ConnectionFactory connectionFactory() throws JMSException{//连接工厂
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61626");
		ConnectionFactory connectionFactory = new CachingConnectionFactory(activeMQConnectionFactory);
		return connectionFactory;
	}
	@Bean
	public JmsOperations jmsOperations(ConnectionFactory connectionFactory){//JMS模板
		JmsOperations jmsOperations = new JmsTemplate(connectionFactory);
		return jmsOperations;
	}
	
	/**
	 * 客户端（异步消息）
	 * @param connectionFactory
	 * @return
	 */
    @Bean
	public JmsInvokerProxyFactoryBean alertService(ConnectionFactory connectionFactory){
		JmsInvokerProxyFactoryBean proxy = new JmsInvokerProxyFactoryBean();
		proxy.setConnectionFactory(connectionFactory);//连接工厂
		proxy.setQueueName("spittle.alert.queue");//队列名
		proxy.setServiceInterface(AlertService.class);//服务接口
		return proxy;
	}
	
	
}
