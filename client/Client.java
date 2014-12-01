package client;

import java.net.*;
import java.io.*;
import java.io.ObjectOutputStream;

import util.Global;

class Client {

    private BufferedReader br = null;
    private DataOutputStream dos = null;
    private Socket socket = null;

	public static void main(String args[]) throws IOException {
		//int port = (args.length < 1) ? Global.DEFAULT_PORT : Integer.parseInt(args[0]);
        int port = 1234;
		
        if (args.length == 0) {
            usage();
        }

		// SEND INFO
		Socket socket = null;
		BufferedReader br = null;
		DataOutputStream dos = null;
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ObjectOutputStream oos = null; 


		try {
			socket = new Socket(InetAddress.getLocalHost(), port, InetAddress.getLocalHost(), Global.RANDOM_PORT);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			dos = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(dos);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		String chaine = "";
	    // READ FILE	
		String filePath = args[0];
        System.out.println(filePath);
		File fileToSend = new File(filePath);

            
        
        
        
        if (fileToSend.isFile()) {
			InputStream ips = new FileInputStream(fileToSend);
			InputStreamReader ipr = new InputStreamReader(ips);
			BufferedReader breader = new BufferedReader(ipr);
			String line;
			while ((line = breader.readLine()) != null) {
				System.out.println(line + '\n');
                dos.writeBytes(line + '\n');
			}
			breader.close();
            ips.close();
            ipr.close();
            System.out.println("EOF"); 
            dos.writeBytes("EOF");
            dos.flush();
		}


        /*

		System.out.println("Welcome message:" + br.readLine());
		String incomingMessage = "";
		String outgoingMessage = "";
		do {
			outgoingMessage = userInput.readLine();
			dos.writeBytes(outgoingMessage);
			dos.write(13);
			dos.write(10);
			dos.flush();
			incomingMessage = br.readLine();
			System.out.println("From server :" + incomingMessage);
		} while(!(Global.END_CONNECTION.equals(outgoingMessage)));

        */

		br.close();
		dos.close();
		socket.close();
	}
    public static void usage() {
        System.out.println("TOCOMPLETE");
    }
}
