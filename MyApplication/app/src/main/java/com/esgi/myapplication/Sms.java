package com.esgi.myapplication;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sylvainvincent on 24/02/16.
 */
public class Sms extends BroadcastReceiver {

    public static final String SMS_EXTRA_NAME = "pdus";
    public static final String SMS_URI = "data/data/com.android.providers.telephony/databases/mmssms.db";
    public static final String SMS_URI2 = "content://sms/inbox";

    public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;

    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;

    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        String save = "TEST";
        String sms ;
        SmsMessage[] msgs = null;
        String messages = "";

        try {

            final Bundle bundle = intent.getExtras();

            if(bundle != null){
                final Object[] pdusObj = (Object[])bundle.get("pdus");
                ContentResolver contentResolver = context.getContentResolver();
                for (int i = 0; i < pdusObj.length;i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    String senderNum = phoneNumber;

                    Intent newintent = new Intent(context, MainActivity.class);
                    newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    newintent.putExtra("num", senderNum);
                    newintent.putExtra("message", message + " " + save);
                    context.startActivity(newintent);
                    String SMS = message + " " + save;

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();
                    putSmsToDatabase(contentResolver, currentMessage);

                }

            }

        }catch (Exception e){
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private void putSmsToDatabase( ContentResolver contentResolver, SmsMessage currentMessage )
    {
        String save = "Modification";
        // Create SMS row
        ContentValues values = new ContentValues();
        values.put( ADDRESS, currentMessage.getOriginatingAddress() );
        values.put( DATE, currentMessage.getTimestampMillis() );
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, currentMessage.getStatus() );
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
        try
        {
            String encryptedPassword = currentMessage.getMessageBody().toString() ;
            values.put(BODY, encryptedPassword + save);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        // Push row into the SMS table
        contentResolver.insert( Uri.parse("content://sms/inbox"), values );
        Log.e("SmsReceiver", "senderNum: " + values);
    }
}

