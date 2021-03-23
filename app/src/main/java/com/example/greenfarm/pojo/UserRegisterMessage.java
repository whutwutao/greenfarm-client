package com.example.greenfarm.pojo;

public class UserRegisterMessage {

    //是否注册成功
    private boolean successful;
    //失败原因
    private String failReason;

    public UserRegisterMessage() {
    }

    public UserRegisterMessage(boolean isSuccessful, String failReason) {
        this.successful = isSuccessful;
        this.failReason = failReason;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        successful = successful;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return "UserRegisterMessage{" +
                "successful=" + successful +
                ", failReason='" + failReason + '\'' +
                '}';
    }
}