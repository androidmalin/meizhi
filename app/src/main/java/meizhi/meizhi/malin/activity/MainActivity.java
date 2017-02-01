package meizhi.meizhi.malin.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import meizhi.meizhi.malin.R;
import meizhi.meizhi.malin.fragment.ImageListFragment;

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
public class MainActivity extends AppCompatActivity {

    private ImageListFragment mImageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        setNavigationBarColor();
    }

    private void setNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
    }

    private void setDefaultFragment() {
        if (mImageListFragment == null) {
            Bundle bundle = new Bundle();
            mImageListFragment = ImageListFragment.newInstance(bundle);
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.root_layout, mImageListFragment, "ImageListFragment");
        fragmentTransaction.commit();
    }
}
