package onlineFeedbackSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FeedbackFormServlets")
public class FeedbackFormServlets extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        out.println("<html><head>");
        out.println("<title>Feedback Form</title>");
        out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
        
        // Add Background Image and Scrollbar Styling
        out.println("<style>");
        out.println("body {");
        out.println("  background: url('https://images.pexels.com/photos/733852/pexels-photo-733852.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1') no-repeat center center fixed;");
        out.println("  background-size: cover;");
        out.println("}");

        out.println(".container {");
        out.println("  display: flex;");
        out.println("  justify-content: center;");
        out.println("  align-items: center;");
        out.println("  height: 100vh;");
        out.println("}");

        out.println(".card {");
        out.println("  background: rgba(255, 255, 255, 0.9);");
        out.println("  padding: 20px;");
        out.println("  border-radius: 10px;");
        out.println("  box-shadow: 0px 4px 10px rgba(0,0,0,0.2);");
        out.println("  max-width: 500px;");
        out.println("}");

        // Scrollable div for questions
        out.println(".scroll-container {");
        out.println("  max-height: 300px;"); // Set a fixed height
        out.println("  overflow-y: auto;"); // Enable vertical scrolling
        out.println("  padding-right: 10px;");
        out.println("}");

        // Custom scrollbar
        out.println(".scroll-container::-webkit-scrollbar { width: 8px; }");
        out.println(".scroll-container::-webkit-scrollbar-thumb { background: #007BFF; border-radius: 4px; }");
        out.println(".scroll-container::-webkit-scrollbar-track { background: #f1f1f1; }");

        out.println("</style>");
        out.println("</head><body>");

        out.println("<div class='container'>");
        out.println("<div class='card p-4'>");
        out.println("<h2 class='text-center mb-4'>Feedback Form</h2>");
        out.println("<form action='FeedbackServlet' method='post'>");

        // Scrollable div
        out.println("<div class='scroll-container'>");

        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement("SELECT id, question_text FROM questions");
             ResultSet rs = ps.executeQuery()) {

            // Check if questions exist
            boolean hasQuestions = false;

            while (rs.next()) {
                hasQuestions = true;
                int questionId = rs.getInt("id");
                String questionText = rs.getString("question_text");

                out.println("<div class='form-group'>");
                out.println("<label class='font-weight-bold'>" + questionText + "</label>");
                out.println("<select name='rating_" + questionId + "' class='form-control'>");
                out.println("<option value='5'>Excellent (5)</option>");
                out.println("<option value='4'>Very Good (4)</option>");
                out.println("<option value='3'>Good (3)</option>");
                out.println("<option value='2'>Fair (2)</option>");
                out.println("<option value='1'>Poor (1)</option>");
                out.println("</select>");
                out.println("</div>");
            }

            if (!hasQuestions) {
                out.println("<div class='alert alert-warning text-center'>No questions available at the moment.</div>");
            }

        } catch (Exception e) {
            out.println("<div class='alert alert-danger text-center'>Error loading questions. Please try again later.</div>");
            e.printStackTrace();
        }

        out.println("<div class='text-center'>");
        out.println("<button type='submit' class='btn btn-primary btn-lg'>Submit Feedback</button>");
        out.println("</div>");

        out.println("</form>");
        out.println("</div>"); // End of card
        out.println("</div>"); // End of container

        out.println("</body></html>");
    }
}
