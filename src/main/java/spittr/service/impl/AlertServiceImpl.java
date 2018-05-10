package spittr.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Service;

import spittr.entity.Spittle;
import spittr.service.AlertService;


/**
 * JMS
 * @author ynding
 *
 */
@Service
public class AlertServiceImpl implements AlertService{

	private JmsOperations jmsOperation;
	
	@Autowired
	public AlertServiceImpl(JmsOperations jmsOperation) {//注入JMS模版
		this.jmsOperation = jmsOperation;
	}

	@Override
	public void sendSpittleAlert(final Spittle spittle) {
//		jmsOperation.convertAndSend(spittle);
		jmsOperation.send(
				"Spittle.alert.queue",//目的地
				new MessageCreator(){
			public Message createMessage(Session session) throws JMSException{
				return session.createObjectMessage(spittle);//创建消息
			}
		});
		
	}
	
	@Override
	public Spittle receiveSpittleAlert(){
//		return (Spittle) jmsOperation.receiveAndConvert();
		try {
			ObjectMessage receivedMessage = (ObjectMessage) jmsOperation.receive();//接收消息
			return (Spittle) receivedMessage.getObject();//获得对象
		} catch (JMSException e) {
			e.printStackTrace();
			throw JmsUtils.convertJmsAccessException(e);//抛出转换后的异常
		}
	}

}
