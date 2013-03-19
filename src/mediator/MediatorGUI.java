package mediator;

import model.Buyer;
import model.Manufacturer;
import model.Offer;
import model.Service;

public interface MediatorGUI {

	// TODO read type and services from config file
	void login(String username, String password);
	void logout();

	// for manufacturers only
	void makeOffer(Service service, Buyer buyer, Offer offer);
	void dropAuction(Service service, Buyer buyer);

	// for buyers only
	void launchOfferRequest(Service service);
	void dropOfferRequest(Service service);
	void acceptOffer(Service service, Manufacturer manufacturer);
	void refuseOffer(Service service, Manufacturer manufacturer);
}
