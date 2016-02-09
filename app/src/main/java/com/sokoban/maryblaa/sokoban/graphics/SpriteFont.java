package com.sokoban.maryblaa.sokoban.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maryf on 09.02.2016.
 */
public class SpriteFont {

    private Material material;
    private Map<Character, CharacterInfo> characterInfos;

    protected SpriteFont(GraphicsDevice graphicsDevice, Typeface typeface, float fontSize) {

        material = new Material();
        characterInfos = new HashMap<Character, SpriteFont.CharacterInfo>();

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(fontSize);
        paint.setARGB(255, 255, 255, 255);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int spacing = (int) Math.ceil(paint.getFontSpacing());

        char[] c = new char[]{' '};
        int x = 0;
        int y = 0;
        Rect charBounds = new Rect();
        int bitmapSize = 1;
        boolean doesFit = false;
        while (!doesFit) {
            while (c[0] < 256) {
                paint.getTextBounds(c, 0, 1, charBounds);

                if (x + charBounds.width() > bitmapSize) {
                    x = 0;
                    y += spacing;
                }

                x += charBounds.width() + 1;

                c[0]++;

                if (c[0] == 128)
                    c[0] = 160;
            }

            if (y + spacing < bitmapSize)
                doesFit = true;
            else {
                bitmapSize *= 2;
                x = 0;
                y = 0;
                c[0] = ' ';
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        x = 0;
        y = 0;
        c[0] = ' ';
        Point charOffset = new Point();
        while (c[0] < 256) {
            paint.getTextBounds(c, 0, 1, charBounds);
            charOffset.set(charBounds.left, charBounds.top);

            if (x + charBounds.width() > bitmap.getWidth()) {
                x = 0;
                y += spacing;
            }

            int drawPosX = x - charOffset.x;
            int drawPosY = y - charOffset.y;
            canvas.drawText(c, 0, 1, drawPosX, drawPosY, paint);

            CharacterInfo info = new CharacterInfo();
            info.width = (int) Math.ceil(paint.measureText(c, 0, 1));
            info.bounds = new Rect(charBounds);
            info.bounds.offset(drawPosX, drawPosY);
            info.offset = new Point(charOffset);
            characterInfos.put(c[0], info);

            x += charBounds.width() + 1;

            c[0]++;

            if (c[0] == 128)
                c[0] = 160;
        }

        Texture texture = graphicsDevice.createTexture(bitmap);
        material.setTexture(texture);
        material.setTextureFilter(TextureFilter.LINEAR_MIPMAP_LINEAR, TextureFilter.LINEAR);
        material.setTextureWrap(TextureWrapMode.CLAMP, TextureWrapMode.CLAMP);
        material.setBlendFactors(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "font.png"); // save for debugging on storage
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public class CharacterInfo {
        public int width;
        public Rect bounds;
        public Point offset;
    }

    public Material getMaterial() {
        return material;
    }

    public Map<Character, CharacterInfo> getCharacterInfos() {
        return characterInfos;
    }
}
