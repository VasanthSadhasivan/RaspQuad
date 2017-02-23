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

                OutputStream out = serialPort.getOutputStream();
                outputStream=out;
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
	            if(Math.abs(channels.get("c"+channelNumber)-power)<3){
                    outputStream.write(("#"+channelNumber+" P" + (1960 * power / 180 + 520) + " T1\n\r").getBytes());
                    channels.put("c"+channelNumber, power);
                    return;
                }
                if (power < channels.get("c"+channelNumber)){
                    if(debugging)
                        System.out.println("Power< Channel");
                    for (int i = channels.get("c"+channelNumber).intValue(); i > power; i += -3) {
		    	        //System.out.println(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r"));
		    	        outputStream.write(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r").getBytes());
                    }
		        }
		        else {
                    if(debugging)
                        System.out.println("Power> Channel");
                    for (int i = channels.get("c"+channelNumber).intValue(); i < power; i += 3) {
                        //System.out.println(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r"));
                        outputStream.write(("#"+channelNumber+" P" + (1960 * i / 180 + 520) + " T1\n\r").getBytes());
                    }
		        }
                channels.put("c"+channelNumber, power);
                return;
            }
        }
    }
}
