package com.example.securedpasswordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Executor executor;

    private void loginUser(String username, String password){
        try {
            //Verificam mai intai amprenta si apoi credentialele introduse de utilizator - autentificare multifactor
            Log.d("debug","Verify the fingerprint and after that the username and password--multifactor authentication!");
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
                            Log.e("error",e.getMessage());
                        }

                        if(!decryptedPassw.equals(password)){
                            Toast.makeText(getApplicationContext(), "Incorrect username or password",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            String message="You are successfully logged in";
                            final Intent intent = new Intent(LoginActivity.this, AccountsActivity.class);
                            intent.putExtra("LOGGED_IN",message);
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Incorrect username or password",
                                Toast.LENGTH_LONG).show();
                        Log.d("debug","Login--Incorrect username or password!");
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
            Log.e("error",e.getMessage());
        }

    }


    private void forgotPassword(String username) {
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
                if(sharedPassw==null) {
                    Toast.makeText(getApplicationContext(), "This user does not exist!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    final Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("SecuredPasswordManager")
                .setDescription("Use your fingerprint to continue to forget password form!").setDeviceCredentialAllowed(true).build();

        biometricPrompt.authenticate(promptInfo);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn = (Button)findViewById(R.id.sendBTN);
        Log.i("access","User is on login activity!");

        //For digital fringerprint
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
        executor = ContextCompat.getMainExecutor(LoginActivity.this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.emailAdd)).getText().toString();
                String password=((EditText) findViewById(R.id.log_passw)).getText().toString();
                loginUser(username,password);
            }
        });
        TextView forgetView = (TextView)findViewById(R.id.forgotPassword);
        forgetView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                forgotPassword(((EditText) findViewById(R.id.emailAdd)).getText().toString());
            }
        });

        }

}