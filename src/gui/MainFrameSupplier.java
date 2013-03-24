package gui;

import javax.swing.JMenuItem;

import mediator.MediatorGUI;

public class MainFrameSupplier extends MainFrame{

	public MainFrameSupplier(MediatorGUI med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
    protected void initComponents() {
		super.initComponents();
		  offersLabel.setText("Buyers");
		  offersPopupMenu.add(new JMenuItem("Make offer"));
		  offersPopupMenu.add(new JMenuItem("Drop auction"));
	}

}
