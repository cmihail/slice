package model.state;

public enum OfferState implements State {
	NONE("No Offer"),
	OFFER_MADE("Offer Made"),
	OFFER_EXCEEDED("Offer Exceeded"),
	OFFER_ACCEPTED("Offer Accepted"),
	OFFER_REFUZED("Offer Refused");

	private String name;

	private OfferState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
