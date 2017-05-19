package meizhi.meizhi.malin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import meizhi.meizhi.malin.R;
import tv.panda.live.image.DraweeTextView;

/**
 * @author  malin on 17-5-8.
 */

public class ImageActivity extends AppCompatActivity {

    DraweeTextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_img_layout);
        mTextView = (DraweeTextView) findViewById(R.id.d_tv_img);
//        mTextView.setSupportBackgroundTintList();
    }
}
