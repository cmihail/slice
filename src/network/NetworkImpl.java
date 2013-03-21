package network;

import java.util.Set;

import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 * Implements {@link Network}.
 *
 * @author cmihail, radu-tutueanu
 */
public class NetworkImpl implements Network {

	private final MediatorNetwork mediator;

	public NetworkImpl(MediatorNetwork mediator) {
		this.mediator = mediator;

		receiveIncomingMessage();
	}

	private void receiveIncomingMessage() {
		// TODO receive incoming message from other clients
	}

	@Override
	public void registerUserServiceToUsers(User mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Register users for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			// TODO registers a user in all usersWithService clients
			// that have the same service
		}
	}

	@Override
	public void unregisterUserServiceFromUsers(User mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Unregister users for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			// TODO unregisters a user in all usersWithService clients
			// that had the same service
		}
	}

	@Override
	public void makeServiceOffer(Manufacturer mainUser, Buyer buyer,
			Service service, Offer offer) {
		// TODO send service offer to buyer
		System.out.println("Service (" + service.getName() +
				") offer for buyer < " + buyer.getUsername() + " >:" +
				offer.getPrice());
	}

	@Override
	public void dropUserAuction(Manufacturer mainUser, Buyer buyer,
			Service service) {
		// TODO send drop auction to buyer
		System.out.println("Droped auction (" + service.getName() +
				") for buyer < " + buyer.getUsername() + " >");
	}

	@Override
	public void launchServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Launch offer request for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			// TODO send launch service offer request to manufactures
			// in usersWithService
		}
	}

	@Override
	public void dropServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Dropped offer request for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			// TODO send drop service offer request to manufactures
			// in usersWithService
		}
	}

	@Override
	public void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service, Set<User> usersWithService) {
		System.out.println("Accept service (" + service.getName() +
				") offer for manufacturer < " + manufacturer.getUsername() + " >");
		// TODO send accepted service offer to manufacturer
		for (User user : usersWithService) {
			if (!user.equals(mainUser)) {
				// TODO send refused service offer to all other manufactures
				// in usersWithService
			}
		}
	}

	@Override
	public void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service) {
		// TODO send refused service offer to manufacturer
		System.out.println("Refuse service (" + service.getName() +
				") offer for manufacturer < " + manufacturer.getUsername() + " >");
	}

	@Override
	public void announceUsersOfServiceOffer(Buyer mainUser,
			Manufacturer originatorManufacturer, Service service,
			Offer offer, Set<User> usersWithService) {
		System.out.println("Annonuce users of service (" + service.getName() +
				") offer (" + offer.getPrice() + ")");
		for (User user : usersWithService) {
			if (!originatorManufacturer.equals(mainUser)) {
				// TODO announce manufactures in usersWithService of service offer
				// in order to find out if their offer was exceeded
			}
		}
	}

	@Override
	public void startTransfer(User mainUser, User toUser, Service service) {
		// TODO transfer service to toUser
		System.out.println("Transfer service (" + service.getName() +
				") to user (" + toUser.getUsername() + ")");
	}

	@Override
	public void cancelTransfer(User mainUser, User toUser, Service service) {
		// TODO cancel service transfer to toUser
		System.out.println("Cancel transfer service (" + service.getName() +
				") to user (" + toUser.getUsername() + ")");
	}
}
