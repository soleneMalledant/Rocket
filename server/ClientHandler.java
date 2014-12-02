package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.File;
import java.nio.file.Files;

import util.Global;


public class ClientHandler implements Runnable {
    private BufferedReader br = null;
    private DataOutputStream dos = null;
    private Socket socket = null;
    private ObjectInputStream ois = null;
    private int id = 0;//Remote port, two different clients could have the same though!

    public ClientHandler(Socket socket) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.id  = this.socket.getPort();//This ID might not be unique!
        this.ois = new ObjectInputStream(socket.getInputStream());
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
        System.out.println("BEGIN OF RUN");
        try {
            System.out.println("try");
            String incomingMessage = null;
            boolean quit = false;

            do {
                //incomingMessage = br.readLine();
                
                try {
                    incomingMessage = (String) ois.readObject();
                } catch (Exception e) {
                    System.err.println(e);
                }

               // incomingMessage = (incomingMessage != null) ? incomingMessage.trim() : "";

                //System.out.println(incomingMessage);


                if("RECEIVE_FILE".equals(incomingMessage)) {
                    try {
                        if (ois == null) {
                            ois = new ObjectInputStream(socket.getInputStream());
                        }
                        File file = (File) ois.readObject();
                        String filePathToWrite = Global.SERVER_REPOSITORY + "/" + file.getName();
                        File fileToWrite = new File(filePathToWrite);
                        Files.copy(file.toPath(), fileToWrite.toPath());
                        System.exit(0);
                    } catch (ClassNotFoundException cnfe) {
                        System.err.println(cnfe.getMessage());
                        cnfe.printStackTrace();
                    } catch (Exception e) {
                        System.err.println(e);
                        e.printStackTrace();
                        System.exit(0);

                    }
                    // }

                    //	quit = ("EOF".equals(incomingMessage));//Apply the equals() to the not null string then. (or test if incoming is null)

            }


        } while (!quit);

            System.out.println("Bye Bye");

            // Close all streams.
            this.ois.close();
            this.dos.close();
            this.socket.close();

            System.out.println("-- #" + String.format("%05d", this.id));
    } catch (IOException e) {
        System.err.println("Error occurred:" + e.getMessage());
    }
}

}
