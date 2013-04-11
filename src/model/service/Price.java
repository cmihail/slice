package model.service;

import java.io.Serializable;

/**
 * Defines price class.
 *
 * @author cmihail, radu-tutueanu
 */
public class Price implements Comparable<Price>, Serializable {

	private static final long serialVersionUID = -582980750646957405L;
	private final int value;

	public Price(int value) {
		this.value = value;
	}

	public int toInt() {
		return value;
	}

	@Override
	public String toString() {
		return value + "";
	}

	@Override
	public int compareTo(Price price) {
		return this.value - price.value;
	}
}
