package desktop_app;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.rmi.ConnectException;

public class main {

	public static void main(String[] args) throws AWTException, InterruptedException, ConnectException {
		
		if (SystemTray.isSupported()) {
			//START ARDUINO
			ArduinoReader reader = new ArduinoReader();
			reader.start();
			
			JFrame frame = new JFrame("Temp app");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 300);

			JPanel panel = new JPanel();
			JPanel panelChild = new JPanel();
			JPanel panelStatus = new JPanel();
			JLabel label = new JLabel("Temperatura: ");
			JLabel status =new JLabel();
			JLabel temp =new JLabel();
			
			temp.setFont(new Font("Serif", Font.PLAIN, 25));
			label.setFont(new Font("Serif", Font.PLAIN, 20));
			status.setFont(new Font("Serif", Font.PLAIN, 17));
			
			panel.add(label);
			panelStatus.add(status);
			panelChild.add(temp);
			
			panel.setLayout(new GridBagLayout());
			panel.add(panelChild, new GridBagConstraints());

			frame.getContentPane().add(BorderLayout.CENTER, panel);
			frame.getContentPane().add(BorderLayout.SOUTH, panelStatus);
			frame.setVisible(true);

			float temperature=ArduinoReader.temperature;
			temp.setText(String.valueOf(temperature)+" \u2103");

			while(temperature != 9999) {
				if(temperature == ArduinoReader.temperature && temperature != 0) {
					continue;
				}
				temperature = ArduinoReader.temperature;
				temp.setText(String.valueOf(temperature)+" \u2103");

				if(temperature > 30) {
					System.out.println("Too hot outside!");
					status.setText("Too hot outside!");
					status.setForeground (Color.red);
					displayTray("Too hot outside!");
				}else if(temperature < -5) {
					System.out.println("Too cold outside!");
					status.setText("Too cold outside!");
					status.setForeground (Color.blue);
					displayTray("Too cold outside!");
				} else {
					status.setText("");
				}
			}
		} else {
			System.err.println("System tray not supported!");
		}

	}

	public static void displayTray(String message) throws AWTException {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("System tray icon demo");
		tray.add(trayIcon);

		trayIcon.displayMessage("Warning", message, MessageType.WARNING);
	}

}
