package network.model;

import mediator.MediatorNetwork;
import model.service.Service;
import model.user.User;

public class UnregisterUserForService implements NetworkObject {

	private static final long serialVersionUID = -2999893200458255394L;
	private final User sender;
	private final Service service;
	private final User receiver;

	public UnregisterUserForService(User sender, Service service, User receiver) {
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
		mediator.registerUserForService(sender, service);
	}
}
