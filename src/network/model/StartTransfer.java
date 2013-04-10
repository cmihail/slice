package network.model;

import mediator.MediatorNetwork;
import model.service.Service;
import model.state.TransferState;
import model.user.User;

public class StartTransfer implements NetworkObject {

	private static final long serialVersionUID = -2834917331554845362L;
	private final User sender;
	private final User receiver;
	private final Service service;

	public StartTransfer(User sender, User receiver, Service service) {
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
		// TODO start receiving file
		mediator.setServiceTransferState(sender, service, TransferState.TRANSFER_STARTED);
	}
}
