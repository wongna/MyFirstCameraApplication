package nataliewong.myfirstcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private final int MENUACTIVITY_REQUESTCODE = 42;
    private ImageView startImage, previewImage;
    private Bitmap previewImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startImage = (ImageView) findViewById(R.id.startImage);
        previewImage = (ImageView) findViewById(R.id.previewImage);

        Button takeNewPictureButton = (Button) findViewById(R.id.takeNewPictureButton);
        Button editPictureButton = (Button) findViewById(R.id.editPictureButton);

        takeNewPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Launching camera...", Toast.LENGTH_LONG).show();

                Intent switchActivities = new Intent(getApplicationContext(), CameraActivity.class);
                startActivityForResult(switchActivities, MENUACTIVITY_REQUESTCODE);
            }
        });

        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO: Next time, we'll use this to edit previewImage!
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent activityData)
    {
        if (requestCode == MENUACTIVITY_REQUESTCODE && resultCode == Activity.RESULT_OK)
        {
            String fileName = activityData.getStringExtra("filename key");

            if (previewImageBitmap != null)
                previewImageBitmap.recycle();

            previewImageBitmap = BitmapFactory.decodeFile(fileName);

            if (startImage.getVisibility() != ImageView.INVISIBLE)
                startImage.setVisibility(ImageView.INVISIBLE);

            previewImage.setImageBitmap(previewImageBitmap);
            previewImage.setRotation(90);
        }
    }
}
