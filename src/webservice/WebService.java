package webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.service.Price;
import model.service.Service;
import model.service.ServiceImpl;
import model.service.Timer;
import model.user.Buyer;
import model.user.Manufacturer;
import model.user.User;
import model.user.User.Type;
import model.webservice.WebServiceJson;
import model.webservice.WebServiceJsonImpl;
import webservice.database.Database;
import constants.Constants;

/**
 * Defines the web service.
 *
 * @author cmihail, radu-tutueanu
 */
public class WebService {

	private final WebServiceJson json = new WebServiceJsonImpl();

	public Database db;

	public WebService() throws Exception {
		Class.forName(Constants.DATABASE_DRIVER).newInstance();
		db = new Database(Constants.DATABASE_URL, Constants.DATABASE_USER,
				Constants.DATABASE_PASS);
	}

	// TODO delete (only for testing)
	public String updateDB(String param) throws Exception {
		String query = "UPDATE test SET text = '" + param + "'";
		try {
			db.setCommand(query);
			db.execute();
		} finally {
			db.close();
		}
		return query;
	}

	public String loginUser(String userJson, String password) throws Exception {
		
		User user = json.jsonAsUser(userJson);
		int userID = getUserIDByName(user.getUsername());
		if(userID==-1){
			/*user not in DB, add him*/
			int type = getTypeIDByName(user.getType().toString());
			addUser(user.getUsername(), type, password);
			userID = getUserIDByName(user.getUsername());
			List<Service> services = user.getServices();
			Iterator<Service> it = services.iterator();
			while(it.hasNext()){
				/*check if the service exists*/
				Service temp = it.next();
				int serviceID = getServiceByName(temp.getName());
				/*if it doesn't, add it*/
				if(serviceID<0){
					addService(temp.getName());
					serviceID = getServiceByName(temp.getName());
				}
				String query ="INSERT INTO UserToService (UserID, ServiceID) VALUES ( "+userID+" , "+serviceID+") ;";
				
					db.setCommand(query);
					db.execute();
				
			}
		}
		
		Map<Service, Set<User>> mapServiceUsers = getMapServiceUsers(user.getUsername(),userID);
		return json.mapServiceUsersAsJson(mapServiceUsers);
	}
	
	
	
	public void logout(String userJson, String password) throws SQLException {
		User user = json.jsonAsUser(userJson);
		String query = "UPDATE User SET Status=0 WHERE Name= \""+user.getUsername()+"\";";
			db.setCommand(query);
			db.execute();
		
	}

	/**
	 * Selects one value of the field field_to_select from tablename where the field "field" has the value "value"
	 * @param tablename
	 * @param field_to_select
	 * @param field
	 * @param value
	 * @return value of field_to_select or null if the select returns no value
	 */
	private String selectOne(String tablename,String field_to_select, String field, String value)
	{
		String ret = null;
		String query = "SELECT "+field_to_select+" FROM "+tablename+" WHERE "+field + " ='"+value+"';";
		try {
			db.setCommand(query);
			ResultSet rs =db.executeSelect();
			
			rs.next();
			ret = rs.getString(1);
			System.out.println(ret);
		} catch (SQLException e) {
			if(!e.getMessage().equals("Illegal operation on empty result set."))
				e.printStackTrace();
			else return null;
		}
		
		return ret;
		
	}
	
	private String selectOneTwoConditions(String tablename,String field_to_select, String field1, String value1,String field2, String value2)
	{
		String ret = null;
		String query = "SELECT "+field_to_select+" FROM "+tablename+" WHERE "+field1 + " ='"+value1+"' AND " +field2 + " ='"+value2+"';";
		try {
			db.setCommand(query);
			ResultSet rs =db.executeSelect();
			
			rs.next();
			ret = rs.getString(1);
			System.out.println(ret);
		} catch (SQLException e) {
			if(!e.getMessage().equals("Illegal operation on empty result set."))
				e.printStackTrace();
			else return null;
		}
		
		return ret;
		
	}
	
	private int getServiceByName(String name){
		int result =-1;
		String res = selectOne("Service", "ID", "ServiceName", name);
		if (res!=null) result = Integer.parseInt(res);
		return result;
	}
	
	private String getServiceName(int ID){
		
		return selectOne("Service", "ServiceName", "ID", ID+"");
		
	}
	private String getUserName(int ID){
		
		return selectOne("User", "Name", "ID", ID+"");
		
	}
	
	private int getUserIDByName(String name){
		int result =-1;
		String res = selectOne("User", "ID", "Name", name);
		if (res!=null) result = Integer.parseInt(res);
		return result;
	}
	
	private int getTypeIDByName(String name){
		int result =-1;
		String res = selectOne("UserType", "ID", "TypeName", name);
		if (res!=null) result = Integer.parseInt(res);
		return result;
	}
	
	private void addUser(String name, int type, String password){
		String query = "INSERT INTO User (Name, Type, Status, Password) VALUES ('"+name+"','"+type+"',1,'"+password+"');";
		try {
			db.setCommand(query);
			db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private User.Type getUserType(int ID){
		User.Type result = Type.BUYER;
		String res = selectOne("UserType","TypeName","ID",selectOne("User", "Type", "ID", ID+""));
		if (res!=null &&res.equals(Type.MANUFACTURER.toString()))
			result = Type.MANUFACTURER;
			
		return result;
	}
	
	private void addService(String name){
		String query = "INSERT INTO Service ( ServiceName) VALUES (\""+name+"\");";
		try {
			db.setCommand(query);
			db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private Map<Service, Set<User>> getMapServiceUsers(String userName, int userID) throws Exception  {
		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();
		Set<User> users = new HashSet<User>();
		int serviceID=0;
		//TODO Finish implementation
		String query = "SELECT * FROM User";
		db.setCommand(query);
		ResultSet resUsers = db.executeSelect();
		while(resUsers.next())
		{
			if(resUsers.getString(2).equals(userName)) continue;
			if(resUsers.getInt(4)==0) continue;
			int auxUserID = resUsers.getInt(1);
			User.Type type = getUserType(auxUserID);
			List<Service> userServices = readServices(auxUserID, type);
		}
		
		/*String query = "SELECT DISTINCT ServiceID FROM UserToService WHERE UserID="+userID+" ;";
		db.setCommand(query);
		ResultSet resServices = db.executeSelect();
		while(resServices.next())
		{
			serviceID = resServices.getInt(1);
			query = "SELECT Name from User where ID IN (SELECT DISTINCT UserID FROM UserToService WHERE ServiceID="+serviceID+") AND Status=1;";
			db.setCommand(query);
			ResultSet resUsers = db.executeSelect();
			while(resUsers.next())
			{
				if(resUsers.getString(2).equals(userName)) continue;
				if(resUsers.getInt(4)==0) continue;
				int auxUserID = resUsers.getInt(1);
				User.Type type = getUserType(auxUserID);
			}
		}*/
		
		
		
		return mapServiceUsers;
	}
	
	private List<Service> readServices(int UserID, User.Type userType){
		List<Service> userServices = new ArrayList<Service>();
		//TODO finish implementation. All services?
		return userServices;
		
	}
	/*
	 * TODO delete (only for testing from here)
	 */
	
	
	private Map<Service, Set<User>> readConfigFiles(User user, String password)
			throws Exception {
		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();

		Set<User> users = new HashSet<User>();
		for (int i = 1; i <= 3; i++) {
			if (("user" + i).equals(user.getUsername()))
				continue;

			BufferedReader in = new BufferedReader(new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream(
							"user" + i + Constants.CONFIG_FILE_EXTENSION)));
			User.Type userType = readUserType(in);
			if (userType == null)
				throw new Exception("Invalid user type");

			List<Service> userServices = readServices(in, userType);
			if (userServices == null)
				throw new Exception("Invalid services.");

			switch (userType) {
			case BUYER:
				users.add(new Buyer("user" + i, userServices));
				break;
			case MANUFACTURER:
				users.add(new Manufacturer("user" + i, userServices));
				break;
			}
		}

		Iterator<Service> it = user.getServices().iterator();
		while (it.hasNext()) {
			Service service = it.next();
			Set<User> serviceUsers = new HashSet<User>();

			Iterator<User> usersIt = users.iterator();
			while (usersIt.hasNext()) {
				User u = usersIt.next();

				Iterator<Service> serviceIt = u.getServices().iterator();
				while (serviceIt.hasNext()) {
					Service s = serviceIt.next();

					if (service.getName().equals(s.getName())) {
						serviceUsers.add(u);
					}
				}
			}

			mapServiceUsers.put(service, serviceUsers);
		}

		return mapServiceUsers;
	}

	private User.Type readUserType(BufferedReader in) throws IOException {
		String typeStr = in.readLine();
		if (typeStr == null)
			return null;

		int typeInt = Integer.parseInt(typeStr);
		User.Type userType;
		switch (typeInt) {
		case 0:
			userType = Type.BUYER;
			break;
		case 1:
			userType = Type.MANUFACTURER;
			break;
		default:
			return null;
		}

		return userType;
	}

	
	private List<Service> readServices(BufferedReader in, User.Type userType)
			throws IOException {
		String line;
		List<Service> userServices = new ArrayList<Service>();
		while ((line = in.readLine()) != null) {
			if (userType == Type.BUYER) {
				String[] tokens = line.split(" ");
				if (tokens.length != 3) {
					return null;
				}
				userServices.add(new ServiceImpl(tokens[0], new Price(Integer
						.parseInt(tokens[1])), new Timer(Integer
						.parseInt(tokens[2]))));
			} else {
				userServices.add(new ServiceImpl(line));
			}
		}

		if (userServices.isEmpty())
			return null;
		return userServices;
	}
}
