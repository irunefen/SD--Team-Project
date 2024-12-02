package es.deusto.sd.strava.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FacebookAuthServiceGateway implements IAuthServiceGateway {
	
	private static final String DELIMITER = "#";
	
	private static String serverIP;
	
	private static int serverPort;
	
	public FacebookAuthServiceGateway() {}
	
	
	@Value("${auth.facebook.ip}")
	public void setServerIP(String serverIP) {
		FacebookAuthServiceGateway.serverIP = serverIP;
	}
	
	@Value("${auth.facebook.port}")
	public void setServerPort(int serverPort) {
		FacebookAuthServiceGateway.serverPort = serverPort;
	}
	
	private Boolean sendMessage(String msg) {
		try (Socket socket = new Socket(serverIP, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			
			out.writeUTF(msg);
			
			return in.readBoolean();

		} catch (UnknownHostException e) {
			System.err.println("# Trans. SocketClient: Socket error: " + e.getMessage());	
			return null;
		} catch (EOFException e) {
			System.err.println("# Trans. SocketClient: EOF error: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.err.println("# Trans. SocketClient: IO error: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Optional<Boolean> isEmailRegistered(String email) {
		return Optional.ofNullable(sendMessage("exists" + DELIMITER + email));
	}

	@Override
	public Optional<Boolean> authenticateUser(String email, String password) {
		return Optional.ofNullable(sendMessage("login" + DELIMITER + email + DELIMITER + password));
	}

}
