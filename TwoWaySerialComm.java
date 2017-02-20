import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class TwoWaySerialComm {
    public OutputStream outputStream;
    public Map<String, Double> channels = new HashMap<String, Double>();
    public static boolean debugging = true;
    public TwoWaySerialComm() {
        super();
        channels.put("c1",90.0);
        channels.put("c2",90.0);
        channels.put("c3",90.0);
        channels.put("c4",90.0);
        channels.put("c5",90.0);
        channels.put("c6",90.0);

    }

    void connect ( String portName ) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            if(debugging)
                System.out.println("[-] Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                outputStream=out;
                //(new Thread(new SerialReader(in))).start();
                //(new Thread(new SerialWriter(out))).start();

            }
            else {
                if(debugging)
                    System.out.println("[-] Error: Only serial ports are handled by this example.");
            }
        }
    }

    public void writeData(String data, double power) throws Exception {
        for(int channelNumber=1; channelNumber<=channels.size(); channelNumber++) {
	    if (data.contains("c"+channelNumber)) {
                if (power < channels.get("c"+channelNumber)){
                    if(debugging)
                        System.out.println("Power< Channel");
                    for (int i = channels.get("c"+channelNumber).intValue(); i > power; i += -2) {
		    	        //System.out.println(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r"));
		    	        outputStream.write(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r").getBytes());
                    	Thread.sleep(1);
                    }
		        }
		        else {
                    if(debugging)
                        System.out.println("Power> Channel");
                    for (int i = channels.get("c"+channelNumber).intValue(); i < power; i += 2) {
                        //System.out.println(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r"));
                        outputStream.write(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r").getBytes());
                        Thread.sleep(1);
                    }

		        }
                channels.put("c"+channelNumber, power);
                return;
            }
        }
    }

    public static class SerialReader implements Runnable {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run () {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ( ( len = this.in.read(buffer)) > -1 ){
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e ) {
		        System.out.print("[-]");
                e.printStackTrace();
            }
        }
    }

    public static class SerialWriter implements Runnable {
        OutputStream out;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run () {
            try {
                int c = 0;
                while ( ( c = System.in.read()) > -1 ){
                    this.out.write(c);
                }
            }
            catch ( IOException e ) {
		System.out.print("[-]");
                e.printStackTrace();
            }
        }
    }
}
