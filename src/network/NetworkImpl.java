package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import network.common.Communication;
import network.model.AcceptServiceOffer;
import network.model.AnnounceUserOfServiceOffer;
import network.model.CancelTransfer;
import network.model.DropServiceOfferRequest;
import network.model.DropUserAuction;
import network.model.LaunchServiceOfferRequest;
import network.model.MakeServiceOffer;
import network.model.NetworkObject;
import network.model.NetworkUser;
import network.model.RefuseServiceOffer;
import network.model.RegisterUserForService;
import network.model.StartTransfer;
import network.model.UnregisterUserForService;
import constants.Constants;

/**
 * Implements {@link Network}.
 *
 * @author cmihail, radu-tutueanu
 */
public class NetworkImpl implements Network {

	private final MediatorNetwork mediator;
	private SocketChannel socketChannel = null;

	public NetworkImpl(MediatorNetwork mediator, User mainUser) {
		this.mediator = mediator;

		socketChannel = connectToServer();
		Thread thread = new Thread(new ReceiveIncomingMessagesThread());
		thread.start();
		Communication.send(socketChannel, new NetworkUser(mainUser));
	}

	@Override
	public void registerUserServiceToUsers(User mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Register users for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			synchronized (socketChannel) {
				Communication.send(socketChannel,
						new RegisterUserForService(mainUser, service, user));
			}
		}
	}

	@Override
	public void unregisterUserServiceFromUsers(User mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Unregister users for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			synchronized (socketChannel) {
				Communication.send(socketChannel,
						new UnregisterUserForService(mainUser, service, user));
			}
		}
	}

	@Override
	public void makeServiceOffer(Manufacturer mainUser, Buyer buyer,
			Service service, Offer offer) {
		System.out.println("Service (" + service.getName() +
				") offer for buyer < " + buyer.getUsername() + " >:" +
				offer.getPrice());
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new MakeServiceOffer(mainUser, buyer, service, offer));
		}
	}

	@Override
	public void dropUserAuction(Manufacturer mainUser, Buyer buyer,
			Service service) {
		System.out.println("Droped auction (" + service.getName() +
				") for buyer < " + buyer.getUsername() + " >");
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new DropUserAuction(mainUser, buyer, service));
		}
	}

	@Override
	public void launchServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Launch offer request for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			synchronized (socketChannel) {
				Communication.send(socketChannel,
						new LaunchServiceOfferRequest(mainUser, service, user));
			}
		}
	}

	@Override
	public void dropServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService) {
		System.out.println("Dropped offer request for service < " + service.getName() + " >");
		for (User user : usersWithService) {
			synchronized (socketChannel) {
				Communication.send(socketChannel,
						new DropServiceOfferRequest(mainUser, service, user));
			}
		}
	}

	@Override
	public void acceptServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service, Set<User> usersWithService) {
		System.out.println("Accept service (" + service.getName() +
				") offer for manufacturer < " + manufacturer.getUsername() + " >");
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new AcceptServiceOffer(mainUser, manufacturer, service));
		}

		// Refuse offer for all other manufacturers.
		for (User user : usersWithService) {
			if (!user.equals(mainUser) && user instanceof Manufacturer) {
				synchronized (socketChannel) {
					Communication.send(socketChannel,
							new RefuseServiceOffer(mainUser, (Manufacturer) user, service));
				}
			}
		}
	}

	@Override
	public void refuseServiceOffer(Buyer mainUser, Manufacturer manufacturer,
			Service service) {
		System.out.println("Refuse service (" + service.getName() +
				") offer for manufacturer < " + manufacturer.getUsername() + " >");
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new RefuseServiceOffer(mainUser, manufacturer, service));
		}
	}

	@Override
	public void announceUsersOfServiceOffer(Buyer mainUser,
			Manufacturer originatorManufacturer, Service service,
			Offer offer, Set<User> usersWithService) {
		System.out.println("Annonuce users of service (" + service.getName() +
				") offer (" + offer.getPrice() + ")");
		for (User user : usersWithService) {
			if (!originatorManufacturer.equals(mainUser)) {
				synchronized (socketChannel) {
					Communication.send(socketChannel,
							new AnnounceUserOfServiceOffer(mainUser, service, offer, user));
				}
			}
		}
	}

	@Override
	public void startTransfer(User mainUser, User toUser, Service service) {
		System.out.println("Transfer service (" + service.getName() +
				") to user (" + toUser.getUsername() + ")");
		// TODO send the file itself
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new StartTransfer(mainUser, toUser, service));
		}
	}

	@Override
	public void cancelTransfer(User mainUser, User toUser, Service service) {
		System.out.println("Cancel transfer service (" + service.getName() +
				") to user (" + toUser.getUsername() + ")");
		// TODO stop file sending
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new CancelTransfer(mainUser, toUser, service));
		}
	}

	private SocketChannel connectToServer() {
		SocketChannel socketChannel = null;

		try {
	    	socketChannel = SocketChannel.open();
	        socketChannel.socket().connect(
	        		new InetSocketAddress(Constants.SERVER_IP, Constants.SERVER_PORT));
	        socketChannel.configureBlocking(false);
		} catch (IOException e) {
			System.out.println("Couldn't connect to server"); // TODO maybe something else
			System.exit(1);
		}

		return socketChannel;
	}

	/**
	 * Receives incoming messages in a different thread.
	 *
	 * @author cmihail, radu-tutueanu
	 */
	private class ReceiveIncomingMessagesThread implements Runnable {

		private void connect(SelectionKey key) throws IOException {
			synchronized (socketChannel) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				if (!socketChannel.finishConnect())
					throw new IOException("Error at connecting to server");
				key.interestOps(SelectionKey.OP_READ);
			}
		}

		private void read(SelectionKey key) {
			synchronized (socketChannel) {
				SocketChannel socketChannel = (SocketChannel) key.channel();

				try {
					NetworkObject networkObj = Communication.recv(socketChannel);
				} catch (Communication.EndConnectionException e) {
					System.out.println("TODO"); // TODO
					System.exit(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			Selector selector = null;
			try {
				selector = Selector.open();
				synchronized (socketChannel) {
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
				}

				while (true) {
					selector.select();

					for (Iterator<SelectionKey> it = selector.selectedKeys().iterator();
							it.hasNext(); ) {
						SelectionKey key = it.next();
						it.remove();

						if (key.isConnectable())
							connect(key);
						else if (key.isReadable())
							read(key);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	private class ReceiveIncomingMessagesThread implements Runnable {
//
//		private final MediatorNetwork mediator;
//		private final User mainUser;
//		private final UserServicesInfo userServicesInfo;
//
//		public ReceiveIncomingMessagesThread(MediatorNetwork mediator,
//				User mainUser, UserServicesInfo userServicesInfo) {
//			this.mediator = mediator;
//			this.mainUser = mainUser;
//			this.userServicesInfo = userServicesInfo;
//		}
//
//		@Override
//		public void run() {
//			// TODO receive incoming message from other clients
//
//			// for testing only: we assume we received offers
//			Random random = new Random();
//
//			if (userServicesInfo.getServices().isEmpty())
//				return;
//
//			while (true) {
//				int seconds = random.nextInt(2) + 1;
//				try {
//					Thread.sleep(seconds * 1000);
//
//					// generate random service
//					Set<Service> usi = userServicesInfo.getServices();
//					int index = random.nextInt(usi.size()), i;
//					ServiceInfo si = null;
//					Iterator<Service> it = usi.iterator();
//					i = 0;
//					while (it.hasNext()) {
//						si = userServicesInfo.getServiceInfo(it.next());
//						if (i == index)
//							break;
//						i++;
//					}
//
//					// generate random user
//					Set<User> users = si.getUsers();
//					if (users.isEmpty())
//						continue;
//
//					index = random.nextInt(users.size());
//					User user = null;
//					Iterator<User> itUsers = users.iterator();
//					i = 0;
//					while (itUsers.hasNext()) {
//						user = itUsers.next();
//						if (i == index)
//							break;
//						i++;
//					}
//
//					if (mainUser instanceof Buyer) {
//						if (user != null && user instanceof Manufacturer) {
//							int price = random.nextInt(10000) + 1;
//							mediator.receiveServiceOffer((Manufacturer) user,
//									si.getService(), new OfferImpl(new Price(price)));
//						}
//					}
//					if (mainUser instanceof Manufacturer) {
//						if (user != null && user instanceof Buyer) {
//							mediator.receiveLaunchedServiceOfferRequest((Buyer) user, si.getService());
//
//						}
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
