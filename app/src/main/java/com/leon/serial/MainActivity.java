package com.leon.serial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import static android.os.Build.UNKNOWN;


public class MainActivity extends AppCompatActivity {
    final int READ_PHONE_STATE = 911;
    public static final int CARRIER_PRIVILEGE_STATUS = 901;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    "android.permission.READ_PRIVILEGED_PHONE_STATE"}, READ_PHONE_STATE);
            return;
        }
        setSerial();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setSerial();
            } else {
                Toast.makeText(MainActivity.this, "Read Phone State Permission Denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        } else if (requestCode == CARRIER_PRIVILEGE_STATUS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "Ready with carrier privs");
            }
        }
    }

    @SuppressLint("HardwareIds")
    void setSerial() {
        String serial = Build.SERIAL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (hasCarrierPrivileges())
                serial = Build.getSerial();
        }
        if (serial.equals(UNKNOWN))
            serial = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        AppCompatTextView appCompatTextView = findViewById(R.id.textViewSerial);
        appCompatTextView.setText(serial);

//        Log.e("device serial", Device.getSerialNumber());
//        Log.e("device serial", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

    }

    @SuppressLint("NewApi")
    public boolean hasCarrierPrivileges() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        boolean isCarrier = tm.hasCarrierPrivileges();
        if (isCarrier) {
            Log.e("TAG", "Ready with carrier privs");
        } else {
            Log.e("Error", "No carrier privs");
            int hasPermission = ActivityCompat.checkSelfPermission(getApplicationContext(),
                    "android.permission.READ_PRIVILEGED_PHONE_STATE");
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale("android.permission.READ_PRIVILEGED_PHONE_STATE")) {
                    Log.e("error", "You need to allow access");

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            "android.permission.READ_PRIVILEGED_PHONE_STATE"}, CARRIER_PRIVILEGE_STATUS);
                }
            }
        }
        return isCarrier;
    }
}
