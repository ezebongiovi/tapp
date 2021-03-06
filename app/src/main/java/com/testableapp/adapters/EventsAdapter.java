package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.GEvent;

public class EventsAdapter extends PaginationAdapter<GEvent> {

    private OnEventClick mListener;

    /**
     * Default constructor
     *
     * @param paginationListener listener for being notified when paging is required
     */
    public EventsAdapter(@NonNull final PaginationListener paginationListener) {
        super(paginationListener);
    }

    public interface OnEventClick {
        void onClick(@NonNull GenericViewHolder holder, @NonNull GEvent event);
    }

    @Override
    protected int getHolderLayout() {
        return R.layout.holder_event;
    }

    @Override
    protected void onBind(final GenericViewHolder holder, final GEvent data) {
        final Animation anim = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.list_item_in);
        anim.setStartOffset(anim.getDuration() / 4 * holder.getAdapterPosition());
        holder.itemView.startAnimation(anim);

        ((TextView) holder.itemView.findViewById(R.id.eventDate)).setText(data.date.toString());

        ((TextView) holder.itemView.findViewById(R.id.eventAuthor)).setText(String.format(holder.itemView.getContext().getString(R.string.name),
                data.author.firstName, data.author.lastName));

        if (data.cover != null) {
            Picasso.with(holder.itemView.getContext()).load(data.cover.url)
                    .into((ImageView) holder.itemView.findViewById(R.id.coverView));
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(holder, data);
            }
        });
    }

    public void setOnEventClick(@NonNull final OnEventClick onClickListener) {
        this.mListener = onClickListener;
    }
}
