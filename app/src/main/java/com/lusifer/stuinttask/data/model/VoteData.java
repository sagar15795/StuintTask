package com.lusifer.stuinttask.data.model;


import com.lusifer.stuinttask.data.local.StuintTaskDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = StuintTaskDatabase.class)
public class VoteData extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    long yes;

    @Column
    long no;

    @Column
    long neutral;

    public long getYes() {
        return yes;
    }

    public void setYes(long yes) {
        this.yes = yes;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public long getNeutral() {
        return neutral;
    }

    public void setNeutral(long neutral) {
        this.neutral = neutral;
    }
}
