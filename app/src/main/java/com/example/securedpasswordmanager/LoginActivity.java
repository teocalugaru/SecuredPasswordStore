package com.example.securedpasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    private void loginUser(String username, String password){
        try {
            //Verificam mai intai amprenta si apoi credentialele introduse de utilizator - autentificare multifactor
            BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
            switch(biometricManager.canAuthenticate()){
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Toast.makeText(getApplicationContext(),"This device does not have fingerprint",Toast.LENGTH_LONG).show();
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Toast.makeText(getApplicationContext(),"Not working",Toast.LENGTH_LONG).show();

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Toast.makeText(getApplicationContext(),"No fingerprint assigned",Toast.LENGTH_LONG).show();
            }
            Executor executor = ContextCompat.getMainExecutor(LoginActivity.this);
            biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences( "SAVED_TO_SHARED", Context.MODE_PRIVATE);
                    String sharedPassw = pref.getString(username, null);
                    String decryptedPassw = null;
                    KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
                    if(sharedPassw != null)
                    {
                        try {
                            decryptedPassw = keyHelper.decrypt(getApplicationContext(), sharedPassw);
                        }
                        catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

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
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("SecuredPasswordManager")
                    .setDescription("Use your fingerprint to login with multifactor authentication").setDeviceCredentialAllowed(true).build();

            biometricPrompt.authenticate(promptInfo);


        }
        catch (Exception e) {
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
                String username = ((EditText) findViewById(R.id.log_user)).getText().toString();
                String password=((EditText) findViewById(R.id.log_passw)).getText().toString();
                loginUser(username,password);
            }
        });
    }
}