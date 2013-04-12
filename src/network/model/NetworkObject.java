package network.model;

import java.io.Serializable;

import mediator.MediatorNetwork;
import model.user.User;

/**
 * Defines an object that can be sent though the network.
 *
 * @author cmihail, radu-tutueanu
 */
public interface NetworkObject extends Serializable {

	/**
	 * @return the destination user
	 */
	public User getDestinationUser();

	/**
	 * Handler executed when receiving object by another client.
	 * @param mediator the mediator
	 */
	public void handler(MediatorNetwork mediator);
}
