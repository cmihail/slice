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

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

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
	
	//TODO delete logger
	private final static Logger logger = Logger.getLogger(WebService.class);
	public Database db;
	//public Database db_getMap;
	//public Database db_getService;

	private void initializeDB() throws Exception{
		Class.forName(Constants.DATABASE_DRIVER).newInstance();
		db = new Database(Constants.DATABASE_URL, Constants.DATABASE_USER,
				Constants.DATABASE_PASS);
	}
	public WebService() throws Exception {
		initializeDB();
		
				try {
					PatternLayout layout = new PatternLayout(Constants.LOGGER_PATTERN);
					String file =  "webservice" +
							Constants.LOGGER_FILE_EXTENSION;
					FileAppender appender = new FileAppender(layout, file, true);
					Logger.getRootLogger().addAppender(appender);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.toString());
				}
	}

	
	public String loginUser(String userJson, String password)  {

		
		User user = json.jsonAsUser(userJson);
		logger.info("LOGIN user " +user.getUsername());
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

				try {
					db.setCommand(query);
					db.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("WebService->loginUser: "+e.getMessage()+" for userID "+ userID);
					System.err.println("WebService->loginUser: "+e.getMessage()+" for userID "+ userID);
				}
				

			}
		}
		else{
			String query = "UPDATE User SET Status=1 WHERE Name= \""+user.getUsername()+"\";";
			try {
			db.setCommand(query);
			db.execute();
			int typeID = getTypeIDByName(user.getType().toString());
			query = "UPDATE User SET Type= "+typeID+" WHERE Name= \""+user.getUsername()+"\";";
			db.setCommand(query);
			db.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("WebService->loginUser: "+e.getMessage()+" for userID "+ userID);
				System.err.println("WebService->loginUser: "+e.getMessage()+" for userID "+ userID);
			}
		}

		Map<Service, Set<User>> mapServiceUsers = getMapServiceUsers(user,userID);
		return json.mapServiceUsersAsJson(mapServiceUsers);
	}



	public void logout(String userJson, String password) throws SQLException {
		User user = json.jsonAsUser(userJson);
		logger.info("LOGOUT user " +user.getUsername());
		int userID = getUserIDByName(user.getUsername());
		deleteUserService(userID);
		deleteUser(userID);

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
				{
				logger.error("WebService->selectOne: "+e.getMessage()+" for table "+ tablename +"field_to_select " +field_to_select);
				System.err.println("WebService->selectOne: "+e.getMessage()+" for table "+ tablename +"field_to_select " +field_to_select) ;
				}
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
				{
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
				}
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
			logger.error(e.getLocalizedMessage());
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
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	private void deleteUserService(int userID){
		String query = "DELETE FROM UserToService WHERE UserID = "+userID+" ;";
		try {
			db.setCommand(query);
			db.execute();
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	private void deleteUser(int userID){
		String query = "DELETE FROM User WHERE ID = "+userID+" ;";
		try {
			db.setCommand(query);
			db.execute();
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private Map<Service, Set<User>> getMapServiceUsers(User user, int userID)   {
		Map<Service, Set<User>> mapServiceUsers = new HashMap<Service, Set<User>>();
		Set<User> users = new HashSet<User>();
		HashMap<Integer, String> usernames_ID = new HashMap<Integer, String>();
		int serviceID=0;
		
		String query = "SELECT * FROM User";
		try {
			db.setCommand(query);
			int i=0;
			ResultSet resUsers = db.executeSelect();
			while(resUsers.next())
			{
				logger.info("WebService->getMapServiceUsers: for user "+ user.getUsername()+" adding users to list "+(i++));
				String auxUname = resUsers.getString(2);
				if(auxUname.equals(user.getUsername())) continue;
				/*if(resUsers.getInt(4)==0){
					logger.info("WebService->getMapServiceUsers: for user "+ user.getUsername()+" "+auxUname+" offline");
					continue;
				} this makes comm fail*/
				int auxUserID = resUsers.getInt(1);
				usernames_ID.put(auxUserID, auxUname);
			}
			Iterator<Integer> it = usernames_ID.keySet().iterator();
			while (it.hasNext())
			{
				int auxUserID = it.next();
				String auxUname = usernames_ID.get(auxUserID);
				User.Type type = getUserType(auxUserID);
				List<Service> userServices = readServices(auxUserID, type);
				if (userServices == null)
					throw new Exception("Invalid services.");

				switch (type) {
				case BUYER:
					users.add(new Buyer(auxUname, userServices));
					break;
				case MANUFACTURER:
					users.add(new Manufacturer(auxUname, userServices));
					break;
				}
				logger.info("WebService->getMapServiceUsers: for user "+ user.getUsername()+" adding "+auxUname+" type "+type.toString());
			}
		} catch (Exception e) {
			logger.error("WebService->getMapServiceUsers: "+e.getLocalizedMessage()+" for userID "+ userID);
			System.err.println("WebService->getMapServiceUsers: "+e.getMessage()+" for userID "+ userID);
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

	private List<Service> readServices(int userID, User.Type userType) {

		List<Service> userServices = new ArrayList<Service>();
		String query = "SELECT DISTINCT ServiceID FROM UserToService WHERE UserID="+userID+" ;";
		ArrayList<Integer> serviceIDs = new ArrayList<Integer>();
		try {
			db.setCommand(query);
			int i=0;
			ResultSet resServices = db.executeSelect();
			
			while(resServices.next())
			{
				logger.info("WebService->readServices: user "+userID+" adding service "+ (i++));
				int serviceID = resServices.getInt(1);
				serviceIDs.add(serviceID);
			}
			Iterator<Integer> it =serviceIDs.iterator();
			while (it.hasNext()){
				String sname = getServiceName(it.next());
				if (userType == Type.BUYER) {
					/*price and timer are not used here*/
					userServices.add(new ServiceImpl(sname, new Price(0), new Timer(0)));
				}
				else {
					userServices.add(new ServiceImpl(sname));
				}
				logger.info("WebService->readServices: user "+userID+" adding service "+ sname);
			}
		} catch (SQLException e) {
			logger.error("WebService->readServices: "+e.getLocalizedMessage()+" for userID "+ userID);
			System.err.println("WebService->readServices: "+e.getMessage()+" for userID "+ userID);
		}
		
		
		if (userServices.isEmpty())
			return null;

		return userServices;

	}
	
}
