package com.penglecode.xmodule.samples.common.support.test;

import com.penglecode.xmodule.common.support.Convertible;

import java.time.LocalDate;

public class CustomerDTO implements Convertible {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private char sex;

    private Short age;

    private LocalDate birthday;

    private AccountDTO account;

    private AccountDTO[] elements;

    public CustomerDTO() {
    }

    public CustomerDTO(long id, String name, char sex, Short age, LocalDate birthday, AccountDTO[] elements) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.birthday = birthday;
        this.elements = elements;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public AccountDTO[] getElements() {
        return elements;
    }

    public void setElements(AccountDTO[] elements) {
        this.elements = elements;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

}