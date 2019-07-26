package com.android.chalkboard.util;

public class JwtTokenSingleton {
    private static  JwtTokenSingleton jwtTokenSingletonInstance;
    private String JwtToken;

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    private JwtTokenSingleton(){}

    public static JwtTokenSingleton getInstance() {
        if(jwtTokenSingletonInstance == null){
            jwtTokenSingletonInstance = new JwtTokenSingleton();
        }
        return jwtTokenSingletonInstance;
    }


}
