package ch.hevs.fbonvin.disasterassistance.views.onBoards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ch.hevs.fbonvin.disasterassistance.R;

public class ActivityOnBoardTutorial extends AppCompatActivity {


    private Button btBack;
    private Button btNext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboard);

        ViewPager viewPager = findViewById(R.id.view_pager_on_board);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));


        setupView(viewPager);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupView(final ViewPager viewPager) {
        TabLayout tabLayout = findViewById(R.id.tab_dots_on_board);
        tabLayout.setupWithViewPager(viewPager, true);

        btBack = findViewById(R.id.bt_back_on_board);
        btNext = findViewById(R.id.bt_next_on_board);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        btBack.setEnabled(false);
                        btBack.setVisibility(View.INVISIBLE);

                        btNext.setText(getString(R.string.next));
                        btNext.setEnabled(true);
                        btNext.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btBack.setEnabled(true);
                        btBack.setVisibility(View.VISIBLE);

                        btNext.setText(getString(R.string.next));
                        btNext.setEnabled(true);
                        btNext.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        btBack.setEnabled(true);
                        btBack.setVisibility(View.VISIBLE);

                        btNext.setText(getString(R.string.finish));
                        btNext.setEnabled(true);
                        btNext.setVisibility(View.VISIBLE);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                    finish();
                }
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

    }


private class Adapter extends FragmentPagerAdapter {


    Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragOnBoardScreen1();
            case 1:
                return new FragOnBoardScreen3();
            case 2:
                return new FragOnBoardScreen4();
            default:
                return new FragOnBoardScreen2();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
}
