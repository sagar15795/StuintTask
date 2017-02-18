package com.lusifer.stuinttask.ui;

import com.lusifer.stuinttask.R;
import com.lusifer.stuinttask.data.model.Data;
import com.lusifer.stuinttask.data.model.Data_Table;
import com.lusifer.stuinttask.data.model.OtherData;
import com.lusifer.stuinttask.data.model.Type;
import com.lusifer.stuinttask.data.model.VoteData;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainAdapter.TouchHandler {



    @BindView(R.id.rvPost)
    RecyclerView mRecyclerView;

    @BindView(R.id.tvMessage)
    TextView message;


    private MainAdapter mMainAdapter;
    private ImageView   preview;
    private static int RESULT_LOAD_IMAGE = 1;
    AlertDialog alertDialog;
    Type type = Type.Text;
    String filePath;
    public static final String VIDEO_PATH = "video_path";
    List<Data> mData = new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_PERMISSION_SETTING = 200;

    private void getDataFromDataBase() {
        List<Data> data = SQLite.select()
                .from(Data.class)
                .orderBy(Data_Table.id,false)
                .queryList();
        mData.clear();
        if(data.size()>0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            mData.addAll(data);
            mMainAdapter.notifyDataSetChanged();
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

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private AppCompatActivity mAppCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAppCompatActivity = this;
        mMainAdapter = new MainAdapter(this, mData);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMainAdapter);

        getDataFromDataBase();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addPost:
                addPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addPost() {

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

                saveThePost(etPost.getText().toString());
                getDataFromDataBase();
                alertDialog.dismiss();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    verifyStoragePermissions(mAppCompatActivity);
                } else {
                    openFileBrowser();
                }

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

    private void saveThePost(String text) {

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
        voteData.setNeutral(21);

        data.setVoteData(voteData);

        data.save();

    }

    @Override
    public void updateRecyclerView() {
        getDataFromDataBase();
    }

    public void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to External Storage",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        PERMISSIONS_STORAGE,
                                        REQUEST_EXTERNAL_STORAGE
                                );
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            openFileBrowser();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("TAG", "permission granted");

                } else {

                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "permission denied");

                    showMessageOKCancel("This app need to access the External Storage to retrieve videos. Please enable Storage in Permissions!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                }
                            });
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PERMISSION_SETTING) {
                openFileBrowser();
            }
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

                Uri selectedImage = data.getData();
                String type = data.getType();
                String path = generatePath(selectedImage, this);
                if (selectedImage.getPath().toLowerCase().contains("video")) {
                    showThumbnail(path, false);
                    return;
                }

                if (selectedImage.getPath().toLowerCase().contains("image")) {
                    showThumbnail(path, true);
                    return;
                }

                if (type != null) {
                    if (type.contains("video")) {
                        showThumbnail(path, false);
                        return;
                    }

                    if (type.contains("image")) {
                        showThumbnail(path, true);
                        return;
                    }
                }

                Toast.makeText(this,"Format not supported",Toast.LENGTH_LONG).show();
            }
        }
    }

    void openFileBrowser() {
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("video/*, images/*");
        startActivityForResult(mediaChooser, RESULT_LOAD_IMAGE);
    }
}
