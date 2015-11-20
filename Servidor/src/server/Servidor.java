package server;

import java.io.IOException;
import java.net.*;

public class Servidor {
	
	public static final int TCP_SERVICE_PORT = 7000;

	static ServerSocket server = null;

	public static void main(String[] args) {
			
		System.out.println("Servidor> Iniciando servidor");
		try {
			server = new ServerSocket(TCP_SERVICE_PORT);
			while (true) {
				final Socket newsocket = server.accept();
				System.out.println("Servidor> Conexi�n entrante desde "
						+ newsocket.getInetAddress().toString() + ":"
						+ newsocket.getPort());
				new Thread(new Connection(newsocket)).start();
			}
		} catch (IOException e) {
			System.err.println("Server "+e.getMessage());
			e.printStackTrace();
		}

	}

}
