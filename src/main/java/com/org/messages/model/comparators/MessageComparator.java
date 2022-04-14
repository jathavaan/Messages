package com.org.messages.model.comparators;

import com.org.messages.model.messaging.ChatElement;

import java.time.ZoneId;
import java.util.Comparator;

public class MessageComparator implements Comparator<ChatElement> {
    @Override
    public int compare(ChatElement o1, ChatElement o2) {
        ZoneId zoneID = ZoneId.systemDefault();
        long epoch1 = o1.getSent().atZone(zoneID).toEpochSecond();
        long epoch2 = o2.getSent().atZone(zoneID).toEpochSecond();
        return (int) (epoch1 - epoch2);
    }
}
