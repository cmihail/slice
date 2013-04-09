package model.user;

import java.io.Serializable;
import java.util.List;

import model.service.Service;

/**
 * Defines how a user should be implemented.
 *
 * @author cmihail, radu-tutueanu
 */
public interface User extends Serializable {

	enum Type {
		BUYER,
		MANUFACTURER,
	}

	String getUsername();

	User.Type getType();

	List<Service> getServices();
}
