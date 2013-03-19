package model;

public interface Service {

	void setName(String name);
	String getName();

	// for buyers only, otherwise return null
	void setPriceLimit(Price priceLimit);
	Price getPriceLimit();
	void setTimer(Timer timer);
	Timer getTimer();
}