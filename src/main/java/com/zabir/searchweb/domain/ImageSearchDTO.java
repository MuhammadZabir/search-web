package com.zabir.searchweb.domain;

import java.io.Serializable;
import java.util.List;

public class ImageSearchDTO implements Serializable {
    private List<String> directories;
    private Herb herb;

    public List<String> getDirectories() {
        return directories;
    }

    public void setDirectories(List<String> directories) {
        this.directories = directories;
    }

    public Herb getHerb() {
        return herb;
    }

    public void setHerb(Herb herb) {
        this.herb = herb;
    }
}
