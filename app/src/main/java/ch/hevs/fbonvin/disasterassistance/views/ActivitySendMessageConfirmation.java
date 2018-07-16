package ch.hevs.fbonvin.disasterassistance.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.MainActivity;
import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryItem;
import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.AlertDialogBuilder;
import ch.hevs.fbonvin.disasterassistance.utils.CommunicationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.*;


public class ActivitySendMessageConfirmation extends AppCompatActivity {


    private Message mMessage;
    private TextView mTVTitle;
    private TextView mTVCategory;
    private TextView mTVDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_confirmation);

        mMessage = (Message) getIntent().getSerializableExtra("message");

        initView();
    }


    private void initView(){

        mTVTitle = findViewById(R.id.tv_confirm_message_title);
        mTVCategory = findViewById(R.id.tv_confirm_message_category);
        mTVDesc = findViewById(R.id.tv_confirm_message_desc);

        mTVDesc.setMovementMethod(new ScrollingMovementMethod());

        mTVTitle.setText(mMessage.getTitle());
        mTVCategory.setText(mMessage.getCategory());
        mTVCategory.setTextColor(getColorForCategory());
        mTVDesc.setText(mMessage.getDescription());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_new_message, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            //Handle the message sent to other peers
            case R.id.send_message:

                //Check if there are peers connected to the device, if not the message is put on queue
                if (ESTABLISHED_ENDPOINTS.size() > 0){

                    //Add all the connected peers to the send ArrayList
                    for(Endpoint e : ESTABLISHED_ENDPOINTS.values()){
                        mMessage.getMessageSentTo().add(e.getName());
                    }
                    //Add itself to the send ArrayList, because send message saved in MESSAGE_SENT
                    mMessage.getMessageSentTo().add(VALUE_PREF_APPID);

                    CommunicationManagement.sendMessageListRecipient(
                            new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()),
                            mMessage);

                    //Add the message to the history of message sent
                    FRAG_MESSAGES_SENT.updateDisplay(mMessage);


                    Log.i(TAG, "onOptionsItemSelected: " + mMessage.toString());

                    int nbrPeers = ESTABLISHED_ENDPOINTS.size();

                    if(nbrPeers == 1){
                        Toast.makeText(
                                this,
                                getString(R.string.message_send_to_peer, nbrPeers),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(
                                this,
                                getString(R.string.message_send_to_peers, nbrPeers),
                                Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Log.i(TAG, "onOptionsItemSelected:  no peer connected, the message is saved");

                    //Message put in queue and will be sent once a device connect
                    MESSAGE_QUEUE.add(mMessage);

                    AlertDialogBuilder.showAlertDialogPositive(this,
                            getString(R.string.no_connected_peers),
                            getString(R.string.message_no_connected_peers),
                            getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(ActivitySendMessageConfirmation.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private int getColorForCategory(){
        final String catVictim = getResources().getString(R.string.category_Victims);
        final String catDanger = getResources().getString(R.string.category_Danger);
        final String catResource = getResources().getString(R.string.category_Resources);
        final String cateCaretaker = getResources().getString(R.string.category_Caretaker);


        if(mMessage.getCategory().equals(catVictim)){
            return this.getResources().getColor(R.color.category_victim);
        }else if(mMessage.getCategory().equals(catDanger)){
            return this.getResources().getColor(R.color.category_danger);
        }else if(mMessage.getCategory().equals(catResource)){
            return this.getResources().getColor(R.color.category_resource);
        }else if(mMessage.getCategory().equals(cateCaretaker)){
            return this.getResources().getColor(R.color.category_caretaker);
        } else {
            return this.getResources().getColor(R.color.errorColor);
        }
    }
}
