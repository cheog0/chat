package chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChatDAO {
    private Connection conn;

    public ChatDAO() {
        try {
            String dbURL = "jdbc:mysql://localhost:3306/ANCHAT";
            String dbID = "root";
            String dbPassword = "0914";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Chat> getChatList(String nowTime) {
        ArrayList<Chat> chatList = new ArrayList<Chat>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String SQL = "SELECT * FROM CHAT WHERE chatTime > ? ORDER BY chatTime";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, nowTime);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Chat chat = new Chat();
                chat.setChatID(rs.getInt("chatID"));
                chat.setChatName(rs.getString("chatName"));
                chat.setChatContent(rs.getString("chatContent"));
                chat.setChatTime(rs.getString("chatTime"));
                chatList.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chatList;
    }
    
    public ArrayList<Chat> getChatListByRecent(int number) {
        ArrayList<Chat> chatList = new ArrayList<Chat>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String SQL = "SELECT * FROM CHAT WHERE chatTime > (SELECT MAX(chatID) - ? FROM CHAT) ORDER BY chatTime";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, number);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Chat chat = new Chat();
                chat.setChatID(rs.getInt("chatID"));
                chat.setChatName(rs.getString("chatName"));
                chat.setChatContent(rs.getString("chatContent"));
                chat.setChatTime(rs.getString("chatTime"));
                chatList.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chatList;
    }
    
    public ArrayList<Chat> getAllChats() {
        ArrayList<Chat> chatList = new ArrayList<Chat>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String SQL = "SELECT * FROM CHAT ORDER BY chatTime";
        try {
            pstmt = conn.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Chat chat = new Chat();
                chat.setChatID(rs.getInt("chatID"));
                chat.setChatName(rs.getString("chatName"));
                chat.setChatContent(rs.getString("chatContent"));
                chat.setChatTime(rs.getString("chatTime"));
                chatList.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chatList;
    }

    public int submit(String chatName, String chatContent) {
        PreparedStatement pstmt = null;
        String SQL = "INSERT INTO CHAT VALUES (NULL, ?, ?, now())";
        try {
            pstmt = conn.prepareStatement(SQL);
            String processedContent = chatContent
                .replaceAll("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "<br>");

            pstmt.setString(1, chatName);
            pstmt.setString(2, processedContent);
            
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

}