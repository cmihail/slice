package network.model;

import mediator.MediatorNetwork;
import model.user.User;

public class NetworkUser implements NetworkObject {

	private static final long serialVersionUID = -7133670121274777675L;
	private final User user;

	public NetworkUser(User user) {
		this.user = user;
	}

	@Override
	public User getDestinationUser() {
		return user;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
	}
}
