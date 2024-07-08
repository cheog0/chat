package chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        String listType = request.getParameter("listType");
        if (listType == null || listType.isEmpty()) response.getWriter().write("");
        else if (listType.equals("today")) response.getWriter().write(getToday());
        else if (listType.equals("ten")) response.getWriter().write(getTen());
    }

    private String getToday() {
    	StringBuffer result = new StringBuffer("");
    	result.append("{\"result\":[");
        ChatDAO chatDAO = new ChatDAO();
        ArrayList<Chat> chatList = chatDAO.getChatList(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        for (int i = 0; i < chatList.size(); i++) {
            result.append("{\"value\":\"" + chatList.get(i).getChatName() + "\",");
            result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\",");
            result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\",");
            if (i != chatList.size() - 1) result.append(",");
        }
        result.append("]}, \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
        return result.toString();
    }
    
    public String getTen() {
        StringBuilder result = new StringBuilder("");
        result.append("["); // Start of JSON array

        ChatDAO chatDAO = new ChatDAO();
        ArrayList<Chat> chatList = chatDAO.getAllChats(); // Fetches 10 recent chat objects

        for (int i = 0; i < chatList.size(); i++) {
            result.append("{"); // Start of JSON object
            result.append("\"chatName\":\"").append(chatList.get(i).getChatName()).append("\","); // Adds chatName field
            result.append("\"chatContent\":\"").append(chatList.get(i).getChatContent()).append("\","); // Adds chatContent field
            result.append("\"chatTime\":\"").append(chatList.get(i).getChatTime()).append("\","); // Adds chatTime field
            result.append("\"chatID\":\"").append(chatList.get(i).getChatID()).append("\""); // Adds chatID field
            result.append("}"); // End of JSON object

            if (i < chatList.size() - 1) {
                result.append(","); // Adds comma if not the last chat object
            }
        }

        result.append("]"); // Adds last chat ID and end of JSON array
        return result.toString(); // Returns the constructed JSON-like string
    }

}