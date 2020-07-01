package com.leon.serial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    final int READ_PHONE_STATE = 911;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE);
            return;
        }
        String serial = Build.SERIAL;
        AppCompatTextView appCompatTextView = findViewById(R.id.textViewSerial);
        appCompatTextView.setText(serial);
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String serial = Build.SERIAL;
                AppCompatTextView appCompatTextView = findViewById(R.id.textViewSerial);
                appCompatTextView.setText(serial);

            } else {
                Toast.makeText(MainActivity.this, "Read Phone State Permission Denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }
    }
}
