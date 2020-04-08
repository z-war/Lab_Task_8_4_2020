package com.example.lab_task_8_4_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Updateinfo extends AppCompatActivity {
    private EditText name , address , cnic , phone ;
    private FirebaseAuth muth;
    private FirebaseFirestore db ;
    private static String colname = "Users";
    private Button btn ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        try{
            name = findViewById(R.id.user_nameET);
            address = findViewById(R.id.addressET);
            cnic = findViewById(R.id.cnicET);
            phone = findViewById(R.id.phoneET);
            db = FirebaseFirestore.getInstance();
            muth = FirebaseAuth.getInstance();
            btn = findViewById(R.id.updatebtn);        
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadbtn();
                }
            });
            
        }catch (Exception e)
        {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void uploadbtn()
    {
        try {
            FirebaseUser user = muth.getCurrentUser();
            String curEmail = user.getEmail();
            DocumentReference ref = db.collection(colname).document(curEmail);


            if(!name.getText().toString().isEmpty()&&!address.getText().toString().isEmpty()&&!cnic.getText().toString().isEmpty()&&!phone.getText().toString().isEmpty())
            {
                Map<String , Object> mydatamap = new HashMap<>();
                mydatamap.put("user_name" , name.getText().toString());
                mydatamap.put("phone" , phone.getText().toString());
                mydatamap.put("cninc",cnic.getText().toString());
                mydatamap.put("address",address.getText().toString());
                ref.update(mydatamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Updateinfo.this, "User Updated Succesfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Updateinfo.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else{
                Toast.makeText(this, "Please Fill All the Fields ", Toast.LENGTH_SHORT).show();
            }
            
        }catch (Exception e)
        {
            
        }
    }
        
}
