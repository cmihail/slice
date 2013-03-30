package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import mediator.MediatorGUI;
import model.service.Price;
import model.service.Service;
import model.state.ServiceState;
import model.user.User;
//import sun.launcher.resources.launcher;

public class MainFrameBuyer extends MainFrame implements MouseListener  {

	JMenuItem launch, drop, accept, refuse;
	public MainFrameBuyer(MediatorGUI med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		offersLabel.setText("Suppliers");
		launch = new JMenuItem("Launch Offer request");
		servicesPopupMenu.add(launch);
		drop = new JMenuItem("Drop Offer request");
		servicesPopupMenu.add(drop);
		accept = new JMenuItem("Accept offer");
		offersPopupMenu.add(accept);
		refuse = new JMenuItem("Refuse offer");
		offersPopupMenu.add(refuse);
		servicesTable = new javax.swing.JTable() {

	            /**
	             * @inherited <p>
	             */
	           @Override
	            public JPopupMenu getComponentPopupMenu() {
	                Point p = getMousePosition();
	                // mouse over table and valid row
	                if (p != null && rowAtPoint(p) >= 0) {
	                    // condition for showing popup triggered by mouse
	                	int row = rowAtPoint(p);
	                    if (isRowSelected(rowAtPoint(p))) {
	                    	launch.setEnabled(true);
	                    	drop.setEnabled(true);
	                    	if(getModel().getValueAt(row, 1).equals(ServiceState.ACTIVE)) launch.setEnabled(false);
	                    	else drop.setEnabled(false);
	                        return super.getComponentPopupMenu();
	                    } else {
	                        return null;
	                    }
	                }
	                return super.getComponentPopupMenu();
	            }

	        };
	        offersTable = new JTable(){

		            /**
		             * @inherited <p>
		             */
		           @Override
		            public JPopupMenu getComponentPopupMenu() {
		                Point p = getMousePosition();
		                // mouse over table and valid row
		                if (p != null && rowAtPoint(p) >= 0) {
		                    // condition for showing popup triggered by mouse
		                	int row = rowAtPoint(p);
		                    if (isRowSelected(rowAtPoint(p))) {

		                        return super.getComponentPopupMenu();
		                    } else {
		                        return null;
		                    }
		                }
		                return super.getComponentPopupMenu();
		            }

		        };

	    servicesTableInit();
	    offersTableInit();
	    servicesTable.addMouseListener(this);
		launch.addActionListener(this);
		drop.addActionListener(this);
		accept.addActionListener(this);
		refuse.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(launch.getText()))
		{
			/*find the selected service*/
			int index = servicesTable.getSelectedRow();
			if(index == -1 ) drawErrorPage("No Service selected");
			String sname = (String)servicesTable.getModel().getValueAt(index, 0);
			Service aux = userServicesInfo.getServiceByName(sname);
			if (aux.equals(null))
				drawErrorPage("Internal Error. Can't find requested Service");
			else mediator.launchOfferRequest(aux);
		}
		if(e.getActionCommand().equals(drop.getText()))
		{
			//TODO drop request
		}
		if(e.getActionCommand().equals(accept.getText()))
		{
			//TODO accept offer from supplier
		}
		if(e.getActionCommand().equals(refuse.getText()))
		{
			//TODO refuse offer from supplier
		}

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

				if(user.getType().equals(User.Type.MANUFACTURER)){

					i++;
					Price p =userServicesInfo.getServiceInfo(aux).getUserInfo(user).getOffer().getPrice();
					if(p!=null) {
						model.setValueAt(p, i, 1);
						model.setValueAt(user.getUsername(), i, 0);
					}
				}

			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
