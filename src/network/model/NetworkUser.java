package network.model;

import model.user.User;

public class NetworkUser implements NetworkObject {

	private final User user;
	
	public NetworkUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
