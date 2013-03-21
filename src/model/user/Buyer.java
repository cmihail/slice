package model.user;

import java.util.List;

import model.service.Service;

public class Buyer extends AbstractUser {

	public Buyer(String username, List<Service> services) {
		super(username, services, Type.BUYER);
	}
}
