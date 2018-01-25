package spittr.data;

import java.util.List;

import spittr.Spittle;
import spittr.exception.DuplicateSpittleException;

public interface SpittleRepository {

	List<Spittle> findSpittles(long max,int count);

	Spittle findOne(long spittleId);

	void save(Spittle spittle) throws DuplicateSpittleException;
}
