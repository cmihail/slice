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

	// TODO return null if login fails + password is optional
	Map<Service, Set<User>> login(User user, String password);

	void logout(User user);
}
