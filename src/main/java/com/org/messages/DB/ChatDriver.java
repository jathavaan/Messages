package com.org.messages.DB;

import com.org.messages.model.messaging.Chat;
import com.org.messages.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class ChatDriver extends Driver {
    public ChatDriver() throws ClassNotFoundException, SQLException {}

    public void createChat(String chatName, User creator, Collection<User> chatMembers) throws SQLException {
        // need add a check to confirm if a chat with the same members already exists

        Chat chat = new Chat(0, chatName, creator, chatMembers);

        String sql = "insert into chat (chatName, createdDate, lastActive, userID) values  ('" + chatName + "', '" + chat.getCreatedDate() + "', '" + chat.getLastActive() + "', '" + creator.getUserID() + "')";
        getStm().executeUpdate(sql);

        // Need to find a way to get the latest chatID
        sql = "select chatID from chat order by chatID desc limit 1";
        ResultSet rs = getStm().executeQuery(sql);

        while (rs.next()) {
            int dbChatID = rs.getInt("chatID");
            chat = new Chat(dbChatID, chatName, creator, chatMembers);
        }

        Chat finalChat = chat;
        chatMembers.stream().forEach(chatMember -> {
            try {
                addChatMember(finalChat, chatMember);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }); // Adds chatmembers to database

        addAdmin(finalChat, creator); // Adds admin for the chat

        System.out.println("Chat added to database: " + chat);
    }

    public Chat retrieveChat(Chat chat) throws SQLException {
        String sql = "select * from chat inner join user on chat.useID = user.userID where chatID = '" + chat.getChatID() + "' limit 1";
        ResultSet rs = getStm().executeQuery(sql);

        Chat finalChat = null;

        while (rs.next()) {
            int dbChatID = rs.getInt("chatID");
            String dbChatName = rs.getString("chatName");
            String dbCreatedDateString = rs.getString("createdDate");
            String dbLastActiveString = rs.getString("lastActive");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbCreatedDate = LocalDateTime.parse(dbCreatedDateString, formatter);
            LocalDateTime dbLastActive = LocalDateTime.parse(dbLastActiveString, formatter);

            int dbUserID = rs.getInt("userID");
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            String dbFirstName = rs.getString("firstName");
            String dbSurname = rs.getString("surname");
            LocalDateTime dbDateOfBirth = LocalDateTime.parse(rs.getString("dateOfBirth"), formatter);

            User creator = new User(dbUserID, dbEmail, dbPassword, dbFirstName, dbSurname, dbDateOfBirth);

            finalChat = new Chat(dbChatID, dbChatName, dbCreatedDate, dbLastActive, creator);
        }

        return finalChat;
    }

    public ArrayList<Chat> retrieveChatByUser(User user) throws SQLException {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        String sql = "select chat.chatID, chat.chatName, chat.createdDate, chat.lastActive, chat.userID, user.email, user.password, user.firstName, user.surname, user.dateOfBirth from members " +
                "inner join chat on members.chatID = chat.chatID " +
                "inner join user on members.userID = user.userID " +
                "where user.userID = '" + user.getUserID() + "'";
        ResultSet rs = getStm().executeQuery(sql);

        ArrayList<Chat> chats = new ArrayList<Chat>();

        while (rs.next()) {
            int dbChatID = rs.getInt("chatID");
            String dbChatName = rs.getString("chatName");
            String dbCreatedDateString = rs.getString("createdDate");
            String dbLastActiveString = rs.getString("lastActive");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbCreatedDate = LocalDateTime.parse(dbCreatedDateString, formatter);
            LocalDateTime dbLastActive = LocalDateTime.parse(dbLastActiveString, formatter);

            int dbUserID = rs.getInt("userID");
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            String dbFirstName = rs.getString("firstName");
            String dbSurname = rs.getString("surname");
            LocalDateTime dbDateOfBirth = LocalDateTime.parse(rs.getString("dateOfBirth"), formatter);

            User creator = new User(dbUserID, dbEmail, dbPassword, dbFirstName, dbSurname, dbDateOfBirth);

            chats.add(new Chat(dbChatID, dbChatName, dbCreatedDate, dbLastActive, creator));
        }

        return chats;
    }

    private void addChatMember(Chat chat, User chatMember) throws SQLException {
        String sql = "insert into members (chatID, userID) values ('" + chat.getChatID() + "', '" + chatMember.getUserID() + "')";
        getStm().executeUpdate(sql);

        System.out.println("Added chatmember to chat: (chatID: " + chat.getChatID() + ", userID: " + chatMember.getUserID() + ")");
    }

    private void addAdmin(Chat chat, User user) throws SQLException {
        String sql = "insert into admins (userID, chatID) values ('" + user.getUserID() + "', '" + chat.getChatID() + "')";
        getStm().executeUpdate(sql);
    }
}
