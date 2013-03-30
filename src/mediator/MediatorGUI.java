package mediator;

import gui.GUI;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserServicesInfo;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 * Defines Mediator interface used to communicate with {@link GUI}.
 *
 * @author cmihail, radu-tutueanu
 */
public interface MediatorGUI {

	/**
	 * @param userServicesInfo the {@link UserServicesInfo} to be set
	 */
	void setUserServicesInfo(UserServicesInfo userServicesInfo);

	/**
	 * Logins a user.
	 * @param username the username
	 * @param password the password
	 * @return logged in succesfully or not
	 */
	void login(String username, String password);

	/**
	 * Logouts the mainUser.
	 */
	void logout();

	/**
	 * Transfers a serbice to a user.
	 * @param service the service where the action occurs
	 * @param toUser the user where the service must be transfered
	 */
	void transfer(Service service, User toUser);

	/*
	 * for mainUser as buyer only
	 */

	/**
	 * Launches request for a given service.
	 * @param service the service where the action occurs
	 */
	void launchOfferRequest(Service service);

	/**
	 * Drops request for a given service.
	 * @param service the service where the action occurs
	 */
	void dropOfferRequest(Service service);

	/**
	 * Accepts the offer for a given service from a manufacturer.
	 * @param service the service where the action occurs
	 * @param manufacturer the manufacturer
	 */
	void acceptOffer(Service service, Manufacturer manufacturer);

	/**
	 * Refuses the offer for a given service from a manufacturer.
	 * @param service the service where the action occurs
	 * @param manufacturer the manufacturer
	 */
	void refuseOffer(Service service, Manufacturer manufacturer);

	/*
	 * for mainUser as manufacturer only
	 */

	/**
	 * Makes a offer for a given service to a buyer.
	 * @param service the service where the action occurs
	 * @param buyer the buyer
	 * @param offer the offer
	 */
	void makeOffer(Service service, Buyer buyer, Offer offer);

	/**
	 * Drops the buyer auction for a given service.
	 * @param service the service where the action occurs
	 * @param buyer the buyer
	 */
	void dropAuction(Service service, Buyer buyer);
}
