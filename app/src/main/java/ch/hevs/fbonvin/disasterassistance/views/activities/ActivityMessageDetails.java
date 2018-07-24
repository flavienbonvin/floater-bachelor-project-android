package ch.hevs.fbonvin.disasterassistance.views.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.CommunicationManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.ESTABLISHED_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGES_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGE_LIST;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_STATUS_DELETE;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.UPDATE_MESSAGE_STATUS_NON_OK;
import static ch.hevs.fbonvin.disasterassistance.Constant.UPDATE_MESSAGE_STATUS_OK;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;

public class ActivityMessageDetails extends AppCompatActivity {

    private Message mMessage;

    private int position;

    private ImageView ivIcon;
    private TextView tvMessageCategory;
    private TextView tvMessageSender;
    private TextView tvMessageDate;
    private TextView tvMessageTitle;
    private TextView tvMessageDesc;
    private TextView tvMessageDistance;
    private TextView tvMessageRecipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        mMessage = (Message) getIntent().getSerializableExtra("message");
        position = getIntent().getIntExtra("position", -1);

        initView();

        setText();
    }


    /**
     * Init elements presents in the view
     */
    private void initView() {
        ivIcon = findViewById(R.id.im_icon_detail_message);

        tvMessageCategory = findViewById(R.id.tv_message_detail_category);
        tvMessageSender = findViewById(R.id.tv_message_details_sender);
        tvMessageDate = findViewById(R.id.tv_message_details_date);
        tvMessageTitle = findViewById(R.id.tv_message_detail_title);
        tvMessageDesc = findViewById(R.id.tv_message_detail_desc);
        tvMessageDistance = findViewById(R.id.tv_message_details_distance);
        tvMessageRecipients = findViewById(R.id.tv_message_details_recipients);

        handleButtonDelete();

        Button btInfoOk = findViewById(R.id.bt_confirm_info_ok);
        btInfoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseTime();
            }
        });

        Button btInfoNok = findViewById(R.id.bt_confirm_info_nok);
        btInfoNok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseTime();
            }
        });

    }

    private void handleButtonDelete() {
        Button btDeleteMessage = findViewById(R.id.bt_delete_message);

        if(mMessage.getCreatorAppId().equals(VALUE_PREF_APPID)){
            btDeleteMessage.setVisibility(View.VISIBLE);


            btDeleteMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: Delete message, " + mMessage.getTitle());

                    //Check if there are peers connected to the device, if not the message is put on queue
                    if (ESTABLISHED_ENDPOINTS.size() > 0){

                        new AlertDialog.Builder(ActivityMessageDetails.this)
                                .setTitle(getString(R.string.activity_message_detail_dialog_delete_title))
                                .setMessage(getString(R.string.activity_message_detail_dialog_delete_message))
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CommunicationManagement.sendMessageDeletion(
                                                new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()), mMessage);


                                        FRAG_MESSAGES_SENT.removeItem(position);
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {}
                                })
                                .create().show();

                    //Handle case when no peers are connected
                    } else {
                        //Message put in queue and will be deleted once a device connect
                        new AlertDialog.Builder(ActivityMessageDetails.this)
                                .setTitle(getString(R.string.dialog_title_no_peers))
                                .setMessage(getString(R.string.dialog_no_peers_message))
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mMessage.setMessageStatus(MESSAGE_STATUS_DELETE);
                                        MESSAGE_QUEUE_DELETED.add(mMessage);

                                        FRAG_MESSAGES_SENT.removeItem(position);
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {}
                                })
                                .create().show();
                    }
                }
            });
        } else {
            btDeleteMessage.setVisibility(View.GONE);
        }
    }

    private void increaseTime(){

        Toast.makeText(this, "Thank's for your contribution", Toast.LENGTH_LONG).show();

        CommunicationManagement.sendUpdateMessage(
                new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()), mMessage, UPDATE_MESSAGE_STATUS_OK);

        for(int i = 0; i < MESSAGES_RECEIVED.size(); i++){
            Log.i(TAG, "ActivityMessageDetails onClick: received message time increase");
            if(MESSAGES_RECEIVED.get(i).getTitle().equals(mMessage.getTitle()) && MESSAGES_RECEIVED.get(i).getDateCreatedMillis().equals(mMessage.getDateCreatedMillis())){
                MESSAGES_RECEIVED.get(i).extendExpirationDate();
                FRAG_MESSAGE_LIST.updateDisplay();
            }
        }

        for(int i = 0; i < MESSAGE_SENT.size(); i++){
            Log.i(TAG, "ActivityMessageDetails onClick: sent message time increase");
            if(MESSAGE_SENT.get(i).getTitle().equals(mMessage.getTitle()) && MESSAGE_SENT.get(i).getDateCreatedMillis().equals(mMessage.getDateCreatedMillis())){
                MESSAGE_SENT.get(i).extendExpirationDate();
                FRAG_MESSAGES_SENT.updateDisplay();
            }
        }
    }

    private void decreaseTime(){

        Toast.makeText(this, "Thank's for your contribution", Toast.LENGTH_LONG).show();

        CommunicationManagement.sendUpdateMessage(
                new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()), mMessage, UPDATE_MESSAGE_STATUS_NON_OK);

        for(int i = 0; i < MESSAGES_RECEIVED.size(); i++){
            Log.i(TAG, "ActivityMessageDetails onClick: received message time decrease");
            if(MESSAGES_RECEIVED.get(i).getTitle().equals(mMessage.getTitle()) && MESSAGES_RECEIVED.get(i).getDateCreatedMillis().equals(mMessage.getDateCreatedMillis())){
                MESSAGES_RECEIVED.get(i).decreaseExpirationDate();
                FRAG_MESSAGE_LIST.updateDisplay();
            }
        }

        for(int i = 0; i < MESSAGE_SENT.size(); i++){
            Log.i(TAG, "ActivityMessageDetails onClick: sent message time decrease");
            if(MESSAGE_SENT.get(i).getTitle().equals(mMessage.getTitle()) && MESSAGE_SENT.get(i).getDateCreatedMillis().equals(mMessage.getDateCreatedMillis())){
                MESSAGE_SENT.get(i).decreaseExpirationDate();
                FRAG_MESSAGES_SENT.updateDisplay();
            }
        }
    }

    /**
     * Set text of elements presents in the view
     */
    private void setText() {
        ivIcon.setColorFilter(getColorForCategory());

        tvMessageCategory.setText(mMessage.getCategory());
        tvMessageCategory.setTextColor(getColorForCategory());

        String sender = getString(R.string.activity_message_detail_send_by, mMessage.getCreatorUserName());
        tvMessageSender.setText(sender);

        Long dateLong = Long.parseLong(mMessage.getDateCreatedMillis());

        String dateString = DateUtils.getRelativeTimeSpanString(dateLong).toString();

        String dateDisplay = getString(R.string.activity_message_detail_send_time, dateString);
        tvMessageDate.setText(dateDisplay);

        tvMessageTitle.setText(mMessage.getTitle());
        tvMessageDesc.setText(mMessage.getDescription());

        String distance = getString(R.string.activity_message_detail_distance, String.valueOf(mMessage.getDistance()));
        tvMessageDistance.setText(distance);

        String recipient = getString(R.string.activity_message_detail_recipient, String.valueOf(mMessage.retrieveMessageRecipient()));
        tvMessageRecipients.setText(recipient);
    }


    /**
     * Return the integer of the color corresponding to the category of the message
     * @return integer of the color stored int R.Color
     */
    private int getColorForCategory() {
        if (this.getString(R.string.category_Victims).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_victim);
        } else if (this.getString(R.string.category_Danger).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_danger);
        } else if (this.getString(R.string.category_Resources).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_resource);
        } else if (this.getString(R.string.category_Caretaker).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_caretaker);
        }
        return 0;
    }


    /**
     * Make the back button of the action bar behave as the hardware button
     * @param item menu item pressed
     * @return true if ok, false if MenuItem not handled by method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }
}
