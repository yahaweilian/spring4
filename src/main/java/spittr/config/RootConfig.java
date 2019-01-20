package spittr.config;


import javax.jms.ConnectionFactory;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Controller;

import com.sun.mail.util.MailSSLSocketFactory;

import spittr.service.AlertService;
import spittr.service.SpitterService;

/**
 * 其他配置，会被加载到Spring应用上下文中(spitter-servlet.xml)
 * @author ynding
 *
 */
@Configuration
@EnableJpaRepositories(basePackages="spittr.data")//使用spring data 创建repository的实现
@ComponentScan(value = "spittr",excludeFilters = @ComponentScan.Filter(Controller.class))
@PropertySource("classpath:mailserver.properties")//引入properties文件属性
@PropertySource("classpath:env_dev.properties")//引入properties文件属性
public class RootConfig {

	@Autowired
	Environment env;
	
	@Bean(destroyMethod = "shutdown")
	//@Profile("dev")
	public DataSource dataSource(){
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:h2/schema.sql")
				.addScript("classpath:h2/test-data.sql")
				.build();
				
	}
	@Bean
	@Profile("qa")
	public DataSource Data(){//测试数据源
		BasicDataSource ds = new BasicDataSource();//dbcp连接池
		DriverManagerDataSource ds2 = new DriverManagerDataSource();// JDBC数据源
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:tcp//localhost/~/spitter");
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setInitialSize(5);
		ds.setMaxActive(10);
		return ds;
	}
	@Bean
	@Profile("pro")
	public DataSource jndiDataSource(){
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();//JNDI
		jndiObjectFactoryBean.setJndiName("jdbc/myDS");
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
		return (DataSource) jndiObjectFactoryBean.getObject();
	}
	
	/**
	 * JPA
	 * @param dataSource
	 * @param jpaVendorAdapter
	 * @return
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource,JpaVendorAdapter jpaVendorAdapter){
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setPackagesToScan("spittr.entity");
		return emfb;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}
	
	/**
	 * @PersistenceUnit和@PersistenceContext并不是Spring的注解， 
	 * 它们是由JPA规范提供的。 为了让Spring理解这些注解， 并注入EntityManager Factory或EntityManager，
	 * 我们必须要配置Spring的PersistenceAnnotationBeanPostProcessor。
	 * @return
	 */
	@Bean
	public PersistenceAnnotationBeanPostProcessor paPostProcessor(){
		return new PersistenceAnnotationBeanPostProcessor();
	}
	
	/**
	 * 由于没有使用模板类来处理异常， 所以我们需要为Repository添加@Repository注解，
	 *  这样PersistenceExceptionTranslationPostProcessor就会知道要将这个bean产生
	 *  的异常转换成Spring的统一数据访问异常。
	 * @return
	 */
	@Bean
	public BeanPostProcessor persistenceTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	/**
	 * JPA 事务
	 * @param emf
	 * @return
	 */
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf){
		return new JpaTransactionManager(emf);
	}

	/*---------------------------远程服务----------------------------*/
	/**
	 * RMI 服务端配置
	 * @param spitterService
	 * @return
	 */
	@Bean
	public RmiServiceExporter rmiExporter(SpitterService spitterService) {
		RmiServiceExporter rmiExporter = new RmiServiceExporter();
		rmiExporter.setService(spitterService);
		rmiExporter.setServiceName("SpitterService");
		rmiExporter.setServiceInterface(SpitterService.class);
		return rmiExporter;
	}
	/**
	 * RMI 远程方法调用(客户端配置)
	 * RMI 的限制：① RMI很难穿越防火墙， 这是因为RMI使用任意端口来交互——这是防火墙
     * 通常所不允许的。 在企业内部网络环境中， 我们通常不需要担心这个问题。 但是如果在互联网上运行， 我们用RMI可能会遇到麻烦
     *  ②RMI是基于Java的。必须要保证在调用两端的Java运行时中是完全相同的版本
	 * @return
	 */
	/*@Bean
	public RmiProxyFactoryBean spitterService(){
		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
		rmiProxy.setServiceUrl("rmi://localhost/SpitterService");
		rmiProxy.setServiceInterface(SpitterService.class);
		return rmiProxy;
	}*/
	
	/**
	 * JaxWs服务端配置
	 * SimpleJaxWsServiceExporter将bean转变为JAX-WS端点
	 * 当启动的时候， 它会搜索Spring应用上下文来查找所有使用@WebService注解的bean
	 * @return
	 */
	@Bean
	public SimpleJaxWsServiceExporter jaxWsExporter(){
		SimpleJaxWsServiceExporter exporter = new SimpleJaxWsServiceExporter();
		exporter.setBaseAddress("http://localhost:8888/services/");
		return exporter;
	}
	/**
	 * JaxWs客户端配置
	 * @return
	 * @throws MalformedURLException
	 */
	/*@Bean
	public JaxWsPortProxyFactoryBean spitterService() throws MalformedURLException{
		JaxWsPortProxyFactoryBean proxy = new JaxWsPortProxyFactoryBean();
		proxy.setWsdlDocumentUrl(new URL("http://localhost:8888/services/SpitterService?wsdl"));
		proxy.setPortName("spitterServiceHttpPort");
		proxy.setServiceInterface(SpitterService.class);
		proxy.setNamespaceUri("http://spitter.com");
		return proxy;
	}*/
	/*----------------------------------------------------------------------------------*/
	
	/**
	 * 邮件服务器配置
	 * Spring Email
	 * @param env
	 * @return
	 */
	@Bean
	public JavaMailSender mailSender(Environment env){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("mailserver.host"));
		mailSender.setPort(Integer.valueOf(env.getProperty("mailserver.port")));
		mailSender.setUsername(env.getProperty("mailserver.username"));
		mailSender.setPassword(env.getProperty("password"));
		return mailSender;
	}
}
