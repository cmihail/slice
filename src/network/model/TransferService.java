package network.model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import mediator.MediatorNetwork;
import model.service.Service;
import model.state.TransferState;
import model.user.User;

import org.apache.log4j.Logger;

import constants.Constants;

public class TransferService implements NetworkObject {

	private static final Logger logger = Logger.getLogger(TransferService.class);
	private static final long serialVersionUID = -4929271996640829023L;
	private final User sender;
	private final User receiver;
	private final Service service;
	private final String fileName;
	private final int currentSentSize;
	private final int totalSize;
	private final ByteBuffer byteBuffer;

	public TransferService(User sender, User receiver, Service service,
			int currentSentSize, int totalSize, ByteBuffer byteBuffer) {
		this.sender = sender;
		this.receiver = receiver;
		this.service = service;
		this.fileName = Constants.SERVICE_FOLDER + receiver.getUsername() + "_" +
				sender.getUsername() + "_" + service.getName();
		this.currentSentSize = currentSentSize;
		this.totalSize = totalSize;
		this.byteBuffer = byteBuffer;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		logger.info("Receive segment file for < " + fileName + " >");

		// Write data to file.
		DataOutputStream out = null;
		try {
			File file = new File(fileName);

			// Create file if it doesn't exist.
			if (!file.exists())
				file.createNewFile();

			// Append content to file.
			out = new DataOutputStream(new FileOutputStream(file, true));
			out.write(byteBuffer.array());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

		// TODO update sent percentage

		// End of transfer.
		if (currentSentSize == totalSize) {
			mediator.setServiceTransferState(sender, service,
					TransferState.TRANSFER_COMPLETED);
		}
	}
}
