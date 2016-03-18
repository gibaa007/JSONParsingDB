package com.nagainfo.httpurljson;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by nagainfo on 11/3/16.
 */
public class ImageViewActivity extends Activity {
    private SimpleDraweeView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        image = (SimpleDraweeView) findViewById(R.id.image_view);
        String image_url = getIntent().getExtras().getString("image");
        Uri uri = Uri.parse(image_url);
        image.setImageURI(uri);
    }
}
