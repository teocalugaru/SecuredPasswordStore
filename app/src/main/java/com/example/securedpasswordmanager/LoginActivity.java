package com.example.securedpasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private void loginUser(String username, String password){
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences( "SAVED_TO_SHARED", Context.MODE_PRIVATE);
            String sharedPassw = pref.getString(username, null);
            String decryptedPassw = null;
            KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
            if(sharedPassw != null)
            {
                decryptedPassw = keyHelper.decrypt(getApplicationContext(), sharedPassw);
                if(!decryptedPassw.equals(password)){
                    Toast.makeText(getApplicationContext(), "Incorrect username or password",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String message="You are successfully logged in";
                    final Intent intent = new Intent(LoginActivity.this, AccountsActivity.class);
                    intent.putExtra("LOGGED_IN",message);
                    startActivity(intent);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Incorrect username or password",
                        Toast.LENGTH_LONG).show();
            }


        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn = (Button)findViewById(R.id.loginBTN);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=((EditText) findViewById(R.id.log_user)).getText().toString();
                String password=((EditText) findViewById(R.id.log_passw)).getText().toString();
                loginUser(username,password);
            }
        });
    }
}