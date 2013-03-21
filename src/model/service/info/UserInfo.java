package model.service.info;

import model.service.Offer;
import model.state.OfferState;
import model.state.TransferState;
import model.user.User;

public class UserInfo {

	private final User user;
	private Offer offer;
	private OfferState offerState;
	private TransferState transferState;

	public UserInfo(User user) {
		this.user = user;
		setOfferState(OfferState.NONE);
	}

	public User getUser() {
		return user;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public OfferState getOfferState() {
		return offerState;
	}

	public void setOfferState(OfferState offerState) {
		this.offerState = offerState;
		setTransferState(TransferState.NONE);
	}

	public TransferState getTransferState() {
		return transferState;
	}

	public void setTransferState(TransferState transferState) {
		this.transferState = transferState;
	}
}
