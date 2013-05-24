package com.ec1513079.dogear;

import com.ec1513079.dogear.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.GridView;

public class ShelfGridView extends GridView {

    private Bitmap background;

    private int mShelfWidth;
    private int mShelfHeight;

    public ShelfGridView(Context context, AttributeSet attributes) {
        super(context, attributes);

        this.setFocusableInTouchMode(true);
        this.setClickable(false);

        final Bitmap shelfBackground = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bookshelf_portrait);
        setBackground(shelfBackground);
        this.setFocusable(true);
    }

    public void setBackground(Bitmap background) {
        this.background = background;

        mShelfWidth = background.getWidth();
        mShelfHeight = background.getHeight();
    }   

    protected void onClick( int index ) {
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

    	final int count = getChildCount();
        final int top = count > 0 ? getChildAt(0).getTop() : 0;
        final int shelfWidth = mShelfWidth;
        final int shelfHeight = mShelfHeight;
        final int width = getWidth();
        final int height = getHeight();
        final Bitmap background = this.background;

        for (int y = top; y < height; y += shelfHeight) {
            Rect source = new Rect(0, 0, shelfWidth, shelfHeight);
            Rect dest = new Rect(0, y, width, y + shelfHeight );
            canvas.drawBitmap(background, source, dest, null);  
        }

        Rect source = new Rect(0, shelfHeight - top, shelfWidth, shelfHeight);
        Rect dest = new Rect(0, 0, width, top );              

        canvas.drawBitmap(background, source, dest, null);     

        super.dispatchDraw(canvas);
    } 
}
