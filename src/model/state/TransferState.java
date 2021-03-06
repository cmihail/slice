package model.state;

/**
 * Defines transfer states.
 *
 * @author cmihail, radu-tutueanu
 */
public enum TransferState implements State {
	NONE("No transfer"),
	TRANSFER_STARTED("Transfer started"),
	TRANSFER_IN_PROGRESS("Transfer in progress"),
	TRANSFER_COMPLETED("Transfer completed"),
	TRANSFER_FAILED("Transfer failed");

	private String name;

	private TransferState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
