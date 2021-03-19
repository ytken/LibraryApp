package ru.ytken.libraryapp.recycler;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.ytken.libraryapp.R;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    List<Integer> data;

    public RecAdapter() {
        data = new ArrayList<>();
        data.add(R.drawable.cover);
        data.add(R.drawable.cover);
        data.add(R.drawable.cover);
        data.add(R.drawable.cover);
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        Resources res = holder.itemView.getResources();
        holder.covering.setImageDrawable(res.getDrawable(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RecViewHolder extends RecyclerView.ViewHolder {

        public ImageView covering;
        public RecViewHolder(@NonNull View itemView) {
            super(itemView);
            covering = itemView.findViewById(R.id.imageView);
        }
    }

}
