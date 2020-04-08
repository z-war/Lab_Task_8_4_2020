package com.example.lab_task_8_4_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth muth;
    EditText email , pass;
    Button logibtn;
    Dialog progresbar;
    FirebaseUser cur;
    private static String collection = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            muth = FirebaseAuth.getInstance();
            cur = muth.getCurrentUser();
            email = findViewById(R.id.emailET);
            pass = findViewById(R.id.passETL);
            logibtn = findViewById(R.id.loginBTN);
            progresbar =new Dialog(this);
            progresbar.setContentView(R.layout.please_wait_dialog);
            progresbar.setCancelable(false);
            logibtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLogibtn();
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public  void  setLogibtn()
    {
        try {

            if(!email.getText().toString().isEmpty()&&!pass.getText().toString().isEmpty())
            {
                if(muth!=null)
                {
                    if(muth.getCurrentUser()!=null)
                    {
                        muth.signOut();
                        Toast.makeText(this, "SignOut Successfully", Toast.LENGTH_SHORT).show();

                    }
                    progresbar.show();
                    muth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "USer LOgged In", Toast.LENGTH_SHORT).show();
                                progresbar.dismiss();
                                startActivity(new Intent(getBaseContext(),ShowUserData.class));

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progresbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Error Logiing In User"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else
                {
                    Toast.makeText(this, "Authentiction not Succesfull", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Please Enter Email And PAssword", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
