package com.fingerAuth.Auth;

/**
 * Created by baps on 06-03-2018.
 */

public interface VerifyFingerPrint {

    void fingerNotSupport();

    void authSuccessfully();

    void authFail(String reason);
}
