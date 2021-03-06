package mediator;

import gui.GUI;
import gui.LoginFrame;
import gui.MainFrameBuyer;
import gui.MainFrameManufacturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.service.Offer;
import model.service.Price;
import model.service.Service;
import model.service.ServiceImpl;
import model.service.Timer;
import model.service.info.ServiceInfo;
import model.service.info.UserInfo;
import model.service.info.UserServicesInfo;
import model.state.OfferState;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import model.user.User.Type;
import network.Network;
import network.NetworkImpl;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import webserviceclient.WebServiceClient;
import webserviceclient.WebServiceClientImpl;
import constants.Constants;

/**
 * Implements {@link MediatorGUI}, {@link MediatorNetwork},
 * {@link MediatorWebServiceClient} in order to respect the Mediator Design.
 *
 * @author cmihail, radu-tutueanu
 */
public class MediatorImpl implements MediatorGUI, MediatorNetwork,
		MediatorWebServiceClient {

	private final static Logger logger = Logger.getLogger(MediatorImpl.class);
	private final LoginFrame login;
	private final WebServiceClient webServiceClient;
	private GUI gui;
	private Network network;
	private User mainUser;
	private UserServicesInfo userServicesInfo;

	public MediatorImpl() {
		login = new LoginFrame(this);
		webServiceClient = new WebServiceClientImpl(this);
		login.setVisible(true);
	}

	// TODO only for testing
	private void testTransfer() {
		User toUser = null;
		Service sentService = null;
		Set<Service> services = userServicesInfo.getServices();
		for (Service s : services) {
			Iterator<User> usersIt = userServicesInfo.getServiceInfo(s).getUsers().iterator();
			while (usersIt.hasNext()) {
				User u = usersIt.next();

				if ("user2".equals(u.getUsername()) &&
						"service2".equals(s.getName())) {
					toUser = u;
					sentService = s;
				}
			}
		}

		if ("user1".equals(mainUser.getUsername()))
			transfer(sentService, toUser);
	}

	@Override
	public void webServiceError(String errorMessage) {
		login.drawErrorPage(errorMessage);
	}

	@Override
	public void networkError(String errorMessage) {
		logError(errorMessage);
	}

	@Override
	public void setUserServicesInfo(UserServicesInfo userServicesInfo) {
		this.userServicesInfo = userServicesInfo;
	}

	@Override
	public void login(String username, String password) {
		readConfigFileAndLogin(username + Constants.CONFIG_FILE_EXTENSION,
				username, password);
		network = new NetworkImpl(this, mainUser);

		// Add logger file.
		try {
			PatternLayout layout = new PatternLayout(Constants.LOGGER_PATTERN);
			String file = Constants.LOGGER_FOLDER + username +
					Constants.LOGGER_FILE_EXTENSION;
			FileAppender appender = new FileAppender(layout, file, true);
			Logger.getRootLogger().addAppender(appender);
		} catch (IOException e) {
			logError("Couln't append logger file");
			logger.error(e.toString());
		}
	}

	@Override
	public void logout() {
		webServiceClient.logout(mainUser);
		network.userLogout(mainUser);
	}

	@Override
	public void disconnectedUserFromServer(User disconnectedUser) {
		for (Service service : disconnectedUser.getServices()) {
			ServiceInfo si = userServicesInfo.getServiceInfo(service);
			// Ignore services which disconnected user doesn't own.
			if (si == null)
				continue;

			for (User user : si.getUsers()) {
				// Ignore user if not the same.
				if (!user.equals(disconnectedUser))
					continue;

				UserInfo ui = si.getUserInfo(user);

				// Refuse offers in progress.
				if (mainUser instanceof Manufacturer && user instanceof Buyer &&
						ui.getOfferState() == OfferState.OFFER_MADE)
					receiveRefusedServiceOffer((Buyer) user, service);

				// Cancel transfers in progress.
				if (ui.getTransferState() == TransferState.TRANSFER_STARTED ||
						ui.getTransferState() == TransferState.TRANSFER_IN_PROGRESS) {
					setServiceTransferState(user, service, TransferState.TRANSFER_FAILED);
					logger.warn("Transfer canceled for < " + service.getName() +
							" > from < " + user.getUsername() + " >");
					System.out.println("Transfer canceled");
				}

				// Unregister disconnected user.
///				unregisterUserForService(disconnectedUser, service);
			}
		}
	}

	@Override
	public void registerUserForService(User userToRegister, Service service) {
		gui.addUserForService(userToRegister, service);
	}

	@Override
	public void registerUserForAllServices(User userToRegister) {
		if (mainUser.getType() == userToRegister.getType()) {
			logger.warn("Invalid user to register for all services < " +
					userToRegister.getUsername() + ">");
			return;
		}

		List<Service> userServices = mainUser.getServices();
		for (Service service : userToRegister.getServices()) {
			if (userServices.contains(service)) {
				registerUserForService(userToRegister, service);
			}
		}
	}

	@Override
	public void unregisterUserForService(User userToUnregister, Service service) {
		gui.removeUserForService(userToUnregister, service);
	}

	@Override
	public void receiveServiceOffer(Manufacturer manufacturer, Service service,
			Offer offer) {
		gui.setManufacturerServiceOffer(manufacturer, service, offer);
		if (mainUser instanceof Buyer)
			network.announceUsersOfServiceOffer((Buyer) mainUser, manufacturer,
					service, offer, userServicesInfo.getServiceInfo(service).getUsers());
		else
			logError("Invalid user type at receiveServiceOffer.");
	}

	@Override
	public void dropAuctionForManufacturer(Manufacturer manufacturer,
			Service service) {
		unregisterUserForService(manufacturer, service);
	}

	@Override
	public void receiveLaunchedServiceOfferRequest(Buyer buyer, Service service) {
		gui.activateBuyerForService(buyer, service);
	}

	@Override
	public void receiveDroppedServiceOfferRequest(Buyer buyer, Service service) {
		gui.deactivateBuyerForService(buyer, service);
	}

	@Override
	public void receiveRefusedServiceOffer(Buyer buyer, Service service) {
		gui.changeBuyerOfferState(buyer, service, OfferState.OFFER_REFUSED);
	}

	@Override
	public void receiveAcceptedServiceOffer(Buyer buyer, Service service) {
		gui.changeBuyerOfferState(buyer, service, OfferState.OFFER_ACCEPTED);
	}

	@Override
	public void receiveServiceOfferAnnouncement(Buyer buyer, Service service,
			Offer offer) {
		gui.compareServiceOffer(buyer, service, offer);
	}

	@Override
	public void setServiceTransferState(User fromUser, Service service,
			TransferState transferState) {
		gui.setTransferState(fromUser, service, transferState);
	}

	@Override
	public void setServiceTransferPercentage(User fromUser, Service service,
			int percentage) {
		gui.setTransferPercentage(fromUser, service, percentage);
	}

	@Override
	public void makeOffer(Service service, Buyer buyer, Offer offer) {
		if (mainUser instanceof Manufacturer)
			network.makeServiceOffer((Manufacturer) mainUser, buyer, service, offer);
		else
			logError("Invalid user type at makeOffer.");
	}

	@Override
	public void dropAuction(Service service, Buyer buyer) {
		if (mainUser instanceof Manufacturer)
			network.dropUserAuction((Manufacturer) mainUser, buyer, service);
		else
			logError("Invalid user type at dropAuction.");
	}

	@Override
	public void launchOfferRequest(Service service) {
		if (mainUser instanceof Buyer)
			network.launchServiceOfferRequest((Buyer) mainUser, service,
					userServicesInfo.getServiceInfo(service).getUsers());
		else
			logError("Invalid user type at launchOfferRequest.");
	}

	@Override
	public void dropOfferRequest(Service service) {
		if (mainUser instanceof Buyer)
			network.dropServiceOfferRequest((Buyer) mainUser, service,
					userServicesInfo.getServiceInfo(service).getUsers());
		else
			logError("Invalid user type at dropOfferRequest.");
	}

	@Override
	public void acceptOffer(Service service, Manufacturer manufacturer) {
		if (mainUser instanceof Buyer)
			network.acceptServiceOffer((Buyer) mainUser, manufacturer, service,
					userServicesInfo.getServiceInfo(service).getUsers());
		else
			logError("Invalid user type at acceptOffer.");
	}

	@Override
	public void refuseOffer(Service service, Manufacturer manufacturer) {
		if (mainUser instanceof Buyer)
			network.refuseServiceOffer((Buyer) mainUser, manufacturer, service);
		else
			logError("Invalid user type at refuseOffer.");
	}

	@Override
	public void transfer(Service service, User toUser) {
		network.startTransfer(mainUser, toUser, service);
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
		String line;
		List<Service> userServices = new ArrayList<Service>();
		while ((line = in.readLine()) != null) {
			if (userType == Type.BUYER) {
				String[] tokens = line.split(" ");
				if (tokens.length != 3) {
					login.drawErrorPage("Invalid service line");
					return null;
				}
				userServices.add(new ServiceImpl(tokens[0],
						new Price(Integer.parseInt(tokens[1])),
						new Timer(Integer.parseInt(tokens[2]))));
			} else {
				userServices.add(new ServiceImpl(line));
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

	private boolean login(User user, String password) {
		Map<Service, Set<User>> mapServiceUsers =
				webServiceClient.login(mainUser, password);

		// Draw corresponded page in GUI.
		if (mapServiceUsers == null) {
			logError("Error at login credentials (see config file).");
			return false;
		}

		login.setVisible(false);
		if(mainUser.getType()== User.Type.BUYER)
			gui = new MainFrameBuyer(this);
		else
			gui = new MainFrameManufacturer(this);
		gui.drawMainPage(mainUser, mapServiceUsers);
		return true;
	}

	private void readConfigFileAndLogin(String configFile, String username, String password) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(configFile));
			User.Type userType = readUserType(in);
			if (userType == null)
				login.drawErrorPage("Invalid user type (0 = Buyer or 1 = Manufacturer).");

			List<Service> userServices = readServices(in, userType);
			if (userServices == null)
				login.drawErrorPage("Invalid services.");

			mainUser = createUser(username, userType, userServices);
			if (mainUser == null)
				login.drawErrorPage("Invalid user creation.");

			login(mainUser, password);
		} catch(NumberFormatException e) {
			login.drawErrorPage("Invalid user type (0 = Buyer or 1 = Manufacturer).");
		} catch (IOException e) {
			logger.error(e.toString());
			login.drawErrorPage("Error at reading config file.");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.toString());
					login.drawErrorPage("Error at reading config file.");
				}
			}
		}
	}

	private void logError(String errorMessage) {
		gui.drawErrorPage(errorMessage);
	}
}
