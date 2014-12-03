package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.File;
import java.nio.file.Files;
import javax.net.ssl.SSLSocket;

import util.Global;


public class ClientHandler implements Runnable {
    private BufferedReader br = null;
    private DataOutputStream dos = null;
    private SSLSocket sslSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private int id = 0;//Remote port, two different clients could have the same though!

    public ClientHandler(SSLSocket sslSocket) throws IOException {
        this.sslSocket = sslSocket;
        this.id  = this.sslSocket.getPort();//This ID might not be unique!
        this.ois = new ObjectInputStream(sslSocket.getInputStream());
        //	this.sendWelcomeMessage();
        System.out.println("++ #" + String.format("%05d", this.id));
    }

    private void sendWelcomeMessage() throws IOException {
        this.dos.write(new String("Write \"" + Global.END_CONNECTION + "\" to disconnect").getBytes());
        this.dos.write('\r');//send LF(0xa): Line Feed
        this.dos.write('\n');//send CR(0xd): Carriage Return
        this.dos.flush();
    }

    @Override
    public void run() {
        try {
            String incomingMessage = null;
            String[] message = null;
            boolean quit = false;

            do {

                try {
                    incomingMessage = (String) ois.readObject();
                    message = incomingMessage.split("@@");
                } catch (Exception e) {
                    System.err.println(e);
                }




                if("RECEIVE_FILE@@".equals(incomingMessage)) {
                    try {
                        if (ois == null) {
                            ois = new ObjectInputStream(sslSocket.getInputStream());
                        }
                        File file = (File) ois.readObject();
                        String filePathToWrite = Global.SERVER_REPOSITORY + "/" + file.getName();
                        File fileToWrite = new File(filePathToWrite);
                        Files.copy(file.toPath(), fileToWrite.toPath());
                        quit = true;
                        System.out.println("File receive!");
                    } catch (ClassNotFoundException cnfe) {
                        System.err.println(cnfe.getMessage());
                        cnfe.printStackTrace();
                    } catch (Exception e) {
                        System.err.println(e);
                        e.printStackTrace();
                        System.exit(0);

                    }

                } else if ("SEND_FILE".equals(message[0])) {
                    File fileToSend = new File(Global.SERVER_REPOSITORY + "/" + message[1]);
                    if(oos == null) {
                        oos = new ObjectOutputStream(sslSocket.getOutputStream());
                    }
                    oos.writeObject(fileToSend);
                    oos.flush();
                    quit = true; 
                    System.out.println("File send to client");
                }


            } while (!quit);

            System.out.println("Bye Bye");

            // Close all streams.

            if(ois != null) {
                this.ois.close();
            }
            if (oos != null) {
                this.oos.close();
            }
            this.sslSocket.close();

            System.out.println("-- #" + String.format("%05d", this.id));
        } catch (IOException e) {
            System.err.println("Error occurred:" + e.getMessage());
        }
    }

}
