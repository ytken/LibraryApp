package ru.ytken.libraryapp.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.ytken.libraryapp.R;

public class RecAdapter extends RecyclerView.Adapter<RecHolder> implements RecHolder.IListener{

    List<StoryItem> data;
    StoryClickedListener listener;

    @Override
    public void onStoryClicked(int id) {
        listener.storyClicked(data.get(id));
    }

    public interface StoryClickedListener {
        void storyClicked(StoryItem item);
    }

    public RecAdapter(StoryClickedListener listener) {
        super();
        this.listener = listener;
        data = new ArrayList<>();
        StoryItem firstStory = new StoryItem(1, R.drawable.cover);
        //StoryItem loadStory = new StoryItem(0, R.drawable.loading);
        data.add(firstStory);
        //data.add(loadStory);
    }

    @NonNull
    @Override
    public RecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.item_game, parent, false);
        return new RecHolder(layout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecHolder holder, int position) {
        StoryItem item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
