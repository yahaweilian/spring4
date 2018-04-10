package spittr.data.impl;

import org.springframework.stereotype.Service;

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;

@Service("spitterRepository")
public class SpitterRepositoryImpl implements SpitterRepository {

	@Override
	public Spitter save(Spitter spitter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spitter findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
