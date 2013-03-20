package mediator;

import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Offer;
import model.service.Service;

public interface MediatorNetwork {

	/**
	 * Registers a new user for a given service.
	 */
	void registerUserForService(User userToRegister, Service service);

	/**
	 * Unregisters a user for a given service.
	 */
	void unregisterUserForService(User userToRegister, Service service);

	// for buyers only
	void receiveServiceOffer(Manufacturer manufacturer, Service service, Offer offer);
	void dropAuctionForManufacturer(Manufacturer manufacturer, Service service);

	// for manufacturers only
	void receiveLaunchServiceOfferRequest(Buyer buyer, Service service);
	void receiveDropServiceOfferRequest(Buyer buyer, Service service);
	void receiveRefusedServiceOffer(Buyer buyer, Service service, Offer offer);
	void receiveAcceptedServiceOffer(Buyer buyer, Service service, Offer offer);
	void receiveServiceOfferAnnouncement(Buyer buyer, Service service, Offer offer);
}
