package com.example.chatroom;

public class Message {
    private String content,user;

    public Message(){

    }
    public Message(String content,String user)
    {
        this.content = content;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}