package model;

import java.util.List;

import model.service.Service;

public class Manufacturer extends AbstractUser {

	public Manufacturer(String username, List<Service> services) {
		super(username, services, Type.MANUFACTURER);
	}
}
