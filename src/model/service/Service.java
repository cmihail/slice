package model.service;

public interface Service {

	String getName();

	// for buyers only, otherwise return null
	Price getPriceLimit();
	Timer getTimer();
}