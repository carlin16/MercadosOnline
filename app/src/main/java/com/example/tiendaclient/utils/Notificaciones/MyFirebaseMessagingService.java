package com.example.tiendaclient.utils.Notificaciones;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.tiendaclient.R;
import com.example.tiendaclient.view.Login;
import com.example.tiendaclient.view.Principal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e("notificacion", "DE: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("notificacion", "Dato: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                // scheduleJob();
            } else {
                // Handle message within 10 seconds
                //  handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //("notificacion", "Titulo notificacion: " + remoteMessage.getNotification().getTitle());
            //("notificacion", "Titulo notificacion: " + remoteMessage.getNotification().getBody());
            //("notificacion", "TAG localizacion: " + remoteMessage.getNotification().getTitleLocalizationKey());
            //("notificacion", "TAG body: " + remoteMessage.getNotification().getBodyLocalizationKey());
            //("notificacion", "TAG url: " + remoteMessage.getNotification().getImageUrl());


            //   Toast.makeText(getApplicationContext(), (CharSequence) remoteMessage.getNotification().getImageUrl(),Toast.LENGTH_SHORT).show();
            Bitmap bmImg = null;

            /*
            try {
                bmImg = Ion.with(getApplicationContext())
                        .load(String.valueOf(remoteMessage.getNotification().getImageUrl()))
                        .asBitmap().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            notificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),bmImg);

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        // super.onMessageReceived(remoteMessage);
    }


    @Override
    public void onNewToken(@NonNull String Token) {

            //("actualizacion",""+Token);




        //    Global.enviar_token(Token);
        // Global.tokenaux= Token;
        super.onNewToken(Token);
    }

    private void notificacion(String Titulo,String Body,Bitmap imagen){
        NotificationManager Managernotify;

        String channelid="Municipio";

       /* NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setLargeIcon((((BitmapDrawable)getResources()
                                .getDrawable(R.drawable.perrito)).getBitmap()))
                        .setContentTitle("Mensaje de Alerta")
                        .setContentText("Se Detecto Humo")
                        .setContentInfo("4")
                        .setVibrate(new long[] {100, 250, 100, 500})
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setTicker("Alerta!");*/
        Managernotify =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Intent notificationIntent = new Intent(this,
                Principal.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        notificationIntent.putExtra("notificacion","activo");

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                0);

   /*     Intent intent;
        intent = new Intent(this, Principal.class);


       // intent.putExtras(noBundle);
        intent.putExtra("notificacion","activo");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_CANCEL_CURRENT);
*/
        //("notificacion","Notificacion recibida");
        NotificationCompat.Builder  mBuilder=new NotificationCompat.Builder(this,null);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence alerta="Municipio";
            String description="Compras Online";
            int importancia = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel mChanel = new NotificationChannel(channelid,alerta,importancia);
            mChanel.setDescription(description);
            mChanel.enableLights(true);

            mChanel.setLightColor(Color.RED);
            mChanel.enableVibration(true);
            mChanel.setVibrationPattern(new long[] {100, 200, 300, 400,500,400,300,200,400});

            Managernotify.createNotificationChannel(mChanel);
            mBuilder=new NotificationCompat.Builder(this,channelid);
        }





        mBuilder.setSmallIcon(R.drawable.ic_add)
                .setContentTitle(Titulo)
                .setContentText(Body)
                .setAutoCancel(true)
              //  .setLargeIcon(imagen)
/*
                .setLargeIcon((((BitmapDrawable)getResources()
                        .getDrawable(R.drawable.profile_icon)).getBitmap()))
                */


                .setVibrate(new long[] {100, 250, 100, 500})
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("HEY TU !");

        mBuilder.setChannelId(channelid);
       // mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentIntent(contentIntent);


        Random r = new Random(); // id random para notificacion
        int randomNo = r.nextInt(1000+1);
        Managernotify.notify(randomNo, mBuilder.build());

    }






}