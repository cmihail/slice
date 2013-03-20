package model.service.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.User;
import model.service.Service;
import model.state.ServiceState;

/**
 * Contains all the info about a service.
 */
public class ServiceInfo {

	private final Service service;
	private final Map<User, UserInfo> usersInfo;
	private ServiceState serviceState;

	public ServiceInfo(Service service, Set<User> users) {
		this.service = service;
		this.usersInfo = new HashMap<User, UserInfo>();
		setServiceState(ServiceState.INACTIVE);

		for (User user : users) {
			usersInfo.put(user, new UserInfo(user));
		}
	}

	public Service getService() {
		return service;
	}

	public Set<User> getUsers() {
		return usersInfo.keySet();
	}

	public ServiceState getServiceState() {
		return serviceState;
	}

	public void setServiceState(ServiceState serviceState) {
		this.serviceState = serviceState;
	}

	public void addUser(User user) {
		usersInfo.put(user, new UserInfo(user));
	}

	public void removeUser(User user) {
		usersInfo.remove(user);
	}

	public UserInfo getUserInfo(User user) {
		return usersInfo.get(user);
	}
}
