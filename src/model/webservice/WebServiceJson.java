package model.webservice;

import java.util.Map;
import java.util.Set;

import model.service.Service;
import model.user.User;

/**
 * TODO comments
 *
 * @author cmihail, radu-tutueanu
 */
public interface WebServiceJson {

	public String userAsJson(User user);

	public User jsonAsUser(String userJson);

	public String mapServiceUsersAsJson(Map<Service, Set<User>> mapServiceUsers);

	public Map<Service, Set<User>> jsonAsMapServiceUsers(String jsonMapServiceUsers);
}
