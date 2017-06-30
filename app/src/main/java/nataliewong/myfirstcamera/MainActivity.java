package nataliewong.myfirstcamera;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
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
                if (previewImage == null)
                {
                    Toast.makeText(getApplicationContext(), "This feature is unavailable.", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertTheme);
                alertDialogBuilder.setTitle("Edit Current Picture")
                        .setSingleChoiceItems(R.array.options, 0 ,null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                        })
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int selectedItem = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                if (selectedItem == 0)
                                    previewImage.setImageBitmap(previewImageBitmap);
                                else if (selectedItem == 1)
                                    previewImage.setImageBitmap(BitmapTransformer.turnUpperRightCornerBlack(previewImageBitmap));
                                else if (selectedItem == 2)
                                    previewImage.setImageBitmap(BitmapTransformer.turnGray(previewImageBitmap));
                                else if (selectedItem == 3)
                                    previewImage.setImageBitmap(BitmapTransformer.turnBinary(previewImageBitmap, 119));
                                else
                                    previewImage.setImageBitmap(BitmapTransformer.convertToLAB(previewImageBitmap));

                                dialog.dismiss();
                            }
                        }).show();
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
