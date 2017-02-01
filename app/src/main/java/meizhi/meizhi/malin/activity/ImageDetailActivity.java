package meizhi.meizhi.malin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.adapter.DepthPageTransformer;
import meizhi.meizhi.malin.adapter.ImagePagerAdapter;
import meizhi.meizhi.malin.network.bean.ImageBean;
import meizhi.meizhi.malin.utils.HackyViewPager;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/01/31 18:21
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public class ImageDetailActivity extends AppCompatActivity {
    private ArrayList<ImageBean> mList;
    private int mPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Intent intent = getIntent();
        if (intent != null) {
            mPosition = intent.getIntExtra("position", 0);
            mList = intent.getParcelableArrayListExtra("datas");
        }

        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager_iv);
        setContentView(mViewPager);

        ImagePagerAdapter adapter = new ImagePagerAdapter();

        adapter.setData(mList, mPosition, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }
}
