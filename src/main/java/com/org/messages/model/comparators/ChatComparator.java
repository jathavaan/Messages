package com.org.messages.model.comparators;

import com.org.messages.model.messaging.Chat;
import com.org.messages.model.messaging.ChatElement;

import java.time.ZoneId;
import java.util.Comparator;

public class ChatComparator implements Comparator<Chat> {
    @Override
    public int compare(Chat o1, Chat o2) {
        if (o1.getMessages().size() > 0 && o2.getMessages().size() > 0) {
            ChatElement latest1 = o1.getMessages().get(0);
            ChatElement latest2 = o2.getMessages().get(0);

            ZoneId zoneID = ZoneId.systemDefault();
            long epoch1 = latest1.getSent().atZone(zoneID).toEpochSecond();
            long epoch2 = latest2.getSent().atZone(zoneID).toEpochSecond();
            return (int) (epoch1 - epoch2);
        } else if (o1.getMessages().size() > 0) {
            return 1;
        } else if (o2.getMessages().size() > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
