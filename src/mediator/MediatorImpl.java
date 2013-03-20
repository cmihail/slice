package mediator;

import gui.GUI;
import gui.GUIImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Buyer;
import model.Manufacturer;
import model.User;
import model.User.Type;
import model.service.Offer;
import model.service.Price;
import model.service.Service;
import model.service.ServiceImpl;
import model.service.Timer;
import network.Network;
import network.NetworkImpl;
import webserviceclient.WebServiceClient;
import webserviceclient.WebServiceClientImpl;

public class MediatorImpl implements MediatorGUI, MediatorNetwork,
		MediatorWebServiceClient {

	private final String CONFIG_FILE = "config";
	private final GUI gui;
	private final Network network;
	private final WebServiceClient webServiceClient;
	private User mainUser;

	public MediatorImpl() {
		this.gui = new GUIImpl(this);
		this.network = new NetworkImpl(this);
		this.webServiceClient = new WebServiceClientImpl(this);

		readConfigFileAndLogin();
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerUserForService(User userToRegister, Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterUserForService(User userToRegister, Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveServiceOffer(Manufacturer manufacturer, Service service,
			Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropAuctionForManufacturer(Manufacturer manufacturer,
			Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveLaunchServiceOfferRequest(Buyer buyer, Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveDropServiceOfferRequest(Buyer buyer, Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveRefusedServiceOffer(Buyer buyer, Service service,
			Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveAcceptedServiceOffer(Buyer buyer, Service service,
			Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveServiceOfferAnnouncement(Buyer buyer, Service service,
			Offer offer) {
		// TODO Auto-generated method stub

	}


	@Override
	public void makeOffer(Service service, Buyer buyer, Offer offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropAuction(Service service, Buyer buyer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void launchOfferRequest(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropOfferRequest(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptOffer(Service service, Manufacturer manufacturer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refuseOffer(Service service, Manufacturer manufacturer) {
		// TODO Auto-generated method stub

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

	private User createUser(String username, User.Type userType,
			List<Service> userServices) {
		User user = null;
		switch (userType) {
		case BUYER:
			user = new Buyer(username, userServices);
			break;
		case MANUFACTURER:
			user = new Manufacturer(username, userServices);
			break;
		default:
			break;
		}

		return user;
	}

	private void login(User user, String password) {
		Map<Service, List<User>> mapServiceUsers =
				webServiceClient.login(mainUser, password);

		if (mapServiceUsers == null)
			gui.drawErrorPage("Error at login credentials (see config file).");
		else
			gui.drawMainPage(mainUser, mapServiceUsers);
	}

	private void readConfigFileAndLogin() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(CONFIG_FILE));
			String username = in.readLine();
			if (username == null) {
				gui.drawErrorPage("No username.");
				return;
			}
			String password = in.readLine();
			if (password == null) {
				gui.drawErrorPage("No password.");
				return;
			}
			User.Type userType = readUserType(in);
			if (userType == null) {
				gui.drawErrorPage("Invalid user type (0 = Buyer or 1 = Manufacturer).");
				return;
			}
			List<Service> userServices = readServices(in, userType);
			if (userServices == null) {
				gui.drawErrorPage("Invalid services.");
				return;
			}

			mainUser = createUser(username, userType, userServices);
			if (mainUser == null) {
				gui.drawErrorPage("Invalid user creation.");
				return;
			}

			login(mainUser, password);
		} catch(NumberFormatException e) {
			gui.drawErrorPage("Invalid user type (0 = Buyer or 1 = Manufacturer).");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			gui.drawErrorPage("Error at reading config file.");
		}
	}
}
