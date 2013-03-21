package webserviceclient;

import java.util.Map;
import java.util.Set;

import model.service.Service;
import model.user.User;

public interface WebServiceClient {

	// TODO return null if login fails + password is optional
	Map<Service, Set<User>> login(User user, String password);

	void logout(User user);
}
