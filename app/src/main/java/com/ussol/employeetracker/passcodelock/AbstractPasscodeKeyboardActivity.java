package com.ussol.employeetracker.passcodelock;

import com.ussol.employeetracker.MainActivity;
import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.FingerprintHandler;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public abstract class AbstractPasscodeKeyboardActivity extends AppCompatActivity {
    public static final String KEY_MESSAGE = "message";

    protected EditText mPinCodeField;
    protected InputFilter[] filters = null;

    // Variable used for storing the key in the Android Keystore container
    protected TextView topMessage = null;
    private KeyStore keyStore;
    private static final String KEY_NAME = "emptrackerkey";
    private Cipher cipher;
    private TextView textView;
    private ImageView finger;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getResources().getBoolean(R.bool.allow_rotation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.app_passcode_keyboard);
        
        topMessage = (TextView) findViewById(R.id.passcodelock_prompt);

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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
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
                            FingerprintHandler helper = new FingerprintHandler(this);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString(KEY_MESSAGE);
            if (message != null) {
                topMessage.setText(message);
            }
        }
        
        filters = new InputFilter[2];
        filters[0]= new InputFilter.LengthFilter(1);
        filters[1] = onlyNumber;
        
        mPinCodeField = (EditText)findViewById(R.id.pin_field);
        
        //setup the keyboard
        findViewById(R.id.button0).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button1).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button2).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button3).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button4).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button5).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button6).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button7).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button8).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button9).setOnClickListener(defaultButtonListener);
        findViewById(R.id.button_erase).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String curText = mPinCodeField.getText().toString();

                        if (curText.length() > 0) {
                            mPinCodeField.setText(curText.substring(0, curText.length() - 1));
                            mPinCodeField.setSelection(mPinCodeField.length());
                        }
                    }
                });
    }
    
    private OnClickListener defaultButtonListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            int currentValue = -1;
            int id = arg0.getId();
			if (id == R.id.button0) {
				currentValue = 0;
			} else if (id == R.id.button1) {
				currentValue = 1;
			} else if (id == R.id.button2) {
				currentValue = 2;
			} else if (id == R.id.button3) {
				currentValue = 3;
			} else if (id == R.id.button4) {
				currentValue = 4;
			} else if (id == R.id.button5) {
				currentValue = 5;
			} else if (id == R.id.button6) {
				currentValue = 6;
			} else if (id == R.id.button7) {
				currentValue = 7;
			} else if (id == R.id.button8) {
				currentValue = 8;
			} else if (id == R.id.button9) {
				currentValue = 9;
			}

            //set the value and move the focus
            String currentValueString = String.valueOf(currentValue);
            mPinCodeField.setText(mPinCodeField.getText().toString() + currentValueString);
            mPinCodeField.setSelection(mPinCodeField.length());

            if(mPinCodeField.length() >= 4) {
                onPinLockInserted();
            }
        }
    };

    protected void showPasswordError(){
        Toast toast = Toast.makeText(AbstractPasscodeKeyboardActivity.this, getString(R.string.passcode_wrong_passcode), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 30);
        toast.show();
    }
    
    protected abstract void onPinLockInserted();

    private InputFilter onlyNumber = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.length() > 1) {
                return "";
            }

            if (source.length() == 0) {
                return null;
            }

            try {
                int number = Integer.parseInt(source.toString());
                if (number >= 0 && number <= 9) {
                    return String.valueOf(number);
                }

                return "";
            } catch (NumberFormatException e) {
                return "";
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
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
            throw new RuntimeException(e);
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
