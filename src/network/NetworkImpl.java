package network;

import java.util.List;

import mediator.MediatorNetwork;
import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Offer;
import model.service.Service;

public class NetworkImpl implements Network {

	private final MediatorNetwork mediator;

	public NetworkImpl(MediatorNetwork mediator) {
		this.mediator = mediator;
	}

	@Override
	public void registerUserServiceToUsers(User mainUser, Service service,
			List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterUserServiceFromUsers(User mainUser, Service service,
			List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeServiceOffer(Manufacturer mainUser, Buyer buyer,
			Service service, Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropUserAuction(Manufacturer mainUser, Buyer buyer,
			Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void launchServiceOfferRequest(Buyer mainUser, Service service,
			List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropServiceOfferRequest(Buyer mainUser, Service service,
			List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service, Offer offer, List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service, Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void announceUsersOfServiceOffer(Buyer mainUser, Service service,
			Offer offer, List<User> usersWithService) {
		// TODO Auto-generated method stub

	}

}
