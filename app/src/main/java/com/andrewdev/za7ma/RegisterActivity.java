package com.andrewdev.za7ma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andrewdev.za7ma.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    EditText textEmail , textPassword , textName ;
    ProgressBar progressBar;

    FirebaseAuth auth;
    DatabaseReference reference ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        textEmail = findViewById(R.id.email_ed_register);
        textPassword = findViewById(R.id.password_ed_register);
        progressBar = findViewById(R.id.progressBarRegister);
        textName = findViewById(R.id.name_ed_register);

        auth = FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");





    }

    public void RegisterUser(View v)
    {
        progressBar.setVisibility(View.VISIBLE);
        final String email = textEmail.getText().toString();
        final String password = textPassword.getText().toString();
        final String name = textName.getText().toString();

        if(!email.equals("")&&!password.equals("")&&password.length()>6)
        {
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //insert values inside the database
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            User u =new User();
                            u.setEmail(email);
                            u.setName(name);
                            reference.child(firebaseUser.getUid()).setValue(u)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                                Intent i =new Intent(RegisterActivity.this , GroupChatActivity.class);
                                                startActivity(i);
                                            }
                                            else
                                            {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(),"User could not be Created",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                }
            });
        }
    }

    public void gotoLogin(View view)
    {
        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);
    }
}
