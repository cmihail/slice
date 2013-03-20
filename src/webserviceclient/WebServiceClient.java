package webserviceclient;

import java.util.Map;
import java.util.Set;

import model.User;
import model.service.Service;

public interface WebServiceClient {

	// TODO return null if login fails + password is optional
	Map<Service, Set<User>> login(User user, String password);

	void logout(User user);
}
