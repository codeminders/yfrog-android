/**
 * 
 */
package com.codeminders.yfrog.android.view.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @author idemydenko
 * 
 */
public class Zoom extends ImageView {
	static final float SCALE_RATE = 1.25F;

	private Matrix baseMatrix = new Matrix();
    private Matrix processMatrix = new Matrix();
    private final Matrix displayMatrix = new Matrix();
    private final float[] matrixValues = new float[9];

    private Bitmap bitemap;

    private int layoutWidth = -1;
    private int layoutHeight = -1;

    private float maxZoom;
    
    private float lastMoveX;
    private float lastMoveY;
    
    private boolean isScale = false;

    public Zoom(Context context, Bitmap bitmap, boolean scaleImage) {
        super(context);
        bitemap = bitmap;
        isScale = scaleImage;
        setImageBitmapResetBase(bitmap, isScale);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top,
                            int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutWidth = right - left;
        layoutHeight = bottom - top;
        Runnable r = onLayoutRunnable;
        if (r != null) {
            onLayoutRunnable = null;
            r.run();
            return;
        }
        getProperBaseMatrix(bitemap, baseMatrix, isScale);
        setImageMatrix(getImageViewMatrix());
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
    }

    private Runnable onLayoutRunnable = null;


    public void setImageBitmapResetBase(final Bitmap bitmap,
            final boolean scale) {
        final int viewWidth = getWidth();

        if (viewWidth <= 0)  {
            onLayoutRunnable = new Runnable() {
                public void run() {
                    setImageBitmapResetBase(bitmap, scale);
                }
            };
            return;
        }

        getProperBaseMatrix(bitmap, baseMatrix, scale);
        setImageBitmap(bitmap);
        
        setImageMatrix(getImageViewMatrix());
        maxZoom = maxZoom();
    }

    protected void center(boolean horizontal, boolean vertical) {
        Matrix m = getImageViewMatrix();

        RectF rect = new RectF(0, 0,
                bitemap.getWidth(),
                bitemap.getHeight());

        m.mapRect(rect);

        float height = rect.height();
        float width  = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            int viewHeight = getHeight();
            if (height < viewHeight) {
                deltaY = (viewHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < viewHeight) {
                deltaY = getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int viewWidth = getWidth();
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }

        postTranslate(deltaX, deltaY);
        setImageMatrix(getImageViewMatrix());
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(matrixValues);
        return matrixValues[whichValue];
    }

    // Get the scale factor out of the matrix.
    private float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    private float getScale() {
        return getScale(processMatrix);
    }

    private void getProperBaseMatrix(Bitmap bitmap, Matrix matrix, boolean isScale) {
        matrix.reset();
        
        if (isScale) {
            float viewWidth = getWidth();
            float viewHeight = getHeight();

            float w = bitmap.getWidth();
            float h = bitmap.getHeight();

	        float widthScale = Math.min(viewWidth / w, 2.0f);
	        float heightScale = Math.min(viewHeight / h, 2.0f);
	        float scale = Math.min(widthScale, heightScale);
	
	        matrix.postScale(scale, scale);
	
	        matrix.postTranslate(
	                (viewWidth  - w * scale) / 2F,
	                (viewHeight - h * scale) / 2F);
        }
    }

    private Matrix getImageViewMatrix() {
        displayMatrix.set(baseMatrix);
        displayMatrix.postConcat(processMatrix);
        return displayMatrix;
    }

    private float maxZoom() {
        float fw = (float) bitemap.getWidth()  / (float) layoutWidth;
        float fh = (float) bitemap.getHeight() / (float) layoutHeight;
        float max = Math.max(fw, fh) * 4;
        return max;
    }


    public void zoomIn() {
        zoomIn(SCALE_RATE);
    }

    public void zoomOut() {
        zoomOut(SCALE_RATE);
    }

    private void zoomIn(float rate) {
        if (getScale() >= maxZoom) {
            return;     // Don't let the user zoom into the molecular level.
        }
        
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        processMatrix.postScale(rate, rate, cx, cy);
        setImageMatrix(getImageViewMatrix());
    }

	private void zoomOut(float rate) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        // Zoom out to at most 1x.
        Matrix tmp = new Matrix(processMatrix);
        tmp.postScale(1F / rate, 1F / rate, cx, cy);

        if (getScale(tmp) < 1F) {
            processMatrix.setScale(1F, 1F, cx, cy);
        } else {
            processMatrix.postScale(1F / rate, 1F / rate, cx, cy);
        }
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    private void postTranslate(float dx, float dy) {
        processMatrix.postTranslate(dx, dy);
    }

	public void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }
	
	public void startMove(float x, float y) {
		lastMoveX = x;
		lastMoveY = y;
	}
	
	public void stopMove(float x, float y) {
		panBy(x - lastMoveX, y - lastMoveY);
	}

}
