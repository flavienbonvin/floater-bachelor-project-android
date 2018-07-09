package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryAdapter;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryItem;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.CommunicationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.*;

public class ActivitySendMessage extends AppCompatActivity {

    private ArrayList<SpinnerCategoryItem> mCategoryList;

    private EditText etMessageTitle;
    private EditText etMessageDesc;

    private Spinner mSpinner;

    private Message mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initView();

        //Creation of the new message object that will be sent
        mMessage = new Message();

        mMessage.setCreatorAppId(VALUE_PREF_APPID);
        mMessage.setSenderAppID(VALUE_PREF_APPID);
        mMessage.setCreatorUserName(VALUE_PREF_USERNAME);
    }

    private void initView() {

        this.setTitle("New Message");

        initList();

        //Init elements on view
        mSpinner = findViewById(R.id.spinner_categories);
        SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, mCategoryList);
        mSpinner.setAdapter(categoryAdapter);

        etMessageTitle = findViewById(R.id.tv_new_message_title);
        etMessageDesc = findViewById(R.id.tv_new_message_desc);

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


                //Check that all the mandatory fields are filled
                if (checkInputs()) {

                    mMessage.setTitle(etMessageTitle.getText().toString().trim());
                    mMessage.setDescription(etMessageDesc.getText().toString().trim());
                    mMessage.setCategory(((SpinnerCategoryItem) mSpinner.getSelectedItem()).getCategoryName());

                    //TODO handle the case where there is no peer around the user
                    //TODO Fix closing, close when send to 0 peers
                    //There are no peer connected to the device
                    if (CommunicationManagement.sendDataAsByte(mMessage.toString())){

                        Log.i(TAG, "onOptionsItemSelected:  NO PEERS CONNECTED, HANDLE");
                    }
                    else {

                        int nbrPeers = NearbyManagement.getEstablishedConnections().size();

                        if(nbrPeers == 1){
                            Toast.makeText(
                                    this,
                                    String.format("Message sent to %s nearby peer", nbrPeers),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(
                                    this,
                                    String.format("Message sent to %s nearby peers", nbrPeers),
                                    Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkInputs() {

        if (etMessageTitle.getText().toString().trim().isEmpty()) {
            etMessageTitle.setError("You have to give a title");
            return false;
        }
        //TODO: Looks strange, to fix
        if (etMessageDesc.getText().toString().trim().isEmpty()) {
            etMessageDesc.setError("You have to give a description");
            return false;
        }
        return true;
    }

    private int getColorForCategory(String color) {
        if (this.getString(R.string.category_victims).equals(color)) {
            return this.getResources().getColor(R.color.category_victim);
        } else if (this.getString(R.string.category_danger).equals(color)) {
            return this.getResources().getColor(R.color.category_danger);
        } else if (this.getString(R.string.category_resources).equals(color)) {
            return this.getResources().getColor(R.color.category_resource);
        } else if (this.getString(R.string.category_caretaker).equals(color)) {
            return this.getResources().getColor(R.color.category_caretaker);
        }
        return 0;
    }

    private void initList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_victims), R.drawable.ic__category_victim, R.color.category_victim));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_danger), R.drawable.ic__category_danger, R.color.category_danger));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_resources), R.drawable.ic__category_resource, R.color.category_resource));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_caretaker), R.drawable.ic__category_caretaker, R.color.category_caretaker));
    }
}
