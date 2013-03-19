package webserviceclient;

import java.util.List;

import model.Service;
import model.User;

public interface WebServiceClient {

	// TODO return null if login fails and password is optional
	User login(String username, String password, User.Type type,
			List<Service> userServices);

	void logout(User user);
}
