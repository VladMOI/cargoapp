package ua.moyseienko.cargoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;

import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectUser;
import ua.moyseienko.cargoapp.services.localdb.LocalInsertUser;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvRegister;
    EditText etEmail;
    EditText etPassword;

    String strEmail, strPasswrod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin1);
        tvRegister = (TextView) findViewById(R.id.tvSignUp);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(onClickListener);
        tvRegister.setOnClickListener(onClickListener);

    }

    public void login(String email, String passwrod)
    {
        System.out.println("Login...");

        new Thread(new Runnable() {
            public void run() {
                ExternalSelectUser externalSelectUser = new ExternalSelectUser();
                String result = externalSelectUser.selectUser(email);
                System.out.println("result " + result);
                if(result != ""){

                    Gson gson = new Gson();
                    Type type = TypeToken.get(String[][].class).getType();
                    String[][] array = gson.fromJson(result, type);
                    System.out.println(Arrays.deepToString(array));

                    LocalInsertUser localInsertUser = new LocalInsertUser();
                    String[] user = array[0];
                    localInsertUser.insert(
                            LoginActivity.this,
                            user[0],
                            user[1],
                            user[2],
                            user[3],
                            Integer.parseInt(user[5]),
                            Integer.parseInt(user[6]));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }
            }
        }).start();    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLogin1:
                    strPasswrod = etPassword.getText().toString();
                    strEmail = etEmail.getText().toString();
                    if(strPasswrod.isEmpty() || strEmail.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }else{
                        login(strEmail, strPasswrod);
                    }
                    break;
                case R.id.tvSignUp:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    break;
            }
        }
    };
}