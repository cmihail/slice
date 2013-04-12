package mediator;

import model.service.Offer;
import model.service.Service;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import network.Network;

/**
 * Defines Mediator interface used to communicate with {@link Network}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface MediatorNetwork {

	/**
	 * Sends a network error message to the mediator.
	 * @param errorMessage the error
	 */
	void networkError(String errorMessage);

	/**
	 * Announces the disconnection of a user.
	 * @param disconnectedUser the user that was disconnected
	 */
	void disconnectedUserFromServer(User disconnectedUser);

	/**
	 * Registers a new user for a given service. Used when userToRegister logins.
	 * @param userToRegister the user to be registered
	 * @param service the service where the action occurs
	 */
	void registerUserForService(User userToRegister, Service service);

	/**
	 * Unregisters a user for a given service. Used when userToUnregister logouts.
	 * @param userToUnregisterthe user to be unregistered
	 * @param service the service where the action occurs
	 */
	void unregisterUserForService(User userToUnregister, Service service);

	/**
	 * Sets the transfer state of a service received from a user.
	 * @param fromUser the user from which the service is transfered
	 * @param service the service where the action occurs
	 * @param transferState the transfer state
	 */
	void setServiceTransferState(User fromUser, Service service,
			TransferState transferState);

	/**
	 * Sets the transfer percentage of a service received from a user.
	 * @param fromUser the user from which the service is transfered
	 * @param service the service where the action occurs
	 * @param percentage the transfer percentage
	 */
	void setServiceTransferPercentage(User fromUser, Service service, int percentage);

	/*
	 * for mainUser as buyer only
	 */

	/**
	 * Receives a service offer from a manufacturer.
	 * @param manufacturer the manufacturer
	 * @param service the service where the action occurs
	 * @param offerthe offer
	 */
	void receiveServiceOffer(Manufacturer manufacturer, Service service, Offer offer);

	/**
	 * Receives dropped auction from a manufacturer.
	 * @param manufacturer the manufacturer
	 * @param service the service where the action occurs
	 */
	void dropAuctionForManufacturer(Manufacturer manufacturer, Service service);

	/*
	 * for mainUser as manufacturer only
	 */

	/**
	 * Receives a service offer request from a buyer.
	 * @param buyer the buyer
	 * @param service the service where the action occurs
	 */
	void receiveLaunchedServiceOfferRequest(Buyer buyer, Service service);

	/**
	 * Receives a dropped service offer request from a buyer.
	 * @param buyer the buyer
	 * @param service the service where the action occurs
	 */
	void receiveDroppedServiceOfferRequest(Buyer buyer, Service service);

	/**
	 * Receives refused service offer from a buyer.
	 * @param buyer the buyer
	 * @param service the service where the action occurs
	 */
	void receiveRefusedServiceOffer(Buyer buyer, Service service);

	/**
	 * Receives accepted service offer from a buyer.
	 * @param buyer the buyer
	 * @param service the service where the action occurs
	 */
	void receiveAcceptedServiceOffer(Buyer buyer, Service service);

	/**
	 * Receives service offer announcement made from another manufacturer for buyer.
	 * This is used in order to compare own offer with the other manufacturer offer.
	 * @param buyer the buyer
	 * @param service the service where the action occurs
	 * @param offer the offer own by the other manufacturer
	 */
	void receiveServiceOfferAnnouncement(Buyer buyer, Service service, Offer offer);
}
