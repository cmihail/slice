package network.model;

import mediator.MediatorNetwork;
import model.service.Service;
import model.user.User;

public class RegisterUserForService implements NetworkObject {

	private static final long serialVersionUID = 865120252873738996L;
	private final User sender;
	private final Service service;
	private final User receiver;

	public RegisterUserForService(User sender, Service service, User receiver) {
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
		mediator.unregisterUserForService(sender, service);
	}
}
