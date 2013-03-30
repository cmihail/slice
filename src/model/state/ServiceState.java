package model.state;

/**
 * Defines service states.
 *
 * @author cmihail, radu-tutueanu
 */
public enum ServiceState implements State {
	INACTIVE("Inactiv"),
	ACTIVE("Activ");

	private String name;

	private ServiceState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
