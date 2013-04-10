package network.model;

import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public class MakeServiceOffer implements NetworkObject {

	private static final long serialVersionUID = -7344751289640588275L;
	private final Manufacturer sender;
	private final Buyer receiver;
	private final Service service;
	private final Offer offer;

	public MakeServiceOffer(Manufacturer sender, Buyer receiver, Service service, Offer offer) {
		this.sender = sender;
		this.receiver = receiver;
		this.service = service;
		this.offer = offer;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.receiveServiceOffer(sender, service, offer);
	}
}
