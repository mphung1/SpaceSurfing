package com.example.SpaceSurfing.models;

import android.text.format.DateUtils;

import com.google.firebase.firestore.Exclude;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Parcel
public class Chat {
    public static final String KEY_CHATS = "chats";
    public static final String KEY_LAST_MESSAGE_DATE = "lastMessageDate";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_MEMBER_ONE_READ = "memberOneRead";
    public static final String KEY_MEMBER_TWO_READ = "memberTwoRead";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String dateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    private String lastMessage;
    private ArrayList<String> members;
    private Date lastMessageDate;
    private boolean memberOneRead;
    private boolean memberTwoRead;

    @Exclude
    private String chatId;

    public Chat() {
    }

    public Chat(String lastMessage, ArrayList<String> members, Date lastMessageDate,
                boolean memberOneRead, boolean memberTwoRead, String chatId) {
        this.lastMessage = lastMessage;
        this.members = members;
        this.lastMessageDate = lastMessageDate;
        this.memberOneRead = memberOneRead;
        this.memberTwoRead = memberTwoRead;
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public boolean isMemberOneRead() {
        return memberOneRead;
    }

    public void setMemberOneRead(boolean memberOneRead) {
        this.memberOneRead = memberOneRead;
    }

    public boolean isMemberTwoRead() {
        return memberTwoRead;
    }

    public void setMemberTwoRead(boolean memberTwoRead) {
        this.memberTwoRead = memberTwoRead;
    }

    @Exclude
    public String getRelativeTime() {
        String timeCreated = lastMessageDate.toString();

        SimpleDateFormat sf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(timeCreated).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
