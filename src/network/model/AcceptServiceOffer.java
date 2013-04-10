package network.model;

import mediator.MediatorNetwork;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public class AcceptServiceOffer implements NetworkObject {

	private static final long serialVersionUID = -2191454554337723164L;
	private final Buyer sender;
	private final Manufacturer receiver;
	private final Service service;

	public AcceptServiceOffer(Buyer sender, Manufacturer receiver, Service service) {
		this.sender = sender;
		this.receiver = receiver;
		this.service = service;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.receiveAcceptedServiceOffer(sender, service);
	}
}
