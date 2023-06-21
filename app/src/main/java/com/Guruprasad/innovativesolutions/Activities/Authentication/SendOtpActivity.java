package com.Guruprasad.innovativesolutions.Activities.Authentication;

import static com.Guruprasad.innovativesolutions.Constants.error;
import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivitySendOtpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtpActivity extends AppCompatActivity {

    ActivitySendOtpBinding binding ;
    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        binding.btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.info(SendOtpActivity.this,"Please Wait , This can take a moment");
                ProgressDialog pd = Constants.progress_dialog(SendOtpActivity.this,"Please Wait","Sending OTP...");
                pd.show();
                String phoneno = "+91"+binding.etMobileNo.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneno, 60, TimeUnit.SECONDS, SendOtpActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        pd.dismiss();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        error(SendOtpActivity.this,"Failed To Send OTP : "+e.getMessage()+" Try Again Later");
                        Log.d("Guru",e.getMessage());
                        Log.d("Guru",phoneno);
                        pd.dismiss();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        pd.dismiss();
                        startActivity(new Intent(SendOtpActivity.this,VerifyOTPActivity.class)
                                .putExtra("verification_id",s)
                                .putExtra("phoneno",phoneno));
                        finish();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}