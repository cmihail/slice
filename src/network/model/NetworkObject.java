package network.model;

import java.io.Serializable;

import mediator.MediatorNetwork;
import model.user.User;

public interface NetworkObject extends Serializable {

	public User getDestinationUser();

	public void handler(MediatorNetwork mediator);
}
