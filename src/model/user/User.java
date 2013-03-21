package model.user;

import java.util.List;

import model.service.Service;

public interface User {

	enum Type {
		BUYER,
		MANUFACTURER,
	}

	String getUsername();

	User.Type getType();

	List<Service> getServices();
}
