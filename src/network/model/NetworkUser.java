package network.model;

import mediator.MediatorNetwork;
import model.user.User;
import network.server.Server;

/**
 * Defines a user used by {@link Server}.
 *
 * @author cmihail, radu-tutueanu
 */
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
		// Nothing to do.
	}
}
