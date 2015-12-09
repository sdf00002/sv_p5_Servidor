package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Connection2 implements Runnable {

	//PDU formada por: Version+secuencia+tipo+comando+payload
	
	//Comandos utilizados en el protocolo
	public static final String CRLF="\r\n";
	public static final String OK="+OK";
	public static final String ERR="-ERR";
	public static final String QUIT="QUIT";
	//Tipos de mensajes
	public static final byte MSG_LOGIN=0x01; 
	public static final byte MSG_OPERACION=0x02;
	public static final byte MSG_FIN=0X04;

	Socket mSocket;



	public Connection2(Socket s) {
		mSocket = s;
	}

	@Override
	public void run() {
		String inputData = null;
		String outputData = "";
		String comando,payload,user="",pass="",valor="";
		byte version=0, tipo=0,VERSION=1;
		int estado = 0,secuencia;
		Authentication auth = new Authentication("");
		if (mSocket != null) {
			try {

			DataOutputStream output = new DataOutputStream(mSocket.getOutputStream());
			//BufferedReader input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			DataInputStream input = new DataInputStream(mSocket.getInputStream());
			

			while (estado==0) {
				
				try{
					version = input.readByte();
					if(version!=1){
						//outputData = ERR+" [" + version + "] VERSION NO VALIDA";
						String aux="Version no valida";
						output.write(VERSION);
						output.writeInt(1);
						output.write(MSG_FIN);
						output.writeUTF(ERR);
						output.writeUTF(aux);
						break;

					}
					} catch(NumberFormatException e){
						//Acciones asociadas a una version invalida
						//outputData = ERR+" [" + version + "] FORMATO DE VERSION NO VALIDO"+CRLF;
						String aux="Formato version no valido";
						output.write(VERSION);
						output.writeInt(1);
						output.write(MSG_FIN);
						output.writeUTF(ERR);
						output.writeUTF(aux);
						output.flush();
						break;
					}
				
					secuencia=input.readInt();
					tipo=input.readByte();
					comando=input.readUTF();
					payload=input.readUTF();



							//Comprobamos el usuario y su contraseña
							String[] credencial = payload.split("_");
							user=credencial[0];
							pass = credencial[1];
							valor=credencial[2];
							auth = new Authentication(user);
							//Comprobamos que exista el usuario y que la contraseña sea correcta
							if (auth.open(user)==true && auth.checkKey(pass) == true) {	
								//outputData =String.valueOf(VERSION) + secuencia + MSG_LOGIN + OK + "Realiza la operacion";
								if(comando.equalsIgnoreCase("sin")){
									double res=Math.sin(Double.parseDouble(valor));
									outputData =String.valueOf(VERSION) + secuencia + MSG_FIN + OK + res;
									
								}
								else if (comando.equalsIgnoreCase("cos")){
									double res=Math.cos(Double.parseDouble(valor));
									outputData =String.valueOf(VERSION) + secuencia + MSG_FIN + OK + res;
									
								}
								
								else{
									outputData =String.valueOf(VERSION) + secuencia + MSG_FIN + ERR + "comando incorrecto";
								
									
								}
								
							}
							else if (auth.open(user)==false)
								outputData =String.valueOf(VERSION) + secuencia + MSG_FIN + ERR + "usuario no existente";
							else if (auth.checkKey(pass) == false)
								outputData =String.valueOf(VERSION) + secuencia + MSG_FIN + ERR + "password incorrecta";
										
							output.writeUTF(outputData);
							output.flush();
							estado++;

			}
			//output.write(outputData.getBytes());
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
