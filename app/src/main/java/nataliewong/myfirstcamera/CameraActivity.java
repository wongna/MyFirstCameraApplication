package nataliewong.myfirstcamera;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback
{

    private Camera localCamera;
    private SurfaceHolder cameraPreviewHolder;
    private Camera.PictureCallback jpegCallback;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        SurfaceView cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        cameraPreviewHolder = cameraPreview.getHolder();

        cameraPreviewHolder.addCallback(this);
        cameraPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback()
        {
            public void onPictureTaken(byte[] pictureData, Camera localCamera)
            {

                FileOutputStream out = null;
                String fileName = String.format("/sdcard/MyFirstCamera/%d.jpg", System.currentTimeMillis());

                File targetDir = new File("/sdcard/MyFirstCamera");
                if (!targetDir.isDirectory())
                    if (!targetDir.mkdir())
                    {
                        Toast.makeText(getApplicationContext(), "Unable to take picture.", Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);

                        finish();
                    }

                try
                {
                    out = new FileOutputStream(fileName);
                    out.write(pictureData);
                    out.close();

                    Toast.makeText(getApplicationContext(), "Picture taken successfully.", Toast.LENGTH_LONG).show();

                    Intent activityData = new Intent();
                    activityData.putExtra("filename key", fileName);

                    setResult(RESULT_OK, activityData);
                }

                catch (FileNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "Unable to take picture.", Toast.LENGTH_LONG).show();

                    setResult(RESULT_CANCELED);
                }

                catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), "Unable to take picture.", Toast.LENGTH_LONG).show();

                    setResult(RESULT_CANCELED);
                }

                finish();
            }

        };

        Button takePictureButton = (Button) findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                localCamera.takePicture(null, null, jpegCallback);
            }
        });
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            if (localCamera != null)
            {
                localCamera.release();
                localCamera = null;
            }

            localCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        catch (RuntimeException e)
        {
            Toast.makeText(getApplicationContext(), "Unable to connect to camera.", Toast.LENGTH_LONG).show();
            return;
        }

        localCamera.setParameters(getLowResolutionParams(localCamera));
        localCamera.setDisplayOrientation(90);

        try
        {
            localCamera.setPreviewDisplay(cameraPreviewHolder);
            localCamera.startPreview();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Unable to start camera preview.", Toast.LENGTH_LONG).show();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (localCamera != null)
        {
            localCamera.stopPreview();
            localCamera.release();
            localCamera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if (cameraPreviewHolder.getSurface() == null) return;

        localCamera.stopPreview();

        try
        {
            localCamera.setPreviewDisplay(cameraPreviewHolder);
            localCamera.startPreview();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Unable to start camera preview.", Toast.LENGTH_LONG).show();
        }
    }

    protected void onPause()
    {
        super.onPause();

        if (localCamera != null)
        {
            localCamera.release();
            localCamera = null;
        }
    }

    protected void onResume()
    {
        super.onResume();

        if (localCamera == null)
        {
            try
            {
                localCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            catch (RuntimeException e)
            {
                Toast.makeText(getApplicationContext(), "Unable to connect to camera.", Toast.LENGTH_LONG).show();
                return;
            }

            localCamera.setDisplayOrientation(90);

            try
            {
                localCamera.setPreviewDisplay(cameraPreviewHolder);
                localCamera.startPreview();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    private Camera.Parameters getLowResolutionParams(Camera localCamera)
    {
        Camera.Parameters params = localCamera.getParameters();
        List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        Camera.Size lowResolution = supportedSizes.get(0);

        for (int i = 1; i < supportedSizes.size(); i++)
            if (lowResolution.height > supportedSizes.get(i).height)
                lowResolution = supportedSizes.get(i);

        params.setPictureSize(lowResolution.width, lowResolution.height);

        return params;
    }
}
