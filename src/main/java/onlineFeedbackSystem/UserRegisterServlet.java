package onlineFeedbackSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String id = request.getParameter("id"); 
	    String name = request.getParameter("name"); 
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    if (saveUser(id, name, email, password)) {
	    	   response.getWriter().println("Successfullyy  register ..!!!.");
	    	  
	    } else {
	        response.getWriter().println("Failed to register user.");
	    }
	}


    private boolean saveUser(String id, String name, String email, String password) {
        boolean status = false;
        String query = "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, password);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                status = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
