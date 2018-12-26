package spittr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;
import spittr.service.UserDetailService;

/**
 * 假设我们需要认证的用户存储在非关系型数据库中， 如Mongo或Neo4j， 在这种情况下， 我们需要提供一个自定义
 * 的UserDetailsService接口实现
 * @author ynding
 * @version 2018/12/26
 *
 */
public class SpitterUserService implements UserDetailService {

	/**
	 * spitterRepository
	 */
	private final SpitterRepository spitterRepository;
	
	public SpitterUserService(SpitterRepository spitterRepository) {
		this.spitterRepository = spitterRepository;
	}


	/* (non-Javadoc)
	 * @see spittr.service.UserDetailService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Spitter spitter = spitterRepository.findByUsername(username);//查找Spitter
		if(spitter != null) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_SPITTER"));//创建权限列表
			//返回user
			return new User(spitter.getUsername(), spitter.getPassword(), authorities);
		}
		/*SpitterUserService有意思的地方在于它并不知道用户数据存储在什么地方。 设置进来的SpitterRepository能够从关系型数据库、
		文档数据库或图数据中查找Spitter对象， 甚至可以伪造一个。 SpitterUserService不知道也不会关心底层所使用的数据存储。 它只是
		获得Spitter对象， 并使用它来创建User对象。*/
		throw new UsernameNotFoundException("User '" + username + "' not found.");
	}

}
