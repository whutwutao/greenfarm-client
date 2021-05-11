package com.example.greenfarm.pojo;

public class Message {

    private int id;//消息id
    private int from;//发送者的id
    private int to;//接受者的id
    private String content;//消息内容
    private String time;
    private int isMeSend;//0是对方发送，1是自己发送
    private int isRead;//是否已读（0未读，1已读）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", isMeSend=" + isMeSend +
                ", isRead=" + isRead +
                '}';
    }
}
