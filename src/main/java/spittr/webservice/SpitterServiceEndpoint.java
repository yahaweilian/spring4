package spittr.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spittr.entity.Spitter;
import spittr.service.SpitterService;

@Component
@WebService(serviceName="SpitterService")
public class SpitterServiceEndpoint {

	@Autowired
	SpitterService spitterService;
	
	@WebMethod
	public void addSpitter(Spitter spitter){
		spitterService.saveSpitter(spitter);
	}
	
	@WebMethod
	public Spitter getSpitter(String username){
		return spitterService.getSpitter(username);
	}
	
	
}
