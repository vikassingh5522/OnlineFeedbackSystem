package onlineFeedbackSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	  private static final String URL = "jdbc:mysql://localhost:3306/Online_feedback_system";
	  private static final String USER = "root";
	  private static final String PASSWORD = "root";
	  
	  public static Connection getConnected() throws SQLException {
		  
		  return DriverManager.getConnection(URL, USER, PASSWORD);
	  }

}

