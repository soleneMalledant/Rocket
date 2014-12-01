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
        System.out.println("TOCOMPLETE");
    }

    public static void main(String args[]) throws IOException {
        //int port = (args.length < 1) ? Global.DEFAULT_PORT : Integer.parseInt(args[0]);
        int port = Global.DEFAULT_PORT;

        if (args.length == 0) {
            usage();
        }

        // SEND INFO
        Socket socket = null;
        BufferedReader br = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ObjectOutputStream oos = null; 


        try {
            socket = new Socket(InetAddress.getLocalHost(), port, InetAddress.getLocalHost(), Global.RANDOM_PORT);
            //dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("CONNECTION ESTABLISHED...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        String chaine = "";
        String filePath = args[0];

        File fileToSend = new File(filePath);
//        dos.writeBytes("Prepare to write");
 //       dos.flush();
        if (fileToSend.isFile()) {
            if(oos == null) {
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
         //   oos.writeBytes("CREATE_FILE");
         //   oos.flush();
            // Send object file to the server.
            oos.writeObject(fileToSend);
            oos.flush();
            //sendFile(fileToSend);
            System.out.println("FILE SEND!!");


        }
        //oos.writeBytes("EOF");
        
        //oos.flush();
        //br.close();

        // Close all streams.
        oos.close();
        //dos.close();
        socket.close();
    }

    public static void sendFile(File file) {

        /*try { 
            InputStream ips = new FileInputStream(file);
            InputStreamReader ipr = new InputStreamReader(ips);
            BufferedReader breader = new BufferedReader(ipr);
            String line;
            System.out.println("File uploading...");
            while ((line = breader.readLine()) != null) {
                dos.writeBytes(line + '\n');
                dos.flush();
            }
            breader.close();
            ips.close();
            ipr.close();
            dos.writeBytes("EOF");
            dos.flush();

        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.getStackTrace();
        }
        System.out.println("File uploaded!!");*/

    }
}
