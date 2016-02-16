package com.mobileapp.sab.localchat;
import java.math.BigInteger;
import java.security.SecureRandom;


public class CreateUserID {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(130, random).toString(32);
    }

}
