import java.io.*;
import java.net.*;
/**
 * Created by Admin on 2/18/2017.
 */
public class JavaCode {

    public static boolean debugging = true;
    public static ServerSocket server;
    public static Socket client;
    public static DatagramSocket serverSocket;
    public static byte[] receiveData = new byte[20];
    public static BufferedReader input;
    public static PrintStream output;

    public static Runtime rt = Runtime.getRuntime();
    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void initializeSockets() {
        try {
            if(debugging)
	            System.err.println("[+] Starting Server");
            server = new ServerSocket(90);
            client = server.accept();
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintStream(client.getOutputStream());
            if(debugging)
                System.err.println("[+] Server started");
        }catch(Exception e) {
            if(debugging) {
                System.err.print("[-] Server not started");
                e.printStackTrace();
            }
        }
    }

    public static void initializeUdpSockets() throws SocketException {
        serverSocket = new DatagramSocket(800);
    }

    public static String removePadding(String data){
        return data.replace("*","");
    }

    public static void mainOld ( String[] args ) {
        while(!netIsAvailable()) {
        }
        if(debugging)
            System.err.println("[+] Connected to Internet");
        TwoWaySerialComm comm = new TwoWaySerialComm();;
        try{
            if(debugging)
                System.err.println("Trying to connect to port");
            comm.connect("/dev/ttyS80");
        }
        catch ( Exception e ) {
            if(debugging) {
                System.err.print("[-] Port ttyAMA0 not available: ");
                e.printStackTrace();
            }
        }
        initializeSockets();
        while(true) {
            String data="";
            try {
                data = input.readLine();
                if(!data.equals("")) {
                    data = removePadding(data.replace("\n", ""));
                    if (data.contains("poweroff"))
                        rt.exec("sudo poweroff");
                    double power = Double.valueOf(data.split(",")[1].replace(" ", ""));
                    //System.err.println("Data: "+data+"\n Power: "+power);
                    comm.writeData(data, power);
                }
            } catch (Exception e) {
                if(debugging) {
                    System.err.print("[-]");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main (String[] args){
        while(!netIsAvailable()) {
        }
        if(debugging)
            System.err.println("[+] Connected to Internet");
        TwoWaySerialComm comm = new TwoWaySerialComm();;
        try{
            if(debugging)
                System.err.println("Trying to connect to port");
            comm.connect("/dev/ttyS80");
        }
        catch ( Exception e ) {
            if(debugging) {
                System.err.print("[-] Port ttyAMA0 not available: ");
                e.printStackTrace();
            }
        }
        try {
            initializeUdpSockets();
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String data = new String(receivePacket.getData());
                data = removePadding(data.replace("\n", ""));
                if (!data.equals("")) {
                    if (data.contains("poweroff"))
                        rt.exec("sudo poweroff");
                    double power = Double.valueOf(data.split(",")[1].replace(" ", ""));
                    comm.writeData(data, power);
                }
            }
        }catch (Exception e){

        }

    }
}
