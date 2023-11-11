package com.example.employeeondemand.Models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.employeeondemand.Activities.IncomingCall;
import com.example.employeeondemand.Activities.MainActivity;
import com.example.employeeondemand.Activities.NotificationActivity;
import com.example.employeeondemand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.jetbrains.annotations.NotNull;

public class MyFirebaseService extends FirebaseMessagingService {
    String messageText;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        sendNotification(notification.getTitle(), notification.getBody());
    }

    private void sendNotification(String title, String messageBody) {
        NotificationCompat.Builder notificationBuilder;
        String channelId = "1";
        if (title.equals("AC") || title.equals("VC")){
            Intent intent = new Intent(this, IncomingCall.class);
            intent.putExtra("userId", messageBody);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            FirebaseDatabase.getInstance().getReference().child("Users").child(messageBody).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Userdata userdata = snapshot.getValue(Userdata.class);
                    messageText = userdata.getUsername();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.empicon)
                            .setContentTitle(messageText)
                            .setContentText("Incoming Call")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
        }
        else {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.empicon)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
