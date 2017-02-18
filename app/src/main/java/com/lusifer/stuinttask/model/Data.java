package com.lusifer.stuinttask.model;

public class Data {

    String title;

    long id;

    Type type;

    OtherData data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public OtherData getData() {
        return data;
    }

    public void setData(OtherData data) {
        this.data = data;
    }
}
