package onlineFeedbackSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserLoginServlet")
public class UserLoginServlet<HttpServletRequest> extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = ((ServletRequest) request).getParameter("username");
        String password = ((ServletRequest) request).getParameter("password");

        Integer userId = getUserId(username, password);
        if (userId != null) {
            // ✅ Create a session and store user_id
            HttpSession session = ((javax.servlet.http.HttpServletRequest) request).getSession();
            session.setAttribute("user_id", userId);
            session.setAttribute("username", username);
            
            System.out.println("Login successful. User ID: " + userId);
            
            // ✅ Redirect to the feedback form
            response.sendRedirect("FeedbackFormServlets"); 
        } else {
            // ❌ If login fails, show an error message
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Invalid username or password. Try again!</h3>");
        }
    }

    private Integer getUserId(String username, String password) {
        String query = "SELECT id FROM users WHERE name = ? AND password = ?";

        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");  // ✅ Return user_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // ❌ User not found
    }
}
