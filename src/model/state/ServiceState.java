package model.state;

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
