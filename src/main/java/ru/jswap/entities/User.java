package ru.jswap.entities;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
    private long id;
    private String username;
    private String pwd;
    private int pincode;
    private String email;
    private long phone;
    private long sizeLimit;
    private long filesSize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }


    public User() {
        sizeLimit = 5368709120L;
        filesSize = 0;
    }

    public User(String username) {
        this.username = username;
        sizeLimit = 5368709120L;
        filesSize = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(pwd, user.pwd) &&
                Objects.equals(pincode, user.pincode) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(sizeLimit, user.sizeLimit) &&
                Objects.equals(filesSize, user.filesSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, pwd, pincode, email, phone, sizeLimit, filesSize);
    }

    public long getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public long getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(long filesSize) {
        this.filesSize = filesSize;
    }
}

