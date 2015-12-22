package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
	
	public static void main(String argv[]) throws Exception {
		String user, pass, cmd = "", num;
		String modifiedSentence = "";
		int estado=0;
		byte v=0;
		final byte MSG_LOGIN=0x01; 
		final byte MSG_OPERACION=0x02;
		final byte MSG_FIN=0X04;
		final byte version=1;
		
		try {
			Socket clientSocket = new Socket("localhost", 7000);
			
			byte tipo = 1;
			int secuencia = 1;
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromUser = new BufferedReader(
                    new InputStreamReader(System.in));
			DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
		
			
			while (estado==0) {
				
				System.out.println("Introduce usuario");
				user = inFromUser.readLine();
				System.out.println("Introduce contraseña");
				pass = inFromUser.readLine();
				System.out.println("Introduce un valor");
				num = inFromUser.readLine();

				

				outToServer.write(version);

				outToServer.writeInt(secuencia);

				outToServer.write(MSG_OPERACION);
				outToServer.writeUTF("cos");
				outToServer.writeUTF(user + "_" + pass+"_"+num);
				outToServer.flush();
				
				modifiedSentence=dis.readUTF();																			
				
				
				System.out.println(modifiedSentence);
				estado++;
			}


			System.out.println("Has salido");
			dis.close();
			outToServer.close();
			clientSocket.close();
		} catch (IOException e) {

		}
	}

}
