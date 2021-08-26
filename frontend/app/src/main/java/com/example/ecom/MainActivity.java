package com.example.ecom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    View loginPage, registerPage;
    EditText loginPhone, loginPassword, registerName, registerPhone, registerAddress, registerEmail, registerPassword, registerRePassword;
    FloatingActionButton loginDone, registerDone;
    ToggleButton loginOrRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginOrRegister = findViewById(R.id.loginOrRegister);

        loginPage = findViewById(R.id.loginPage);
        registerPage = findViewById(R.id.registerPage);

        loginPhone = findViewById(R.id.loginPhone);
        loginPassword = findViewById(R.id.loginPassword);
        loginDone = findViewById(R.id.loginDone);

        registerName = findViewById(R.id.registerName);
        registerPhone = findViewById(R.id.registerPhone);
        registerAddress = findViewById(R.id.registerAddress);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerRePassword = findViewById(R.id.registerRePassword);
        registerDone = findViewById(R.id.registerDone);


        loginDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin()) {
                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                    intent.putExtra("myId", loginPhone.getText().toString());
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        });

        registerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegister()) {
                    loginOrRegister.setChecked(false);
                    loginPage.setVisibility(View.VISIBLE);
                    registerPage.setVisibility(View.GONE);

                } else {

                }
            }
        });

        loginOrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginOrRegister.isChecked()) {
                    loginPage.setVisibility(View.GONE);
                    registerPage.setVisibility(View.VISIBLE);
                } else {
                    loginPage.setVisibility(View.VISIBLE);
                    registerPage.setVisibility(View.GONE);
                }
            }
        });
    }

    boolean validateLogin() {
        if (loginPhone.getText().toString().length() > 0 && loginPassword.getText().toString().length() > 0) {
            String[] field = new String[2];
            String[] data = new String[2];

            data[0] = loginPhone.getText().toString();
            data[1] = loginPassword.getText().toString();
            field[0] = "personPhone";
            field[1] = "personPassword";

            PutData putData = new PutData("http://192.168.43.115/ECOM/login.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {

                    String result = putData.getResult();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    Log.i("PutData", result);
                    if (result.equals("Login Success")) {
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "All fields Required", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    boolean validateRegister() {
        if (registerName.getText().toString().length() > 0 && registerPhone.getText().toString().length() > 0 && registerAddress.getText().toString().length() > 0 && registerEmail.getText().toString().length() > 0
                && registerPassword.getText().toString().length() > 0 && registerRePassword.getText().toString().length() > 0) {
            if (registerPassword.getText().toString().equals(registerRePassword.getText().toString())) {


                String[] field = new String[6];

                field[0] = "personId";
                field[1] = "personName";
                field[2] = "personEmail";
                field[3] = "personPhone";
                field[4] = "personPassword";
                field[5] = "personAddress";

                String[] data = new String[6];
                data[0] = registerPhone.getText().toString();
                data[1] = registerName.getText().toString();
                data[2] = registerEmail.getText().toString();
                data[3] = registerPhone.getText().toString();
                data[4] = registerPassword.getText().toString();
                data[5] = registerAddress.getText().toString();

                PutData putData = new PutData("http://192.168.43.115/ECOM/signup.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {

                        String result = putData.getResult();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Log.i("PutData", result);
                        if (result.equals("Sign Up Success")) {

                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } else {
                Toast.makeText(getApplicationContext(), "All fields Required", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

}