package gui;

import java.util.Map;
import java.util.Set;

import model.service.Offer;
import model.service.Service;
import model.state.OfferState;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public interface GUI {

	void drawErrorPage(String errorMessage);
	void drawMainPage(User user, Map<Service, Set<User>> mapServiceUsers);

	void addUserForService(User newUser, Service service);
	void removeUserForService(User oldUser, Service service);

	// for buyers only
	void setManufacturerServiceOffer(Manufacturer manufacturer, Service service,
			Offer offer);

	// for manufactures only
	void activateBuyerForService(Buyer buyer, Service service);
	void deactivateBuyerForService(Buyer buyer, Service service);
	void changeBuyerOfferState(Buyer buyer, Service service, OfferState offerState);
	void compareServiceOffer(Buyer buyer, Service service, Offer offer);

	// for transfers
	void setTransferState(User user, Service service, TransferState transferState);

	void generateEvents(); // TODO delete (only for testing)
}
