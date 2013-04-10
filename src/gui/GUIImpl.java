package gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mediator.MediatorGUI;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserInfo;
import model.service.info.UserServicesInfo;
import model.service.info.UserServicesInfoImpl;
import model.state.OfferState;
import model.state.ServiceState;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

import org.apache.log4j.Logger;

/**
 * Implements {@link GUI}.
 *
 * @author cmihail, radu-tutueanu
 */
public class GUIImpl implements GUI {

	private static final Logger logger = Logger.getLogger(GUIImpl.class);
	private final MediatorGUI mediator;
	private UserServicesInfo userServicesInfo;

	public GUIImpl(MediatorGUI mediator) {
		this.mediator = mediator;
	}

	@Override
	public void drawErrorPage(String errorMessage) {
		logger.warn(errorMessage);
		System.exit(1);
	}

	@Override
	public void drawMainPage(User mainUser, Map<Service, Set<User>> mapServiceUsers) {
		// Init services info.
		userServicesInfo = new UserServicesInfoImpl(mapServiceUsers);
		mediator.setUserServicesInfo(userServicesInfo);

		// Print user.
		logger.info("User:");
		logger.info("\tUsername: " + mainUser.getUsername());
		logger.info("\tType: " + mainUser.getType());

		logger.info("\tServices: ");
		for (Service s : mainUser.getServices())
			logger.info(s.getName() + ", ");
		logger.info("\n");

		// Print map service users.
		logger.info("Services users:");
		Set<Service> services = mapServiceUsers.keySet();
		for (Service s : services) {
			logger.info("\tService: " + s.getName());

			Iterator<User> usersIt = mapServiceUsers.get(s).iterator();
			if (usersIt.hasNext())
				logger.info("\t\tUsers: ");
			else
				logger.info("\t\tNo users");
			while (usersIt.hasNext()) {
				User u = usersIt.next();
				logger.info(u.getUsername() + ", ");
			}
			logger.info("\n");
		}
		logger.info("\n");
	}

	@Override
	public void addUserForService(User newUser, Service service) {
		userServicesInfo.getServiceInfo(service).addUser(newUser);
		// TODO gui change
	}

	@Override
	public void removeUserForService(User oldUser, Service service) {
		userServicesInfo.getServiceInfo(service).removeUser(oldUser);
		// TODO gui change
	}

	@Override
	public void setManufacturerServiceOffer(Manufacturer manufacturer, Service service,
			Offer offer) {
		userServicesInfo.getServiceInfo(service).getUserInfo(manufacturer).setOffer(offer);
		// TODO gui change
		logger.info("Recevied offer <" + offer.getPrice() + "> from <" +
				manufacturer.getUsername() + "> for service <" +
				service.getName() + ">"); // TODO only for testing
	}

	@Override
	public void activateBuyerForService(Buyer buyer, Service service) {
		// TODO gui change (show buyer)
	}

	@Override
	public void deactivateBuyerForService(Buyer buyer, Service service) {
		// TODO gui change (hide buyer)
	}

	@Override
	public void changeBuyerOfferState(Buyer buyer, Service service,
			OfferState offerState) {
		userServicesInfo.getServiceInfo(service).getUserInfo(buyer).setOfferState(offerState);
		// TODO gui change
	}

	@Override
	public void compareServiceOffer(Buyer buyer, Service service, Offer offer) {
		UserInfo userInfo = userServicesInfo.getServiceInfo(service).getUserInfo(buyer);
		if (offer.getPrice().compareTo(userInfo.getOffer().getPrice()) >= 0) {
			userInfo.setOfferState(OfferState.OFFER_EXCEEDED);
			// TODO gui change
		}
	}

	@Override
	public void setTransferState(User user, Service service,
			TransferState transferState) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO delete from here down when GUI ready
	 * TODO synchronize interface (can send or receive events for drawing)
	 */
	@Override
	public void generateEvents() {
		synchronized (userServicesInfo) {
			Iterator<Service> it = userServicesInfo.getServices().iterator();
			Service s1 = null, s2 = null;
			if (it.hasNext()) {
				s1 = it.next();
				userServicesInfo.getServiceInfo(s1).setServiceState(ServiceState.ACTIVE);
				mediator.launchOfferRequest(s1);
			}
			if (it.hasNext()) {
				s2 = it.next();
				userServicesInfo.getServiceInfo(s2).setServiceState(ServiceState.ACTIVE);
				mediator.launchOfferRequest(s2);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.toString());
				System.exit(1);
			}

			if (s2 != null) {
				userServicesInfo.getServiceInfo(s2).setServiceState(ServiceState.INACTIVE);
				mediator.dropOfferRequest(it.next());
			}
		}
	}
}
