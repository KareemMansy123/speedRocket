package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 9/16/2018.
 */

public interface Session {

    boolean isLoggedIn();

    void saveToken(String token);

    String getToken();

    void savePassword(String password) throws Exception;

    String getPassword() throws Exception;

    void saveEmail(String email) ;

    String getEmail();

    void invalidate();
}
