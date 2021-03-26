package com.example.greenfarm.pojo;

import androidx.annotation.NonNull;

public class UserMessage {

    //是否成功(注册或登录）
    private boolean successful;
    //失败原因
    private String failReason;

    private User user;

    public UserMessage() {
    }

    public UserMessage(boolean successful, String failReason, User user) {
        this.successful = successful;
        this.failReason = failReason;
        this.user = user;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "successful=" + successful +
                ", failReason='" + failReason + '\'' +
                ", user=" + user +
                '}';
    }
}