package com.Guruprasad.innovativesolutions.Activities.ui.home.RealTimeDevelopement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.Guruprasad.innovativesolutions.Activities.ui.home.Contact.ContactActivity;
import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.Model.RealtimeDevelopmentModel;
import com.Guruprasad.innovativesolutions.Model.RegisterModel;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityRealTimeDevelopmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealTimeDevelopment extends AppCompatActivity {

    private ActivityRealTimeDevelopmentBinding binding;
    FirebaseDatabase database ;
    DatabaseReference reference ;

    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRealTimeDevelopmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Real Time Development");
        auth = FirebaseAuth.getInstance();
        String id = auth.getCurrentUser().getUid().toString();


        ProgressDialog pd = Constants.progress_dialog(RealTimeDevelopment.this , "Please Wait","Fetching Details");
        pd.show();

        database.getReference("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterModel model = snapshot.getValue(RegisterModel.class);
                if (model!=null)
                {
                    pd.dismiss();
                    binding.etPhoneno.setText(model.getPhoneNo());
                    binding.etEmail.setText(model.getEmail());
                }
                else
                {
                    pd.dismiss();
                    Constants.error(RealTimeDevelopment.this,"Does not have user data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Constants.error(RealTimeDevelopment.this,"Error : "+error.getMessage());
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RealTimeDevelopment.this,R.array.purpose, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        binding.purpose.setAdapter(adapter);

        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealTimeDevelopment.this,ContactActivity.class));
            }
        });



        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = binding.etTopic.getText().toString();
                String requirements = binding.etRequirement.getText().toString();
                String phoneno = binding.etPhoneno.getText().toString();
                String email = binding.etEmail.getText().toString();
                String whatsappno = binding.etWhatsapp.getText().toString();
                String budget = binding.etBudget.getText().toString();
                String purpose = binding.purpose.getSelectedItem().toString();

              boolean check= check(topic,requirements,phoneno,email,whatsappno,budget);
              if (check)
              {
                firebasedatabase(id ,topic,requirements,phoneno,email,whatsappno,budget,purpose);
              }
              else
              {
                  new MaterialAlertDialogBuilder(RealTimeDevelopment.this, R.style.RoundShapeTheme)
                          .setTitle("Error")
                          .setMessage("Please fill all the fields")
                          .setIcon(R.drawable.logo_1)
                          .setCancelable(false)
                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.dismiss();
                              }
                          }).show();
              }


            }
        });
    }

//    private void check(String topic , String requirement , String phoneno , String email , String whatsapp , String budget) {
//        if (topic.isEmpty())
//        {
//            binding.etTopic.setError("Topic Required");
//            Constants.error(RealTimeDevelopment.this,"Topic Required");
//            return;
//        }
//
//        if (topic.length()<10)
//        {
//            Constants.error(RealTimeDevelopment.this,"Topic Length is Short");
//            return;
//        }
//        if (requirement.length()<10)
//        {
//            Constants.error(RealTimeDevelopment.this,"Requirements are too short please Enter the more information");
//            return;
//        }
//
//        if (requirement.isEmpty())
//        {
//            binding.etRequirement.setError("Requirements Required");
//            return;
//        }
//        if (phoneno.isEmpty())
//        {
//            binding.etPhoneno.setError("Phone No required");
//            return;
//        }
//        if (email.isEmpty())
//        {
//            binding.etEmail.setError("Email required");
//            return;
//        }
//        if (whatsapp.isEmpty())
//        {
//            binding.etWhatsapp.setError("Whatsapp no required");
//            return;
//        }
//        if (budget.isEmpty())
//        {
//            binding.etBudget.setError("Budget Required");
//            return;
//        }
//    }


    private boolean check(String topic , String requirement , String phoneno , String email , String whatsapp , String budget) {
        if (topic.isEmpty() || topic.length() < 10 || requirement.length() < 10 || requirement.isEmpty() || phoneno.isEmpty() || email.isEmpty() || whatsapp.isEmpty() || budget.isEmpty()) {
            return false;
        } else{
            return true;
        }
    }

    private void firebasedatabase(String id ,String topic , String requirement , String phoneno , String email , String whatsapp , String budget , String purpose ) {

        ProgressDialog pd = Constants.progress_dialog(RealTimeDevelopment.this,"Please Wait", "Project is being recorded");
        pd.show();

        RealtimeDevelopmentModel model = new RealtimeDevelopmentModel(id,topic,requirement,phoneno,email,whatsapp,budget, purpose);
        reference.child(reference.push().getKey()).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference("User Side").child("RealtimeDevelopment").child(id).child(database.getReference().push().getKey()).setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Constants.success(RealTimeDevelopment.this,"Your project has been recorded");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Constants.error(RealTimeDevelopment.this,"Failed to record response : "+e.getMessage());
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Constants.error(RealTimeDevelopment.this,"Failed to record respose : "+e.getMessage());
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}