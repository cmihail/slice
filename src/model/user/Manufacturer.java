package model.user;

import java.util.List;

import model.service.Service;

/**
 * Defines a manufacturer.
 *
 * @author cmihail, radu-tutueanu
 */
public class Manufacturer extends AbstractUser {

	public Manufacturer(String username, List<Service> services) {
		super(username, services, Type.MANUFACTURER);
	}
}
