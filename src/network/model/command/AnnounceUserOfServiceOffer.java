package network.model.command;

import network.model.NetworkObject;
import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.User;

public class AnnounceUserOfServiceOffer implements NetworkObject {

	private static final long serialVersionUID = 4263228237255302526L;
	private final Buyer sender;
	private final Service service;
	private final Offer offer;
	private final User receiver;

	public AnnounceUserOfServiceOffer(Buyer sender, Service service, Offer offer,
			User receiver) {
		this.sender = sender;
		this.service = service;
		this.offer = offer;
		this.receiver = receiver;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.receiveServiceOfferAnnouncement(sender, service, offer);
	}
}
