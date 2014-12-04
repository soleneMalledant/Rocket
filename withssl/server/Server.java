package server;

import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import util.Global;

class Server {

	/**
	 * @param args [port] [max accepted clients]
	 */
	public static void main(String args[]) {
		// CREATE SERVER FILE REPOSITORY
		File repository = new File(Global.SERVER_REPOSITORY);
		repository.mkdirs();
		
		int port = (args.length < 1) ? Global.DEFAULT_PORT : Integer.parseInt(args[0]);
		int maxAcceptedClients = (args.length < 2) ? Global.MAX_ACCEPTED_CLIENTS : Integer.parseInt(args[1]);
		maxAcceptedClients = (maxAcceptedClients < 100) ? maxAcceptedClients : 99;

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = null;
		//Listening on a port:
		try {
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} 

		System.out.println("################################");
        System.out.println("###  ROCKET Server with SSL  ###");
		System.out.println("#      Listening on " + String.format("%05d", port) + "      #");
		System.out.println("# Waiting for clients (" + String.format("%02d", maxAcceptedClients) + " max) #");
		System.out.println("################################");

		int acceptedClients = 0;
		try {
			do {
				new Thread(new ClientHandler((SSLSocket) sslServerSocket.accept())).start();
				acceptedClients++;
			} while(acceptedClients < maxAcceptedClients);
		} catch (IOException ioe) {
			System.err.println("Server failed to accept a client");
		}
	}
}
