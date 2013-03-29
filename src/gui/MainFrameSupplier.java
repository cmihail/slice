package gui;

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
		  servicesTable.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
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
