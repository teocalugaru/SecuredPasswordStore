package com.example.securedpasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RegisterActivity extends AppCompatActivity {


    private void registerUser(String username, String password){
        try {
            KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
            String encryptedPassword = keyHelper.encrypt(getApplicationContext(),password);
            SharedPreferences pref = getApplicationContext().getSharedPreferences( "SAVED_TO_SHARED", Context.MODE_PRIVATE);
            String sameUsr = pref.getString(username, null);
            String resultMessage;
            if(sameUsr != null)
            {
                //Utilizator existent
                resultMessage="The username already exists...";
                Toast.makeText(getApplicationContext(), resultMessage,
                        Toast.LENGTH_LONG).show();
            }
            else {
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(username, encryptedPassword);
                edit.apply();
                resultMessage="You are successfully registered in face app system";
                final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("REGISTERED", resultMessage);
                startActivity(intent);
                //String sharedPassw = pref.getString(username, null);
                //String decryptedPassw = keyHelper.decrypt(getApplicationContext(), sharedPassw);
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn = (Button)findViewById(R.id.registerBTN);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=((EditText) findViewById(R.id.reg_user)).getText().toString();
                String password=((EditText) findViewById(R.id.reg_passw)).getText().toString();
                String confirm=((EditText) findViewById(R.id.reg_passw2)).getText().toString();
                if(!password.equals(confirm)){
                    String error_message="The passwords do not match";
                    Toast.makeText(getApplicationContext(), error_message,
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    registerUser(username,password);
                }
            }
        });
    }
}