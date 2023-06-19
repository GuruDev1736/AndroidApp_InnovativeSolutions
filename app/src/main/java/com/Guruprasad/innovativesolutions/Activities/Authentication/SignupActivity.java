package com.Guruprasad.innovativesolutions.Activities.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.Model.RegisterModel;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding ;
    FirebaseAuth auth;
    FirebaseDatabase database ;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");


        Intent intent = getIntent();
        String mobile = intent.getStringExtra("phoneno");
        binding.etPhoneno.setText(mobile);

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname  = binding.etFullname.getText().toString();
                String phoneno = binding.etPhoneno.getText().toString();
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                String confirmpassword  = binding.etConfirmPassword.getText().toString();
                String address  = binding.etAddress.getText().toString();

                if (fullname.isEmpty())
                {
                    binding.etFullname.setError("Full name is required");
                    return;
                }
                if (phoneno.isEmpty())
                {
                    binding.etPhoneno.setError("Phone no is required");
                    return;
                }
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
                if (password.length()<8)
                {
                    Constants.error(SignupActivity.this,"Password must be gerater than 8 characters");
                    return;
                }
                if (confirmpassword.isEmpty())
                {
                    binding.etConfirmPassword.setError("Please Re-type the password for confirmation");
                    return;
                }
                if (!confirmpassword.equals(password))
                {
                    Constants.error(SignupActivity.this,"Password does not match recheck the password ");
                    return;
                }
                if (address.isEmpty())
                {
                    binding.etAddress.setError("Address is required");
                    return;
                }
                if (address.length()<10)
                {
                    Constants.error(SignupActivity.this,"Address must be greater than 10 characters");
                    return;
                }


                create_account(fullname,phoneno,address,email,password);

            }
        });


    }

    private void create_account(String fullname , String phoneno , String address ,String email , String password)
    {
        ProgressDialog pd = Constants.progress_dialog(SignupActivity.this,"Please Wait","Creating New User Account...");
        pd.show();
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            RegisterModel model = new RegisterModel(auth.getCurrentUser().getUid(),
                                    fullname,phoneno,email,password,address);
                            reference.child(auth.getCurrentUser().getUid()).setValue(model)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pd.dismiss();
                                            new MaterialAlertDialogBuilder(SignupActivity.this,R.style.RoundShapeTheme)
                                                    .setTitle("Note")
                                                    .setMessage("User account has registered successfully , We will send  Email verification link on your registered email address " +
                                                            "please verify your email and login again")
                                                    .setIcon(R.drawable.logo_1)
                                                    .setCancelable(false)
                                            .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                           auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void unused) {
                                                                   Constants.success(SignupActivity.this,"Email verification link sent successfully");
                                                                   startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                                   finish();
                                                               }
                                                           }).addOnFailureListener(new OnFailureListener() {
                                                               @Override
                                                               public void onFailure(@NonNull Exception e) {
                                                                   Constants.error(SignupActivity.this,"Failed to send email verification link : "+e.getMessage());
                                                               }
                                                           });
                                                       }
                                                   }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                           Constants.error(SignupActivity.this,"Sorry you can't login , Please verify the email first");
                                                           startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                           finish();
                                                       }
                                                   }).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Constants.error(SignupActivity.this,"Failed to register user account : "+e.getMessage());
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Constants.error(SignupActivity.this,"Failed to register user account : "+e.getMessage());
                        }
                    });
    }

}