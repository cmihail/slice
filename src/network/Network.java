package network;

import java.util.Set;

import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Offer;
import model.service.Service;

public interface Network {

	/**
	 * Registers a user with a given service in all users clients that have
	 * same service.
	 */
	void registerUserServiceToUsers(User mainUser, Service service,
			Set<User> usersWithService);

	/**
	 * Unregisters a user with a given service from all users clients that have
	 * the same service.
	 */
	void unregisterUserServiceFromUsers(User mainUser, Service service,
			Set<User> usersWithService);

	// for manufacturers only
	void makeServiceOffer(Manufacturer mainUser, Buyer buyer, Service service, Offer offer);
	void dropUserAuction(Manufacturer mainUser, Buyer buyer, Service service);

	// for buyers only
	void launchServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService);
	void dropServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService);
	void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service,
			Set<User> usersWithService);
	void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service);
	void announceUsersOfServiceOffer(Buyer mainUser, Manufacturer originatorManufacturer,
			Service service, Offer offer, Set<User> usersWithService);

	// for transfers
	void startTransfer(User mainUser, User toUser, Service service);
	void cancelTransfer(User mainUser, User toUser, Service service);
}
