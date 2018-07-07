package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryAdapter;
import ch.hevs.fbonvin.disasterassistance.adapter.SpinnerCategoryItem;

public class ActivitySendMessage extends AppCompatActivity {

    private ArrayList<SpinnerCategoryItem> mCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initList();

        Spinner spinner = findViewById(R.id.spinnerCategories);
        SpinnerCategoryAdapter categoryAdapter = new SpinnerCategoryAdapter(this, mCategoryList);
        spinner.setAdapter(categoryAdapter);

        this.setTitle("New Message");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();

                //TODO: retrieve the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                //TODO: handle the message send here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new SpinnerCategoryItem("Victims", R.drawable.ic_category_victim, R.color.categoryVictim));
        mCategoryList.add(new SpinnerCategoryItem("Danger", R.drawable.ic_category_danger, R.color.categoryDanger));
        mCategoryList.add(new SpinnerCategoryItem("Resources", R.drawable.ic_category_resource, R.color.categoryResource));
        mCategoryList.add(new SpinnerCategoryItem("Caretaker", R.drawable.ic_category_caretaker, R.color.categoryCaregiver));
    }
}
