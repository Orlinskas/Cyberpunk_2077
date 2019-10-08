package com.orlinskas.cyberpunk.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;

public class FlashlightActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startFlashlight();
            }
            else {
                ActivityCompat.requestPermissions(FlashlightActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        ToastBuilder.create(getBaseContext(),"Flashlight not available");
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFlashlight();
            } else {
                showErrorMessage();
            }
        }
    }

    private void startFlashlight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            try {
                for (String camID : mCameraManager.getCameraIdList()) {
                    try {
                        mCameraManager.setTorchMode(camID, true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Camera camera = Camera.open();
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            try {
                for (String camID : mCameraManager.getCameraIdList()) {
                    try {
                        mCameraManager.setTorchMode(camID, false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Camera camera = Camera.open();
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
