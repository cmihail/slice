package network.model.command;

import network.model.NetworkObject;
import mediator.MediatorNetwork;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public class RefuseServiceOffer implements NetworkObject {

	private static final long serialVersionUID = -5005474121680775007L;
	private final Buyer sender;
	private final Manufacturer receiver;
	private final Service service;

	public RefuseServiceOffer(Buyer sender, Manufacturer receiver, Service service) {
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
		mediator.receiveRefusedServiceOffer(sender, service);
	}
}
