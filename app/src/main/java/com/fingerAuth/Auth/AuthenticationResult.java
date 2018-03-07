package com.fingerAuth.Auth;

/**
 * Created by baps on 07-03-2018.
 */

public interface AuthenticationResult {

    void authSuccessfully();

    void authFailed(String reason);
}
