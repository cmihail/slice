package network.model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mediator.MediatorNetwork;
import model.service.Service;
import model.state.TransferState;
import model.user.User;

import org.apache.log4j.Logger;

import constants.Constants;

/**
 * Defines a segment from a transfer service.
 *
 * @author cmihail, radu-tutueanu
 */
public class NetworkTransferService implements NetworkObject {

	private static final Logger logger = Logger.getLogger(NetworkTransferService.class);
	private static final long serialVersionUID = -4929271996640829023L;
	private final User sender;
	private final User receiver;
	private final Service service;
	private final String fileName;
	private final int currentSentSize;
	private final int totalSize;
	private final byte[] data_segment;

	public NetworkTransferService(User sender, User receiver, Service service,
			int currentSentSize, int totalSize, byte[] data_segment) {
		this.sender = sender;
		this.receiver = receiver;
		this.service = service;
		this.fileName = Constants.SERVICE_FOLDER + receiver.getUsername() + "_" +
				service.getName() + "_" + sender.getUsername();
		this.currentSentSize = currentSentSize;
		this.totalSize = totalSize;
		this.data_segment = data_segment;
	}

	@Override
	public User getDestinationUser() {
		return receiver;
	}

	@Override
	public void handler(MediatorNetwork mediator) {
		// Write data to file.
		DataOutputStream out = null;
		try {
			File file = new File(fileName);

			// Create file if it doesn't exist.
			if (!file.exists())
				file.createNewFile();

			// Append content to file.
			out = new DataOutputStream(new FileOutputStream(file, true));
			out.write(data_segment);
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

		// Update transfer percentage
		float totalSentSize = currentSentSize + data_segment.length;
		int percentage = Math.round(totalSentSize / totalSize * 100);
		mediator.setServiceTransferPercentage(sender, service, percentage);

		// End of transfer.
		if (currentSentSize < totalSize) {
			logger.info("Received service segment (" + currentSentSize + ") for < " +
					service.getName() + " > from < " + sender.getUsername() +
					" > (percentage: " + percentage + "%)");
			mediator.setServiceTransferState(sender, service,
					TransferState.TRANSFER_IN_PROGRESS);
		} else {
			logger.info("Finished transfer service for < " + service.getName() +
					" > from < " + sender.getUsername() + " >");
			mediator.setServiceTransferState(sender, service,
					TransferState.TRANSFER_COMPLETED);
		}
	}
}
