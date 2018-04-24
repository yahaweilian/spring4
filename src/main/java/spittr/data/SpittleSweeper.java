package spittr.data;

import java.util.List;

import spittr.entity.Spittle;
import spittr.exception.DuplicateSpittleException;

public interface SpittleSweeper {

	void saveSpittle(Spittle spittle) throws DuplicateSpittleException;
	
	List<Spittle> findSpittles(long max,int count);
}
