package com.Guruprasad.innovativesolutions.Activities.ui.home.RealTimeDevelopement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.Guruprasad.innovativesolutions.Activities.ui.home.Contact.ContactActivity;
import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.Model.RealtimeDevelopmentModel;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityRealTimeDevelopmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                check(topic,requirements,phoneno,email,whatsappno,budget);
                firebasedatabase(id ,topic,requirements,phoneno,email,whatsappno,budget,purpose);

            }
        });
    }

    private void check(String topic , String requirement , String phoneno , String email , String whatsapp , String budget) {
        if (topic.isEmpty())
        {
            binding.etTopic.setError("Topic Required");
            return;
        }
        if (requirement.isEmpty())
        {
            binding.etRequirement.setError("Requirements Required");
            return;
        }
        if (phoneno.isEmpty())
        {
            binding.etPhoneno.setError("Phone No required");
            return;
        }
        if (email.isEmpty())
        {
            binding.etEmail.setError("Email required");
            return;
        }
        if (whatsapp.isEmpty())
        {
            binding.etWhatsapp.setError("Whatsapp no required");
            return;
        }
        if (budget.isEmpty())
        {
            binding.etBudget.setError("Budget Required");
            return;
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
                        pd.dismiss();
                        Constants.success(RealTimeDevelopment.this,"Your project has been recorded");
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