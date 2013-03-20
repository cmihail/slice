package model.service;


public class ServiceImpl implements Service {

	private final String name;
	private final Price priceLimit;
	private final Timer timer;

	public ServiceImpl(String name) {
		this(name, null, null);
	}

	public ServiceImpl(String name, Price priceLimit, Timer timer) {
		this.name = name;
		this.priceLimit = priceLimit;
		this.timer = timer;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Price getPriceLimit() {
		return priceLimit;
	}

	@Override
	public Timer getTimer() {
		return timer;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Service))
			return false;

		// Two services with same name are equal.
		Service service = (Service) o;
		return name.equals(service.getName());
	}
}
