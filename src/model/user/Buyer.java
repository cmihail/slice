package model.user;

import java.util.List;

import model.service.Service;

/**
 * Defines a buyer.
 *
 * @author cmihail, radu-tutueanu
 */
public class Buyer extends AbstractUser {

	public Buyer(String username, List<Service> services) {
		super(username, services, Type.BUYER);
	}
}
