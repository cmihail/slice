package mediator;

import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserServicesInfo;

public interface MediatorGUI {

	void setUserServicesInfo(UserServicesInfo userServicesInfo);

	// TODO read type and services from config file
	void logout();

	// for manufacturers only
	void makeOffer(Service service, Buyer buyer, Offer offer);
	void dropAuction(Service service, Buyer buyer);

	// for buyers only
	void launchOfferRequest(Service service);
	void dropOfferRequest(Service service);
	void acceptOffer(Service service, Manufacturer manufacturer);
	void refuseOffer(Service service, Manufacturer manufacturer);

	// TODO add transfer methods
	void transfer(Service service, User toUser);
}
