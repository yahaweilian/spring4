package spittr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import spittr.util.Shout;

@Controller
public class MarcoController {

	private static final Logger logger = LoggerFactory.getLogger(MarcoController.class);
	
	@MessageMapping("/macro")//处理发往"/app/macro"目的地的消息
	public void handleShout(Shout incoming) {
		logger.info("Received message: " + incoming);
	}
	
	/**
	 * 当处理这个订阅时， handleSubscription()方法会产生一个输出的Shout对象并将
	 * 其返回。 然后， Shout对象会转换成一条消息， 并且会按照客户端订阅时相同的目的地发送回客户端
	 * @return
	 */
	@SubscribeMapping({"/macro"})
	public Shout handleSubscription(){
		Shout outgoing = new Shout();
		outgoing.setMessage("Polo!");
		return outgoing;
	}
}
