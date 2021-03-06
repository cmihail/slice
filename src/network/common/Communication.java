package network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import network.model.NetworkObject;

import org.apache.log4j.Logger;

/**
 * Defines methods used to transfer data between client and server.
 * Methods are common for both of them.
 *
 * @author cmihail, radu-tutueanu
 */
public class Communication {

	private static final Logger logger = Logger.getLogger(Communication.class);

	/**
	 * Defines end connection exception used to notify the end of communication
	 * between client / server.
	 *
	 * @author cmihail, radu-tutueanu
	 */
	public static class EndConnectionException extends Exception {
		private static final long serialVersionUID = 3407535627371086058L;
	}

	/**
	 * Sends a {@link NetworkObject} through socketChannel.
	 * @param socketChannel the channel on which the object is sent
	 * @param obj the network object to send
	 * @return number of sent chars
	 */
	public static int send(SocketChannel socketChannel, NetworkObject obj) {
		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();

			int size = baos.size();
			final ByteBuffer byteBuffer = ByteBuffer.allocate(size + 4); // 4 needed for size.
			byteBuffer.putInt(size);
			byteBuffer.put(baos.toByteArray());
			byteBuffer.flip();

			int sentSize = 0;
			while (byteBuffer.hasRemaining()) {
				sentSize += socketChannel.write(byteBuffer);
				if (sentSize <= 0)
					break;
			}
			return sentSize;
		} catch (IOException e) {
			logger.warn(e.getMessage());
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}
		}
		return 0;
	}

	/**
	 * Receives a {@link NetworkObject} from socketChannel
	 * @param socketChannel the socket channel on which the object is received
	 * @return a {@link NetworkObject} object
	 * @throws Exception EndConnectionException, IOException
	 */
	public static NetworkObject recv(SocketChannel socketChannel) throws Exception {
		NetworkObject result = null;
		ObjectInputStream ois = null;
		try {
			final ByteBuffer sizeByteBuffer = ByteBuffer.allocate(4); // Needed for size.
			while (sizeByteBuffer.hasRemaining()) {
				int bytes = socketChannel.read(sizeByteBuffer);
				if (bytes == -1)
					throw new EndConnectionException();
				if (bytes == 0)
					throw new IOException("No data");
			}
			sizeByteBuffer.flip();
			int size = sizeByteBuffer.getInt();

			ByteBuffer data = ByteBuffer.allocate(size);
			while (data.hasRemaining()) {
				int bytes = socketChannel.read(data);
				if (bytes == -1)
					throw new EndConnectionException();
				if (bytes == 0)
					throw new IOException("No data");
			}
			data.flip();

			ois = new ObjectInputStream(new ByteArrayInputStream(data.array()));
			Object obj = ois.readObject();
			if (!(obj instanceof NetworkObject))
				throw new IOException("Invalid object received");
			result = (NetworkObject) obj;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}
		}

		return result;
	}
}
