package model.webservice;

import java.util.Map;
import java.util.Set;

import model.service.Service;
import model.user.User;

/**
 * Defines JSON parser used between web service and its clients.
 *
 * @author cmihail, radu-tutueanu
 */
public interface WebServiceJson {

	/**
	 * @param user the user to be transformed
	 * @return user as JSON string
	 */
	public String userAsJson(User user);

	/**
	 * @param userJson user as JSON string
	 * @return correspondent user
	 */
	public User jsonAsUser(String userJson);

	/**
	 * @param mapServiceUsers the map to be transformed
	 * @return map as JSON string
	 */
	public String mapServiceUsersAsJson(Map<Service, Set<User>> mapServiceUsers);

	/**
	 *
	 * @param jsonMapServiceUsers map as JSON string
	 * @return correspondent map
	 */
	public Map<Service, Set<User>> jsonAsMapServiceUsers(String jsonMapServiceUsers);
}
