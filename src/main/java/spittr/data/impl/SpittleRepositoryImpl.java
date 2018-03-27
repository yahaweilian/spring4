package spittr.data.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import spittr.Spittle;
import spittr.data.SpittleRepository;
import spittr.exception.DuplicateSpittleException;

@Service("spittleRepository")
public class SpittleRepositoryImpl implements SpittleRepository {

	@Override
	public List<Spittle> findSpittles(long max, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spittle findOne(long spittleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Spittle spittle) throws DuplicateSpittleException {
		// TODO Auto-generated method stub

	}

}
