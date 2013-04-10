package model.service;

/**
 * Implements {@link Service}.
 * Overrides {@link #equals(Object)} so that services are equal if names are equal.
 *
 * @author cmihail, radu-tutueanu
 */
public class ServiceImpl implements Service {

	private static final long serialVersionUID = -8282420573977941799L;
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
