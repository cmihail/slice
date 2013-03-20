package gui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mediator.MediatorGUI;
import model.User;
import model.service.Offer;
import model.service.Service;
import model.state.OfferState;
import model.state.ServiceState;

import com.sun.jdi.connect.spi.TransportService;

public class GUIImpl implements GUI {

	private final MediatorGUI mediator;

	public GUIImpl(MediatorGUI mediator) {
		this.mediator = mediator;
	}

	@Override
	public void drawErrorPage(String errorMessage) {
		System.err.println(errorMessage);
		System.exit(1);
	}

	@Override
	public void drawMainPage(User user, Map<Service, List<User>> mapServiceUsers) {
		// Print user.
		System.out.println("User:");
		System.out.println("\tUsername: " + user.getUsername());
		System.out.println("\tType: " + user.getType());
		System.out.println("\tServices: ");
		Iterator<Service> it = user.getServices().iterator();
		while (it.hasNext()) {
			Service s = it.next();
			System.out.println("\t\t" + s.getName());
		}
		System.out.println();

		// Print map service users.
		System.out.println("Services users:");
		Set<Service> services = mapServiceUsers.keySet();
		it = services.iterator();
		while (it.hasNext()) {
			Service s = it.next();
			System.out.println("\tService: " + s.getName());
			Iterator<User> usersIt = mapServiceUsers.get(s).iterator();
			while (usersIt.hasNext()) {
				User u = usersIt.next();
				System.out.println("\t\tUser: " + u.getUsername() + " / " + u.getType());
			}
		}
		System.out.println();
	}

	@Override
	public void changeServiceState(Service service, ServiceState serviceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeOfferState(Service service, OfferState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeTransferState(Service service, TransportService state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServiceOffer(Service service, User user, Offer offer) {
		// TODO Auto-generated method stub

	}

}
