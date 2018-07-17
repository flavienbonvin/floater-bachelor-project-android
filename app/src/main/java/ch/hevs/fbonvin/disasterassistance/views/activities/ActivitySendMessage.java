package ch.hevs.fbonvin.disasterassistance.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryAdapter;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryItem;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_STATUS_NEW;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

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

        mMessage.setMessageStatus(MESSAGE_STATUS_NEW);
        mMessage.setCreatorAppId(VALUE_PREF_APPID);
        mMessage.setSenderAppID(VALUE_PREF_APPID);
        mMessage.setCreatorUserName(VALUE_PREF_USERNAME);
    }




    /**
     * Set text of elements presents in the view
     */
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

                    //Retrieve values of the view and put them in the message
                    mMessage.setTitle(etMessageTitle.getText().toString().trim());
                    mMessage.setDescription(etMessageDesc.getText().toString().trim());
                    mMessage.setCategory(((SpinnerCategoryItem) mSpinner.getSelectedItem()).getCategoryName());

                    LocationManagement.getDeviceLocation();
                    Double messageLatitude = CURRENT_DEVICE_LOCATION.getLatitude();
                    Double messageLongitude = CURRENT_DEVICE_LOCATION.getLongitude();
                    mMessage.setMessageLatitude(messageLatitude);
                    mMessage.setMessageLongitude(messageLongitude);

                    Intent intent = new Intent(this, ActivitySendMessageConfirmation.class);
                    intent.putExtra("message", mMessage);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check that the inputs are filled and correct
     * @return true if ok
     */
    private boolean checkInputs() {

        if (etMessageTitle.getText().toString().trim().isEmpty()) {
            etMessageTitle.setError(getString(R.string.give_title_error));
            return false;
        }
        //TODO: Looks strange, to fix
        if (etMessageDesc.getText().toString().trim().isEmpty()) {
            etMessageDesc.setError(getString(R.string.give_desc_error));
            return false;
        }
        return true;
    }


    /**
     * Initialize the list for the spinner
     */
    private void initList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_Victims), R.drawable.ic__category_victim, R.color.category_victim));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_Danger), R.drawable.ic__category_danger, R.color.category_danger));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_Resources), R.drawable.ic__category_resource, R.color.category_resource));
        mCategoryList.add(new SpinnerCategoryItem(getString(R.string.category_Caretaker), R.drawable.ic__category_caretaker, R.color.category_caretaker));
    }
}
