package com.penglecode.xmodule.samples.common.support.test;

import com.penglecode.xmodule.common.support.Convertible;

import java.util.Set;

public class CustomerDO implements Convertible {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String sex;

    private Integer age;

    private String birthday;

    private AccountDO account;

    private Set<AccountDO> elements;

    public CustomerDO() {
    }

    public CustomerDO(Long id, String name, String sex, Integer age, String birthday) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Set<AccountDO> getElements() {
        return elements;
    }

    public void setElements(Set<AccountDO> elements) {
        this.elements = elements;
    }

    public AccountDO getAccount() {
        return account;
    }

    public void setAccount(AccountDO account) {
        this.account = account;
    }
}