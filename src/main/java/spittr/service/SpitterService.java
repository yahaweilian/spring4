package spittr.service;

import spittr.entity.Spitter;

/**
 * @author ynding
 *
 */
public interface SpitterService {

	/**
	 * 根据名字获取spitter
	 * @param username
	 * @return
	 */
	Spitter getSpitter(String username);

	/**
	 * 根据名字获取spittles
	 * @param username
	 * @return
	 */
	Object getSpittlesForSpitter(String username);

	
	/**
	 * 更新 spitter
	 * @param spitter
	 */
	void saveSpitter(Spitter spitter);

	/**
	 * 根据用户名获取spitter
	 * @param username
	 * @return
	 */
	Spitter getSpitterByUsername(String username);
	
}
