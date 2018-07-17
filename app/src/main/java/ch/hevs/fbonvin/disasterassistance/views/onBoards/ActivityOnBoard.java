package ch.hevs.fbonvin.disasterassistance.views.onBoards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.SliderAdapter;

import static ch.hevs.fbonvin.disasterassistance.Constant.SLIDE_HEADING;

public class ActivityOnBoard extends AppCompatActivity {

    private ViewPager mViewPager;


    private Button mBackButton;
    private Button mNextButton;
    private Button mFinishButton;

    private int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);


        TabLayout tabLayout = findViewById(R.id.tab_dots_on_board);


        mViewPager = findViewById(R.id.view_pager_on_board);
        mBackButton = findViewById(R.id.bt_back_on_board);
        mNextButton = findViewById(R.id.bt_next_on_board);
        mFinishButton = findViewById(R.id.bt_finish_on_board);

        tabLayout.setupWithViewPager(mViewPager, true);

        SliderAdapter sliderAdapter = new SliderAdapter(this, this.getLayoutInflater());


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPage = position;

                if (position == 0){
                    mBackButton.setEnabled(false);
                    mBackButton.setVisibility(View.INVISIBLE);

                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);

                    mFinishButton.setEnabled(true);
                    mFinishButton.setVisibility(View.VISIBLE);
                } else  if (position == SLIDE_HEADING.length-1){
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);

                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                    mNextButton.setText("Finish");

                    mFinishButton.setEnabled(false);
                    mFinishButton.setVisibility(View.INVISIBLE);
                } else {
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);

                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                    mNextButton.setText("Next");

                    mFinishButton.setEnabled(true);
                    mFinishButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPage == SLIDE_HEADING.length-1)
                    finish();

                mViewPager.setCurrentItem(mPage+1);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mPage-1);
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mViewPager.setAdapter(sliderAdapter);
    }
}
