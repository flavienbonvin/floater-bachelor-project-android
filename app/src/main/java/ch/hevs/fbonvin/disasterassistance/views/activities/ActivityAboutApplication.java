package ch.hevs.fbonvin.disasterassistance.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ch.hevs.fbonvin.disasterassistance.R;

public class ActivityAboutApplication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        ImageView imHevs = findViewById(R.id.im_about_hevs);
        imHevs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hevs.ch/"));
                startActivity(intent);
            }
        });

        ImageView imGit = findViewById(R.id.im_about_git);
        imGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/flavienbonvin/bachelor_disaster_rescue"));
                startActivity(intent);
            }
        });
    }
}
