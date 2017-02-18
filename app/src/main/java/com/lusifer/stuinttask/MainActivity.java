package com.lusifer.stuinttask;

import com.lusifer.stuinttask.data.model.Data;
import com.lusifer.stuinttask.data.model.Data_Table;
import com.lusifer.stuinttask.data.model.OtherData;
import com.lusifer.stuinttask.data.model.Type;
import com.lusifer.stuinttask.data.model.VoteData;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "hello";
    @BindView(R.id.rvPost)
    RecyclerView mRecyclerView;

    private MainAdapter mMainAdapter;
    private ImageView   preview;

    private static int RESULT_LOAD_IMAGE = 1;

    AlertDialog alertDialog;

    Type type = Type.Text;

    String filePath;



    List<Data> mData = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainAdapter = new MainAdapter(getApplicationContext(),mData);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMainAdapter);
        getDataFromDataBase();

    }

    private void getDataFromDataBase() {
        List<Data> data = SQLite.select()
                .from(Data.class)
                .orderBy(Data_Table.id,false)
                .queryList();
        mData.clear();
        mData.addAll(data);
        mMainAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            if(selectedImage.getPath().toLowerCase().contains("video")){

                String path = generatePath(selectedImage,this);
                Log.e(TAG, "onActivityResult: "+path);
                showThumbnail(path,false);
            }

            if (selectedImage.getPath().toLowerCase().contains("image")){
                String[] filePathColumn = { MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                showThumbnail(picturePath,true);
            }

            //ImageView imageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

    private void showThumbnail(String path, boolean flag) {

        if(flag) {
            preview.setImageBitmap(BitmapFactory.decodeFile(path));
            type=Type.Image;

        }else{
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
            preview.setImageBitmap(bMap);
            type=Type.Video;
        }
        filePath=path;
    }


    public String generatePath(Uri uri,Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat){
            filePath = generateFromKitkat(uri,context);
        }

        if(filePath != null){
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri,Context context){
        String filePath = null;
        if(DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Video.Media.DATA };
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);



            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addPost:
                add_post();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_post() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.post_add_layout, null);
        dialogBuilder.setView(dialogView);
        final EditText etPost = ButterKnife.findById(dialogView, R.id.etPost);
        Button dismiss = ButterKnife.findById(dialogView, R.id.bDismiss);
        Button browse = ButterKnife.findById(dialogView, R.id.bBrowse);
        preview = ButterKnife.findById(dialogView, R.id.ivImage);
        Button buttonOk = ButterKnife.findById(dialogView, R.id.bOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savethepost(etPost.getText().toString());
                getDataFromDataBase();
                alertDialog.dismiss();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                mediaChooser.setType("video/*, images/*");
                startActivityForResult(mediaChooser, RESULT_LOAD_IMAGE);
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog=dialogBuilder.create();
        alertDialog.show();

    }

    private void savethepost(String text) {
        Data data =new Data();
        data.setId(System.currentTimeMillis());
        data.setTitle(text);
        data.setType(type);
        if(type==Type.Text){
            data.setData(null);
        }else{
            OtherData od = new OtherData();
            od.setPath(filePath);
            data.setData(od);
        }

        VoteData voteData =new VoteData();

        voteData.setYes(0);
        voteData.setNo(0);
        voteData.setNeutral(100);

        data.setVoteData(voteData);

        data.save();

    }

}
