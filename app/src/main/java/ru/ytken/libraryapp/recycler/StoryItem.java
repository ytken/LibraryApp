package ru.ytken.libraryapp.recycler;

public class StoryItem {
    private Integer covering, id;

    public StoryItem(Integer id, Integer covering) { this.id = id; this.covering = covering; }

    public Integer getId() { return id; }

    public Integer getCovering() { return covering; }
}
