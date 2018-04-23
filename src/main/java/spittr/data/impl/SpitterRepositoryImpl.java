package spittr.data.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import spittr.data.SpitterSweeper;

public class SpitterRepositoryImpl implements SpitterSweeper {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public int eliteSweep() {
		String update = "UPDATE Spitter spitter" +
	"SET spitter.status = 'Elite'"
	+ "WHERE spitter.status= 'Newbie'"
	+ "AND spitter.id IN ("
	+ "SELECT s FROM Spitter s WHERE ("
	+ "SELECT COUNT(spittles) FROM s.spittles spittles) >10000"
	+ ")";
		return em.createQuery(update).executeUpdate();
	}

}
