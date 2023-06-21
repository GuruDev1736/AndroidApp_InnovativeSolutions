package com.Guruprasad.innovativesolutions.Activities.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityVerifyOtpactivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    ActivityVerifyOtpactivityBinding binding ;
    private String verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        verification_id = intent.getStringExtra("verification_id");
        String phone_no = intent.getStringExtra("phoneno");
        binding.tvMobileNo.setText(phone_no);


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = Constants.progress_dialog(VerifyOTPActivity.this,"Please Wait","Verifying OTP...");
                String code = binding.otpView.getOTP();
                if (code.isEmpty())
                {
                    Constants.error(VerifyOTPActivity.this,"OTP Invalid");
                    return;
                }
                else
                {
                    verify(verification_id,code,pd,phone_no);
                }
            }
        });

        binding.tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(VerifyOTPActivity.this, R.style.RoundShapeTheme)
                        .setTitle("Resend OTP")
                        .setMessage("OTP will resend to following phone number "+phone_no+" Please double check the phone number")
                        .setIcon(R.drawable.logo_1)
                        .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resend_code(phone_no);
                            }
                        })
                        .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
    }


    private void verify(String id , String otp ,ProgressDialog pd , String mobile)
    {

        if (otp.isEmpty())
        {
            Constants.error(VerifyOTPActivity.this,"Please enter the OTP");
            return;
        }

        if (id!=null)
        {
                pd.show();
            PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(id,otp);
            FirebaseAuth.getInstance().signInWithCredential(authCredential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Constants.success(VerifyOTPActivity.this,"Verification Successful");
                            startActivity(new Intent(VerifyOTPActivity.this,SignupActivity.class)
                                    .putExtra("phoneno",mobile));
                            finish();
                            pd.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Constants.error(VerifyOTPActivity.this,"Invalid OTP : "+e.getMessage());
                            pd.dismiss();
                        }
                    });
        }

    }

    public void resend_code( String phone_number)
    {
        ProgressDialog pd = Constants.progress_dialog(VerifyOTPActivity.this,"Please Wait","Re-Sending OTP...");
        pd.show();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 30, TimeUnit.SECONDS, VerifyOTPActivity.this
                    , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            pd.dismiss();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                Constants.error(VerifyOTPActivity.this,"Failed to resend OTP : "+e.getMessage());
                                pd.dismiss();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            pd.dismiss();
                            verification_dailog(s , phone_number);

                        }
            });
    }

    public void resend_verification(String otp , String verification_id ,ProgressDialog pd , String phone_number)
    {
        if (verification_id!=null)
        {
            pd.show();
            PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(verification_id,otp);
            FirebaseAuth.getInstance().signInWithCredential(authCredential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Constants.success(VerifyOTPActivity.this,"Verification Successful");
                            startActivity(new Intent(VerifyOTPActivity.this,SignupActivity.class)
                                    .putExtra("phoneno",phone_number));
                            finish();
                            pd.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Constants.error(VerifyOTPActivity.this,"Invalid OTP : "+e.getMessage());
                            pd.dismiss();
                        }
                    });
        }
    }


    public void verification_dailog( String verification_id , String phoneno)
    {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(VerifyOTPActivity.this , R.style.RoundShapeTheme);
        alertDialog.setTitle("OTP");
        alertDialog.setMessage("Enter new OTP which has been send now");
        final EditText input = new EditText(VerifyOTPActivity.this);
        alertDialog.setView(input);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.logo_1);

        alertDialog.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog progressDialog = Constants.progress_dialog(VerifyOTPActivity.this,"Please Wait","Verifying OTP...");
                String otp = input.getText().toString();
                if (otp.isEmpty())
                {
                    Constants.error(VerifyOTPActivity.this,"Please enter the OTP");
                    return;
                }
                else
                {
                resend_verification(otp,verification_id,progressDialog,phoneno);
                }


            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}