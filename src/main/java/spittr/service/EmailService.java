package spittr.service;

import javax.mail.MessagingException;

import spittr.entity.Spittle;

public interface EmailService {

	/**
	 * 发送消息
	 * @param to
	 * @param spittle
	 */
	void sendSimpleSpittleEmail(String to,Spittle spittle);
	
	/**
	 * 发送带附件的消息
	 * @param to
	 * @param spittle
	 */
	void sendSpittleEmailWithAttachment(String to,Spittle spittle) throws MessagingException;
	
	
	/**
	 * 发送带有富文本内容和嵌入式图片的email
	 * @param to
	 * @param spittle
	 * @throws MessagingException
	 */
	void sendRichSpitterEmail(String to,Spittle spittle) throws MessagingException;
	
	/**
	 * 使用模板生成Email
	 * @param to
	 * @param spittle
	 * @throws MessagingException
	 */
	void sendRichSpitterEmailWithThymeleaf(String to,Spittle spittle) throws MessagingException;
}
