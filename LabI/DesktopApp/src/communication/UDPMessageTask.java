package communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;

public class UDPMessageTask implements Runnable {
    private String message;
    private String ip;
    private int port;

    public UDPMessageTask(String ip, int port, String message) {
        this.message = message;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
        	clientSocket.setSoTimeout(1000);
            try (ByteArrayOutputStream bStream = new ByteArrayOutputStream(); ObjectOutput oo = new ObjectOutputStream(bStream)) {
                Message messageClass = new Message(message);
                oo.writeObject(messageClass);
                byte[] serializedMessage = bStream.toByteArray();
                InetAddress IPAddress = InetAddress.getByName(ip);
                DatagramPacket sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length, IPAddress, port);
                clientSocket.send(sendPacket);
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
