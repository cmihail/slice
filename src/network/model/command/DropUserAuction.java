package network.model.command;

import network.model.NetworkObject;
import mediator.MediatorNetwork;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public class DropUserAuction implements NetworkObject {

	private static final long serialVersionUID = 1154598897130639460L;
	private final Manufacturer sender;
	private final Buyer receiver;
	private final Service service;

	public DropUserAuction(Manufacturer sender, Buyer receiver, Service service) {
		this.sender = sender;
		this.receiver = receiver;
		this.service = service;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		mediator.dropAuctionForManufacturer(sender, service);
	}
}
