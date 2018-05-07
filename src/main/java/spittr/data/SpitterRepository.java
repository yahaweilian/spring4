package spittr.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spittr.entity.Spitter;

/**
 * @author ynding
 * SpitterRepository扩展了Spring Data JPA的JpaRepository
 * 
 * 当spring data 和@Query 都无法满足需求时，我们让它扩展SpitterSweeper，SpitterSweeper使用传统方式实现。
 * 当Spring Data JPA为Repository接口生成实现的时候， 它还会查找名字与接口相同， 并且添加了Impl后缀的一个类。 如果这个类存在的
 * 话， Spring Data JPA将会把它的方法与Spring Data JPA所生成的方法合并在一起。对于SpitterRepository接口而言， 要查找的类名
 * 为SpitterRepositoryImpl。
 */
public interface SpitterRepository extends JpaRepository<Spitter, Long> ,SpitterSweeper{

	//Spitter save(Spitter spitter);//spring data的18中方法之一，不用写

	/**
	 * Spring Data JPA 自定义查询方法：
	 * Spring Data允许在方法名中使用四种动词： get、 read、 find和count。 其
     *  中， 动词get、 read和find是同义的， 这三个动词对应的Repository方法都会查询数据并返回对象。
     * Repository方法是由一个动词、 一个可选的主题（Subject） 、 关键词By以及一个断言所组成。
     *  在findByUsername()这个样例中， 动词是find， 断言是Username， 主题并没有指定， 暗含的主题是Spitter
	 * @param username
	 * @return
	 */
	Spitter findByUsername(String username);
	
	/**
	 * 有时候spring data 的方法命名约定不能满足我们的需求，可以如下
	 * @return
	 */
	@Query("select s from Spitter s where s.email like '%gmail.com'")
	List<Spitter> findAllGmailSpitters();

	Spitter findSpitterByUsernameOrEmail(String username,String email);

}
