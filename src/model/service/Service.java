package model.service;

import java.io.Serializable;

/**
 * Defines service interface.
 *
 * @author cmihail, radu-tutueanu
 */
public interface Service extends Serializable {

	String getName();

	/*
	 * for buyers only, otherwise return null
	 */
	/**
	 * @return the price limit for this service, or null if not own by a buyer
	 */
	Price getPriceLimit();

	/**
	 * @return the timer for this service, or null if not own by a buyer
	 */
	Timer getTimer();
}