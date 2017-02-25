# fragmentTabHostDemo
FragmentTabHost的使用
1. xml中定义
<android.support.v4.app.FragmentTabHost
            android:id="@+id/tabHost"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="3dp"
        />
2.初始化TabHost
mMainTabhost.setup(this, getSupportFragmentManager(), R.id.main_fl_content);

if (android.os.Build.VERSION.SDK_INT > 10) {
        // 去除分割线
        mTabHost.getTabWidget().setShowDividers(0);
 }
初始化TabHost.TabSpec以及Fragment
//创建TabSpec
TabHost.TabSpec tabSpec1 = mMainTabhost.newTabSpec("tag1");
tabSpec1.setIndicator("label1");

//添加TabSpec到TabHost
mMainTabhost.addTab(tabSpec1, TabSpec1Fragment.class, null);
分析常见一个TabSpec需要啥
public int   noteId;//文字id,tag文字
public int   iconId;//图标
public Class clz;//对应的fragment
普通类的方式封装
 class MainBottomBean {
    public int   noteId;//文字id,tag文字
    public int   iconId;//图标
    public Class clz;//对应的fragment

    public MainBottomBean(int noteId, int iconId, Class clz) {
        this.noteId = noteId;
        this.iconId = iconId;
        this.clz = clz;
    }
}

     mainBottomBeanList.add(new MainBottomBean(R.string.main_tab_name_news, R.drawable.tab_icon_new, new ZHFragment().getClass()));
    mainBottomBeanList.add(new MainBottomBean(R.string.main_tab_name_tweet, R.drawable.tab_icon_tweet, new ZHFragment().getClass()));
    mainBottomBeanList.add(new MainBottomBean(R.string.main_tab_name_quick, R.drawable.tab_icon_me, null));
    mainBottomBeanList.add(new MainBottomBean(R.string.main_tab_name_explore, R.drawable.tab_icon_explore, new ZHFragment().getClass()));
    mainBottomBeanList.add(new MainBottomBean(R.string.main_tab_name_my, R.drawable.tab_icon_me, new ZHFragment().getClass()));
枚举方式封装创建TabSpec需要的内容
 NEWS(0, R.string.main_tab_name_news, R.drawable.tab_icon_new,
        GeneralViewPagerFragment.class),

TWEET(1, R.string.main_tab_name_tweet, R.drawable.tab_icon_tweet,
        TweetsViewPagerFragment.class),

QUICK(2, R.string.main_tab_name_quick, R.drawable.tab_icon_new,
        null),

EXPLORE(3, R.string.main_tab_name_explore, R.drawable.tab_icon_explore,
        ExploreFragment.class),

ME(4, R.string.main_tab_name_my, R.drawable.tab_icon_me,
        MyInformationFragment.class);
引入SmartTabLayout
依赖

compile 'com.ogaclejapan.smarttablayout:library:1.6.1@aar'

修改属性

app:stl_distributeEvenly="true"//均分

app:stl_dividerThickness="0dp"//去掉分割

//修改文本颜色 app:stlunderlineColor="#06ed63" app:stlindicatorColor="#06ed63"

//自定义布局,控制tab的选中效果 app:stlcustomTabTextLayoutId="@layout/customtab" app:stlcustomTabTextViewId="@id/customtext"

//option,设置indicator两种效果 //下划线 app:stlindicatorColor="#06ed63" app:stlindicatorThickness="4dp" app:stl_indicatorGravity="bottom"

//滑块 app:stlindicatorColor="#33000000" app:stlindicatorCornerRadius="18dp" app:stlindicatorThickness="36dp" app:stlindicatorGravity="center"

选中的时候修改颜色,自定义TabView

app:stl_customTabTextLayoutId="@layout/custom_tab"
app:stl_customTabTextViewId="@id/custom_text"

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:gravity="center"
              android:orientation="vertical">


    <TextView
        android:id="@+id/custom_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/tab_text"
        android:textSize="14sp"/>
</LinearLayout> 


<?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:color="#0cce5c" android:state_pressed="true"></item>
        <item android:color="#0cce5c" android:state_selected="true"></item>
        <item android:color="#999"></item>
    </selector>
BaseTabFragment抽取
带有tab标签页面的Fragment
分析NewsFragment对应的adapter
分析MyFragmentStatePagerAdapter

   class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ViewPagerFragment();
    }

    @Override
    public int getCount() {
        if (mTitleArr != null) {
            return mTitleArr.length;
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleArr[position];
    }
}
做解耦处理

 class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {


    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
     }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mContext, clz, args);
    }

    @Override
    public int getCount() {
        if (mTabInfos != null) {
            return mTabInfos.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title;
    }
}

 class TabInfo {
    public Class  clz;
    public Bundle args;
    public String title;
}
解耦完成

private List<TabInfo> mTabInfos = new ArrayList<>();
Context        mContext;
ViewPager      mViewPager;
SmartTabLayout mTab;

public void setTabInfos(List<TabInfo> tabInfos) {
    mTabInfos = tabInfos;
    notifyDataSetChanged();
    mTab.setViewPager(mViewPager);
}

public MyFragmentStatePagerAdapter(FragmentManager fm, Context context, ViewPager viewPager, SmartTabLayout tab) {
    super(fm);
    mContext = context;
    mViewPager = viewPager;
    mTab = tab;
}

@Override
public Fragment getItem(int position) {
    TabInfo info = mTabInfos.get(position);
    return Fragment.instantiate(mContext, info.clz.getName(), info.args);
}

@Override
public int getCount() {
    if (mTabInfos != null) {
        return mTabInfos.size();
    }
    return 0;
}

@Override
public CharSequence getPageTitle(int position) {
    System.out.println("title");
    TabInfo info = mTabInfos.get(position);
    return info.title;
}
修改NewsFragment位如下

    public class NewsFragment extends BaseFragment {

    @Bind(R.id.tab)
    SmartTabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager      mViewpager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, null);
    }

    @Override
    public void initData() {
        //模拟数据
        MyFragmentStatePagerAdapter adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), mContext, mViewpager, mTab);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);

        List<TabInfo> tabInfos = new ArrayList<>();
        tabInfos.add(new TabInfo("咨询", NewsPagerFragment.class, null));
        tabInfos.add(new TabInfo("热点", NewsPagerFragment.class, null));
        tabInfos.add(new TabInfo("博客", NewsPagerFragment.class, null));
        tabInfos.add(new TabInfo("推荐", NewsPagerFragment.class, null));
        adapter.setTabInfos(tabInfos);
    }

}
复制方式创建TweetFragment

针对NewsFragment和TweetFragment抽取基类,创建BaseTabFragment,因为两者很多地方相同,只需要拷贝NewsFragment做一些共性分析即可完成

public abstract class BaseTabFragment extends BaseFragment {
@Bind(R.id.tab)
SmartTabLayout mTab;
@Bind(R.id.viewpager)
ViewPager      mViewpager;
private String[] mTitleArr;

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_tabs, null);
}

@Override
public void initData() {
    //模拟数据
    MyFragmentStatePagerAdapter adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), mContext, mViewpager, mTab);
    mViewpager.setAdapter(adapter);
    mTab.setViewPager(mViewpager);

    List<TabInfo> tabInfos = onInitTabInfo();

    adapter.setTabInfos(tabInfos);
}

@NonNull
public abstract List<TabInfo> onInitTabInfo();
}

修改NewsFragment
