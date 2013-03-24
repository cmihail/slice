package gui;

import javax.swing.JMenuItem;

import mediator.MediatorGUI;

public class MainFrameBuyer extends MainFrame {
	
	public MainFrameBuyer(MediatorGUI med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	protected void initComponents() {
		super.initComponents();
		 offersLabel.setText("Suppliers");
		  servicesPopupMenu.add(new JMenuItem("Launch Offer request"));
		  servicesPopupMenu.add(new JMenuItem("Drop Offer request"));
		  offersPopupMenu.add(new JMenuItem("Accept offer"));
		  offersPopupMenu.add(new JMenuItem("Refuse offer"));
	}

}
