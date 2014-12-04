package client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;

import util.Global;

class Client {

    private Socket socket = null;

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
        Socket socket = null;
        ObjectOutputStream oos = null; 
        ObjectInputStream ois = null;


        try {
            socket = new Socket(InetAddress.getLocalHost(), port, InetAddress.getLocalHost(), Global.RANDOM_PORT);
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
                    oos = new ObjectOutputStream(socket.getOutputStream());
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
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
            System.out.println("debug: downloading file");
            oos.writeObject("SEND_FILE@@" + args[1]);
            oos.flush();
            if (ois == null)  {
                ois =  new ObjectInputStream(socket.getInputStream()); 
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

        socket.close();

    }     


}




