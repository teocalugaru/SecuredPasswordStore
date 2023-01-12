package com.example.securedpasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPasswordActivity extends AppCompatActivity {

    private boolean validateEmailAddress(String email){
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
    }

    private void sendEmail(String receiverEmail,String messageBody) throws MessagingException {
        String senderEmail = "calugaruteodor@yahoo.com";
        String passwordSenderEmail = "password123";
        String host = "smtp.mail.yahoo.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail,passwordSenderEmail);
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(receiverEmail)});
        mimeMessage.setSubject("Recovery Account");
        mimeMessage.setText(messageBody);


        //Facem trimiterea pe un thread separat
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button sendBtn = (Button)findViewById(R.id.sendBTN);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.emailAdd)).getText().toString();
                if(!validateEmailAddress(email)) {
                    Toast.makeText(getApplicationContext(), "The e-mail address is not correct!", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        Bundle extras = getIntent().getExtras();
                        String username = extras.getString("username");
                        String messageBody  = "Your password account is: "+email;
                        sendEmail(email,messageBody);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "The password was sent on your e-mail account!", Toast.LENGTH_LONG).show();
                    final Intent intent = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}