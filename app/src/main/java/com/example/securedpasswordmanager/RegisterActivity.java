package com.example.securedpasswordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.DialogInterface;
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
                Log.d("debug","Generate random password for user!");
                Toast.makeText(getApplicationContext(), resultMessage,
                        Toast.LENGTH_LONG).show();
            }
            else {
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(username, encryptedPassword);
                edit.apply();
                resultMessage="You are successfully registered in face app system";
                Log.d("debug","User successfully registered!");
                final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("REGISTERED", resultMessage);
                startActivity(intent);
                //String sharedPassw = pref.getString(username, null);
                //String decryptedPassw = keyHelper.decrypt(getApplicationContext(), sharedPassw);
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("access","User is on register activity!");
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
                    Log.d("debug","Register user: the passwords do not match");
                }
                else
                {
                    registerUser(username,password);
                }
            }
        });

        Button genPasswBtn = (Button)findViewById(R.id.genRandPasswBtn);
        genPasswBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("access","Generate random password for user!");
                PasswordGenerator pw = new PasswordGenerator();
                String generatedPassw = pw.generatePassword();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("Your random generated password is : "+generatedPassw)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText password=((EditText) findViewById(R.id.reg_passw));
                                EditText confirm=((EditText) findViewById(R.id.reg_passw2));
                                password.setText(generatedPassw);
                                confirm.setText(generatedPassw);
                                Log.d("debug","SUCCESS: Generate random password for user!");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

}