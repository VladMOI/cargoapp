package ua.moyseienko.cargoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class WelcomeActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(buttonClickListener);
        btnSignUp.setOnClickListener(buttonClickListener);

        if(isUserLogged()){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
    }

    public boolean isUserLogged()
    {
        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> map = selectUser.selectUser(WelcomeActivity.this);
        System.out.println("map = " + map);
        if(!map.isEmpty()){
            HashMap<String, String> user = map.get(0);
            System.out.println("user email = " + user.get("email"));
            if(!user.get("email").isEmpty()){
                System.out.println("User logged");
                return true;
            }
            return false;
        }
        return false;
    }

    public View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLogIn:
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                    break;
                case R.id.btnSignUp:
                    startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                    finish();
                    break;
            }
        }
    };
}