package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class WindowEventHandler extends WindowAdapter {
	public void windowClosing(WindowEvent evt) {
		JOptionPane.showMessageDialog(evt.getComponent(), "Logging out and exiting");
		((MainFrame)evt.getComponent()).logout();
		System.exit(0);
	}
}
