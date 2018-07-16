package com.example.user.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    PhotoDB photoHelper;
    SQLiteDatabase photo_db;
    Cursor photo_cursor;
    PhotoAdapter1 photoAdapter1;
    PhotoAdapter2 photoAdapter2;
    Drawable d;
    GridView gridView;

    long now;
    Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String strdate, sql;

    int exifOrientation;
    int exifDegree;
    ExifInterface exif;
    Bitmap bitmap;

    Button take, cancel, delete_1;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ImageView im =(ImageView) view.findViewById(R.id.image_db);
                d = im.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

                TextView tv2 = (TextView) view.findViewById(R.id.textView_db);
                String day = tv2.getText().toString();

                final Dialog dialog = new Dialog(Photo.this);
                dialog.setContentView(R.layout.item_photo_big);
                dialog.setCancelable(true);

                ImageView im2 = (ImageView) dialog.findViewById(R.id.image_ph);
                im2.setImageBitmap(bitmap);
                TextView tv = (TextView) dialog.findViewById(R.id.day_info);
                tv.setText("< " +day+" >");
                Button button = (Button) dialog.findViewById(R.id.btn_ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);

                dialog.show();

            }
        });

        photoHelper = new PhotoDB(this);
        photo_db = photoHelper.getWritableDatabase();
        String sql = "select * from photo";
        photo_cursor = photo_db.rawQuery(sql,null);
        photoAdapter1 = new PhotoAdapter1(this,photo_cursor);
        photoAdapter2 = new PhotoAdapter2(this,photo_cursor);
        gridView.setAdapter(photoAdapter1);
        registerForContextMenu(gridView);


        now = System.currentTimeMillis();
        date = new Date(now);
        strdate = sdf.format(date);

        cancel = (Button) findViewById(R.id.cancle);
        take = (Button) findViewById(R.id.take);
        delete_1 = (Button) findViewById(R.id.delete_1);

        cancel.setVisibility(View.INVISIBLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setAdapter(photoAdapter1);
                registerForContextMenu(gridView);
                cancel.setVisibility(View.INVISIBLE);
                take.setVisibility(View.VISIBLE);
                delete_1.setVisibility(View.VISIBLE);
            }
        });

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });
    }

    class PhotoAdapter1 extends CursorAdapter {

        public PhotoAdapter1(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            return inflater.inflate(R.layout.item_photo,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int id = cursor.getInt(0);

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.image_db);
            String puri = cursor.getString(1);
            Uri uri = Uri.parse(puri);
            Bitmap bm=null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(rotate(bm, exifDegree));

            TextView tv = (TextView) view.findViewById(R.id.textView_db);
            tv.setText(cursor.getString(2));
        }
    }

    class PhotoAdapter2 extends CursorAdapter {

        public PhotoAdapter2(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            return inflater.inflate(R.layout.item_photo_check,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int id = cursor.getInt(0);

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.image_db);
            final String  puri = cursor.getString(1);
            Uri uri = Uri.parse(puri);
            Bitmap bm=null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(rotate(bm, exifDegree));
            TextView tv = (TextView) view.findViewById(R.id.textView_db);
            tv.setText(cursor.getString(2));

            TextView btnDel = (TextView) view.findViewById(R.id.btnDel);
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder box = new AlertDialog.Builder(Photo.this);
                    box.setMessage("삭제하시겠습니까?");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String sql = "delete from photo where puri = '" + puri + "'";
                            photo_db.execSQL(sql);
                            photo_cursor=photo_db.rawQuery("select * from photo",null);
                            photoAdapter1.changeCursor(photo_cursor);
                            photoAdapter2.changeCursor(photo_cursor);
                        }
                    });
                    box.setNegativeButton("아니요",null);
                    box.show();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(imageFilePath);
            exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            sql = "insert into photo(puri, date) values(";
            sql += "'"+photoUri+"',";
            sql += "'"+strdate+"')";
            photo_db.execSQL(sql);
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onResume() {
        super.onResume();
        photoHelper = new PhotoDB(this);
        photo_db = photoHelper.getWritableDatabase();
        String sql = "select * from photo";
        photo_cursor = photo_db.rawQuery(sql, null);

        photoAdapter1 = new PhotoAdapter1(this, photo_cursor);
        gridView.setAdapter(photoAdapter1);
        registerForContextMenu(gridView);

        photoAdapter2 = new PhotoAdapter2(this, photo_cursor);

    }

    public void onClick(View v) {
        count = 0;
        gridView.setAdapter(photoAdapter2);
        registerForContextMenu(gridView);

        take.setVisibility(v.INVISIBLE);
        cancel.setVisibility(v.VISIBLE);
        delete_1.setVisibility(v.INVISIBLE);
    }


}
