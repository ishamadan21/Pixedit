package com.example.recruitment.pixedit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private static final int CAMERA_TASK_REQUEST = 1;
    private static final int GALLERY_TASK_REQUEST = 2;

    private static final int EDITOR_TASK_REQUEST = 10;
    private Button cambtn,gallbtn;

    private static final String STATE_FILE_PATH = "state_file";
    private static final String STATE_FINAL_IMAGE="state_file";

    private File cameraOutputFile , finalImage, outputDir;

    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cambtn=(Button)findViewById(R.id.btn_cam);
        gallbtn=(Button)findViewById(R.id.btn_gall);
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File sdStorageDirectory = Environment.getExternalStorageDirectory();
                String timeStamp = dateFormat.format(new Date());
                String fileName = "capturedImage" + timeStamp+".jpg";
                cameraOutputFile = new File(sdStorageDirectory, "DCIM/"+fileName);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraOutputFile));

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                   HomeActivity.this.startActivityForResult(cameraIntent, CAMERA_TASK_REQUEST);
                }
            }
        });
        gallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                if (galleryIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(galleryIntent, GALLERY_TASK_REQUEST);
                }
            }
        });

        outputDir = new File(Environment.getExternalStorageDirectory(), "Pixedit");

        if (!outputDir.exists()){
            outputDir.mkdirs();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_TASK_REQUEST && resultCode == RESULT_OK){

            Uri imageUri = Uri.fromFile(cameraOutputFile);

            String timeStamp = dateFormat.format(new Date());
            String fileName = "outputimage_" + timeStamp+".jpg";


            File outputImageFile = new File(outputDir, fileName);
            finalImage=outputImageFile;
            Intent intent = new AviaryIntent.Builder(this).setData(imageUri).withOutput(outputImageFile).withOutputFormat(Bitmap.CompressFormat.JPEG)
                    .withOutputSize(MegaPixels.Mp5).withPreviewSize(1024).build();
            startActivityForResult(intent, EDITOR_TASK_REQUEST);

        }

        if (requestCode == GALLERY_TASK_REQUEST && resultCode == RESULT_OK){
            Uri imageUri=data.getData();
            String timeStamp=dateFormat.format(new Date());
            String filename="outputimage"+timeStamp+".jpg";
            File outputImageFile=new File(outputDir, filename);
            finalImage=outputImageFile;
            Intent intent=new AviaryIntent.Builder(this).setData(imageUri).withOutput(outputImageFile).withOutputFormat(Bitmap.CompressFormat.JPEG).withOutputSize(MegaPixels.Mp5).withPreviewSize(1024).build();
            startActivityForResult(intent,EDITOR_TASK_REQUEST);

        }
        if(requestCode==EDITOR_TASK_REQUEST && resultCode==RESULT_OK){
           Intent intent=new Intent(this,PriviewActivity.class);
            intent.putExtra("path", finalImage.getAbsolutePath());
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (cameraOutputFile != null){
            outState.putString(STATE_FILE_PATH, cameraOutputFile.getAbsolutePath());
        }
        if(finalImage!=null) {
            outState.putString(STATE_FINAL_IMAGE, finalImage.getAbsolutePath());
        }

        }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String absPath = savedInstanceState.getString(STATE_FILE_PATH, null);
        if (absPath!=null){
            cameraOutputFile = new File(absPath);
        }
        String absPath2 = savedInstanceState.getString(STATE_FINAL_IMAGE,null);
        if(absPath2!=null){
            finalImage=new File(absPath2);
        }
    }



}
