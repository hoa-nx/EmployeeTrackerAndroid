package com.ussol.employeetracker.passcodelock;

import com.ussol.employeetracker.MainActivity;
import com.ussol.employeetracker.R;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PasscodeUnlockActivity extends AbstractPasscodeKeyboardActivity {
    
    @Override
    public void onBackPressed() {
        AppLockManager.getInstance().getCurrentAppLock().forcePasswordLock();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
        finish();
    }


    @Override
    protected void onPinLockInserted() {
        String passLock = mPinCodeField.getText().toString();
        if( AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock) ) {
            setResult(RESULT_OK);
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
			startActivity(intent);
            finish();
        } else {
            Thread shake = new Thread() {
                public void run() {
                    Animation shake = AnimationUtils.loadAnimation(PasscodeUnlockActivity.this, R.anim.shake);
                    findViewById(R.id.AppUnlockLinearLayout1).startAnimation(shake);
                    showPasswordError();
                    mPinCodeField.setText("");
                }
            };
            runOnUiThread(shake);
        }
    }
}