package model.state;

/**
 * Defines offer states.
 *
 * @author cmihail, radu-tutueanu
 */
public enum OfferState implements State {
	NONE("No Offer"),
	OFFER_MADE("Offer Made"),
	OFFER_EXCEEDED("Offer Exceeded"),
	OFFER_ACCEPTED("Offer Accepted"),
	OFFER_REFUSED("Offer Refused"),
	OFFER_REQUESTED("Offer Requested");
	private String name;

	private OfferState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
