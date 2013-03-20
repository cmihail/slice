package gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mediator.MediatorGUI;
import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserInfo;
import model.service.info.UserServicesInfo;
import model.service.info.UserServicesInfoImpl;
import model.state.OfferState;
import model.state.ServiceState;
import model.state.TransferState;

public class GUIImpl implements GUI {

	private final MediatorGUI mediator;
	private UserServicesInfo userServicesInfo;

	public GUIImpl(MediatorGUI mediator) {
		this.mediator = mediator;
	}

	@Override
	public void drawErrorPage(String errorMessage) {
		System.err.println(errorMessage);
		System.exit(1);
	}

	@Override
	public void drawMainPage(User user, Map<Service, Set<User>> mapServiceUsers) {
		// Init services info.
		userServicesInfo = new UserServicesInfoImpl(mapServiceUsers);
		mediator.setUserServicesInfo(userServicesInfo);

		// Print user.
		System.out.println("User:");
		System.out.println("\tUsername: " + user.getUsername());
		System.out.println("\tType: " + user.getType());

		System.out.print("\tServices: ");
		for (Service s : user.getServices())
			System.out.print(s.getName() + ", ");
		System.out.println();

		// Print map service users.
		System.out.println("Services users:");
		Set<Service> services = mapServiceUsers.keySet();
		for (Service s : services) {
			System.out.println("\tService: " + s.getName());

			Iterator<User> usersIt = mapServiceUsers.get(s).iterator();
			if (usersIt.hasNext())
				System.out.print("\t\tUsers: ");
			else
				System.out.print("\t\tNo users");
			while (usersIt.hasNext()) {
				User u = usersIt.next();
				System.out.print(u.getUsername() + ", ");
			}
			System.out.println();
		}
		System.out.println();
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
				e.printStackTrace();
				System.exit(1);
			}

			if (s2 != null) {
				userServicesInfo.getServiceInfo(s2).setServiceState(ServiceState.INACTIVE);
				mediator.dropOfferRequest(it.next());
			}
		}
	}
}
