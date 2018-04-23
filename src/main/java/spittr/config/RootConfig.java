package spittr.config;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author ynding
 *
 */
@Configuration
@EnableJpaRepositories(basePackages="spittr.data")//使用spring data 创建repository的实现
@ComponentScan(value = "spittr",excludeFilters = @ComponentScan.Filter(Controller.class))
public class RootConfig {

	@Autowired
	Environment env;
	
	@Bean(destroyMethod = "shutdown")
	@Profile("dev")
	public DataSource dataSource(){
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:h2/schema.sql")
				.addScript("classpath:h2/test-data.sql")
				.build();
				
	}
	@Bean
	@Profile("qa")
	public DataSource Data(){//QA 数据源
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
}
