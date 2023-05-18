package ua.moyseienko.cargoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ua.moyseienko.cargoapp.services.externaldb.ExternalCreateOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalInsertUser;
import ua.moyseienko.cargoapp.services.localdb.LocalInsertUser;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class RegisterActivity extends AppCompatActivity {
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etExperience;
    Button btnRegister;
    TextView tvLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmailAddress1);
        etPassword = (EditText) findViewById(R.id.etPassword1);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etExperience = (EditText) findViewById(R.id.etExperience);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLogIn = (TextView) findViewById(R.id.tvLogin2);

        btnRegister.setOnClickListener(onClickListener);
        tvLogIn.setOnClickListener(onClickListener);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnRegister:
                    String firstname,lastname,email,password,confirmPassword,experience;
                    firstname = etFirstName.getText().toString();
                    lastname = etLastName.getText().toString();
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    confirmPassword = etConfirmPassword.getText().toString();
                    experience = etExperience.getText().toString();
                    System.out.println(firstname+lastname+email+password+confirmPassword+experience);
                    if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || experience.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "Please field all fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            register(firstname,lastname,email,password,confirmPassword,experience);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case  R.id.tvLogin2:
                    break;
            }
        }
    };
    public void register(String firstname,String lastname,String email,String password,String confirmPassword,String experience) throws InterruptedException {
        System.out.println("Registering..");

        LocalInsertUser insertUser = new LocalInsertUser();

        long rowsAffected = insertUser.insert(RegisterActivity.this, firstname,lastname,email,password,0, Integer.parseInt(experience));
        System.out.println("Rows affected " + rowsAffected);
        //uploading new user to db

        Thread createUserThread = new Thread(){
            @Override
            public void run() {
                ExternalInsertUser externalInsertUser = new ExternalInsertUser();
                String result = externalInsertUser.createUser(firstname,lastname, email, experience, "0", password);
            }
        };
        createUserThread.start();
        createUserThread.join();


        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }
}