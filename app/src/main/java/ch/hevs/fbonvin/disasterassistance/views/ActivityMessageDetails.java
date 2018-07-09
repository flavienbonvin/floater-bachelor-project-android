package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;

public class ActivityMessageDetails extends AppCompatActivity {

    private Message mMessage;

    private ImageView ivIcon;
    private TextView tvMessageCategory;
    private TextView tvMessageSender;
    private TextView tvMessageDate;
    private TextView tvMessageTitle;
    private TextView tvMessageDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        mMessage = (Message) getIntent().getSerializableExtra("message");

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
    }


    /**
     * Set text of elements presents in the view
     */
    private void setText() {
        ivIcon.setColorFilter(getColorForCategory());

        tvMessageCategory.setText(mMessage.getCategory());
        tvMessageCategory.setTextColor(getColorForCategory());

        String sender = getString(R.string.send_by_message_details, mMessage.getCreatorUserName());
        tvMessageSender.setText(sender);

        Long dateLong = Long.parseLong(mMessage.getDateCreatedMillis());

        String dateString = DateUtils.getRelativeTimeSpanString(dateLong).toString();

        String dateDisplay = getString(R.string.send_message_details, dateString);
        tvMessageDate.setText(dateDisplay);

        tvMessageTitle.setText(mMessage.getTitle());
        tvMessageDesc.setText(mMessage.getDescription());
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
