package model.service;

/**
 * Implements {@link Offer}.
 *
 * @author cmihail, radu-tutueanu
 */
public class OfferImpl implements Offer {

	private static final long serialVersionUID = 3620710594973567260L;
	private final Price price;

	public OfferImpl(Price price) {
		this.price = price;
	}

	@Override
	public Price getPrice() {
		return price;
	}
}
