package webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.service.Price;
import model.service.Service;
import model.service.ServiceImpl;
import model.service.Timer;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import model.user.User.Type;
import model.webservice.WebServiceJson;
import model.webservice.WebServiceJsonImpl;
import constants.Constants;

/**
 * Defines the web service.
 *
 * @author cmihail, radu-tutueanu
 */
public class WebService {

	private final WebServiceJson json = new WebServiceJsonImpl();

	public String loginUser(String userJson, String password) throws Exception {
		// TODO correct action
		User user = json.jsonAsUser(userJson);
		Map<Service, Set<User>> mapServiceUsers = readConfigFiles(user, password);
		return json.mapServiceUsersAsJson(mapServiceUsers);
	}

	public void logout(String userJson, String password) {
		// TODO correct action
		User user = json.jsonAsUser(userJson);
	}

	/*
	 * TODO delete (only for testing from here)
	 */
	private Map<Service, Set<User>> readConfigFiles(User user, String password)
			throws Exception {
		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();

		Set<User> users = new HashSet<User>();
		for (int i = 1; i <= 3; i++) {
			if (("user" + i).equals(user.getUsername()))
				continue;

			BufferedReader in = new BufferedReader(new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream(
							"user" + i + Constants.CONFIG_FILE_EXTENSION)));
			User.Type userType = readUserType(in);
			if (userType == null)
				throw new Exception("Invalid user type");

			List<Service> userServices = readServices(in, userType);
			if (userServices == null)
				throw new Exception("Invalid services.");

			switch (userType) {
			case BUYER:
				users.add(new Buyer("user" + i, userServices));
				break;
			case MANUFACTURER:
				users.add(new Manufacturer("user" + i, userServices));
				break;
			}
		}

		Iterator<Service> it = user.getServices().iterator();
		while (it.hasNext()) {
			Service service = it.next();
			Set<User> serviceUsers = new HashSet<User>();

			Iterator<User> usersIt = users.iterator();
			while (usersIt.hasNext()) {
				User u = usersIt.next();

				Iterator<Service> serviceIt = u.getServices().iterator();
				while (serviceIt.hasNext()) {
					Service s = serviceIt.next();

					if (service.getName().equals(s.getName())) {
						serviceUsers.add(u);
					}
				}
			}

			mapServiceUsers.put(service, serviceUsers);
		}

		return mapServiceUsers;
	}

	private User.Type readUserType(BufferedReader in) throws IOException {
		String typeStr = in.readLine();
		if (typeStr == null)
			return null;

		int typeInt = Integer.parseInt(typeStr);
		User.Type userType;
		switch (typeInt) {
		case 0:
			userType = Type.BUYER;
			break;
		case 1:
			userType = Type.MANUFACTURER;
			break;
		default:
			return null;
		}

		return userType;
	}

	private List<Service> readServices(BufferedReader in, User.Type userType)
			throws IOException {
		String line;
		List<Service> userServices = new ArrayList<Service>();
		while ((line = in.readLine()) != null) {
			if (userType == Type.BUYER) {
				String[] tokens = line.split(" ");
				if (tokens.length != 3) {
					return null;
				}
				userServices.add(new ServiceImpl(tokens[0], new Price(Integer
						.parseInt(tokens[1])), new Timer(Integer
						.parseInt(tokens[2]))));
			} else {
				userServices.add(new ServiceImpl(line));
			}
		}

		if (userServices.isEmpty())
			return null;
		return userServices;
	}
}
