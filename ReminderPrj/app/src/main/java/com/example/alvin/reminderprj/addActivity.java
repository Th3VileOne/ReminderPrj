package com.example.alvin.reminderprj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;

public class addActivity extends AppCompatActivity {

    private static final int UPLOAD_PHOTO = 100;
    private static final int TAKE_PHOTO = 200;
    private String titleString;
    private String descriptionString;
    private Bitmap imgString;
    private Bitmap emojiString;
    private long dateString;
    public String username = getIntent().getStringExtra("username");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add");
    }

    public void add(View view) {
        blogContent theBlogContent = null;

        EditText title = (EditText) findViewById(R.id.editTitle);
        titleString = title.toString();
        theBlogContent.setTitle(titleString);

        ImageView image = (ImageView) findViewById(R.id.editImage);
        image.buildDrawingCache();
        imgString = image.getDrawingCache();
        theBlogContent.setBlogImg(imgString);

        EditText description = (EditText) findViewById(R.id.editDescription);
        descriptionString = description.toString();
        theBlogContent.setBlogDescription(descriptionString);

        theBlogContent.setEmoji(emojiString);

        dateString = new Date().getTime();
        theBlogContent.setDate(dateString);

        toMainActivity();
        clearFields();
    }
    public void setHappy(View view){
        ImageView image = (ImageView) findViewById(R.id.happyIcon);
        image.buildDrawingCache();
        emojiString = image.getDrawingCache();
    }
    public void setNeut(View view){
        ImageView image = (ImageView) findViewById(R.id.neutIcon);
        image.buildDrawingCache();
        emojiString = image.getDrawingCache();
    }
    public void setSad(View view){
        ImageView image = (ImageView) findViewById(R.id.sadIcon);
        image.buildDrawingCache();
        emojiString = image.getDrawingCache();
    }

    public void discard(View view) {
        toMainActivity();
        clearFields();
    }

    private void toMainActivity() {
        Intent intent = new Intent(addActivity.this, mainActivity.class);
        startActivity(intent);
    }

    private void clearFields() {
        EditText title = (EditText) findViewById(R.id.editTitle);
        title.setText("");

        ImageView image = (ImageView) findViewById(R.id.editImage);
        image.setImageResource(R.drawable.camera);

        EditText description = (EditText) findViewById(R.id.editDescription);
        description.setText("");
    }

    public void SelectImage(View view) {
        CharSequence picker[] = new CharSequence[] {"Upload a photo", "Take a photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Action");
        builder.setItems(picker, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which){
                    case 0:{
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, UPLOAD_PHOTO);
                        }
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, TAKE_PHOTO);
                        }
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        upload photo
        if (requestCode == UPLOAD_PHOTO && resultCode == RESULT_OK) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    ImageView imageView = (ImageView) findViewById(R.id.editImage);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
//        take photo
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.editImage);
            imageView.setImageBitmap(bitmap);
        }
    }

}

