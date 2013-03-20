package webserviceclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mediator.MediatorWebServiceClient;
import model.Buyer;
import model.Manufacturer;
import model.User;
import model.service.Service;
import model.service.ServiceImpl;

public class WebServiceClientImpl implements WebServiceClient {

	private final MediatorWebServiceClient mediator;

	public WebServiceClientImpl(MediatorWebServiceClient mediator) {
		this.mediator = mediator;
	}

	@Override
	public Map<Service, List<User>> login(User user, String password) {
		try {
			Thread.sleep(200); // TODO delete (only for testing)
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Create mockup data.
		Map<Service, List<User>> mapServiceUsers = new HashMap<Service, List<User>>();
		Random random = new Random();

		// Randomize users.
		List<User> users = new ArrayList<User>();
		int numOfUsers = random.nextInt(4) + 2; // minimum 2 users
		for (int i = 0; i < numOfUsers; i++) {
			int numOfServices = random.nextInt(user.getServices().size() - 1);
			numOfServices++; // minimum 1 service
			List<Service> services = new ArrayList<Service>();
			for (int j = 0; j < numOfServices; j++) {
				services.add(new ServiceImpl(user.getServices().get(j).getName()));
			}

			switch (user.getType()) {
			case BUYER:
				users.add(new Buyer("Username " + i, services));
				break;
			case MANUFACTURER:
				users.add(new Manufacturer("Username " + i, services));
				break;
			}
		}

		Iterator<Service> it = user.getServices().iterator();
		while (it.hasNext()) {
			Service service = it.next();
			List<User> serviceUsers = new ArrayList<User>();

			Iterator<User> usersIt = users.iterator();
			while (usersIt.hasNext()) {
				User u = usersIt.next();

				Iterator<Service> serviceIt = u.getServices().iterator();
				while (serviceIt.hasNext()) {
					Service s = serviceIt.next();

					if (service.getName().equals(s.getName())) {
						serviceUsers.add(u);
					}
				}
			}

			mapServiceUsers.put(service, serviceUsers);
		}

		return mapServiceUsers;
	}

	@Override
	public void logout(User user) {
		// TODO Auto-generated method stub

	}
}
