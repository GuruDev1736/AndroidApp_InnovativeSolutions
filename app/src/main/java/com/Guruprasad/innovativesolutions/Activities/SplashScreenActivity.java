package com.Guruprasad.innovativesolutions.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.Guruprasad.innovativesolutions.Activities.Authentication.LoginActivity;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivitySplashScreenBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {

               Dexter.withContext(SplashScreenActivity.this).withPermissions(
                       Manifest.permission.READ_CONTACTS,
                       Manifest.permission.READ_EXTERNAL_STORAGE,
                       Manifest.permission.WRITE_EXTERNAL_STORAGE
               ).withListener(new MultiplePermissionsListener() {
                   @Override
                   public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                       startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                       finish();
                   }

                   @Override
                   public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                   }
               }).check();
           }
       },1000);
    }
}