package ru.ytken.libraryapp.recycler;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.ytken.libraryapp.R;

public class RecHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    Resources resources;

    public RecHolder(@NonNull View itemView, IListener listener) {
        super(itemView);
        resources = itemView.getResources();
        mImage = itemView.findViewById(R.id.item_image);
        itemView.setOnClickListener(v -> listener.onStoryClicked(getAdapterPosition()));
    }

    interface IListener {
        void onStoryClicked(int id);
    }

    void bind(StoryItem item) {
        mImage.setImageDrawable(resources.getDrawable(item.getCovering()));
    }


}
