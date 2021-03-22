package com.example.greenfarm.pojo;

public class User {

    private int id;

    private String username;

    private String password;

    private String telephone;

    private int isFarmer;

    private int isAdministrator;

    public User() {
    }

    public User(String username, String password, String telephone) {
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.isAdministrator = 0;
        this.isFarmer = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIsFarmer() {
        return isFarmer;
    }

    public void setIsFarmer(int isFarmer) {
        this.isFarmer = isFarmer;
    }

    public int getIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(int isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", isFarmer=" + isFarmer +
                ", isAdministrator=" + isAdministrator +
                '}';
    }
}