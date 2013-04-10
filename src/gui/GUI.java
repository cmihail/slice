package gui;

import java.util.Map;
import java.util.Set;

import mediator.MediatorGUI;
import model.service.Offer;
import model.service.Service;
import model.state.OfferState;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 * Defines GUI interface used to communicate with {@link MediatorGUI}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface GUI {

	public void generateEvents();

	/**
	 * Draws a page with a error message.
	 * @param errorMessage the error message
	 */
	void drawErrorPage(String errorMessage);

	/**
	 * Draws main page with the services table.
	 * @param mainUser the user for which the page is drawn
	 * @param mapServiceUsers a map with all services and all correspondent users
	 */
	void drawMainPage(User mainUser, Map<Service, Set<User>> mapServiceUsers);

	/**
	 * Adds a user to a service users list.
	 * @param newUser the added user
	 * @param service the service where the action occurs
	 */
	void addUserForService(User newUser, Service service);

	/**
	 * Removes a user from a service users list.
	 * @param oldUser the removed user
	 * @param service the service where the action occurs
	 */
	void removeUserForService(User oldUser, Service service);

	/**
	 * Sets the state of a transfer for the given user.
	 * @param user the user for which the state is changed
	 * @param service the service where the action occurs
	 * @param transferState the new state
	 */
	void setTransferState(User user, Service service, TransferState transferState);

	/*
	 * for mainUser as buyer only
	 */

	/**
	 * Sets a offer made by manufacturer for a given service.
	 * @param manufacturer the manufacturer that made the offer
	 * @param service the service where the action occurs
	 * @param offer the offer
	 */
	void setManufacturerServiceOffer(Manufacturer manufacturer, Service service,
			Offer offer);

	/*
	 * for mainUser as manufacturer only
	 */

	/**
	 * Activates a service entry in main page for the given buyer.
	 * @param buyer the buyer that must be activated
	 * @param service the service where the action occurs
	 */
	void activateBuyerForService(Buyer buyer, Service service);

	/**
	 * Deactivates a service entry in main page for the given buyer.
	 * @param buyer the buyer that must be deactivated
	 * @param service the service where the action occurs
	 */
	void deactivateBuyerForService(Buyer buyer, Service service);

	/**
	 * Changes the offer state of a given buyer.
	 * @param buyer
	 * @param service the service where the action occurs
	 * @param offerState the offer state
	 */
	void changeBuyerOfferState(Buyer buyer, Service service, OfferState offerState);

	/**
	 * Compares if a given offer is better then the buyer offer.
	 * @param buyer the buyer for which its own offer is compared to the given offer
	 * @param service the service where the action occurs
	 * @param offer the offer
	 */
	void compareServiceOffer(Buyer buyer, Service service, Offer offer);
}
