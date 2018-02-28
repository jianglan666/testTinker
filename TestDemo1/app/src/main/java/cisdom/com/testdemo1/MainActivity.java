package cisdom.com.testdemo1;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.viewPage_main)
    ViewPager viewPageMain;
    @InjectView(R.id.bottomBarItem_home_main)
    BottomBarItem bottomBarItemHomeMain;
    @InjectView(R.id.bottomBarItem_ball_main)
    BottomBarItem bottomBarItemBallMain;
    @InjectView(R.id.bottomBarItem_person_main)
    BottomBarItem bottomBarItemPersonMain;
    @InjectView(R.id.bottomBarLayout_main)
    BottomBarLayout bottomBarLayoutMain;

    private List<TabFragment> mFragmentList = new ArrayList<>();
    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        TabFragment homeFragment = new TabFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(TabFragment.CONTENT, "主页");
        homeFragment.setArguments(bundle1);
        mFragmentList.add(homeFragment);

        TabFragment ballFragment = new TabFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(TabFragment.CONTENT, "星球");
        ballFragment.setArguments(bundle2);
        mFragmentList.add(ballFragment);

        TabFragment personFragment = new TabFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(TabFragment.CONTENT, "个人");
        personFragment.setArguments(bundle3);
        mFragmentList.add(personFragment);

        viewPageMain.setAdapter(new MyAdapter(getSupportFragmentManager()));
        bottomBarLayoutMain.setViewPager(viewPageMain);
        //开启切换的滑动效果
        bottomBarLayoutMain.setSmoothScroll(true);

        bottomBarLayoutMain.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int i) {
                if (i == 0) {
                    if (bottomBarLayoutMain.getCurrentItem() == i) {
                        DoubleClick.registerDoubleClickListener(bottomBarItemHomeMain, new OnDoubleClickListener() {
                            @Override
                            public void OnSingleClick(View v) {
                                bottomBarItemHomeMain.setIconSelectedResourceId(R.mipmap.fresh);
                                bottomBarItemHomeMain.setStatus(true);

                                //播放旋转动画
                                if (mRotateAnimation == null) {
                                    mRotateAnimation = new RotateAnimation(0, 360,
                                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                            0.5f);
                                    mRotateAnimation.setDuration(800);
                                    mRotateAnimation.setRepeatCount(-1);
                                }
                                ImageView bottomImageView = bottomBarItem.getImageView();
                                bottomImageView.setAnimation(mRotateAnimation);
                                bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画

                                //模拟数据刷新完毕
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        bottomBarItemHomeMain.setIconSelectedResourceId(R.mipmap.home_sel);//更换成首页原来图标
                                        bottomBarItemHomeMain.setStatus(true);//刷新图标
                                        cancelTabLoading(bottomBarItemHomeMain);
                                    }
                                }, 3000);
                                return;
                            }

                            @Override
                            public void OnDoubleClick(View v) {
                                Log.i("start", "OnDoubleClick: ---->>start");
                                bottomBarItemHomeMain.setIconSelectedResourceId(R.mipmap.ic_launcher_round);//更换成首页原来图标
                                bottomBarItemHomeMain.setStatus(true);

                            }
                        });
                    }
                }

                //如果点击了其他条目
                BottomBarItem bottomItem = bottomBarLayoutMain.getBottomItem(0);
                bottomItem.setIconSelectedResourceId(R.mipmap.home_sel);//更换为原来的图标

                cancelTabLoading(bottomItem);//停止旋转动画
            }
        });

        bottomBarLayoutMain.setUnread(0, 4);
        bottomBarLayoutMain.showNotify(1);
        bottomBarLayoutMain.hideNotify(1);
        bottomBarLayoutMain.setMsg(2, "new");
    }

    /**
     * 停止首页页签的旋转动画
     */
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null) {
            animation.cancel();
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
