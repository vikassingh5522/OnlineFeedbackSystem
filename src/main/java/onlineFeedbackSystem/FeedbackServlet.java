package onlineFeedbackSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            System.out.println("Session expired or user not logged in. Redirecting to login page.");
            response.sendRedirect("login.html");
            return;
        }

        int userId;  
        try {
            userId = Integer.parseInt(session.getAttribute("user_id").toString());
            System.out.println("User ID retrieved from session: " + userId);
        } catch (NumberFormatException e) {
            System.out.println("Error converting user_id from session.");
            response.sendRedirect("login.html");
            return;
        }

        try {
            conn = DBConnection.getConnected();
            String query = "INSERT INTO feedback (user_id, question_id, answer) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(query);
            
            boolean feedbackReceived = false;
            
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (paramName.startsWith("rating_")) {
                    int questionId = Integer.parseInt(paramName.split("_")[1]);
                    int rating = Integer.parseInt(request.getParameter(paramName));

                    ps.setInt(1, userId);  // ✅ Add user_id
                    ps.setInt(2, questionId);
                    ps.setInt(3, rating);  // ✅ Insert into 'answer' column
                    ps.executeUpdate();
                    
                    feedbackReceived = true;
                    System.out.println("Feedback saved: Question " + questionId + " -> Rating " + rating);
                }
            }

            if (!feedbackReceived) {
                System.out.println("No feedback received.");
            }

            // ✅ Redirect to Thank You page
            response.sendRedirect("Thank_U.html");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
