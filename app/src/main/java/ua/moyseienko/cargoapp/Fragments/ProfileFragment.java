package ua.moyseienko.cargoapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

import ua.moyseienko.cargoapp.MainActivity;
import ua.moyseienko.cargoapp.R;
import ua.moyseienko.cargoapp.WelcomeActivity;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrderByEmail;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectUser;
import ua.moyseienko.cargoapp.services.localdb.LocalDeleteUser;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class ProfileFragment extends Fragment {
    View rootView;
    String email;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> map = selectUser.selectUser(rootView.getContext());
        HashMap<String, String> user = map.get(0);
        email = user.get("email");
        System.out.println("Current user = " + user);
        TextView tvFirstname = rootView.findViewById(R.id.tvFirstNameData);
        TextView tvLastname = rootView.findViewById(R.id.tvLastNameData);
        TextView tvEmail = rootView.findViewById(R.id.tvEmailData);
        TextView tvExperience = rootView.findViewById(R.id.tvExperienceData);
        TextView tvBalance = rootView.findViewById(R.id.tvBalanceData);
        Button btnLogout = rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(onClickListener);
        HashMap<String, String> userFromDb = null;
        try {
            userFromDb = getUser(email);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tvFirstname.setText(userFromDb.get("firstname"));
        tvLastname.setText(userFromDb.get("lastname"));
        tvBalance.setText(userFromDb.get("balance"));
        tvEmail.setText(userFromDb.get("email"));
        tvExperience.setText(userFromDb.get("experience"));
        System.out.println("User from database: " + userFromDb);
        return rootView;
    }

    public class UserRunnable implements Runnable {
        volatile String result;
        public UserRunnable(String email){}
        @Override
        public void run() {
            ExternalSelectUser selectUser = new ExternalSelectUser();
            result = selectUser.selectUser(email);
            System.out.println(result);
        }
        public String getResult(){
            return result;
        }
    }

    public HashMap<String, String> getUser(String email) throws InterruptedException {
        UserRunnable userRunnable = new UserRunnable(email);
        Thread thread = new Thread(userRunnable);
        thread.start();
        thread.join();
        String result = userRunnable.getResult();
        HashMap<String, String> map = parseServerResponse(result);

        return map;
    }

    public static HashMap<String, String> parseServerResponse(String jsonResponse) {
        HashMap<String, String> resultMap = new HashMap<>();

        // Используем Gson для парсинга JSON
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonResponse);

        if (jsonElement instanceof JsonArray) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() > 0) {
                JsonArray innerArray = jsonArray.get(0).getAsJsonArray();
                if (innerArray.size() == 7) {
                    resultMap.put("id", innerArray.get(0).getAsString());
                    resultMap.put("firstname", innerArray.get(1).getAsString());
                    resultMap.put("lastname", innerArray.get(2).getAsString());
                    resultMap.put("email", innerArray.get(3).getAsString());
                    resultMap.put("password", innerArray.get(4).getAsString());
                    resultMap.put("experience", innerArray.get(5).getAsString());
                    resultMap.put("balance", innerArray.get(6).getAsString());
                }
            }
        }

        return resultMap;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLogout:
                    LocalDeleteUser localDeleteUser = new LocalDeleteUser();
                    int rowsAffected = localDeleteUser.deleteUser(rootView.getContext());
                    System.out.println("Rows affected = " + rowsAffected);
                    if(rowsAffected > 0){
                        startActivity(new Intent(rootView.getContext(), WelcomeActivity.class));
                    }
                    break;
            }
        }
    };
}