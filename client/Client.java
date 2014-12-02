package client;

import java.net.*;
import java.io.*;
import java.io.ObjectOutputStream;

import util.Global;

class Client {

    private BufferedReader br = null;
    private static DataOutputStream dos = null;
    private Socket socket = null;

    public static void usage() {
        System.out.println("Please add a file to upload.");
    }

    public static void main(String args[]) throws IOException {
        //int port = (args.length < 1) ? Global.DEFAULT_PORT : Integer.parseInt(args[0]);
        int port = Global.DEFAULT_PORT;

        if (args.length == 0) {
            usage();
            System.exit(1);
        }

        // SEND INFO
        Socket socket = null;
        BufferedReader br = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ObjectOutputStream oos = null; 
        DataInputStream dis = null;

        try {
            socket = new Socket(InetAddress.getLocalHost(), port, InetAddress.getLocalHost(), Global.RANDOM_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("CONNECTION ESTABLISHED...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        String filePath = args[0];

        File fileToSend = new File(filePath);
        if (fileToSend.isFile()) {
            if(oos == null) {
                oos = new ObjectOutputStream(socket.getOutputStream());
            }

            oos.writeObject("RECEIVE_FILE");
            oos.flush();
            
            // Send object file to the server.
            oos.writeObject(fileToSend);
            oos.flush();
            System.out.println("FILE SEND!!");
        }
        
        // Close all streams.
        oos.close();
        socket.close();
    }

}
