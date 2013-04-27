package network;

import java.util.Set;

import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 * Defines Network interface used to communicate with {@link MediatorNetwork}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface Network {

	/**
	 * Called when a user logouts in order to notify other users.
	 * @param userToLogout the user that logouts
	 */
	void userLogout(User userToLogout);

	/**
	 * Registers a user with a given service in all users clients that have
	 * same service.
	 * @param mainUser the user that must be registered
	 * @param service the service where the action occurs
	 * @param usersWithService users that have the service
	 */
	void registerUserServiceToUsers(User mainUser, Service service,
			Set<User> usersWithService);

	/**
	 * Unregisters a user with a given service from all users clients that have
	 * the same service.
	 * @param mainUser
	 * @param service the service where the action occurs
	 * @param usersWithService users that have the service
	 */
	void unregisterUserServiceFromUsers(User mainUser, Service service,
			Set<User> usersWithService);

	/**
	 * Starts the transfer of a service.
	 * @param mainUser the user that send the service
	 * @param toUser the user that receives the service
	 * @param service the service where the action occurs
	 */
	void startTransfer(User mainUser, User toUser, Service service);

	/*
	 * for mainUser as buyer only
	 */

	/**
	 * Launches a service offer request.
	 * @param mainUser the buyer that launches the requests
	 * @param service the service where the action occurs
	 * @param usersWithService users that have the service
	 */
	void launchServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService);

	/**
	 * Drops a service offer request.
	 * @param mainUser the buyer that drops the request
	 * @param service the service where the action occurs
	 * @param usersWithService users that have the service
	 */
	void dropServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService);

	/**
	 * Accepts a service offer.
	 * @param mainUser the buyer that accepts the offer
	 * @param manufacturer the manufacturer for which the service offer is accepted
	 * @param service the service where the action occurs
	 * @param usersWithService users that have the service
	 */
	void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service,
			Set<User> usersWithService);

	/**
	 * Refuses a service offer
	 * @param mainUser the buyer that refuses the offer
	 * @param manufacturer the manufacturer for which the service offer is refused
	 * @param service the service where the action occurs
	 */
	void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer, Service service);

	/**
	 * Announces other manufactures of a received service offer.
	 * @param mainUser the buyer that received the service offer
	 * @param originatorManufacturer the manufacturer that sent the service offer
	 * @param service the service where the action occurs
	 * @param offer the offer received from originator manufacturer
	 * @param usersWithService users that have the service
	 */
	void announceUsersOfServiceOffer(Buyer mainUser, Manufacturer originatorManufacturer,
			Service service, Offer offer, Set<User> usersWithService);

	/*
	 * for mainUser as manufacturer only
	 */

	/**
	 * Makes a service offer.
	 * @param mainUser the manufacturer that makes the offer
	 * @param buyer the buyer for which the offer is made
	 * @param service the service where the action occurs
	 * @param offer the offer
	 */
	void makeServiceOffer(Manufacturer mainUser, Buyer buyer, Service service, Offer offer);

	/**
	 * Drops a auction.
	 * @param mainUser the manufacturer that drops the auction
	 * @param buyer the buyer for which the auction is dropeed
	 * @param service the service where the action occurs
	 */
	void dropUserAuction(Manufacturer mainUser, Buyer buyer, Service service);
}
