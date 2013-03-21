package model.service;

public class Price implements Comparable<Price> {

	private final int value;

	public Price(int value) {
		this.value = value;
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