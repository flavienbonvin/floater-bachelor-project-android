package ch.hevs.fbonvin.disasterassistance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hevs.fbonvin.disasterassistance.R;

import static ch.hevs.fbonvin.disasterassistance.Constant.SLIDE_HEADING;
import static ch.hevs.fbonvin.disasterassistance.Constant.SLIDE_IMAGE;
import static ch.hevs.fbonvin.disasterassistance.Constant.SLIDE_TEXT;

public class SliderAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;



    public SliderAdapter(Context context, LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return SLIDE_HEADING.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = mLayoutInflater.inflate(R.layout._slide_layout_onboard, container, false);

        ImageView imageView = view.findViewById(R.id.im_on_board);
        TextView tvHeading = view.findViewById(R.id.tv_heading_on_board);
        TextView tvText = view.findViewById(R.id.tv_content_on_board);


        imageView.setImageResource(SLIDE_IMAGE[position]);
        tvHeading.setText(SLIDE_HEADING[position]);
        tvText.setText(SLIDE_TEXT[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout)object);
    }
}
