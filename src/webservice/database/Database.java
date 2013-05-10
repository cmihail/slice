package webservice.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* Defines database class.
*
* @author cmihail, radu-tutueanu
*/
public class Database {

private final Connection conn;
private PreparedStatement stmt = null;

public Database(String url, String user, String pass) throws SQLException,
ClassNotFoundException {
conn = DriverManager.getConnection(url, user, pass);
conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
}

public void setCommand(String command) throws SQLException {
if (stmt != null)
stmt.close();

stmt = conn.prepareStatement(command);
}

public ResultSet executeSelect() throws SQLException {
return stmt.executeQuery();
}

public boolean execute() throws SQLException {
return stmt.execute();
}

public void close() throws SQLException {
if (stmt != null)
stmt.close();
conn.close();
}
}