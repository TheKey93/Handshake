package com.ece.handshake.model.data;

import java.io.Serializable;
import java.util.ArrayList;


public class NfcTransfer implements Serializable{
    public ArrayList<SMAccount> accounts;
    public PhoneContact contact;

    public NfcTransfer(ArrayList<SMAccount> accounts, PhoneContact contact) {
        this.accounts = accounts;
        this.contact = contact;
    }
}
