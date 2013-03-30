package gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import mediator.MediatorGUI;
import model.service.Offer;
import model.service.OfferImpl;
import model.service.Price;
import model.service.Service;
import model.state.OfferState;
import model.state.ServiceState;
import model.user.Buyer;
import model.user.User;

public class MainFrameManufacturer extends MainFrame  implements MouseListener{
	JMenuItem makeOffer;
	JMenuItem dropAuction;
	
	public MainFrameManufacturer(MediatorGUI med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
    protected void initComponents() {
		super.initComponents();
		  offersLabel.setText("Buyers");
		  makeOffer=new JMenuItem("Make offer");
		  offersPopupMenu.add(makeOffer);
		  dropAuction=new JMenuItem("Drop auction");
		  offersPopupMenu.add(dropAuction);
		  servicesTable.addMouseListener(this);
		  
		  makeOffer.addActionListener(this);
		  dropAuction.addActionListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		/*show users for selected service*/
		if(SwingUtilities.isLeftMouseButton(e) && e.getSource() == servicesTable)
		{
			int index = servicesTable.getSelectedRow();
			if(index == -1 ) 
				{
				drawErrorPage("No Service selected");
				return;
				}
			String sname = (String)servicesTable.getModel().getValueAt(index, 0);
			Service aux = userServicesInfo.getServiceByName(sname);

			if (aux ==null)
			{
				
				return;
			}
			offersTableInit();
			if(userServicesInfo.getServiceInfo(aux).getServiceState().equals(ServiceState.ACTIVE))
			{
				Iterator<User> usersIt = userServicesInfo.getServiceInfo(aux).getUsers().iterator();
				int i =0;
				TableModel model = offersTable.getModel();
				while (usersIt.hasNext()){
					User user = usersIt.next();

					if(user.getType().equals(User.Type.BUYER)){

						if(userServicesInfo.getServiceInfo(aux).getUserInfo(user).getOfferState().equals(OfferState.NONE)) 
							continue;
						Offer o = userServicesInfo.getServiceInfo(aux).getUserInfo(user).getOffer();
						model.setValueAt(user.getUsername(), i, 0);
						if(o!=null)
						{
							Price p =o.getPrice();
							
							if(p!=null) {
								model.setValueAt(p, i, 1);
								
							}

						}
						i++;
					}
				}
			}
		}
			
	}

	public void actionPerformed(ActionEvent e) {
		
		Service selectedService = getSelectedService();
		if (selectedService == null)
			{
			drawErrorPage("Internal Error. Can't find requested Service");
			return;
			}
		User aUser = getSelectedOfferUser(selectedService);
		if(aUser == null)
		{
			drawErrorPage("Internal Error. Can't find requested user");
			return;
			}
		if(e.getActionCommand().equals(makeOffer.getText()))
		{
			String str = JOptionPane.showInputDialog(null, "Enter price quoted : ", 
					"Make an offer", 1);
			int i;
			if(str==null )
				{
				drawErrorPage("Illegal price. Price should be an integer");
				return;
				}
			try{
				i=Integer.parseInt(str);
				Offer o =  new OfferImpl(new Price(i));
				mediator.makeOffer(selectedService, (Buyer) aUser,o );
				userServicesInfo.getServiceInfo(selectedService).getUserInfo(aUser).setOffer(o);
				userServicesInfo.getServiceInfo(selectedService).setOfferState(OfferState.OFFER_MADE);
			}
			catch (NumberFormatException ex)
			{
				drawErrorPage("Illegal price. Price should be an integer");
			}
			
		}
		
		if(e.getActionCommand().equals(dropAuction.getText()))
		{
			mediator.dropAuction(getSelectedService(), (Buyer) aUser);
			userServicesInfo.getServiceInfo(selectedService).setServiceState(ServiceState.INACTIVE);
			
		}
		updateServicesTable();

	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
