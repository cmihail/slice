package model.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// TODO add gson lib to build.xml

public class WebServiceJsonImpl implements WebServiceJson {

	private final Gson gson = new Gson();

	@Override
	public String userAsJson(User user) {
		return gson.toJson(user);
	}

	@Override
	public User jsonAsUser(String userJson) {
		JsonParser parser = new JsonParser();
		JsonObject userObj = parser.parse(userJson).getAsJsonObject();
		return parseUser(userObj);
	}

	@Override
	public String mapServiceUsersAsJson(Map<Service, Set<User>> mapServiceUsers) {
		List<ServiceEntry> servicesEntries = new ArrayList<ServiceEntry>();
		for (Map.Entry<Service, Set<User>> entry : mapServiceUsers.entrySet()) {
			servicesEntries.add(new ServiceEntry(entry.getKey(), entry.getValue()));
		}

		return gson.toJson(servicesEntries);
	}

	@Override
	public Map<Service, Set<User>> jsonAsMapServiceUsers(String jsonMapServiceUsers) {
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonMapServiceUsers).getAsJsonArray();

		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();
		for (int i = 0, limit = array.size(); i < limit; i++) {
			JsonObject obj = array.get(i).getAsJsonObject();

			// Get service.
			Service service = parseService(obj.get("service").getAsJsonObject());

			// Get users set.
			JsonArray usersArray = obj.get("users").getAsJsonArray();
			Set<User> users = new HashSet<User>();
			for (int j = 0, usersLimit = usersArray.size(); j < usersLimit; j++) {
				JsonObject userObj = usersArray.get(j).getAsJsonObject();
				User user = parseUser(userObj);
				users.add(user);
			}

			// Add entry to map.
			mapServiceUsers.put(service, users);
		}

		return mapServiceUsers;
	}

	private User parseUser(JsonObject userObj) {
		// Get username.
		String username = userObj.get("username").getAsJsonPrimitive().getAsString();

		// Get type.
		String type = userObj.get("type").getAsJsonPrimitive().getAsString();
		User.Type userType;
		if (type.equals(User.Type.BUYER.name()))
			userType = User.Type.BUYER;
		else if (type.equals(User.Type.MANUFACTURER.name()))
			userType = User.Type.MANUFACTURER;
		else
			return null;

		// Get list of services.
		JsonArray servicesArray = userObj.get("services").getAsJsonArray();
		List<Service> services = new ArrayList<Service>();
		for (int i = 0, limit = servicesArray.size(); i < limit; i++) {
			JsonObject serviceObj = servicesArray.get(i).getAsJsonObject();
			Service service = parseService(serviceObj);
			if (service == null)
				return null;
			services.add(service);
		}

		// Create user.
		switch (userType) {
		case BUYER:
			return new Buyer(username, services);
		case MANUFACTURER:
			return new Manufacturer(username, services);
		default:
			return null;
		}
	}

	private Service parseService(JsonObject serviceObj) {
		String name = serviceObj.get("name").getAsJsonPrimitive().getAsString();

		if (serviceObj.get("priceLimit") != null && serviceObj.get("timer") != null) {
			Price priceLimit = new Price(serviceObj.get("priceLimit")
					.getAsJsonObject().get("value").getAsJsonPrimitive().getAsInt());
			Timer timer = new Timer(serviceObj.get("timer")
					.getAsJsonObject().get("value").getAsJsonPrimitive().getAsInt());
			return new ServiceImpl(name, priceLimit, timer);
		}

		return new ServiceImpl(name);
	}

	/**
	 * Defines a service entry.
	 *
	 * @author cmihail, radu-tutueanu
	 */
	private class ServiceEntry {
		final Service service;
		final Set<User> users;

		ServiceEntry(Service service, Set<User> users) {
			this.service = service;
			this.users = users;
		}
	}
}
