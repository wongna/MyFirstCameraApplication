package nataliewong.myfirstcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
{
    private ImageView startImage, previewImage;

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
                //  TODO: Next time, we'll use this to switch Activities!
            }
        });

        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO: Next time, we'll use this to edit previewImage!
            }
        });
    }
}
