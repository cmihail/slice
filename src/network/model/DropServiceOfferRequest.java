package network.model;

import mediator.MediatorNetwork;
import model.service.Service;
import model.user.Buyer;
import model.user.User;

public class DropServiceOfferRequest implements NetworkObject {

	private static final long serialVersionUID = -9000441612722204148L;
	private final Buyer sender;
	private final Service service;
	private final User receiver;

	public DropServiceOfferRequest(Buyer sender, Service service, User receiver) {
		this.sender = sender;
		this.service = service;
		this.receiver = receiver;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.receiveDroppedServiceOfferRequest(sender, service);
	}
}
