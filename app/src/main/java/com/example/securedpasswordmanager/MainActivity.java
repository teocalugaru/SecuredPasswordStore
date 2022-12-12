package com.example.securedpasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText username; EditText password; Button registerBtn;
        username = (EditText)findViewById(R.id.editUsername);
        password = (EditText)findViewById(R.id.editPassword);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String encryptedPassword = keyHelper.encrypt(getApplicationContext(),password.getText().toString());
                    SharedPreferences pref = getApplicationContext().getSharedPreferences( "SAVED_TO_SHARED", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString(username.getText().toString(), encryptedPassword);
                    edit.apply();
                    Log.e("SECURED_PASSWD","Parola criptata:"+encryptedPassword);
                    String sharedPassw = pref.getString(username.getText().toString(), null);
                    Log.e("SECURED_PASSWD","Parola din share:"+encryptedPassword);

                    String decryptedPassw = keyHelper.decrypt(getApplicationContext(),sharedPassw);
                    Log.e("SECURED_PASSWD","Parola decriptata:"+decryptedPassw);

                } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}