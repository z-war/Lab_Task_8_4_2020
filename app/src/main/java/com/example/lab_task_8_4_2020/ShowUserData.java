package com.example.lab_task_8_4_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowUserData extends AppCompatActivity {
    TextView name, email, add, cnic;
    FirebaseUser user;
    FirebaseAuth muath;
    FirebaseFirestore db;
    private static String coll = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_data);
        try {
            name = findViewById(R.id.usernameTV);
            email = findViewById(R.id.useremailTV);
            add = findViewById(R.id.useraddressTV);
            cnic = findViewById(R.id.usercnicTV);
            muath = FirebaseAuth.getInstance();
            user = muath.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            final String curremail = user.getEmail();
            final DocumentReference doc = db.collection(coll).document(curremail);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
if(!documentSnapshot.exists())
{
    Toast.makeText(ShowUserData.this, "Data Not Returned", Toast.LENGTH_SHORT).show();
}else
{
name.setText(documentSnapshot.get("user_name").toString());
email.setText(documentSnapshot.getId());
}
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void toupDate(View v)
    {
        startActivity(new Intent(getBaseContext(),Updateinfo.class));
    }
}
