package model.service;

import java.io.Serializable;

/**
 * Defines offer interface.
 *
 * @author cmihail, radu-tutueanu
 */
public interface Offer extends Serializable {

	Price getPrice();
}
