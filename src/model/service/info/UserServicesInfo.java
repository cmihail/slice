package model.service.info;

import java.util.Set;

import model.service.Service;

public interface UserServicesInfo {

	Set<Service> getServices();
	ServiceInfo getServiceInfo(Service service);
}
