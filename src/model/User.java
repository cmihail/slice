package model;

public interface User {

	enum Type {
		BUYER,
		MANUFACTURER,
	}

	String getName();

	User.Type getType();
}
