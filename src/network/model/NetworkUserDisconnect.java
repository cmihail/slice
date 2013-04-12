package network.model;

import mediator.MediatorNetwork;
import model.user.User;

public class NetworkUserDisconnect implements NetworkObject {

	private static final long serialVersionUID = 1529064033199959515L;
	private final User disconnectedUser;
	private final User receiver;

	public NetworkUserDisconnect(User disconnectedUser, User receiver) {
		this.disconnectedUser = disconnectedUser;
		this.receiver = receiver;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.disconnectedUserFromServer(disconnectedUser);
	}
}
