package ch.hevs.fbonvin.disasterassistance.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivitySendMessage;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DEPRECATED;

public class RecyclerViewAdapterDeprecated extends RecyclerViewAdapter{

    private Context mContext;

    private ArrayList<Message> mMessages;

    public RecyclerViewAdapterDeprecated(Context context, ArrayList<Message> messages) {
        super(context, messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new android.support.v7.app.AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.frag_message_deprecated_dialog_title))
                        .setMessage(mContext.getString(R.string.frag_message_deprecated_dialog_message))
                        .setPositiveButton(mContext.getString(R.string.frag_message_deprecated_dialog_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Message m = mMessages.get(holder.getAdapterPosition());
                                MESSAGES_DEPRECATED.remove(m);

                                Intent intent = new Intent(mContext, ActivitySendMessage.class);
                                intent.putExtra("message", m);
                                mContext.startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .create().show();

            }
        });
    }

}
