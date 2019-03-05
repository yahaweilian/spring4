package spittr.listener;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spittr.util.PropertiesUtil;

/**
 * 激活profile
 * @author ynding
 * @version 2019/03/04
 *
 */
@WebListener
public class InitConfigListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
        //加载Properties属性文件获取environment值
		Properties properties = PropertiesUtil.getProperties("env_dev.properties");
		String profile = properties.getProperty("profile");
        //侦测jvm环境，并缓存到全局变量中
        System.setProperty("spring.profiles.active",profile);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}	

}
