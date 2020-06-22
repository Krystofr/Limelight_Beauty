package com.krystofrapp.limelightbeauty.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.krystofrapp.limelightbeauty.Homepage;
import com.krystofrapp.limelightbeauty.R;
import com.krystofrapp.limelightbeauty.UserLogin;

public class AccountFragment extends AppCompatActivity {

    private AppCompatButton profile, settings, update_pwd, upgrade_account, logout_btn;
    private AppCompatImageButton toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);

        profile = findViewById(R.id.profile_btn);
        settings = findViewById(R.id.settings_btn);
        update_pwd = findViewById(R.id.update_pwd_btn);
        upgrade_account = findViewById(R.id.upgrade_acc_btn);
        logout_btn = findViewById(R.id.logout);

        toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountFragment.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSettings();
            }
        });
        update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountFragment.this, UpdatePassword.class);
                startActivity(intent);
            }
        });
        upgrade_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upgradeUser();
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });


    }

    private void userProfile() {
        Intent profileIntent = new Intent(AccountFragment.this, Profile.class);
        startActivity(profileIntent);
    }

    private void userSettings() {

    }

    private void upgradeUser() {
        Intent intent = new Intent(AccountFragment.this, UpgradeAccount.class);
        startActivity(intent);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent signOutIntent = new Intent(AccountFragment.this, UserLogin.class);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signOutIntent);
        finish();
    }
}
