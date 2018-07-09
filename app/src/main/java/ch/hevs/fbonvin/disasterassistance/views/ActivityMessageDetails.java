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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        mMessage = (Message) getIntent().getSerializableExtra("message");

        ImageView ivIcon = findViewById(R.id.im_icon_detail_message);

        TextView tvMessageCategory = findViewById(R.id.tv_message_detail_category);
        TextView tvMessageSender = findViewById(R.id.tv_message_details_sender);
        TextView tvMessageDate = findViewById(R.id.tv_message_details_date);
        TextView tvMessageTitle = findViewById(R.id.tv_message_detail_title);
        TextView tvMessageDesc = findViewById(R.id.tv_message_detail_desc);

        ivIcon.setColorFilter(getColorForCategory());

        tvMessageCategory.setText(mMessage.getCategory());
        tvMessageCategory.setTextColor(getColorForCategory());

        String sender = "Send by: " + mMessage.getCreatorUserName();
        tvMessageSender.setText(sender);

        Long dateLong = Long.parseLong(mMessage.getDateCreatedMilis());

        String dateString = DateUtils.getRelativeTimeSpanString(dateLong).toString();

        String dateDisplay = "Send " + dateString;
        tvMessageDate.setText(dateDisplay);

        tvMessageTitle.setText(mMessage.getTitle());
        tvMessageDesc.setText(mMessage.getDescription());
    }


    private int getColorForCategory() {
        if (this.getString(R.string.category_victims).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_victim);
        } else if (this.getString(R.string.category_danger).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_danger);
        } else if (this.getString(R.string.category_resources).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_resource);
        } else if (this.getString(R.string.category_caretaker).equals(mMessage.getCategory())) {
            return this.getResources().getColor(R.color.category_caretaker);
        }
        return 0;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }
}
