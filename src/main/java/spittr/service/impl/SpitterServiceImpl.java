package spittr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;
import spittr.service.SpitterService;

/**
 * 
 * @author ynding
 *
 */
@Service("spitterServiceImpl")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SpitterServiceImpl implements SpitterService {
	
	private SpitterRepository spitterRepository;
	
	@Autowired
	public SpitterServiceImpl(SpitterRepository spitterRepository) {
		this.spitterRepository = spitterRepository;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public Spitter getSpitter(String username) {
		
		return spitterRepository.findSpitterByUsernameOrEmail(username,null);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public Object getSpittlesForSpitter(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void saveSpitter(Spitter spitter) {
		
		spitterRepository.save(spitter);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public Spitter getSpitterByUsername(String username) {
		return spitterRepository.findSpitterByUsernameOrEmail(username,null);
	}

}
