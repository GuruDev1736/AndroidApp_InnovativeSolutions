package com.Guruprasad.innovativesolutions.Activities.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.Guruprasad.innovativesolutions.Activities.HomeActivity;
import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityloginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityloginBinding binding ;
    FirebaseAuth auth;

    public static final String SHAREDPREF = "sharedpref";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    private String mail ;
    private String pass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();

        loadData();
        binding.etEmail.setText(mail);
        binding.etPassword.setText(pass);


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (email.isEmpty())
                {
                    binding.etEmail.setError("Email is required");
                    return;
                }
                if (password.isEmpty())
                {
                    binding.etPassword.setError("Password is required");
                    return;
                }

                savedata(email,password);

                email_verfication_login(email,password);

            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SendOtpActivity.class));
            }
        });


        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foregotpassword();
            }
        });

    }

    private void foregotpassword() {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(LoginActivity.this , R.style.RoundShapeTheme);
        alertDialog.setTitle("Forgot Password");
        alertDialog.setMessage("Enter your register email to send the password reset link ");
        final EditText input = new EditText(LoginActivity.this);
        alertDialog.setView(input);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.logo_1);
        alertDialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = input.getText().toString();
                if (email.isEmpty())
                {
                    Constants.error(LoginActivity.this,"Please Enter the email to proceed");
                    return;
                }else
                {

                ProgressDialog progressDialog = Constants.progress_dialog(LoginActivity.this,"Please Wait" , "Sending password reset link on your email...");
                progressDialog.show();
                auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Constants.success(LoginActivity.this,"Reset link has send successfully on your email");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Constants.error(LoginActivity.this,"Failed to send : "+e.getMessage());
                            }
                        });
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();



    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF,MODE_PRIVATE);
        mail = sharedPreferences.getString(EMAIL,"");
        pass = sharedPreferences.getString(PASSWORD,"");
    }

    public void savedata(String email , String password)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.putString(PASSWORD,password);
        editor.apply();
    }

    private void email_verfication_login(String email , String password)
    {
        ProgressDialog pd = Constants.progress_dialog(LoginActivity.this,"Please Wait","Signing user....");
        pd.show();
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               if (auth.getCurrentUser().isEmailVerified())
               {
                   pd.dismiss();
                   Constants.success(LoginActivity.this,"Login Successful");
                   startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                   finish();
               }
               else
               {
                   pd.dismiss();
                   new MaterialAlertDialogBuilder(LoginActivity.this,R.style.RoundShapeTheme)
                           .setTitle("Verification")
                           .setMessage("Hello user ! Your email "+email+" is not verified. Click on SEND , To get verification link ")
                           .setCancelable(false)
                           .setIcon(R.drawable.logo_1)
                           .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                                   ProgressDialog pd = Constants.progress_dialog(LoginActivity.this,"Please Wait","Sending email verification link");
                                   pd.show();
                                   auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                            pd.dismiss();
                                            Constants.success(LoginActivity.this,"Email has send successfully, Please check your inbox and login again");
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           pd.dismiss();
                                           Constants.error(LoginActivity.this,"Failed to send : "+e.getMessage());
                                       }
                                   });
                               }
                           })
                           .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   Constants.error(LoginActivity.this,"Sorry you can't login because your email is not verified");
                               }
                           }).show();
               }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Constants.error(LoginActivity.this,"Failed to login : "+e.getMessage());
                    }
                });
    }
}