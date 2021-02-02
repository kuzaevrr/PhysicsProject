package com.alina.physicsproject.sendMail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alina.physicsproject.R;

import java.util.Properties;

import javax.mail.*;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import static android.provider.ContactsContract.Intents.Insert.EMAIL;
import static android.provider.Telephony.Carriers.PASSWORD;

public class SendMail extends AsyncTask<Void, Void, Boolean> {


    @SuppressLint("StaticFieldLeak")//хз, что за аннатация)))
    private final Context context;
    //Information to send email
    private final String email;
    private final String subject;
    private final String message;
    //ProgressDialog to show while sending email
    private ProgressDialog progressDialog;


    //Class Constructor
    public SendMail(Context context, String email, String subject, String message) {
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Отправка результата.", "Пожалуйста подождите...", false, false);
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Протоколы для gmail
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", 465); //465 2525

        final String mailF = context.getString(R.string.mail_dispatch);
        final String passwordF = context.getString(R.string.password_mail_dispatch);
        //Creating a new session
        //Authenticating the password
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailF, passwordF);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            //Setting sender address
            mm.setFrom(new InternetAddress(mailF));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }
}