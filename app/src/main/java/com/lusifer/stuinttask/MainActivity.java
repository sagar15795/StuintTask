package com.lusifer.stuinttask;

import com.lusifer.stuinttask.model.Data;
import com.lusifer.stuinttask.model.OtherData;
import com.lusifer.stuinttask.model.Type;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rvPost)
    RecyclerView mRecyclerView;

    private MainAdapter mMainAdapter;


    private static int RESULT_LOAD_IMAGE = 1;



    List<Data> mData = new ArrayList<>();



    private void fakedata() {
        Data d= new Data();
        d.setTitle("Post1");
        d.setId(11);
        d.setType(Type.Text);
        d.setData(null);
        mData.add(d);

        Data d1= new Data();
        d1.setTitle("Post2");
        d1.setId(12);
        d1.setType(Type.Image);
        OtherData od = new OtherData();
        od.setId(121);
        od.setPath("/storage/sdcard0/Pictures/Messenger/received_1306630966124541.jpeg");
        d1.setData(od);
        mData.add(d1);

        Data d2= new Data();
        d2.setTitle("Post3");
        d2.setId(13);
        d2.setType(Type.Video);
        OtherData od1 = new OtherData();
        od1.setId(121);
        od1.setPath("/storage/sdcard0/DCIM/Camera/VID_20170218_141344.mp4");
        d2.setData(od1);
        mData.add(d2);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fakedata();
        ButterKnife.bind(this);
        mMainAdapter = new MainAdapter(getApplicationContext(),mData);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMainAdapter);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.e("TAG", filePathColumn[0]+"onActivityResult: "+picturePath);
            //ImageView imageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }


}
