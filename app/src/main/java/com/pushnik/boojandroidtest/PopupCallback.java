package com.pushnik.boojandroidtest;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by John on 4/21/2018.
 */

public interface PopupCallback {
    public void showPopup(View view, Realtor realtor);
    public void loadImage(ImageView imageView, String url, int width);
}
