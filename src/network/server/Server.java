package network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import network.common.Communication;
import network.model.NetworkObject;
import network.model.NetworkUser;
import constants.Constants;

public class Server {

	private static final Map<String, SocketChannel> userSocketsMap =
			new HashMap<String, SocketChannel>();

	public static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);

		System.out.println("Connection from: " +
				socketChannel.socket().getRemoteSocketAddress()); // TODO
	}

	public static void read(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			NetworkObject networkObj = Communication.recv(socketChannel);
			System.out.println(networkObj.getDestinationUser().getUsername()); // TODO

			if (networkObj instanceof NetworkUser) { // New user.
				NetworkUser networkUser = (NetworkUser) networkObj;
				userSocketsMap.put(networkUser.getDestinationUser().getUsername(), socketChannel);
			} else { // Packet that needs to be redirect.
				SocketChannel userChannel =
						userSocketsMap.get(networkObj.getDestinationUser().getUsername());
				if (userChannel == null)
					throw new Exception("Invalid receiver");
				Communication.send(userChannel, networkObj);
			}
		} catch (Communication.EndConnectionException e) {
			Iterator<Entry<String, SocketChannel>> it = userSocketsMap.entrySet().iterator();
			while (it.hasNext()) {
				if (it.next().getValue().equals(socketChannel)) {
					it.remove();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Start"); // TODO logger

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
			e.printStackTrace();
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
