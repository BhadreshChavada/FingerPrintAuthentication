package com.fingerAuth.Auth;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.fingerAuth.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by baps on 06-03-2018.
 */

public class FingerPrintVerification {

    Context context;
    VerifyFingerPrint verifyFingurePrint;

    private static final String KEY_NAME = "VerifyFingerPrint";
    private static Cipher cipher;
    private static boolean isSkip = false;
    private static KeyStore keyStore;
    Dialog dialog;

    public FingerPrintVerification(Context context, int layout, VerifyFingerPrint verifyFingurePrint) {
        this.context = context;
        this.verifyFingurePrint = verifyFingurePrint;
        dialog = new Dialog(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                verifyFingurePrint.fingerNotSupport();
            } else {
                fingureVerificationDialog(layout, false);

            }
        }
    }

    public FingerPrintVerification(Context context, int layout, Boolean fullScreen, VerifyFingerPrint verifyFingurePrint) {
        this.context = context;
        this.verifyFingurePrint = verifyFingurePrint;
        dialog = new Dialog(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                verifyFingurePrint.fingerNotSupport();
            } else {
                fingureVerificationDialog(layout, fullScreen);

            }
        }
    }


    public FingerPrintVerification(Context context, VerifyFingerPrint verifyFingurePrint) {
        this.context = context;
        this.verifyFingurePrint = verifyFingurePrint;
        dialog = new Dialog(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                verifyFingurePrint.fingerNotSupport();
            } else {
                fingureVerificationDialog(false);

            }
        }
    }

    public FingerPrintVerification(Context context, Boolean fullScreen, VerifyFingerPrint verifyFingurePrint) {
        this.context = context;
        this.verifyFingurePrint = verifyFingurePrint;
        dialog = new Dialog(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                verifyFingurePrint.fingerNotSupport();
            } else {
                fingureVerificationDialog(fullScreen);

            }
        }
    }


    void fingureVerificationDialog(int layout, Boolean fullScreen) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        if (fullScreen) {
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        FingerAuth();
    }

    void fingureVerificationDialog(Boolean fullScreen) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_finger_print);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        if (fullScreen) {
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        FingerAuth();
    }


    void FingerAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(context.FINGERPRINT_SERVICE);
                // Check whether the device has a Fingerprint sensor.
                if (!fingerprintManager.isHardwareDetected()) {
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * Example:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
                    // textView.setText("Your Device does not have a Fingerprint Sensor");
                    verifyFingurePrint.fingerNotSupport();
                } else {
                    // Checks whether fingerprint permission is set on manifest
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        //   textView.setText("Fingerprint authentication permission not enabled");
                        verifyFingurePrint.fingerNotSupport();
                    } else {
                        // Check whether at least one fingerprint is registered
                        if (!fingerprintManager.hasEnrolledFingerprints()) {
                            //     textView.setText("Register at least one fingerprint in Settings");
                            verifyFingurePrint.fingerNotSupport();
                        } else {
                            // Checks whether lock screen security is enabled or not
                            if (!keyguardManager.isKeyguardSecure()) {
                                //       textView.setText("Lock screen security not enabled in Settings");
                                verifyFingurePrint.fingerNotSupport();
                            } else {
                                generateKey();
                                if (cipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                                    FingerprintHandler helper = new FingerprintHandler(context, isSkip, new AuthenticationResult() {
                                        @Override
                                        public void authSuccessfully() {
                                            dialog.dismiss();
                                            verifyFingurePrint.authSuccessfully();
                                        }

                                        @Override
                                        public void authFailed(String reason) {
//                                            dialog.dismiss();
                                            verifyFingurePrint.authFail(reason);
                                        }
                                    });
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                verifyFingurePrint.fingerNotSupport();
            }
        } else {
            verifyFingurePrint.fingerNotSupport();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {

        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
