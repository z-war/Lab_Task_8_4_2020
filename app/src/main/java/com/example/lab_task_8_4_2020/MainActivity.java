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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static String collection = "Users";
    FirebaseFirestore db;
    EditText name, email, pass;
    FirebaseAuth mauth;
    Dialog progressBar;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mauth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            name = findViewById(R.id.nameET);
            email = findViewById(R.id.emailET);
            pass = findViewById(R.id.passET);
            progressBar = new Dialog(this);
            progressBar.setContentView(R.layout.please_wait_dialog);
            progressBar.setCancelable(false);
            signup = findViewById(R.id.signupBTN);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSignup();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setSignup() {
        try {
            if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                progressBar.show();
                if (mauth != null) {
                    mauth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            DocumentReference ref = db.collection(collection).document(email.getText().toString());
                            ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (!documentSnapshot.exists()) {

                                        Map<String, Object> mydatamap = new HashMap<>();
                                        mydatamap.put("user_name", name.getText().toString());
                                        mydatamap.put("pass",pass.getText().toString());
                                        db.collection(collection).document(email.getText().toString()).set(mydatamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.dismiss();
                                                Toast.makeText(MainActivity.this, "User Data Added To FireBase ", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.dismiss();
                                                Toast.makeText(MainActivity.this, "User Data Not Added", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        progressBar.dismiss();
                                        Toast.makeText(MainActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            startActivity(new Intent(getBaseContext(),ShowUserData.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.dismiss();
                            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Mauth not connected", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(this, "You Have To Fill All The INput Fields", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in SignUp" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void tologin(View v)
    {
        startActivity(new Intent(getBaseContext(),LoginActivity.class));
    }

}
