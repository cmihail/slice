package network.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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

import model.user.User;

import constants.Constants;

public class Server {
	
	private static final Map<User, SocketChannel> userSocketsMap =
			new HashMap<User, SocketChannel>();
	
	public static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(0); // Not used.
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);

		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress()); // TODO
	}
	
	public static void read(SelectionKey key) throws IOException, ClassNotFoundException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
		try {
			NetworkObject obj = Communication.recv(socketChannel);
			if (obj instanceof NetworkUser) {
				NetworkUser networkUser = (NetworkUser) obj;
				userSocketsMap.put(networkUser.getUser(), socketChannel);
				System.out.println(networkUser.getUser().getUsername()); // TODO del
			}
		} catch (Communication.EndConnectionException e) {
			Iterator<Entry<User, SocketChannel>> it = userSocketsMap.entrySet().iterator();
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

	public static void write(SelectionKey key) throws IOException {
		// TODO
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
					new InetSocketAddress(Constants.SERVER_IP, Constants.SERVER_PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				selector.select();
				
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator();
						it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();
					
					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
					else if (key.isWritable())
						write(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {}
			}
			
			if (serverSocketChannel != null) {
				try {
					serverSocketChannel.close();
				} catch (IOException e) {}
			}
		}
	}
}
