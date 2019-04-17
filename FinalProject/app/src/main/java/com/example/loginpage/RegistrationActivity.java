package com.example.loginpage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private EditText userEmail;
    private Button buttonCreate;
    private TextView signIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try
        {
            this.getSupportActionBar().hide();

        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_registration);

        setupUI();

        firebaseAuth = FirebaseAuth.getInstance();

        //New user details will be uploaded in database
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateUserDetails()){
                    String userEmail_ = userEmail.getText().toString().trim();
                    String userPassword_ = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(userEmail_, userPassword_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "New Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

    }

    private void setupUI(){
        userName = (EditText)findViewById(R.id.rgNameID);
        userPassword = (EditText)findViewById(R.id.rgPswdID);
        userEmail = (EditText)findViewById(R.id.rgEmailID);
        buttonCreate = (Button)findViewById(R.id.rgButton);
        signIn = (TextView)findViewById(R.id.rgSignedInID);

    }

    private Boolean validateUserDetails(){
        Boolean result = false;

        String name = userName.getText().toString();
        String pswd = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || pswd.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }
}
