package com.ussol.employeetracker.passcodelock;

import com.ussol.employeetracker.MainActivity;
import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.AlertDialogRadio;
import com.ussol.employeetracker.helpers.FingerprintHandler;
import com.ussol.employeetracker.models.MasterConstants;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.crypto.Cipher;

public class PasscodeManagePasswordActivity extends AbstractPasscodeKeyboardActivity  implements View.OnClickListener {
    public static final String  KEY_TYPE = "type";

    private int type = -1;
    private String unverifiedPasscode = null;

    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "emptrackerkey";
    private Cipher cipher;
    private TextView textView;
    private ImageView finger;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt(KEY_TYPE, -1);
        }

        finger = (ImageView) findViewById(R.id.imageFinger);
        finger.setOnClickListener(this);

    }

    @Override
    protected void onPinLockInserted() {
        String passLock = mPinCodeField.getText().toString();
        mPinCodeField.setText("");

        switch (type) {
            case PasscodePreferenceFragment.DISABLE_PASSLOCK:
                if( AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock) ) {
                    setResult(RESULT_OK);
                    AppLockManager.getInstance().getCurrentAppLock().setPassword(null);
                    finish();
                } else {
                    showPasswordError();
                }
                break;
            case PasscodePreferenceFragment.ENABLE_PASSLOCK:
                if( unverifiedPasscode == null ) {
                    ((TextView) findViewById(R.id.passcodelock_prompt)).setText(R.string.passcode_re_enter_passcode);
                    unverifiedPasscode = passLock;
                } else {
                    if( passLock.equals(unverifiedPasscode)) {
                        setResult(RESULT_OK);
                        AppLockManager.getInstance().getCurrentAppLock().setPassword(passLock);
                        finish();
                    } else {
                        unverifiedPasscode = null;
                        topMessage.setText(R.string.passcodelock_prompt_message);
                        showPasswordError();
                    }
                }
                break;
            case PasscodePreferenceFragment.CHANGE_PASSWORD:
                //verify old password
                if( AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock) ) {
                    topMessage.setText(R.string.passcodelock_prompt_message);
                    type = PasscodePreferenceFragment.ENABLE_PASSLOCK;
                } else {
                    showPasswordError();
                } 
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        /** phân nhánh xử lý theo từng button*/
        switch (view.getId()) {
            case R.id.imageFinger:
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Đăng nhập");
                alertDialog.setMessage("Vui lòng quét vân tay để tiếp tục");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();

                // Initializing both Android Keyguard Manager and Fingerprint Manager
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                //--FINGER START
                textView = (TextView) findViewById(R.id.errorText);
                // Check whether the device has a Fingerprint sensor.
                if(!fingerprintManager.isHardwareDetected()){
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * Example:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
                    textView.setText("Your Device does not have a Fingerprint Sensor");
                }else {
                    // Checks whether fingerprint permission is set on manifest
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        textView.setText("Fingerprint authentication permission not enabled");
                    }else{
                        // Check whether at least one fingerprint is registered
                        if (!fingerprintManager.hasEnrolledFingerprints()) {
                            textView.setText("Register at least one fingerprint in Settings");
                        }else{
                            // Checks whether lock screen security is enabled or not
                            if (!keyguardManager.isKeyguardSecure()) {
                                textView.setText("Lock screen security not enabled in Settings");
                            }else{
                                generateKey();

                                if (cipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler helper = new FingerprintHandler(getApplicationContext());
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                    if(helper.getAuthenticationStates()){
                                        //logon ok
                                        setResult(RESULT_OK);
                                        //AppLockManager.getInstance().getCurrentAppLock().setPassword(passLock);
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }
                        }
                    }
                }
                //--FINGER END
                break;
        }
    }
}
