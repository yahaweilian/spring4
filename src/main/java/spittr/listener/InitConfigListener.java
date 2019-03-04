package spittr.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * ServletContext初始化
 * @author dyn
 * @version 2019/03/04
 *
 */
@WebListener
public class InitConfigListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String environment = "dev";
        //加载Properties属性文件获取environment值 
        //侦测jvm环境，并缓存到全局变量中
        String env = System.setProperty("spring.profiles.active",environment);
	}

}
