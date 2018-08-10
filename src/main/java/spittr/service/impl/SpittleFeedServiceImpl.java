package spittr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import spittr.entity.Spittle;
import spittr.service.SpittleFeedService;

//@Service
public class SpittleFeedServiceImpl implements SpittleFeedService {

	private SimpMessageSendingOperations messaging;

	@Autowired
	public SpittleFeedServiceImpl(SimpMessageSendingOperations messaging) {//注入消息模板
		this.messaging = messaging;
	}
	
	public void broadcastSpittle(Spittle spittle){
		messaging.convertAndSend("/topic/spittlefeed",spittle);//发送消息
	}
	
	
}
