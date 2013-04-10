package model.user;

import java.util.List;

import model.service.Service;

/**
 * Defines a manufacturer.
 *
 * @author cmihail, radu-tutueanu
 */
public class Manufacturer extends AbstractUser {

	private static final long serialVersionUID = 630915680630025674L;

	public Manufacturer(String username, List<Service> services) {
		super(username, services, Type.MANUFACTURER);
	}
}
