package network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import network.model.NetworkObject;

public class Communication {
	
	public static class EndConnectionException extends Exception {
		
	}

	public static void send(SocketChannel socketChannel, NetworkObject obj) {
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
			
			while (byteBuffer.hasRemaining()) {
				socketChannel.write(byteBuffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
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
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
}
