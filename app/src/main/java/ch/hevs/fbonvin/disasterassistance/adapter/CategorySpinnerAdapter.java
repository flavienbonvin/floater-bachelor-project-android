package ch.hevs.fbonvin.disasterassistance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;

public class CategorySpinnerAdapter extends ArrayAdapter<CategorySpinnerItem> {

    public CategorySpinnerAdapter(@NonNull Context context, ArrayList<CategorySpinnerItem> categoryList) {
        super(context, 0, categoryList);
    }


    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout._category_spinner, parent, false
            );
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.category_spinner_icon);
        TextView textViewName = convertView.findViewById(R.id.category_spinner_text);

        CategorySpinnerItem currentItem = getItem(position);


        imageViewIcon.setImageResource(currentItem.getIcon());
        imageViewIcon.setColorFilter(getContext().getResources().getColor(currentItem.getColor()));
        textViewName.setText(currentItem.getCategoryName());
        textViewName.setTextColor(getContext().getResources().getColor(currentItem.getColor()));

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
}
