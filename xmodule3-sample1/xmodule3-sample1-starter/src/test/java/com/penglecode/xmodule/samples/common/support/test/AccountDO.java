package com.penglecode.xmodule.samples.common.support.test;

import com.penglecode.xmodule.common.support.ConvertibleObject;

import java.util.Set;

public class AccountDO implements ConvertibleObject {

    private static final long serialVersionUID = 1L;

    private Long acctId;

    private String acctNo;

    private String acctType;

    private Double balance;

    private Set<String> tags;

    public AccountDO() {
    }

    public AccountDO(Long acctId, String acctNo, String acctType, Double balance, Set<String> tags) {
        this.acctId = acctId;
        this.acctNo = acctNo;
        this.acctType = acctType;
        this.balance = balance;
        this.tags = tags;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}