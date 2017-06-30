package nataliewong.myfirstcamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v4.graphics.ColorUtils;

public class BitmapTransformer
{
    public static Bitmap turnUpperRightCornerBlack(Bitmap originalBitmap)
    {
        Bitmap transformedBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);

        int width = transformedBitmap.getWidth(), height = transformedBitmap.getHeight();

        for (int i = 0; i < width / 2; i++)
            for (int j = height / 2; j < height; j++)
                transformedBitmap.setPixel(i, j, Color.BLACK);

        return transformedBitmap;
    }

    public static Bitmap turnGray(Bitmap originalBitmap)
    {
        Bitmap transformedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas grayCanvas = new Canvas(transformedBitmap);

        Paint grayPaint = new Paint();
        ColorMatrix colormap = new ColorMatrix();
        colormap.setSaturation(0);

        grayPaint.setColorFilter(new ColorMatrixColorFilter(colormap));
        grayCanvas.drawBitmap(originalBitmap, 0, 0, grayPaint);

        return transformedBitmap;
    }

    public static Bitmap turnBinary(Bitmap originalBitmap, int threshold)
    {
        Bitmap transformedBitmap = turnGray(originalBitmap);
        int width = transformedBitmap.getWidth(), height = transformedBitmap.getHeight();

        int [] pixArray = new int[width * height];
        transformedBitmap.getPixels(pixArray, 0, width, 0, 0, width, height);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                if (Color.red(pixArray[i + j*width]) < threshold)
                    transformedBitmap.setPixel(i, j, Color.BLACK);
                else
                    transformedBitmap.setPixel(i, j, Color.WHITE);
            }

        return transformedBitmap;
    }

    public static Bitmap convertToLAB(Bitmap originalBitmap)
    {
        Bitmap transformedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
        int width = transformedBitmap.getWidth(), height = transformedBitmap.getHeight();

        double [] outLAB = {0, 0, 0};

        int [] pixArray = new int[width * height];
        originalBitmap.getPixels(pixArray, 0, width, 0, 0, width, height);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                ColorUtils.colorToLAB(pixArray[i + j * width], outLAB);

                transformedBitmap.setPixel(i, j, Color.argb(Color.alpha(pixArray[i + j * width]), (int)(outLAB[0] * 255 / 100.0),
                        (int)(outLAB[1] + 128), (int)(outLAB[2] + 128)));
            }

        return transformedBitmap;
    }
}
