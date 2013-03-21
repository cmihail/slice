package mediator;

import model.service.Offer;
import model.service.Service;
import model.service.info.UserServicesInfo;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

public interface MediatorGUI {

	void setUserServicesInfo(UserServicesInfo userServicesInfo);

	void logout();

	// for manufacturers only
	void makeOffer(Service service, Buyer buyer, Offer offer);
	void dropAuction(Service service, Buyer buyer);

	// for buyers only
	void launchOfferRequest(Service service);
	void dropOfferRequest(Service service);
	void acceptOffer(Service service, Manufacturer manufacturer);
	void refuseOffer(Service service, Manufacturer manufacturer);

	// for transfers
	void transfer(Service service, User toUser);
}
