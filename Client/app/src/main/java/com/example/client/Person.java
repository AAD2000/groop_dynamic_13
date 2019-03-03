package com.example.client;
import java.io.Serializable;

public class Person implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    Person(String name,
           String surname,
           String nickname,
           String telephone,
           String password) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.telephone = telephone;
        this.password = password;
    }
}
