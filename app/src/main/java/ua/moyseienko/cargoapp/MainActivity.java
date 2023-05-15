package ua.moyseienko.cargoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ua.moyseienko.cargoapp.Fragments.GlobalCatalogueFragment;
import ua.moyseienko.cargoapp.Fragments.LocalOrdersFragment;
import ua.moyseienko.cargoapp.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity  {
    private GlobalCatalogueFragment globalCatalogueFragment = new GlobalCatalogueFragment();
    private LocalOrdersFragment localOrdersFragment = new LocalOrdersFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navClickListener);

    }

    public NavigationBarView.OnItemSelectedListener navClickListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.catalogue:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, globalCatalogueFragment)
                            .commit();
                    return true;

                case R.id.localorders:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, localOrdersFragment)
                            .commit();
                    return true;

                case R.id.profilepage:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, profileFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };
}