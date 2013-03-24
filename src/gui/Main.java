package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import mediator.MediatorGUI;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserServicesInfo;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 *
 * @author cmihail, radu-tutueanu
 */
public class Main extends GUIImpl implements PropertyChangeListener{

	static LoginFrame loginFrame;
	static MainFrame mainFrame;
	
	public Main(MediatorGUI mediator) {
		super(mediator);
		loginFrame = new LoginFrame();
		loginFrame.addPropertyChangeListener(this);
		mainFrame = new MainFrameBuyer(new MediatorGUI() {
			
			@Override
			public void transfer(Service service, User toUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setUserServicesInfo(UserServicesInfo userServicesInfo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void refuseOffer(Service service, Manufacturer manufacturer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void makeOffer(Service service, Buyer buyer, Offer offer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void logout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void launchOfferRequest(Service service) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dropOfferRequest(Service service) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dropAuction(Service service, Buyer buyer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void acceptOffer(Service service, Manufacturer manufacturer) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		Main me = new Main(null);
		loginFrame.setVisible(true);
		/*
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				LoginFrame a=new LoginFrame().setVisible(true);
			}
		});*/
	}

	


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("status")&& evt.getNewValue().equals("LoggedIn"))
		{
			  JOptionPane.showMessageDialog(loginFrame, "Succesfully logged in");
			  loginFrame.setVisible(false);
			  mainFrame.setVisible(true);
		}
		
	}
}