package spittr.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import spittr.entity.Spittle;
import spittr.service.EmailService;

/**
 * 邮件发送服务
 * 
 * @author ynding
 *
 */
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine thymeleaf;

	@Override
	public void sendRichSpitterEmailWithThymeleaf(String to,Spittle spittle) throws MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String spitterName = spittle.getSpitter().getFullName();
		
		Context ctx = new Context();
		ctx.setVariable("spitterName", spitterName);
		ctx.setVariable("spittleText", spittle.getMessage());
		String emailText = thymeleaf.process("email.html", ctx);
		
		helper.setFrom("913690560@qq.com");
		helper.setTo(to);
		helper.setSubject("New spittle from " + spitterName);
		helper.setText(emailText,true);
		mailSender.send(message);
	}
	
	@Override
	public void sendSimpleSpittleEmail(String to, Spittle spittle) {
		SimpleMailMessage message = new SimpleMailMessage();
		String spitterName = spittle.getSpitter().getFullName();
		message.setFrom("913690560@qq.com");
		message.setTo(to);
		message.setSubject("New spittle from " + spitterName);
		message.setText(spitterName + "says: " + spittle.getMessage());// 设置消息
		mailSender.send(message);
	}

	@Override
	public void sendSpittleEmailWithAttachment(String to, Spittle spittle) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String spitterName = spittle.getSpitter().getFullName();
		helper.setFrom("913690560@qq.com");
		helper.setTo(to);
		helper.setSubject("New spittle from " + spitterName);
		helper.setText(spitterName + "says: " + spittle.getMessage());
		//加载位于应用类路径下的coupon.png
		FileSystemResource couponImage = new FileSystemResource("/collateral/coupon.png");
		helper.addAttachment("Counpon.png", couponImage);
		mailSender.send(message);
	}

	@Override
	public void sendRichSpitterEmail(String to, Spittle spittle) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String spitterName = spittle.getSpitter().getFullName();
		helper.setFrom("913690560@qq.com");
		helper.setTo(to);
		helper.setSubject("New spittle from " + spitterName);
		helper.setText("<html><body><img src='cid:spitterLogo'>"+
				"<h4>" + spittle.getSpitter().getFullName() + "says...</h4>" +
				"<i>" + spittle.getMessage() +"</i>" + "</body></html>",
				true);
		ClassPathResource image = new ClassPathResource("resources/images/spittr_logo_50.png");
		helper.addInline("spitterLogo", image);
		mailSender.send(message);
	}
}
