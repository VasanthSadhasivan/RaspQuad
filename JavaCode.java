import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
/**
 * Created by Admin on 2/18/2017.
 */
public class JavaCode {


    public static ServerSocket server;
    public static Socket client;
    public static DataInputStream input;
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
	    System.out.println("[+] Starting Server");
            server = new ServerSocket(90);
            client = server.accept();
            input = new DataInputStream(client.getInputStream());
            output = new PrintStream(client.getOutputStream());
	    System.out.println("[+] Server started");
        }catch(Exception e) {
            System.out.print("[-] Server not started");
	    e.printStackTrace();
        }
    }

    public static String removePadding(String data){
        return data.replace("*","");
    }

    public static void main ( String[] args ) {
        while(!netIsAvailable()) {
        }
	System.out.println("[+] Connected to Internet");
        TwoWaySerialComm comm = new TwoWaySerialComm();;
        try{
            System.out.println("Trying to connect to port");
            comm.connect("/dev/ttyS80");
        }
        catch ( Exception e ) {
            System.out.print("[-] Port ttyAMA0 not available: ");
	    e.printStackTrace();
        }
        initializeSockets();
        while(true){
            try {
                String data = input.readLine();
                data = removePadding(data.replace("\n",""));
                if(data.contains("poweroff"))
                    rt.exec("sudo poweroff");
                double power = Double.valueOf(data.split(",")[1].replace(" ",""));
                //System.out.println("Data: "+data+"\n Power: "+power);
		comm.writeData(data,power);
            } catch (Exception e) {
                System.out.print("[-]");
		e.printStackTrace();
            }
        }
    }

}
