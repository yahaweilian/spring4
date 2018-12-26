package spittr.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义用户服务
 * @author ynding
 * @version 2018/12/26
 *
 */
public interface UserDetailService {

	/**
	 * 根据给定的用户名来查找用户
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
