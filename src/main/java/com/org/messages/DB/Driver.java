package com.org.messages.DB;

import com.org.messages.model.messaging.*;
import com.org.messages.model.user.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Driver {
    private final Connection con;
    private final Statement stm;

    public Driver() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.con = DriverManager.getConnection("jdbc:mysql://:3306/messagedb", "root", "jathavaan12"); // Connects to DB
        this.stm = con.createStatement();
    }

    public Connection getCon() {
        return con;
    }

    public Statement getStm() {
        return stm;
    }

    // HANDLE USER

    public void createUser(String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) throws SQLException {
        email = email.toLowerCase(); // changes email to lowercase letters

        if (retrieveUser(email) != null)
            throw new IllegalStateException("An account with this email has already been registered.");

        User user = new User(0, email, password, firstName, surname, dateOfBirth); // Validation of user input

        String sql = "insert into user (email, password, firstName, surname, dateOfBirth) values ('" + email + "', '" + password + "', '" + firstName + "', '" + surname + "', '" + dateOfBirth + "')";
        getStm().executeUpdate(sql);
        System.out.println("User added to database: " + user);
    }

    public User retrieveUser(String email) throws SQLException {
        email = email.toLowerCase();
        String sql = "select * from messagedb.user where email = '" + email + "'";
        ResultSet rs = getStm().executeQuery(sql);

        ArrayList<User> result = new ArrayList<>(); // Should not contain more than 1 element

        while (rs.next()) {
            int dbUserID = rs.getInt("userID");
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            String dbFirstName = rs.getString("firstName");
            String dbSurname = rs.getString("surname");
            String dbDateOfBirth = rs.getString("dateOfBirth");

            // Converting date of birth to LocalDateTime object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbDob = LocalDateTime.parse(dbDateOfBirth, formatter);

            if (result.size() == 0) {
                result.add(new User(dbUserID, dbEmail, dbPassword, dbFirstName, dbSurname, dbDob));
            } else {
                break;
            }
        }

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    // HANDLE CHAT

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
    
    public ArrayList<Chat> retrieveChatByUserID(User user) throws SQLException {
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

    // HANDLE MESSAGE

    public void sendMessage(Chat chat, ChatElement message) throws SQLException {
        if (chat == null)
            throw new IllegalArgumentException("Chat cannot be null");

        if (message == null)
            throw new IllegalArgumentException("Chat element cannot be null");

        int chatID = chat.getChatID();

        if (message instanceof Message) {
            LocalDateTime sent = message.getSent();
            LocalDateTime opened = message.getOpened();
            int userID = message.getSender().getUserID();
            String text = ((Message) message).getMessage();

            String sql = "insert into chatelement (sent, opened, message, chatID, userID) values ('" + sent + "', '" + opened + "', '" + message + "', '" + chat.getChatID() + "', '" + userID + "')";
            getStm().executeUpdate(sql);

            System.out.println("Message sent: " + message);
        } else if (message instanceof Media || message instanceof VoiceNote) {
            // Add functionality for media files and voicenotes
        }
    }

    public static void main(String[] args) {
        Driver d = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            d = new Driver();
            d.createUser("jathavaan12@gmail.com", "Jathavaan12", "Jathavaan", "Shankarr", LocalDateTime.parse("2001-07-12 00:00:00", formatter));
            d.createUser("shankarrv@gmail.com", "Ramanir16", "Shankarr", "Vinajagam", LocalDateTime.parse("1969-04-16 00:00:00", formatter));
            d.createUser("ramanir16@gmail.com", "Shankarrv09", "Ramani", "Ramanathan", LocalDateTime.parse("1969-11-09 00:00:00", formatter));

            User jatha = d.retrieveUser("jathavaan12@gmail.com");
            User shankarr = d.retrieveUser("shankarrv@gmail.com");
            User ramani = d.retrieveUser("ramanir16@gmail.com");

            ArrayList<User> chatMembers = new ArrayList<>(Arrays.asList(jatha, shankarr, ramani));
            d.createChat("Famile chat", jatha, chatMembers);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
