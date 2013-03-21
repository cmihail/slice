package mediator;

import model.service.Offer;
import model.service.Service;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public interface MediatorNetwork {

	/**
	 * Registers a new user for a given service.
	 */
	void registerUserForService(User userToRegister, Service service);

	/**
	 * Unregisters a user for a given service.
	 */
	void unregisterUserForService(User userToUnregister, Service service);

	// for buyers only
	void receiveServiceOffer(Manufacturer manufacturer, Service service, Offer offer);
	void dropAuctionForManufacturer(Manufacturer manufacturer, Service service);

	// for manufacturers only
	void receiveLaunchedServiceOfferRequest(Buyer buyer, Service service);
	void receiveDroppedServiceOfferRequest(Buyer buyer, Service service);
	void receiveRefusedServiceOffer(Buyer buyer, Service service);
	void receiveAcceptedServiceOffer(Buyer buyer, Service service);
	void receiveServiceOfferAnnouncement(Buyer buyer, Service service, Offer offer);

	// for transfers
	void setServiceTransferState(User fromUser, Service service,
			TransferState transferState);
}
