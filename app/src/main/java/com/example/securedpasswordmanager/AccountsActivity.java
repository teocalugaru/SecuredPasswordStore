package com.example.securedpasswordmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AccountsActivity extends AppCompatActivity {

    ListView listView;
    Vector<String> mUsername = new Vector<>();
    Vector<String> mPassword = new Vector<>();
    Vector<String> mComplexity = new Vector<>();
    String username;
    private void getUsers(String username) throws ParseException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException {
        SharedPreferences pref = getApplicationContext().getSharedPreferences( "SAVED_TO_SHARED", Context.MODE_PRIVATE);
        Map<String,String> allUsers= (Map<String, String>) pref.getAll();
        KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
        for(Map.Entry<String,String> entry:allUsers.entrySet()){
            if(!entry.getKey().equals("ENCRYPTED_KEY") && !entry.getKey().equals("PUBLIC_IV")) {
                mUsername.add(entry.getKey());
                if(entry.getKey().equals(username)){
                    mPassword.add(keyHelper.decrypt(getApplicationContext(), entry.getValue()));
                }
                else {
                    mPassword.add(entry.getValue());
                }
            }
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        Vector<String> rUsernames;
        Vector<String> rPasswords;
        Vector<String> rComplexity;

        MyAdapter(Context c, Vector<String> username, Vector<String> passw,Vector<String> complex) {
            super(c, R.layout.row, R.id.textView1,username);
            this.context = c;
            this.rUsernames = username;
            this.rPasswords = passw;
            this.rComplexity = complex;
        }
        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);
            TextView myComplexity = row.findViewById(R.id.textView3);

            KeyHelper keyHelper = KeyHelper.getInstance(getApplicationContext());
            try {
                String decryptedPassw;
                if(rUsernames.get(position).equals(username)) {
                    decryptedPassw = rPasswords.get(position);
                    myTitle.setText(rUsernames.get(position)+" ---LOGGED USER--- ");
                }
                else {
                    decryptedPassw = keyHelper.decrypt(getApplicationContext(), rPasswords.get(position));
                    myTitle.setText(rUsernames.get(position));
                }
                myDescription.setText(rPasswords.get(position));

                switch(PasswordChecker.checkComplexity(decryptedPassw)){
                    case 2:
                        myComplexity.setText("Strong complexity!");
                        myComplexity.setTextColor(Color.GREEN);
                        break;
                    case 1:
                        myComplexity.setText("Moderate complexity!");
                        myComplexity.setTextColor(Color.BLUE);
                        break;
                    case 0:
                        myComplexity.setText("Low complexity!");
                        myComplexity.setTextColor(Color.RED);
                        break;
                }

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }
            return row;
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("access","User is on accounts activity!");
        setContentView(R.layout.activity_accounts);
        Bundle extras = getIntent().getExtras();
        String newString= extras.getString("LOGGED_IN");
        Toast.makeText(getApplicationContext(), newString,
                Toast.LENGTH_LONG).show();
        try {
            getUsers(extras.getString("username"));
            username = extras.getString("username");
        } catch (ParseException | NoSuchPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException | BadPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());
        }
        listView = findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this, mUsername, mPassword,mComplexity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}