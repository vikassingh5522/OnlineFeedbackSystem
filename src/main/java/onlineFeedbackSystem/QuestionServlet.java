package onlineFeedbackSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addQuestion(request, response);
        } else if ("update".equals(action)) {
            updateQuestion(request, response);
        } else if ("delete".equals(action)) {
            deleteQuestion(request, response);
        } else {
            response.getWriter().println("Invalid action!");
        }
    }

    private void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String question = request.getParameter("question");
        String subject = request.getParameter("subject");
        int maxMarks = Integer.parseInt(request.getParameter("maxMarks"));

        String sql = "INSERT INTO questions (question_text, subject, max_marks) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, question);
            ps.setString(2, subject);
            ps.setInt(3, maxMarks);
            ps.executeUpdate();

            response.sendRedirect("questions_management.html");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error adding question!");
        }
    }

    private void updateQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String question = request.getParameter("question");
        String subject = request.getParameter("subject");
        int maxMarks = Integer.parseInt(request.getParameter("maxMarks"));

        String sql = "UPDATE questions SET question_text=?, subject=?, max_marks=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, question);
            ps.setString(2, subject);
            ps.setInt(3, maxMarks);
            ps.setInt(4, id);
            ps.executeUpdate();

            response.sendRedirect("questions_management.html");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error updating question!");
        }
    }

    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String sql = "DELETE FROM questions WHERE id=?";
        
        try (Connection conn = DBConnection.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            ps.executeUpdate();

            response.sendRedirect("questions_management.html");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error deleting question!");
        }
    }
}
