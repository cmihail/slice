package model.service;

/**
 * Defines timer class.
 *
 * @author cmihail, radu-tutueanu
 */
public class Timer implements Comparable<Timer> {

	private final int value; // in seconds

	public Timer(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value + "s";
	}

	@Override
	public int compareTo(Timer timer) {
		return this.value - timer.value;
	}
}
