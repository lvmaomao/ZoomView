#ZoomViewHepler

**图片放大：放大动画，滑动关闭**


> `ImageView`的放大利用的就是`ActivityOptions`类，其中关键的两个元素`transitionView`和`transitionName` 将前后Activity元素设置相同就可以进行放大和缩小的位移动画了。

***

##使用方法
`ViewerActivity`是图片展示的activity，需要在`AndroidManifest.xml`中注册；

```
<activity android:name="com.yy.www.libs.view.ViewerActivity"/>
```


### 单个ImageView放大
`TransitionSingleHelper` 针对单个View的辅助类，首先在当前页面进行初始化

```
TransitionSingleHelper singleHelper;
singleHelper = new TransitionSingleHelper();
```

需要配置当前页面的`transitionView`和`TransitionName`；

```
setExitSharedElementCallback(singleHelper.sharedElementCallback);
```
跳转的时候直接执行`startViewerActivity`，犹豫图片放大基本都是网络图片，所以直接将`transitionName`设置成了url；

```
singleHelper.startViewerActivity(OneActivity.this, v, url);
```

### 多个ImageView放大
其实多个图片与单个图片主要的不同就是返回缩略图时候transitionView和transitionName的同步问题，返回之后我们需要更新一下，我还是将主要的代码贴出。

```
private TransitionMultiHelper multiHelper;
multiHelper = new TransitionMultiHelper();
setExitSharedElementCallback(helper.sharedElementCallback);
```
重点的地方来了，我们需要复写Activity下的onActivityReenter()方法，当前方法下我们可以拿到放大页面中更改后的transitionView和transitionName;

```
@Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        multiHelper.update(data, new TransitionMultiHelper.UpdateTransitionListener() {
            @Override
            public View updateView(int position) {
                return ViewGroup.getImageView(position);
            }

            @Override
            public String updateName(int position) {
                return Urls.get(position);

            }
        });
    }
```
最后就是打开新页面的方法也不同, 将url替换成了ArrayList<String> 并且增加了一个Index，为了选择url和设置viewPager

```
multiHelper.startViewerActivity(TwoActivity.this, view, (ArrayList<String>) Urls, index);
```
