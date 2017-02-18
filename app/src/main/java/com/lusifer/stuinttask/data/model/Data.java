package com.lusifer.stuinttask.data.model;

import com.lusifer.stuinttask.data.local.StuintTaskDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


@Table(database = StuintTaskDatabase.class)
public class Data extends BaseModel{

    @Column
    String title;

    @PrimaryKey
    long id;

    @Column
    Type type;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    OtherData data;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    VoteData mVoteData;

    public VoteData getVoteData() {
        return mVoteData;
    }

    public void setVoteData(VoteData voteData) {
        mVoteData = voteData;
    }

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
