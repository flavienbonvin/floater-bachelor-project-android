package ch.hevs.fbonvin.disasterassistance.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.IListRecyclerAdapter;
import ch.hevs.fbonvin.disasterassistance.views.ActivityMessageDetails;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  implements IListRecyclerAdapter {

    private final Context mContext;

    private final LayoutInflater mLayoutInflater;

    private final ArrayList<Message> mMessagesList;

    public RecyclerViewAdapter(Context context, ArrayList<Message> messages) {

        mLayoutInflater = LayoutInflater.from(context);

        mContext = context;
        mMessagesList = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout._list_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Message current = mMessagesList.get(position);

        String date = current.getDateCreatedString().split("/")[1];
        date = date.split(":")[0] + ":" + date.split(":")[1];


        holder.tvMessageTitle.setText(current.getTitle());
        holder.tvCategoryName.setText(current.getCategory());
        holder.tvMessageDate.setText(date);
        holder.tvMessageDesc.setText(current.getDescription());

        if(current.getDistance() != -1){
            String txt = String.valueOf(current.getDistance() + " m");
            holder.tvMessageDistance.setText(txt);
        } else {
            holder.tvMessageDistance.setText(R.string.nan);
        }


        if (mContext.getString(R.string.category_Victims).equals(current.getCategory())) {
            holder.vMessageCategory.setBackgroundColor(mContext.getResources().getColor(R.color.category_victim));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.category_victim));
        } else if (mContext.getString(R.string.category_Danger).equals(current.getCategory())) {
            holder.vMessageCategory.setBackgroundColor(mContext.getResources().getColor(R.color.category_danger));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.category_danger));
        } else if (mContext.getString(R.string.category_Resources).equals(current.getCategory())) {
            holder.vMessageCategory.setBackgroundColor(mContext.getResources().getColor(R.color.category_resource));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.category_resource));
        } else if (mContext.getString(R.string.category_Caretaker).equals(current.getCategory())) {
            holder.vMessageCategory.setBackgroundColor(mContext.getResources().getColor(R.color.category_caretaker));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.category_caretaker));
        }


        holder.mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityMessageDetails.class);
                intent.putExtra("message", current);
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public void remove(int pos){
        mMessagesList.remove(pos);
        notifyItemRemoved(pos);
    }


    @Override
    public void updateDistance(float distance, int pos) {
        mMessagesList.get(pos).setDistance(distance);
        notifyItemChanged(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final ConstraintLayout mConstraintLayout;

        final View vMessageCategory;

        final TextView tvMessageTitle;
        final TextView tvCategoryName;
        final TextView tvMessageDate;
        final TextView tvMessageDesc;
        final TextView tvMessageDistance;

        final ImageView imMessageType;

        ViewHolder(View itemView) {
            super(itemView);

            mConstraintLayout = itemView.findViewById(R.id.recycle_view_message_parent);
            vMessageCategory = itemView.findViewById(R.id.view_message_category);
            tvMessageTitle = itemView.findViewById(R.id.tv_message_detail_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvMessageDate = itemView.findViewById(R.id.tv_message_date);
            tvMessageDesc = itemView.findViewById(R.id.tv_message_description);
            tvMessageDistance = itemView.findViewById(R.id.tv_message_distance);
            imMessageType = itemView.findViewById(R.id.im_message_type);
        }
    }
}
