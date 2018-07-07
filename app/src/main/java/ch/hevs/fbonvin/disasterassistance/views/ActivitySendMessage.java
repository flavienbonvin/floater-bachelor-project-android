package ch.hevs.fbonvin.disasterassistance.views;

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

import static ch.hevs.fbonvin.disasterassistance.Constant.NEARBY_MANAGEMENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public class ActivitySendMessage extends AppCompatActivity {

    private ArrayList<SpinnerCategoryItem> mCategoryList;

    private EditText mMessageTitle;
    private EditText mMessageDesc;

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

        mMessageTitle = findViewById(R.id.tv_new_message_title);
        mMessageDesc = findViewById(R.id.tv_new_message_desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_new_message, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.send_message:

                //TODO handle the case where there is no peer around the user
                if (checkInputs()) {
                    mMessage.setTitle(mMessageTitle.getText().toString().trim());
                    mMessage.setDescription(mMessageDesc.getText().toString().trim());
                    mMessage.setCategory(((SpinnerCategoryItem) mSpinner.getSelectedItem()).getCategoryName());

                    NEARBY_MANAGEMENT.sendDataAsByte(mMessage.getString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkInputs() {

        if (mMessageTitle.getText().toString().trim().isEmpty()) {
            mMessageTitle.setError("You have to give a title");
            return false;
        }

        //TODO: Looks strange, unfocused keyboard is needed i think
        if (mMessageDesc.getText().toString().trim().isEmpty()) {
            mMessageDesc.setError("You have to give a description");
            return false;
        }
        return true;
    }

    private void initList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new SpinnerCategoryItem("Victims", R.drawable.ic__category_victim, R.color.categoryVictim));
        mCategoryList.add(new SpinnerCategoryItem("Danger", R.drawable.ic__category_danger, R.color.categoryDanger));
        mCategoryList.add(new SpinnerCategoryItem("Resources", R.drawable.ic__category_resource, R.color.categoryResource));
        mCategoryList.add(new SpinnerCategoryItem("Caretaker", R.drawable.ic__category_caretaker, R.color.categoryCaregiver));
    }
}
