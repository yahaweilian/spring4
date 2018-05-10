package spittr.service;

import spittr.entity.Spittle;

public interface AlertService {

	void sendSpittleAlert(Spittle spittle);
	
	Spittle receiveSpittleAlert();
}
