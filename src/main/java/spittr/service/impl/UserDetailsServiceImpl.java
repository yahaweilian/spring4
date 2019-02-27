package spittr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;

@Service(value = "userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService{

	/**
	 * spitterRepository
	 */
	@Autowired
	private final SpitterRepository spitterRepository;
	
	public UserDetailsServiceImpl(SpitterRepository spitterRepository) {
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

	/**
	 * 获取所属角色
	 * 
	 * @param user
	 * @param list
	 */
	public void getRoles(Spitter spitter, List<GrantedAuthority> list) {
		for (String role : spitter.getEmail().split(",")) {
			// 权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
			list.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
	}
}
