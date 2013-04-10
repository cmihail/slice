package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import mediator.MediatorGUI;
import model.service.Offer;
import model.service.Service;
import model.service.info.UserInfo;
import model.service.info.UserServicesInfo;
import model.service.info.UserServicesInfoImpl;
import model.state.OfferState;
import model.state.ServiceState;
import model.state.TransferState;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;

/**
 *
 * @author cmihail, radu-tutueanu
 */
public class MainFrame extends javax.swing.JFrame implements GUI , ActionListener{

	protected final MediatorGUI mediator;
	protected UserServicesInfo userServicesInfo;
	protected User mainUser;
	/**
	 * Creates new form Main
	 */
	public MainFrame(MediatorGUI med) {
		initComponents();
		mediator= med;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings({ "unchecked", "serial" })

	protected void servicesTableInit()
	{
		servicesTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
						{null, null, null, null},
						{null, null, null, null},
						{null, null, null, null},
						{null, null, null, null},
						{null, null, null, null}
				},
				new String [] {
						"Service Name", "Status", "No offer/OfferMade", "Accepted/Refused"
				}
				) {
			Class[] types = new Class [] {
					java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
			};
			boolean[] canEdit = new boolean [] {
					false, false, false, false
			};

			@Override
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		jScrollPane1.setViewportView(servicesTable);
		servicesTable.add(servicesPopupMenu);
		servicesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					servicesPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
			@Override
			public void mouseReleased(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					servicesPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
		}
				);
	}

	protected void offersTableInit(){
		offersTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
						{null, null},
						{null, null},
						{null, null},
						{null, null}
				},
				new String [] {
						"Name", "Offer"
				}
				) {
			Class[] types = new Class [] {
					java.lang.String.class, java.lang.Integer.class
			};
			boolean[] canEdit = new boolean [] {
					false, false
			};

			@Override
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		jScrollPane2.setViewportView(offersTable);


		offersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					offersPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
			@Override
			public void mouseReleased(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					offersPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
		}
				);

	}
	protected void initComponents() {

		servicesPopupMenu = new javax.swing.JPopupMenu();
		offersPopupMenu = new javax.swing.JPopupMenu();
		usernameLabel = new javax.swing.JLabel();
		logoutButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
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
					if (isRowSelected(rowAtPoint(p))) {

						return super.getComponentPopupMenu();
					} else {
						return null;
					}
				}
				return super.getComponentPopupMenu();
			}

		};

		servicesLabel = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		offersTable = new javax.swing.JTable() {

			/**
			 * @inherited <p>
			 */
			@Override
			public JPopupMenu getComponentPopupMenu() {
				Point p = getMousePosition();
				// mouse over table and valid row
				if (p != null && rowAtPoint(p) >= 0) {
					// condition for showing popup triggered by mouse
					if (isRowSelected(rowAtPoint(p))) {
						return super.getComponentPopupMenu();
					} else {
						return null;
					}
				}
				return super.getComponentPopupMenu();
			}

		};
		offersLabel = new javax.swing.JLabel();
		myProgressBar = new javax.swing.JProgressBar();
		progressLabel = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		usernameLabel.setText("Username ");

		logoutButton.setText("Logout");
		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logoutButtonActionPerformed(evt);
			}
		});

		servicesTableInit();
		servicesLabel.setText("Services");

		offersTableInit();
		progressLabel.setText("Transaction progress");



		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(logoutButton))
										.addGroup(layout.createSequentialGroup()
												.addGap(203, 203, 203)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(progressLabel)
														.addComponent(myProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)))
														.addGroup(layout.createSequentialGroup()
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addGroup(layout.createSequentialGroup()
																				.addGap(52, 52, 52)
																				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addGroup(layout.createSequentialGroup()
																						.addGap(245, 245, 245)
																						.addComponent(servicesLabel)))
																						.addGap(47, 47, 47)
																						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																								.addComponent(offersLabel)
																								.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
																								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGap(20, 20, 20)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(logoutButton))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(servicesLabel)
										.addComponent(offersLabel))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(24, 24, 24)
												.addComponent(progressLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(myProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(19, 19, 19))
				);

		pack();
	}// </editor-fold>

	private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
		JOptionPane.showMessageDialog(this, "Logging out and exiting");
		mediator.logout();
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * @param args the command line arguments
	 */
	// Variables declaration - do not modify
	protected javax.swing.JPopupMenu servicesPopupMenu;
	protected javax.swing.JPopupMenu offersPopupMenu;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JButton logoutButton;
	private javax.swing.JProgressBar myProgressBar;
	private javax.swing.JLabel progressLabel;
	private javax.swing.JLabel servicesLabel;
	protected javax.swing.JTable servicesTable;
	protected javax.swing.JTable offersTable;
	protected javax.swing.JLabel offersLabel;
	private javax.swing.JLabel usernameLabel;
	// End of variables declaration
	@Override
	public void generateEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawErrorPage(String errorMessage) {
		JOptionPane.showMessageDialog(this,"ERORR : "+ errorMessage);

	}


	protected void updateServicesTable()
	{

		int i=0;
		for (Service s : mainUser.getServices()) {
			servicesTable.getModel().setValueAt(s.getName(),i, 0);
			ServiceState currentServState = userServicesInfo.getServiceInfo(s).getServiceState();
			servicesTable.getModel().setValueAt(currentServState,i,1);
			if(currentServState.equals(ServiceState.ACTIVE)){
				OfferState curentOfferState=userServicesInfo.getServiceInfo(s).getOfferState();
				if(curentOfferState.equals(OfferState.NONE))
					{
					servicesTable.getModel().setValueAt("No Offer",i,2);
					servicesTable.getModel().setValueAt("",i,3);
					}
				else
				{
					servicesTable.getModel().setValueAt("Offer Made",i,2);
					servicesTable.getModel().setValueAt(curentOfferState,i,3);
				}
			}
			else
			{
				servicesTable.getModel().setValueAt("",i,3);
				servicesTable.getModel().setValueAt("",i,2);
			}
			i++;
		}
	}
	@Override
	public void drawMainPage(User mainUser,
			Map<Service, Set<User>> mapServiceUsers) {
		userServicesInfo = new UserServicesInfoImpl(mapServiceUsers);
		this.mainUser= mainUser;
		mediator.setUserServicesInfo(userServicesInfo);
		usernameLabel.setText(mainUser.getUsername()+" - "+mainUser.getType());
		updateServicesTable();
		this.setVisible(true);
	}

	@Override
	public void addUserForService(User newUser, Service service) {
		userServicesInfo.getServiceInfo(service).addUser(newUser);
	}

	@Override
	public void removeUserForService(User oldUser, Service service) {
		userServicesInfo.getServiceInfo(service).removeUser(oldUser);

	}

	@Override
	public void setTransferState(User user, Service service,
			TransferState transferState) {
		userServicesInfo.getServiceInfo(service).getUserInfo(user).setTransferState(transferState);

	}

	@Override
	public void setManufacturerServiceOffer(Manufacturer manufacturer,
			Service service, Offer offer) {
		userServicesInfo.getServiceInfo(service).getUserInfo(manufacturer).setOffer(offer);
		userServicesInfo.getServiceInfo(service).setOfferState(OfferState.OFFER_MADE);
		updateServicesTable();
	}

	@Override
	public void activateBuyerForService(Buyer buyer, Service service) {
		if (userServicesInfo.getServiceInfo(service) == null)
			System.out.println("ERR");
		userServicesInfo.getServiceInfo(service).getUserInfo(buyer).setOfferState(OfferState.OFFER_REQUESTED);
		userServicesInfo.getServiceInfo(service).setServiceState(ServiceState.ACTIVE);
		updateServicesTable();
	}

	@Override
	public void deactivateBuyerForService(Buyer buyer, Service service) {
		userServicesInfo.getServiceInfo(service).getUserInfo(buyer).setOfferState(OfferState.NONE);
		updateServicesTable();
	}

	@Override
	public void changeBuyerOfferState(Buyer buyer, Service service,
			OfferState offerState) {
		userServicesInfo.getServiceInfo(service).getUserInfo(buyer).setOfferState(offerState);
		userServicesInfo.getServiceInfo(service).setOfferState(offerState);
		updateServicesTable();
	}

	@Override
	public void compareServiceOffer(Buyer buyer, Service service, Offer offer) {
		UserInfo userInfo = userServicesInfo.getServiceInfo(service).getUserInfo(buyer);
		if (offer.getPrice().compareTo(userInfo.getOffer().getPrice()) >= 0) {
			userInfo.setOfferState(OfferState.OFFER_EXCEEDED);
			updateServicesTable();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	protected Service getSelectedService()
	{
		int index = servicesTable.getSelectedRow();
		if(index == -1 ) drawErrorPage("No Service selected");
		String sname = (String)servicesTable.getModel().getValueAt(index, 0);
		Service aux = userServicesInfo.getServiceByName(sname);

		return aux;
	}

	protected User getSelectedOfferUser(Service service)
	{
		int index = offersTable.getSelectedRow();
		if(index == -1 ){
			drawErrorPage("No Service selected");
			return null;
		}
		String uname = (String)offersTable.getModel().getValueAt(index, 0);
		User aux = userServicesInfo.getServiceInfo(service).getUserByName(uname);
		return aux;
	}
}
