package ch.hevs.fbonvin.disasterassistance.views.onBoards;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class ActivityOnBoard extends AppCompatActivity {


    private Button btBack;
    private Button btNext;

    private String text = "";


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

                        btNext.setText("Next");
                        btNext.setEnabled(true);
                        btNext.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btBack.setEnabled(true);
                        btBack.setVisibility(View.VISIBLE);

                        if(MandatoryPermissionsHandling.hasPermission(ActivityOnBoard.this, MANDATORY_PERMISSION)){
                            btNext.setText("Next");
                            btNext.setEnabled(true);
                            btNext.setVisibility(View.VISIBLE);
                        } else {
                            btNext.setText("Next");
                            btNext.setEnabled(false);
                            btNext.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case 2:
                        btBack.setEnabled(true);
                        btBack.setVisibility(View.VISIBLE);

                        btNext.setText("Finish");
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

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
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

                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1){

                    String username = PreferencesManagement.
                            getDefaultStringPref(
                                    ActivityOnBoard.this,
                                    getString(R.string.key_pref_user_name),
                                    PREF_NOT_SET);


                    if(!username.equals(PREF_NOT_SET)){
                        Log.i(TAG, "onClick: " + username);
                        text = username;
                    }

                    if(!text.equals("")){

                        PreferencesManagement.saveDefaultStringPref(ActivityOnBoard.this, getString(R.string.key_pref_user_name), text);

                        Log.i(TAG, "onClick: " +  text);
                        finish();
                    } else {
                        Toast.makeText(ActivityOnBoard.this, "You must enter a username", Toast.LENGTH_LONG).show();
                    }
                }

                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

    }

    public void onTextChange(String text){
        this.text = text;
    }

    /**
     * Handle the mandatory permissions, if the access is not granted, the application restart
     *
     * @param requestCode  the request code received by the application, used to differentiate mandatory and optional permissions
     * @param permissions  string array containing all the permission required
     * @param grantResults array containing the result code of the permission check
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean showDialog = false;

        //Handling of mandatory permissions of the application, the app do not work without them
        if (requestCode == CODE_MANDATORY_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    showDialog = true;
                }
            }
            if (showDialog) {
                new AlertDialog.Builder(ActivityOnBoard.this)
                        .setTitle(getString(R.string.Mandatory_permissions))
                        .setMessage(getString(R.string.Mandatory_permission_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                                MandatoryPermissionsHandling.checkPermission(
                                        ActivityOnBoard.this,
                                        CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
                            }
                        })
                        .create().show();
            } else {
                btNext.setVisibility(View.VISIBLE);
                btNext.setEnabled(true);
            }
        }
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
                return new FragOnBoardScreen2();
            case 2:
                return new FragOnBoardScreen3();
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
