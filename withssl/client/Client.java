package client;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import util.Global;

class Client {

    public static void usage() {
        System.out.println("Usage: Client COMMAND <filename>");
        System.out.println("Commands: upload|download");
    }

    public static void main(String args[]) throws IOException {

        String option = null; 
        if (args.length <= 1) {
            usage();
            System.exit(0);
        } else if  (("upload".equals(args[0]) || ("download".equals(args[0]))))  {
            option = args[0]; 
        } else {
            usage();
            System.exit(0);
        }

        //int port = (args.length < 1) ? Global.DEFAULT_PORT : Integer.parseInt(args[0]);
        int port = Global.DEFAULT_PORT;



        // SEND INFO
        ObjectOutputStream oos = null; 
        ObjectInputStream ois = null;
        SSLSocket sslsocket = null;
        SSLSocketFactory sslsocketfactory = null;


        try {
            sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 1234);
            System.out.println("CONNECTION ESTABLISHED...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if(option.equals("upload")) {

            String filePath = args[1];
            File fileToSend = new File(filePath);
            if (fileToSend.isFile()) {
                if(oos == null) {
                    oos = new ObjectOutputStream(sslsocket.getOutputStream());
                }

                oos.writeObject("RECEIVE_FILE@@");
                oos.flush();

                // Send object file to the server.
                oos.writeObject(fileToSend);
                oos.flush();
                System.out.println("FILE SEND!!");
            }

        } else if (option.equals("download")) {
            if(oos == null) {
                oos = new ObjectOutputStream(sslsocket.getOutputStream());
            }
            System.out.println("debug: downloading file");
            oos.writeObject("SEND_FILE@@" + args[1]);
            oos.flush();
            if (ois == null)  {
                ois =  new ObjectInputStream(sslsocket.getInputStream()); 
            }
            try {
                File file = (File) ois.readObject();
                String filePathToWrite = file.getName();
                File fileToWrite = new File(filePathToWrite);
                Files.copy(file.toPath(), fileToWrite.toPath());

            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }

        // Close all streams.
        if (ois != null) {
            ois.close();
        }
        if (oos != null) {
            oos.close();
        }

        sslsocket.close();

    }     
}
