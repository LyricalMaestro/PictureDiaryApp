package com.lyricaloriginal.picturediaryapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by LyricalMaestro on 15/01/16.
 */
public class LinedEditText extends EditText {

    public LinedEditText(Context context) {
        super(context);
    }

    public LinedEditText(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // EditTextビューのレイアウト後の幅
        int width = getMeasuredWidth();
        // EditTextビューのレイアウト後の高さ。パディングを考慮する必要がある。
        int height = getMeasuredHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom();
        // パディングを考慮
        int paddingTop = getExtendedPaddingTop();
        // テキストの高さ
        int lineHeight = getLineHeight();
        // 有効な描画領域から行数を計算
        int textCount = height / lineHeight;
        // 入力された行数。画面の高さより大きくなることがある
        int lines = this.getLineCount();
        // 有効な行数と実際に入力された行数のうち、大きい方
        int lineCount = Math.max(textCount, lines);

        float[] points = new float[lineCount << 2];

        // 複数の直線を一気に描画するので座標位置を計算
        for (int i = 0; i < lineCount; i++) {
            points[(i << 2) + 0] = 0;
            points[(i << 2) + 1] = i * lineHeight + paddingTop;
            points[(i << 2) + 2] = width;
            points[(i << 2) + 3] = i * lineHeight;
        }

        // 罫線色の設定
        Paint paint = getPaint();
        paint.setColor(getContext().getResources().getColor(R.color.lineColor));

        // 直線の描画
        canvas.drawLines(points, paint);

        // 親クラスの描画処理
        super.onDraw(canvas);
    }
}
