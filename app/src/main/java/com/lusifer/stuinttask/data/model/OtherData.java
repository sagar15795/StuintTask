package com.lusifer.stuinttask.data.model;

import com.lusifer.stuinttask.data.local.StuintTaskDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = StuintTaskDatabase.class)
public class OtherData extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
