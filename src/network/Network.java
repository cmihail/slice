package network;

import java.util.List;

import model.Buyer;
import model.Manufacturer;
import model.Offer;
import model.Service;
import model.User;

public interface Network {

	/**
	 * Registers a user with a given service in all users clients that have
	 * same service.
	 */
	void registerUserServiceToUsers(User mainUser, Service service,
			List<User> usersWithService);

	/**
	 * Unregisters a user with a given service from all users clients that have
	 * the same service.
	 */
	void unregisterUserServiceFromUsers(User mainUser, Service service,
			List<User> usersWithService);

	// for manufacturers only
	void makeServiceOffer(Manufacturer mainUser, Buyer buyer, Service service, Offer offer);
	void dropUserAuction(Manufacturer mainUser, Buyer buyer, Service service);

	// for buyers only
	void launchServiceOfferRequest(Buyer mainUser, Service service,
			List<User> usersWithService);
	void dropServiceOfferRequest(Buyer mainUser, Service service,
			List<User> usersWithService);
	void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service,
			Offer offer, List<User> usersWithService);
	void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service,
			Offer offer);
	void announceUsersOfServiceOffer(Buyer mainUser, Service service, Offer offer,
			List<User> usersWithService);
}
