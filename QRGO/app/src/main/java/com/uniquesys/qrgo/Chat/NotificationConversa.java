package com.uniquesys.qrgo.Chat;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;

import java.util.concurrent.ExecutionException;

public class NotificationConversa {

    DatabaseReference firebaseLast;
    ValueEventListener valueEventListenerLastMensagemNot;
    int i;

    public void Ativado(final Context con, final Resources res, String user_id, String funcao){

        valueEventListenerLastMensagemNot = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

if (i > 0) {

    NotificationManager nm = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
    PendingIntent p = PendingIntent.getActivity(con, 0, new Intent(con, ChatActivity.class), 0);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(con);
    builder.setTicker("Nova Mensagem");
    builder.setContentTitle("QRGO");
    builder.setContentText("Nova Mensagem");
    builder.setSmallIcon(R.drawable.logo);
    builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo));

    Notification n = builder.build();
    n.vibrate = new long[]{150, 300, 150, 600};
    nm.notify(R.drawable.logo, n);

    try{
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone toque = RingtoneManager.getRingtone(con,som);
        toque.play();
    }catch (Exception e){

    }

}
 i++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        firebaseLast = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");

        if (funcao == "ativar") {


            firebaseLast.addValueEventListener(valueEventListenerLastMensagemNot);
        }else{
            firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
        }

    }

}
