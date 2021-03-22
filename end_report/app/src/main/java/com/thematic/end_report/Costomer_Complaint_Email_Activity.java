package com.thematic.end_report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.Message;

public class Costomer_Complaint_Email_Activity extends AppCompatActivity implements View.OnClickListener {

    final static String MY_EMAIL = "D1054183730@gm.lhu.edu.tw";
    final static int CAMERA_REQUEST = 1;
    final static int CAMERA_PERMISSIONS_REQUEST = 100;
    final static int WRITE_PERMISSIONS_REQUEST = 100;
    final static int IMAGE_REQUEST = 2;

    EditText context_edit, main_edit;
    Button send_complaint_btn;
    ImageView complaint_img;
    TextView img_text;
    Dialog pic_dialog;

    Intent intent;

    Bitmap img_bmp;

    LinearLayout camera_linear, pic_linear, close_linear;
    Uri img_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costomer__complaint__email_);

        init();
    }

    private void init() {
        //main_edit = findViewById(R.id.complaint_main_title);
        context_edit = findViewById(R.id.complaint_context);

        send_complaint_btn = findViewById(R.id.complaint_send_button);
        send_complaint_btn.setOnClickListener(this);
        complaint_img = findViewById(R.id.complaint_img);
        complaint_img.setOnClickListener(this);
        img_text = findViewById(R.id.complaint_img_text);
        img_text.setOnClickListener(this);

        pic_dialog = new Dialog(this);
    }

    private void show_dialog() {
        pic_dialog.setContentView(R.layout.complains_camera_dialog);
        camera_linear = pic_dialog.findViewById(R.id.camera_linearlayout);
        pic_linear = pic_dialog.findViewById(R.id.pic_linearlayout);
        close_linear = pic_dialog.findViewById(R.id.close_linearlayout);

        pic_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_pic();
                pic_dialog.dismiss();
            }
        });

        camera_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkpermission()) {
                    get_camera();
                    pic_dialog.dismiss();
                } else {
                    checkpermission();
                }
            }
        });

        close_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_bmp = null;
                img_uri = null;
                complaint_img.setImageBitmap(img_bmp);
                img_text.setVisibility(View.VISIBLE);
                pic_dialog.dismiss();
            }
        });
        pic_dialog.show();
    }

    private boolean checkpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIONS_REQUEST);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST);

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSIONS_REQUEST:
                    get_camera();
                    break;
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_permission), Toast.LENGTH_LONG).show();
        }
    }

    private void get_camera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        img_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void select_pic() {
        Intent PictureFileintent = new Intent();
        PictureFileintent.setType("image/*");
        PictureFileintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(PictureFileintent, "Select Picture"), IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    try {
                        img_bmp = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(img_uri), null, null);
                    } catch (IOException e) {
                        Toast.makeText(this, "無法讀取照片", Toast.LENGTH_LONG).show();
                    }
                    complaint_img.setImageBitmap(img_bmp);
                    img_text.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    img_uri = data.getData();
                    try {
                        img_bmp = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(img_uri), null, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    complaint_img.setImageBitmap(img_bmp);
                    img_text.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complaint_img_text:
                show_dialog();
                break;
            case R.id.complaint_img:
                show_dialog();
                break;
            case R.id.complaint_send_button:
                send_costomer_complaint();
                break;
        }
    }

    private void send_costomer_complaint() {

        //String sub = main_edit.getText().toString();
        String context = context_edit.getText().toString();
        /*if (sub.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.error_main_title), Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (context.equals("") && img_bmp == null) {
            Toast.makeText(this, getResources().getString(R.string.error_context), Toast.LENGTH_SHORT).show();
            return;
        } else {
            /*
            StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(MY_EMAIL));
            builder.append("?subject=" + Uri.encode(Uri.encode(sub)));
            builder.append("&body=" + Uri.encode(Uri.encode(context)));
            builder.append("&stream=" + Uri.encode(Uri.encode(String.valueOf(img_uri))));
            String uri = builder.toString();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            if (img_uri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, img_uri);
            }
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_mail_device)));*/

            intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.setType("text/plain");
            //intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{MY_EMAIL}); // recipients
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.complaint_main));
            intent.putExtra(Intent.EXTRA_TEXT, context);
            intent.putExtra(Intent.EXTRA_STREAM, img_uri);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_mail_device)));

            /*String mailto = "mailto:useremail@gmail.com" +
                    "?cc=" +
                    "&subject=" + Uri.encode("your subject") +
                    "&body=" + Uri.encode("your mail body");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mailto));
            emailIntent.putExtra(Intent.EXTRA_STREAM, img_uri);
            startActivity(emailIntent);*/
        }
    }
}
