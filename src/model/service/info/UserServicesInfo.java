package model.service.info;

import java.util.Set;

import model.service.Service;

/**
 * Defines a interface that contains all services and correspondent
 * services/users info.
 *
 * @author cmihail, radu-tutueanu
 */
public interface UserServicesInfo {

	Set<Service> getServices();

	ServiceInfo getServiceInfo(Service service);

	Service getServiceByName(String name);
}
