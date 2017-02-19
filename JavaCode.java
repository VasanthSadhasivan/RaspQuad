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
            server = new ServerSocket(90);
            client = server.accept();
            input = new DataInputStream(client.getInputStream());
            output = new PrintStream(client.getOutputStream());
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public static String removePadding(String data){
        return data.replace("*","");
    }

    public static void main ( String[] args ) {
        while(!netIsAvailable()) {
        }
        TwoWaySerialComm comm = new TwoWaySerialComm();;
        try{
            comm.connect("/dev/ttyAMA0");
        }
        catch ( Exception e ) {
            System.out.println(e);
        }
        initializeSockets();
        while(true){
            try {
                String data = input.readLine();
                data = removePadding(data.replace("\n",""));
                if(data.contains("poweroff"))
                    rt.exec("sudo poweroff");
                double power = Double.valueOf(data.split(",")[1].replace(" ",""));
                comm.writeData(data,power);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
