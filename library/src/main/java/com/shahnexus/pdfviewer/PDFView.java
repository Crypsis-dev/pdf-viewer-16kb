package com.shahnexus.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFView extends View {

    private PdfRenderer renderer;
    private PdfRenderer.Page currentPage;
    private Bitmap currentBitmap;
    private int pageCount = 0;
    private int currentIndex = 0;
    
    private float scale = 1f;
    private float translateX = 0f;
    private float translateY = 0f;
    
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public PDFView(Context context) {
        super(context);
        init(context);
    }

    public PDFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void fromAsset(String assetName) throws IOException {
        File tempFile = new File(getContext().getCacheDir(), "temp_pdf_view.pdf");
        try (InputStream is = getContext().getAssets().open(assetName);
             FileOutputStream os = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        }
        fromFile(tempFile);
    }

    public void fromFile(File file) throws IOException {
        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        renderer = new PdfRenderer(pfd);
        pageCount = renderer.getPageCount();
        showPage(0);
    }

    public void showPage(int index) {
        if (renderer == null || index < 0 || index >= pageCount) return;
        
        if (currentPage != null) {
            currentPage.close();
        }
        
        currentIndex = index;
        currentPage = renderer.openPage(index);
        
        // Simple rendering for now - can be optimized with tiling for large PDFs
        int width = getWidth() > 0 ? getWidth() : currentPage.getWidth();
        int height = (int) ((float) width / currentPage.getWidth() * currentPage.getHeight());
        
        if (currentBitmap != null) {
            currentBitmap.recycle();
        }
        
        currentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentPage.render(currentBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentBitmap != null) {
            canvas.save();
            canvas.translate(translateX, translateY);
            canvas.scale(scale, scale);
            canvas.drawBitmap(currentBitmap, 0, 0, null);
            canvas.restore();
        } else {
            canvas.drawColor(Color.LTGRAY);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("No PDF Loaded", 100, 100, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(1.0f, Math.min(scale, 5.0f));
            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translateX -= distanceX;
            translateY -= distanceY;
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0 && currentIndex > 0) {
                    showPage(currentIndex - 1);
                } else if (velocityX < 0 && currentIndex < pageCount - 1) {
                    showPage(currentIndex + 1);
                }
            }
            return true;
        }
    }
    
    public void close() {
        if (currentPage != null) currentPage.close();
        if (renderer != null) renderer.close();
        if (currentBitmap != null) currentBitmap.recycle();
    }
}
