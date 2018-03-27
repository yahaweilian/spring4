package spittr.config;

import javax.activation.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(value = "spittr",excludeFilters = @ComponentScan.Filter(Controller.class))
public class RootConfig {

	@Bean(destroyMethod = "shutdown")
	@Profile("dev")
	public DataSource dataSource(){
		return (DataSource) new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql")
				.build();
				
	}
	
	@Bean
	@Profile("prod")
	public DataSource jndiDataSource(){
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName("jdbc/myDS");
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
		return (DataSource) jndiObjectFactoryBean.getObject();
	}
	
	
	
}
