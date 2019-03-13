package spittr.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import spittr.entity.Spitter;

/**
 * @author ynding
 * @version 2019/03/13
 *
 */
public interface SpitterService {

	/*
	 * @Cacheable和@CachePut的不同：
	 * @Cacheable会条件性地触发对方法的调用， 这取决于缓存中是不是已经有了所需要的值， 对于所注解的方法， @CachePut采用了一种更为
	直接的流程。 带有@CachePut注解的方法始终都会被调用， 而且它的返回值也会放到缓存中。
	@CacheEvict并不会往缓存中添加任何东西。 相反， 如果带有@CacheEvict注解的方法被调用的话， 那么会有一个或更多的条目会在缓存中移除.
	
	与@Cacheable和@CachePut不同， @CacheEvict能够应用在返回值为void的方法上， 而@Cacheable和@CachePut需要非void的返回值， 
	它将会作为放在缓存中的条目。 因为@CacheEvict只是将条目从缓存中移除， 因此它可以放在任意的方法上， 甚至void方法
         
	@CachePut适用场景：增加一条数据时-SAVE
	@Cacheable适用场景：查询数据时-SELECT
	@CacheEvict适用场景：删除数据时-DELETE
	*/
	
	/**
	 * 根据名字获取spitter
	 * @param username
	 * @return
	 */
	@Cacheable("spitterCache")
	//缓存切面会拦截调用并在缓存中查找之前以名spitterCache存储的返回值。 缓存的key是传递到getSpitter()方
	//法中的username参数。 如果按照这个key能够找到值的话， 就会返回找到的值， 方法不会再被调用。否则，调用方法。
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
	@CachePut(value = "spitterCache",key = "#result.id")
	//@CachePut一定会调用方法，返回的Spittle会被放到spittleCache缓存中,并且key为其id
	Spitter saveSpitter(Spitter spitter);

	/**
	 * 根据用户名获取spitter
	 * @param username
	 * @return
	 */
	@Cacheable(value="spitterCache", unless="#result.firstName.contains('NoCache')",condition="#username.contains('ding')")
	/*如果firstName包含NoCache则不对其进行缓存，但不禁止其去到缓存中查找，如果username不包含ding,则禁用缓存，不会到缓存中查找。
	unless属性的表达式能够通过#result引用返回值。 这是很有用的， 这么做之所以可行是因为unless属性只有在缓存方法有
	返回值时才开始发挥作用。 而condition肩负着在方法上禁用缓存的任务， 因此它不能等到方法返回时再确定是否该关闭缓存。 这意味着它
	的表达式必须要在进入方法时进行计算， 所以我们不能通过#result引用返回值。*/
	Spitter getSpitterByUsername(String username);
	
}
