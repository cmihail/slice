package model.user;

import java.util.List;

import model.service.Service;

/**
 * Implements {@link User}.
 * Overrides {@link #equals(Object)} so that users are equal if usernames are equal.
 *
 * @author cmihail, radu-tutueanu
 */
public abstract class AbstractUser implements User {

	private final String username;
	private final User.Type type;
	private final List<Service> services;

	public AbstractUser(String username, List<Service> services, User.Type type) {
		this.username = username;
		this.type = type;
		this.services = services;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public User.Type getType() {
		return type;
	}

	@Override
	public List<Service> getServices() {
		return services;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User))
			return false;

		// Two users with same username are equal.
		User user = (User) o;
		return username.equals(user.getUsername());
	}
}
