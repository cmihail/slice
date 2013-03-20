package webserviceclient;

import java.util.List;
import java.util.Map;

import model.User;
import model.service.Service;

public interface WebServiceClient {

	// TODO return null if login fails and password is optional
	Map<Service, List<User>> login(User user, String password);

	void logout(User user);
}
