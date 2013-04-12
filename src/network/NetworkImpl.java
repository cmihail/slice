package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.SwingWorker;

import mediator.MediatorNetwork;
import model.service.Offer;
import model.service.Service;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import network.common.Communication;
import network.model.NetworkObject;
import network.model.NetworkTransferService;
import network.model.NetworkUser;
import network.model.command.AcceptServiceOffer;
import network.model.command.AnnounceUserOfServiceOffer;
import network.model.command.DropServiceOfferRequest;
import network.model.command.DropUserAuction;
import network.model.command.LaunchServiceOfferRequest;
import network.model.command.MakeServiceOffer;
import network.model.command.RefuseServiceOffer;
import network.model.command.RegisterUserForService;
import network.model.command.StartTransfer;
import network.model.command.UnregisterUserForService;

import org.apache.log4j.Logger;

import constants.Constants;

/**
 * Implements {@link Network}.
 *
 * @author cmihail, radu-tutueanu
 */
public class NetworkImpl implements Network {

	private final static Logger logger = Logger.getLogger(NetworkImpl.class);
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
	public void userLogout(User userToLogout) {
		synchronized (socketChannel) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	@Override
	public void registerUserServiceToUsers(User mainUser, Service service,
			Set<User> usersWithService) {
		logger.info("Register users for service < " + service.getName() + " >");
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
		logger.info("Unregister users for service < " + service.getName() + " >");
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
		logger.info("Service (" + service.getName() + ") offer for buyer < " +
				buyer.getUsername() + " >:" + offer.getPrice());
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new MakeServiceOffer(mainUser, buyer, service, offer));
		}
	}

	@Override
	public void dropUserAuction(Manufacturer mainUser, Buyer buyer,
			Service service) {
		logger.info("Droped auction (" + service.getName() + ") for buyer < " +
				buyer.getUsername() + " >");
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new DropUserAuction(mainUser, buyer, service));
		}
	}

	@Override
	public void launchServiceOfferRequest(Buyer mainUser, Service service,
			Set<User> usersWithService) {
		logger.info("Launch offer request for service < " + service.getName() + " >");
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
		logger.info("Dropped offer request for service < " + service.getName() + " >");
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
		logger.info("Accept service (" + service.getName() + ") offer for manufacturer < " +
				manufacturer.getUsername() + " >");
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
		logger.info("Refuse service (" + service.getName() +
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
		logger.info("Annonuce users of service (" + service.getName() + ") offer (" +
				offer.getPrice() + ")");
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
	public void startTransfer(final User mainUser, final User toUser,
			final Service service) {
		logger.info("Transfer service (" + service.getName() + ") to user (" +
				toUser.getUsername() + ")");
		synchronized (socketChannel) {
			Communication.send(socketChannel,
					new StartTransfer(mainUser, toUser, service));
		}

		// Send the file itself.
		SendFileWorker worker;
		try {
			worker = new SendFileWorker(mainUser, toUser, service);
			worker.execute();
		} catch (Exception e) {
			logger.warn(e.getMessage());
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
			logger.error("Couldn't connect to server");
			System.exit(1);
		}

		return socketChannel;
	}

	/**
	 * Sends a file in a different thread.
	 *
	 * @author cmihail, radu-tutueanu
	 */
	private class SendFileWorker extends SwingWorker<Integer, Integer> {

		private final User sender;
		private final User receiver;
		private final Service service;
		private final int totalSize;
		private int currentSentSize = 0;

		public SendFileWorker(User sender, User receiver, Service service)
				throws Exception {
			if (!(sender instanceof Buyer))
				throw new Exception("Invalid user for sending");
			this.sender = sender;
			this.receiver = receiver;
			this.service = service;
			this.totalSize = service.getPriceLimit().toInt() * Constants.SERVICE_BLOCK_SIZE;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			Random generator = new Random();

			while (totalSize > currentSentSize) {
				int percentage = Math.round((float)currentSentSize / totalSize * 100);
				logger.info("Sending service segment (" + currentSentSize + ") for < " +
						service.getName() + " > to < " + receiver.getUsername() +
						" > (percentage: " + percentage + "%)");

				// Generate random bytes.
				int sendingSize = Math.min(totalSize - currentSentSize,
						Constants.SERVICE_NETWORK_SEGMENT_SIZE);
				byte[] bytes = new byte[sendingSize];
				generator.nextBytes(bytes);

				// Send segment.
				NetworkTransferService transferService =
						new NetworkTransferService(sender, receiver, service,
								currentSentSize, totalSize, bytes);
				synchronized (socketChannel) {
					currentSentSize += Communication.send(socketChannel, transferService);
				}

				// TODO delete (only for testing cancel)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return 0;
		}
	}

	/**
	 * TODO maybe as swing worker
	 * Receives incoming messages in a different thread.
	 *
	 * @author cmihail, radu-tutueanu
	 */
	private class ReceiveIncomingMessagesThread implements Runnable {

		private void read(SelectionKey key) {
			synchronized (socketChannel) {
				SocketChannel socketChannel = (SocketChannel) key.channel();

				try {
					NetworkObject networkObj = Communication.recv(socketChannel);
					networkObj.handler(mediator);
				} catch (Communication.EndConnectionException e) {
					logger.error("Connection with server lost");
					System.exit(1); // TODO maybe another action
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
					socketChannel.register(selector, SelectionKey.OP_READ);
				}

				while (true) {
					selector.select();

					for (Iterator<SelectionKey> it = selector.selectedKeys().iterator();
							it.hasNext(); ) {
						SelectionKey key = it.next();
						it.remove();

						if (key.isReadable())
							read(key);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
