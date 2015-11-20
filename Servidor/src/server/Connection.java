package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;


public class Connection implements Runnable {

	Socket mSocket;
	public static String MSG_WELCOME = "OK Bienvenido al servidor de pruebas\r\n";

	public Connection(Socket s) {
		mSocket = s;
	}

	@Override
	public void run() {
		String inputData = null;
		String outputData = "";
		int estado = 0;
		if (mSocket != null) {
			try {

				DataOutputStream output = new DataOutputStream(mSocket.getOutputStream());
				BufferedReader input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

				output.write(MSG_WELCOME.getBytes());

				while ((inputData = input.readLine()) != null) {
					System.out.println("Servidor [Recibido]> " + inputData);
					String campos[] = inputData.split(" ");

					if (campos.length == 2) {
						String comando = campos[0];
						String parametro = campos[1];

						switch (estado) {

						case 0:
							if (comando.equalsIgnoreCase("USER")) {
								outputData = "OK ";
								// Si el formato es correcto
								estado++;
							}
							break;
						case 1:
							break;

						case 2:

							if (comando.length() == 4) {
								if (comando.equalsIgnoreCase("POWR")) {

									try {
										String power = String
												.valueOf(Integer.parseInt(parametro) * Integer.parseInt(parametro));
										outputData = "OK " + parametro + " EL CUADRADO = " + power + "\r\n";

									} catch (NumberFormatException ex) {
										outputData = "ERROR FORMATO DE NUMERO INCORRECTO\r\n";
									}
								} else

									outputData = "OK [" + comando + "] " + " PARAMETRO= " + parametro + "\r\n";

							} else
								outputData = "ERROR COMANDO DESCONOCIDO\r\n";
							break;
						}
					} else
						outputData = "ERROR [" + inputData + "] NO ES UN COMANDO VALIDO\r\n";

					output.write(outputData.getBytes());

				}
				System.out.println(
						"Servidor [Finalizado]> " + mSocket.getInetAddress().toString() + ":" + mSocket.getPort());

				input.close();
				output.close();
				mSocket.close();
			} catch (SocketException se) {

				se.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
