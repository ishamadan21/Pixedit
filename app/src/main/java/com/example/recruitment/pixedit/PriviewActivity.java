package com.example.recruitment.pixedit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import java.io.File;

import it.sephiroth.android.library.picasso.Picasso;


public class PriviewActivity extends AppCompatActivity {

    File file1;
    private ImageView previewImage;
    private static final String STATE_FINAL_IMAGE="state_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priview);

        String absPath = getIntent().getStringExtra("path");
        file1=new File(absPath);
        previewImage= (ImageView)findViewById(R.id.imagev1);

        previewImage.post(new Runnable() {
            @Override
            public void run() {
                int width = previewImage.getMeasuredWidth();
                int height = previewImage.getMeasuredHeight();


                Picasso.with(PriviewActivity.this).load(file1).resize(width,height).centerInside().into(previewImage);
            }
        });


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (file1 != null) {
            outState.putString(STATE_FINAL_IMAGE, file1.getAbsolutePath());
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String absPath2 = savedInstanceState.getString(STATE_FINAL_IMAGE, null);
        if (absPath2 != null) {
            file1 = new File(absPath2);
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_priview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            Uri url= Uri.fromFile(file1);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM,url);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));



        }
        if (id == R.id.action_delete){
            AlertDialog dialog=new AlertDialog.Builder(PriviewActivity.this).setTitle("Delete").setMessage("Are you sure you want to delete this image?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    file1.delete();
                    finish();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create();
            dialog.show();

        }


        return true;

    }
}
