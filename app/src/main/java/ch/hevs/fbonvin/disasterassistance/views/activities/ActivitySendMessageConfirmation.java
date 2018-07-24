package ch.hevs.fbonvin.disasterassistance.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.MainActivity;
import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.CommunicationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.ESTABLISHED_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGES_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.MIN_DISTANCE_DUPLICATE;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;


public class ActivitySendMessageConfirmation extends AppCompatActivity {


    private Message mMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_confirmation);

        mMessage = (Message) getIntent().getSerializableExtra("message");

        initView();
    }


    private void initView() {

        TextView TVTitle = findViewById(R.id.tv_confirm_message_title);
        TextView TVCategory = findViewById(R.id.tv_confirm_message_category);
        TextView TVDesc = findViewById(R.id.tv_confirm_message_desc);

        TVDesc.setMovementMethod(new ScrollingMovementMethod());

        TVTitle.setText(mMessage.getTitle());
        TVCategory.setText(mMessage.getCategory());
        TVCategory.setTextColor(getColorForCategory());
        TVDesc.setText(mMessage.getDescription());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_new_message, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Handle the message sent to other peers
            case R.id.send_message:
                checkDuplicateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkDuplicateData() {

        ArrayList<Message> temp = new ArrayList<>();
        temp.addAll(MESSAGES_RECEIVED);
        temp.addAll(MESSAGE_SENT);
        temp.addAll(MESSAGE_QUEUE);

        boolean flag = false;
        Message duplicate = null;
        for (Message m : temp) {
            if (m.getDistance() < MIN_DISTANCE_DUPLICATE && m.getCategory().equals(mMessage.getCategory())) {
                flag = true;
                duplicate = m;
            }
        }

        if(flag){
            new AlertDialog.Builder(this)
                    .setTitle("Potential duplicate data")
                    .setMessage("Your message might be the same as : \nTitle: " +
                            duplicate.getTitle() + "\nDescription: " + duplicate.getDescription())
                    .setPositiveButton("This is not duplicate", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkNetworkStatus();
                        }
                    })
                    .setNegativeButton("Yes, delete it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }).create().show();
        }
        else {
            checkNetworkStatus();
        }
    }


    private void checkNetworkStatus() {
        if (ESTABLISHED_ENDPOINTS.size() != 0 && CURRENT_DEVICE_LOCATION != null) {
            sendMessage();
        } else if (ESTABLISHED_ENDPOINTS.size() == 0 && CURRENT_DEVICE_LOCATION != null) {
            addMessageInQueueForPeerLacking();
        } else if (CURRENT_DEVICE_LOCATION == null) {
            addMessageInQueueForLocationLacking();
        }
    }

    private void sendMessage() {

        //Add all the connected peers to the send ArrayList
        for (Endpoint e : ESTABLISHED_ENDPOINTS.values()) {
            mMessage.getMessageSentTo().add(e.getName());
        }

        //Add itself to the send ArrayList, because send message saved in MESSAGE_SENT
        mMessage.getMessageSentTo().add(VALUE_PREF_APPID);

        //Set the distance message distance (used for the send tab)
        mMessage.setDistance(LocationManagement.getDistance(
                mMessage.getTitle(),
                mMessage.getMessageLatitude(),
                mMessage.getMessageLongitude()));

        mMessage.updateExpirationDate();


        CommunicationManagement.sendMessageListRecipient(
                new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()),
                mMessage);

        //Add the message to the history of message sent
        FRAG_MESSAGES_SENT.updateDisplay(mMessage);

        int nbrPeers = ESTABLISHED_ENDPOINTS.size();
        if (nbrPeers == 1) {
            Toast.makeText(
                    this,
                    getString(R.string.activity_sent_message_conf_toast_single, nbrPeers),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    this,
                    getString(R.string.activity_sent_message_conf_toast_many, nbrPeers),
                    Toast.LENGTH_LONG).show();
        }


        Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void addMessageInQueueForPeerLacking() {
        Log.i(TAG, "ActivitySendMessageConfirmation addMessageInQueueForPeerLacking:  no peer connected, the message is saved");

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_no_peers))
                .setMessage(getString(R.string.dialog_no_peers_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Set the distance message distance (used for the send tab)
                        mMessage.setDistance(LocationManagement.getDistance(
                                mMessage.getTitle(),
                                mMessage.getMessageLatitude(),
                                mMessage.getMessageLongitude()));

                        //Message put in queue and will be sent once a device connect
                        MESSAGE_QUEUE.add(mMessage);


                        Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .create().show();
    }

    private void addMessageInQueueForLocationLacking() {
        Log.i(TAG, "ActivitySendMessageConfirmation addMessageInQueueForLocationLacking: no position saved, the message is saved");

        new AlertDialog.Builder(this)
                .setTitle("No location saved yet")
                .setMessage("Your device do not have a location yet, as soon as it gets one, the message will be sent")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MESSAGE_QUEUE_LOCATION.add(mMessage);

                        Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

    }


    private int getColorForCategory() {
        final String catVictim = getResources().getString(R.string.category_Victims);
        final String catDanger = getResources().getString(R.string.category_Danger);
        final String catResource = getResources().getString(R.string.category_Resources);
        final String cateCaretaker = getResources().getString(R.string.category_Caretaker);

        if (mMessage.getCategory().equals(catVictim)) {
            return this.getResources().getColor(R.color.category_victim);
        } else if (mMessage.getCategory().equals(catDanger)) {
            return this.getResources().getColor(R.color.category_danger);
        } else if (mMessage.getCategory().equals(catResource)) {
            return this.getResources().getColor(R.color.category_resource);
        } else if (mMessage.getCategory().equals(cateCaretaker)) {
            return this.getResources().getColor(R.color.category_caretaker);
        } else {
            return this.getResources().getColor(R.color.errorColor);
        }
    }
}
