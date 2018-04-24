package spittr.data.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import spittr.data.SpittleSweeper;
import spittr.entity.Spittle;
import spittr.exception.DuplicateSpittleException;

@Service("spittleRepository")
public class SpittleRepositoryImpl implements SpittleSweeper {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void saveSpittle(Spittle spittle) throws DuplicateSpittleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Spittle> findSpittles(long max, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
