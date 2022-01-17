package com.penglecode.xmodule.samples.common.support.test;

import com.penglecode.xmodule.common.support.Convertible;

import java.math.BigDecimal;

public class AccountDTO implements Convertible {

    private static final long serialVersionUID = 1L;

    private Long acctId;

    private String acctNo;

    private String acctType;

    private BigDecimal balance;

    private String[] tags;

    public AccountDTO() {
    }

    public AccountDTO(Long acctId, String acctNo, String acctType, BigDecimal balance, String[] tags) {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}