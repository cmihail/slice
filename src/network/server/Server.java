package network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.service.Service;
import model.user.User;
import network.common.Communication;
import network.model.NetworkObject;
import network.model.NetworkTransferService;
import network.model.NetworkUser;
import network.model.NetworkUserDisconnect;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

import constants.Constants;

/**
 * Defines the server used to mediate the communication between clients.
 *
 * @author cmihail, radu-tutueanu
 */
public class Server {

	private static final Logger logger = Logger.getLogger(Server.class);
	private static final Map<User, SocketChannel> userSocketsMap =
			new HashMap<User, SocketChannel>();

	/**
	 * Accepts a new connection.
	 * @param key the selection key
	 * @throws IOException
	 */
	public static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);

		logger.info("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}

	/**
	 * Reads data from a client.
	 * @param key the selection key
	 */
	public static void read(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			NetworkObject networkObj = Communication.recv(socketChannel);

			if (networkObj instanceof NetworkUser) { // New user.
				NetworkUser networkUser = (NetworkUser) networkObj;
				User user = networkUser.getDestinationUser();
				userSocketsMap.put(user, socketChannel);
				logger.info("Added connection from: " +
						networkUser.getDestinationUser().getUsername());

				List<Service> userServices = user.getServices();
				for (Entry<User, SocketChannel> entry : userSocketsMap.entrySet()) {
					User u = entry.getKey();
					if (user.getType() == u.getType())
						continue;

					List<Service> uServices = u.getServices();
					for (int i = 0, limit = uServices.size(); i < limit; i++) {
						if (userServices.contains(uServices.get(i))) {
							Communication.send(entry.getValue(), networkObj);
							logger.info("Added user <" + user.getUsername() +
									"> to user <" + u.getUsername() + ">");
						}
					}
				}
			} else { // Packet that needs to be redirect.
				SocketChannel userChannel =
						userSocketsMap.get(networkObj.getDestinationUser());
				if (userChannel == null)
					throw new Exception("Invalid receiver");
				Communication.send(userChannel, networkObj);

				if (!(networkObj instanceof NetworkTransferService))
					logger.info("Packet for: " + networkObj.getDestinationUser().getUsername());
			}
		} catch (Communication.EndConnectionException e) {
			removeUser(socketChannel);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Removes a user from server.
	 * @param socketChannel the user socket channel
	 */
	private static void removeUser(SocketChannel socketChannel) {
		Iterator<Entry<User, SocketChannel>> it = userSocketsMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<User, SocketChannel> entry = it.next();
			User userToRemove = entry.getKey();
			if (entry.getValue().equals(socketChannel)) {
				logger.info("Connection ended for: " + userToRemove.getUsername());

				// Announce users with same service of user disconnect.
				Iterator<Entry<User, SocketChannel>> itAux =
						userSocketsMap.entrySet().iterator();
				while (itAux.hasNext()) {
					Entry<User, SocketChannel> entryAux = itAux.next();
					User user = entryAux.getKey();
					if (!userToRemove.equals(user) && user.getType() != userToRemove.getType())
						Communication.send(entryAux.getValue(),
								new NetworkUserDisconnect(userToRemove, user));
				}

				// Remove user
				it.remove();
				break;
			}
		}
	}

	public static void main(String[] args) {
		// Configure logger.
		PropertyConfigurator.configure("log4j.properties");
		try {
			PatternLayout layout = new PatternLayout(Constants.LOGGER_PATTERN);
			FileAppender appender = new FileAppender(layout, Constants.SERVER_LOGGER_FILE, true);
			Logger.getRootLogger().addAppender(appender);
		} catch (IOException e) {
			logger.error("Couln't append logger file");
			System.exit(1);
		}

		// Start server.
		ServerSocketChannel serverSocketChannel = null;
		Selector selector = null;

		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(
					new InetSocketAddress(Constants.SERVER_IP,
							Constants.SERVER_PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				selector.select();

				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator();
						it.hasNext();) {
					SelectionKey key = it.next();
					it.remove();

					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		} finally {
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) { }
			}

			if (serverSocketChannel != null) {
				try {
					serverSocketChannel.close();
				} catch (IOException e) { }
			}
		}
	}
}
