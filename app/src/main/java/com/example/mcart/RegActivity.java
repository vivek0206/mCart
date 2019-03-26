package com.example.mcart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {
    EditText email,pass;
    Button register;
    String Email,Pass;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        email=findViewById(R.id.txt_email);
        pass=findViewById(R.id.txt_pass);
        register=findViewById(R.id.register);
        mAuth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email=email.getText().toString().trim();
                Pass=pass.getText().toString().trim();
                Toast.makeText(RegActivity.this,"registering",Toast.LENGTH_SHORT).show();
                reg(Email,Pass);
            }
        });
    }
    public void reg(String Email,String Pass){

        if(Email.equals("")||Pass.equals(""))
        {
            Toast.makeText(RegActivity.this,"Fill the Email and Password fields",Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(Email,Pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegActivity.this, "reg success",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegActivity.this, MainActivity.class));

                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegActivity.this, "failed to register",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }
}
