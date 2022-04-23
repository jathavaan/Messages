package com.org.messages.DB;

import com.org.messages.model.messaging.*;
import com.org.messages.model.user.User;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class MessagingDriver extends Driver {
    public MessagingDriver() throws ClassNotFoundException, SQLException {
    }

    public void sendMessage(Chat chat, ChatElement message) throws SQLException {
        if (chat == null)
            throw new IllegalArgumentException("Chat cannot be null");

        if (message == null)
            throw new IllegalArgumentException("Chat element cannot be null");

        User sender = message.getSender();

        if (message instanceof Message) {
            LocalDateTime sent = message.getSent();
            int userID = message.getSender().getUserID();
            String text = ((Message) message).getMessage();

            String sql = "insert into chatelement (sent, message, chatID, userID) values ('" + sent + "', '" + text + "', '" + chat.getChatID() + "', '" + userID + "')";
            getStm().executeUpdate(sql);

            System.out.println("Message sent: " + message);
        } else if (message instanceof Media || message instanceof VoiceNote) {
            // Add functionality for media files and voicenotes
        }
    }

    public ArrayList<ChatElement> getMessagesInChat(Chat chat) throws SQLException {
        if (chat == null)
            throw new IllegalArgumentException("Chat element cannot be null");

        String sql = "select * from chatElement " +
                "inner join user on chatElement.userID = user.userID " +
                "where chatID = '" + chat.getChatID() + "' " +
                "order by sent asc";
        ResultSet rs = getStm().executeQuery(sql);

        ArrayList<ChatElement> chats = new ArrayList<ChatElement>();

        while (rs.next()) {
            int chatElementID = rs.getInt("chatElementID");
            LocalDateTime sent = formatDate(rs.getString("sent"));
            LocalDateTime opened = formatDate(rs.getString("opened"));
            String message = rs.getString("message");
            Blob media = rs.getBlob("media");
            int chatID = rs.getInt("chatID");

            User sender = generateUserFromResultSet(rs);

            if (message != null) {
                chats.add(new Message(chatElementID, sent, opened, message, sender));
            } else if (media != null) {
                // if a voice note or media message is sent
            }
        }

        return chats;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDriver ud = new UserDriver();
        ChatDriver cd = new ChatDriver();
        MessagingDriver md = new MessagingDriver();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ud.createUser("eirikv@gmail.com", "eirikFraTau123", "Eirik", "Varnes", LocalDateTime.parse("1999-11-09 00:00:00", formatter));

        User jatha = ud.retrieveUser("jathavaan12@gmail.com");
        User ramani = ud.retrieveUser("ramanir16@gmail.com");
        User shankarr = ud.retrieveUser("shankarrv@gmail.com");
        User eirik = ud.retrieveUser("eirikv@gmail.com");

        cd.createChat("Myffens mødre", eirik, Arrays.asList(eirik, jatha));
        ArrayList<Chat> chats = cd.retrieveChatByUser(eirik);

        if (chats.size() > 0) {
            Chat chat = chats.get(0);
            Message msg1 = new Message(0, "Hall velkommen til myffens mødre", eirik);
            Message msg2 = new Message(0, "Å kult", jatha);
            md.sendMessage(chat, msg1);
            md.sendMessage(chat, msg2);
            System.out.println(md.getMessagesInChat(chat));
        }

    }
}
