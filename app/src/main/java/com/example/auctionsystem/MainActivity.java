package com.example.auctionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText etUserName, etPassword;
    private Button btnLogin;
    private Switch active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("login").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = etUserName.getText().toString();
                        String password = etPassword.getText().toString();

                        if (snapshot.child(userName).exists()){
                            if (snapshot.child(userName).child("password").getValue(String.class).equals(password)){
                                if (active.isChecked()){
                                    if (snapshot.child(userName).child("as").getValue(String.class).equals("admin")){
                                        preference.setDataLogin(MainActivity.this, true);
                                        preference.setDataAs(MainActivity.this, "admin");
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                    } else if (snapshot.child(userName).child("as").getValue(String.class).equals("user")) {
                                        preference.setDataLogin(MainActivity.this, true);
                                        preference.setDataAs(MainActivity.this, "user");
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                    }
                                } else {
                                    if (snapshot.child(userName).child("as").getValue(String.class).equals("admin")){
                                        preference.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                                    } else if (snapshot.child(userName).child("as").getValue(String.class).equals("user")) {
                                        preference.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void initialize() {
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        active = findViewById(R.id.switchId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preference.getDataLogin(this)) {
            if (preference.getDataAs(this).equals("admin")) {
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();
            }
        }
    }
}