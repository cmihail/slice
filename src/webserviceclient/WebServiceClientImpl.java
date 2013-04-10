package webserviceclient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mediator.MediatorWebServiceClient;
import model.service.Price;
import model.service.Service;
import model.service.ServiceImpl;
import model.service.Timer;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import model.user.User.Type;

import org.apache.log4j.Logger;

import constants.Constants;

/**
 * Implements {@link WebServiceClient}.
 *
 * @author cmihail, radu-tutueanu
 */
public class WebServiceClientImpl implements WebServiceClient {

	private static final Logger logger = Logger.getLogger(WebServiceClientImpl.class);
	private final MediatorWebServiceClient mediator;

	public WebServiceClientImpl(MediatorWebServiceClient mediator) {
		this.mediator = mediator;
	}

	@Override
	public Map<Service, Set<User>> login(User user, String password) {
		// TODO login to WebService
		return readConfigFiles(user, password);
	}

	@Override
	public void logout(User user) {
		// TODO logout user from WebService
	}

	/*
	 * TODO delete (only for testing from here)
	 */
	private Map<Service, Set<User>> readConfigFiles(User user, String password) {
		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();

		Set<User> users = new HashSet<User>();
		for (int i = 1; i <= 3; i++) {
			if (("user" + i).equals(user.getUsername()))
				continue;

			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new FileReader(("user" + i + Constants.CONFIG_FILE_EXTENSION)));
				User.Type userType = readUserType(in);
				if (userType == null)
					throw new Exception("Invalid user type");

				List<Service> userServices = readServices(in, userType);
				if (userServices == null)
					throw new Exception("Invalid services.");

				switch (userType) {
				case BUYER:
					users.add(new Buyer("user" + i, userServices));
					break;
				case MANUFACTURER:
					users.add(new Manufacturer("user" + i, userServices));
					break;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				System.exit(1);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}

		Iterator<Service> it = user.getServices().iterator();
		while (it.hasNext()) {
			Service service = it.next();
			Set<User> serviceUsers = new HashSet<User>();

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

	private User.Type readUserType(BufferedReader in) throws IOException {
		String typeStr = in.readLine();
		if (typeStr == null)
			return null;

		int typeInt = Integer.parseInt(typeStr);
		User.Type userType;
		switch (typeInt) {
		case 0:
			userType = Type.BUYER;
			break;
		case 1:
			userType = Type.MANUFACTURER;
			break;
		default:
			return null;
		}

		return userType;
	}

	private List<Service> readServices(BufferedReader in, User.Type userType)
			throws IOException {
		String serviceName;
		List<Service> userServices = new ArrayList<Service>();
		while ((serviceName = in.readLine()) != null) {
			if (userType == Type.BUYER) {
				// TODO maybe get price and timer from config file
				userServices.add(new ServiceImpl(serviceName, new Price(200),
						new Timer(5)));
			} else {
				userServices.add(new ServiceImpl(serviceName));
			}
		}

		if (userServices.isEmpty())
			return null;
		return userServices;
	}

//	private Map<Service, Set<User>> randomGenerate(User user, String password) {
//		try {
//			Thread.sleep(500); // TODO delete (only for testing)
//		} catch (InterruptedException e) {
//			logger.error(e.getMessage());
//			System.exit(1);
//		}
//
//		// Create mockup data.
//		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();
//		Random random = new Random();
//
//		// Randomize users.
//		Set<User> users = new HashSet<User>();
//		int numOfUsers = random.nextInt(user.getServices().size()) + 2; // minimum 2 users
//		for (int i = 0; i < numOfUsers; i++) {
//			int numOfServices = random.nextInt(user.getServices().size() - 1);
//			numOfServices++; // minimum 1 service
//			int order = random.nextInt(2), limit = 0;
//			if (order == 0)
//				limit = user.getServices().size() - 1;
//
//			List<Service> services = new ArrayList<Service>();
//			for (int j = 0; j < numOfServices; j++) {
//				int index = Math.abs(j - limit);
//				services.add(new ServiceImpl(user.getServices().get(index).getName()));
//			}
//
//			switch (user.getType()) {
//			case BUYER:
//				users.add(new Manufacturer("Username " + i, services));
//				break;
//			case MANUFACTURER:
//				users.add(new Buyer("Username " + i, services));
//				break;
//			}
//		}
//
//		Iterator<Service> it = user.getServices().iterator();
//		while (it.hasNext()) {
//			Service service = it.next();
//			Set<User> serviceUsers = new HashSet<User>();
//
//			Iterator<User> usersIt = users.iterator();
//			while (usersIt.hasNext()) {
//				User u = usersIt.next();
//
//				Iterator<Service> serviceIt = u.getServices().iterator();
//				while (serviceIt.hasNext()) {
//					Service s = serviceIt.next();
//
//					if (service.getName().equals(s.getName())) {
//						serviceUsers.add(u);
//					}
//				}
//			}
//
//			mapServiceUsers.put(service, serviceUsers);
//		}
//
//		return mapServiceUsers;
//	}
}
