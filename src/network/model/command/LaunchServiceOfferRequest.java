package network.model.command;

import network.model.NetworkObject;
import mediator.MediatorNetwork;
import model.service.Service;
import model.user.Buyer;
import model.user.User;

public class LaunchServiceOfferRequest implements NetworkObject {

	private static final long serialVersionUID = 4883249152102024361L;
	private final Buyer sender;
	private final Service service;
	private final User receiver;

	public LaunchServiceOfferRequest(Buyer sender, Service service, User receiver) {
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
		mediator.receiveLaunchedServiceOfferRequest(sender, service);
	}
}
