package onlineFeedbackSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FeedbackResultServlet")
public class FeedbackResultServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Feedback Results</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; text-align: center; margin: 50px;");
        out.println("background: url('https://static.vecteezy.com/system/resources/previews/049/191/664/non_2x/a-sleek-feedback-form-lays-on-a-solid-background-accompanied-by-a-shiny-pen-inviting-users-to-share-their-thoughts-thoughtfully-photo.jpg') no-repeat center center/cover; }");
        out.println(".container { width: 70%; margin: auto; padding: 20px; border: 2px solid #333; border-radius: 10px; background-color: #f9f9f9; }");
        out.println("h2 { color: #333; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("a { display: block; margin-top: 20px; color: #4CAF50; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style></head><body>");
        
        out.println("<div class='container'><h2>Feedback Results</h2>");
        out.println("<table>");
        out.println("<tr><th>User ID</th><th>User Name</th><th>Average Rating</th><th>Overall Rating (%)</th></tr>");

        try {
            Connection conn = DBConnection.getConnected();
            
            // Query to calculate average rating per user and overall rating percentage
            String query = "SELECT u.id AS user_id, u.name AS user_name, " +
                           "AVG(f.answer) AS avg_rating, " +
                           "(AVG(f.answer) / 5) * 100 AS overall_percentage " +
                           "FROM feedback f " +
                           "JOIN users u ON f.user_id = u.id " +
                           "GROUP BY u.id, u.name " +
                           "ORDER BY u.id";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                double avgRating = rs.getDouble("avg_rating");
                double overallPercentage = rs.getDouble("overall_percentage");

                out.println("<tr><td>" + userId + "</td><td>" + userName + "</td><td>" + String.format("%.2f", avgRating) + "</td><td>" + String.format("%.2f", overallPercentage) + "%</td></tr>");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<tr><td colspan='4'>Error fetching data</td></tr>");
        }

        out.println("</table>");
        out.println("<a href='admin_dashboard.html'>Back to Dashboard</a>");
        out.println("</div></body></html>");
    }
}
