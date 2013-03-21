package webserviceclient;

import java.util.Map;
import java.util.Set;

import mediator.MediatorWebServiceClient;
import model.service.Service;
import model.user.User;

/**
 * Defines WebServiceClient interface used to communicate with
 * {@link MediatorWebServiceClient}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface WebServiceClient {

	/**
	 * Logins a user ant retrieves a list with users that have same services
	 * as the user that logins.
	 * @param user the user that logins
	 * @param password the user password
	 * @return a map with services and correspondent users,
	 * or null if logins fails
	 */
	Map<Service, Set<User>> login(User user, String password);

	/**
	 * @param user the user that logouts
	 */
	void logout(User user);
}
