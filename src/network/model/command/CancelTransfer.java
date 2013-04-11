package network.model.command;

import network.model.NetworkObject;
import mediator.MediatorNetwork;
import model.service.Service;
import model.state.TransferState;
import model.user.User;

public class CancelTransfer implements NetworkObject {

	private static final long serialVersionUID = -766725152230174594L;
	private final User sender;
	private final User receiver;
	private final Service service;

	public CancelTransfer(User sender, User receiver, Service service) {
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
		// TODO stop receiving file
		mediator.setServiceTransferState(sender, service, TransferState.TRANSFER_FAILED);
	}
}
