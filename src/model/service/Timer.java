package model.service;

import java.io.Serializable;

/**
 * Defines timer class.
 *
 * @author cmihail, radu-tutueanu
 */
public class Timer implements Comparable<Timer>, Serializable {

	private static final long serialVersionUID = 6171141701046670570L;
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
