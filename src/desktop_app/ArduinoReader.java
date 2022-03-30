package desktop_app;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.rmi.ConnectException;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ArduinoReader {
	SerialPort activePort;
	SerialPort[] ports = SerialPort.getCommPorts();
	public static float temperature = 0;
	public static int readings = 0;
	public static String buff=  new String();

	public void showAllPort() {
		int i = 0;
		for (SerialPort port : ports) {
			System.out.print(i + ". " + port.getDescriptivePortName() + " ");
			System.out.println(port.getPortDescription());
			i++;
		}
	}

	public void setPort(int portIndex) throws ConnectException {
		activePort = ports[portIndex];
		if (activePort.openPort()) {

			System.out.println(activePort.getPortDescription() + " port opened.");
		} else {
			throw new ConnectException("PORT NOT OPPENED");
		}
		activePort.addDataListener(new SerialPortDataListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				//create buffer for data based on size read
				int size = event.getSerialPort().bytesAvailable();
				byte[] buffer = new byte[size];
				
				//Read data from serial
				event.getSerialPort().readBytes(buffer, size);
				
				String stringBuffer = new String();
				for (byte b : buffer) {
					char ch = (char)b;
					System.out.print(ch);
					stringBuffer = stringBuffer + ch;
				}
				ArduinoReader.buff += stringBuffer;
				//if we have a value of type xxxxxZtempZ... we will extract the temperature from string
				if(ArduinoReader.buff.lastIndexOf("Z")>1) {
					String[] res = buff.split("Z");
					if(res.length>1) {
						float val = -99999;
						try {
							val = Float.parseFloat(res[1]);
						} catch (Throwable e) {
							val = -99999;
						}
						if(val!=-99999) {
							ArduinoReader.temperature = val + 50;
						}
					}
					ArduinoReader.buff = ArduinoReader.buff.substring(ArduinoReader.buff.lastIndexOf("Z"));
				}
			}

			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}
		});
	}

	public void start() throws ConnectException {
		showAllPort();
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter Arduino port: ");
		int p = reader.nextInt();
		setPort(p);
		reader.close();
	}
}
