package gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import mediator.MediatorGUI;
import model.service.Price;
import model.service.Service;
import model.user.User;

public class MainFrameSupplier extends MainFrame  implements MouseListener{
	JMenuItem makeOffer;
	JMenuItem dropAuction;
	
	public MainFrameSupplier(MediatorGUI med) {
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
			if(index == -1 ) drawErrorPage("No Service selected");
			String sname = (String)servicesTable.getModel().getValueAt(index, 0);
			Service aux = userServicesInfo.getServiceByName(sname);
			if (aux.equals(null)) 
				{
				drawErrorPage("Internal Error. Can't find requested Service");
				return;
				}
			Iterator<User> usersIt = userServicesInfo.getServiceInfo(aux).getUsers().iterator();
			int i =0;
			TableModel model = offersTable.getModel();
			if (usersIt.hasNext()){
				User user = usersIt.next();
				if(user.getType().equals(User.Type.BUYER)){
					
					i++;
						model.setValueAt(user.getUsername(), i, 0);
					
				}
				
			}
		}
			
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(makeOffer.getText()))
		{
			//TODO make an offer
		}
		if(e.getActionCommand().equals(dropAuction.getText()))
		{
			//TODO drop auction
		}

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
