package com.fingerAuth;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fingerAuth.Auth.FingerPrintVerification;
import com.fingerAuth.Auth.VerifyFingerPrint;

public class MainActivity extends AppCompatActivity implements VerifyFingerPrint {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

    }

    @Override
    public void fingerNotSupport() {
        Toast.makeText(MainActivity.this, "Finger Print Not support", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void authSuccessfully() {
        Toast.makeText(MainActivity.this, "Finger Print Auth successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void authFail(String reason) {
        Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show();
    }

    public void VerifyFinger(View view) {

        switch (view.getId()) {
            case R.id.default_finger_verification_dialog:
                new FingerPrintVerification(context, this);
                break;
            case R.id.full_screen_finger_verification_dialog:
                new FingerPrintVerification(context, true, this);
                break;
            case R.id.custome_theme_finger_verification_dialog:
                new FingerPrintVerification(context, R.layout.layout_finger_print, this);
                break;
            case R.id.custome_theme_full_screen_finger_verification_dialog:
                new FingerPrintVerification(context, R.layout.layout_finger_print, true, this);
                break;

        }

    }
}
