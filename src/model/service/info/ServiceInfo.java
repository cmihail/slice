package model.service.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.service.Service;
import model.state.OfferState;
import model.state.ServiceState;
import model.user.User;

/**
 * Contains all the info about a service, like service state,
 * correspondent users, etc.
 *
 * @author cmihail, radu-tutueanu
 */
public class ServiceInfo {

	private final Service service;
	private final Map<User, UserInfo> usersInfo;
	private ServiceState serviceState;
	private OfferState offerState;

	public ServiceInfo(Service service, Set<User> users) {
		this.service = service;
		this.usersInfo = new HashMap<User, UserInfo>();
		setServiceState(ServiceState.INACTIVE);
		setOfferState(OfferState.NONE);
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

	public OfferState getOfferState() {
		return offerState;
	}

	public void setOfferState(OfferState offerState) {
		this.offerState = offerState;
	}
	public User getUserByName(String name){
		Iterator<User> itUser =getUsers().iterator();
		User aux;
		while (itUser.hasNext())
		{
			aux=itUser.next();
			if(aux.getUsername().equals(name)) return aux;
		}
		
		return null;
		
	}
}
