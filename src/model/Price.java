package model;

public interface Price extends Comparable<Price> {

	/**
	 * @return price as a string
	 */
	@Override
	String toString();
}
