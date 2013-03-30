package model.service;

/**
 * Implements {@link Offer}.
 *
 * @author cmihail, radu-tutueanu
 */
public class OfferImpl implements Offer {

	private final Price price;

	public OfferImpl(Price price) {
		this.price = price;
	}

	@Override
	public Price getPrice() {
		return price;
	}
}
