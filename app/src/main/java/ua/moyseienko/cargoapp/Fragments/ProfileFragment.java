package ua.moyseienko.cargoapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ua.moyseienko.cargoapp.MainActivity;
import ua.moyseienko.cargoapp.R;
import ua.moyseienko.cargoapp.WelcomeActivity;
import ua.moyseienko.cargoapp.services.localdb.LocalDeleteUser;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class ProfileFragment extends Fragment {
    View rootView;

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
        System.out.println("Current user = " + user);
        TextView tvFirstname = rootView.findViewById(R.id.tvFirstNameData);
        TextView tvLastname = rootView.findViewById(R.id.tvLastNameData);
        TextView tvEmail = rootView.findViewById(R.id.tvEmailData);
        TextView tvExperience = rootView.findViewById(R.id.tvExperienceData);
        TextView tvBalance = rootView.findViewById(R.id.tvBalanceData);
        Button btnLogout = rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(onClickListener);
        tvFirstname.setText(user.get("firstname"));
        tvLastname.setText(user.get("lastname"));
        tvEmail.setText(user.get("email"));
        tvExperience.setText(user.get("experience"));
        tvBalance.setText(user.get("balance"));
        return rootView;
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