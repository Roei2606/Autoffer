package com.example.autofferandroid.transformers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class DoorOpenPageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setCameraDistance(20000);

        if (position < -1) { // עמוד מחוץ למסך
            page.setAlpha(0);
        } else if (position <= 0) { // עמוד יוצא - פתיחה לצד שמאל
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(90 * Math.abs(position));
        } else if (position <= 1) { // עמוד נכנס - פתיחה מצד ימין
            page.setAlpha(1);
            page.setPivotX(0);
            page.setRotationY(-90 * Math.abs(position));
        } else { // עמוד מחוץ למסך
            page.setAlpha(0);
        }
    }
}
