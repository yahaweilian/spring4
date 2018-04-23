package spittr.data;

/**
 * SpitterRepository的扩展.当spring data 和@Query 都无法满足需求时，我们使用如此方式
 * @author ynding
 *
 */
public interface SpitterSweeper {

	int eliteSweep();
}
