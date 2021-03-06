package model.service.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.service.Service;
import model.user.User;

/**
 * Implements {@link UserServicesInfo}.
 *
 * @author cmihail, radu-tutueanu
 */
public class UserServicesInfoImpl implements UserServicesInfo {

	private final HashMap<Service, ServiceInfo> mapServicesInfo;

	public UserServicesInfoImpl(Map<Service, Set<User>> mapServiceUsers) {
		mapServicesInfo = new HashMap<Service, ServiceInfo>();

		Set<Entry<Service, Set<User>>> entrySet = mapServiceUsers.entrySet();
		Iterator<Entry<Service, Set<User>>> it = entrySet.iterator();
		while (it.hasNext()) {
			Entry<Service, Set<User>>entry = it.next();
			mapServicesInfo.put(entry.getKey(),
					new ServiceInfo(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public Set<Service> getServices() {
		return mapServicesInfo.keySet();
	}

	@Override
	public Service getServiceByName(String name) {
		Iterator<Service> it = mapServicesInfo.keySet().iterator();
		while (it.hasNext())
		{
			Service aux =it.next();
			if (aux.getName().equals(name)) return aux;
		}
		return null;
	}
	@Override
	public ServiceInfo getServiceInfo(Service service) {
		return mapServicesInfo.get(service);
	}

	
	
}
