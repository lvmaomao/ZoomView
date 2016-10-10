<<<<<<< HEAD
# 模仿即刻点击图片的效果
![mulit image](https://github.com/yyBetter/ZoomView/blob/master/gifs/zoom_mulit_view.gif)

![single image](https://github.com/yyBetter/ZoomView/blob/master/gifs/zoom_single_view.gif)

### ActivityOptionsCompat

**ActivityOptionsCompat**是Material Design中Activity的新的切换动画

想要使用当前动画需要对Theme进行设置
`<item name="android:windowContentTransitions">true</item>`

> 单个图片放大

使用makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName)方法平滑的将一个控件平移的过渡到第二个activity



```
Intent intent = new Intent(MainActivity.this, ViewerActivity.class);
ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, urlStrings.get(index));
activity.startActivity(intent, optionsCompat.toBundle());
```

其中**sharedElement**是需要进行转场动画共享的元素也就是需要进行移动的**View**, **sharedElementName**是对当前**View**的描述，在展示页面ViewerActivity中我们都是必要的。



由于我们在上个页面传递了**sharedElement **与**sharedElementName**参数，那么在**ViewerActivity**设置，如果传递与设置的参数不同动画是不会展示的。

```
setEnterSharedElementCallback(new SharedElementCallback() {
    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        if (mUrlStrings != null && mUrlStrings.size() > 0) {
            String url = mUrlStrings.get(pager.getCurrentItem());
            sharedElements.clear();
            sharedElements.put(url, getCurrent().getSharedElement());
        }
    }
});
```

当需要结束当前Activity并回退这个动画时调用**Activity.finishAfterTransition()**方法

如果当前页面是一个参数的时候我们就可以进行展示了

> 多个图片放大

上文提到过，前后两个Activity中 **sharedElement **与**sharedElementName**参数必须相同，举个例子，如果在**ViewerActivity**关闭时没有更新**MainActivity**内部的 **sharedElement **与**sharedElementName**那么结束的动画并不能返回到我们已经滑动到的位置，所以我们重写**supportFinishAfterTransition()**

```
@Override
public void supportFinishAfterTransition() {
    Intent data = new Intent();
    data.putExtra("index", pager.getCurrentItem());
    setResult(RESULT_OK, data);
    super.supportFinishAfterTransition();
}
```

并且在**MainActivity**的**onActivityReenter()**回掉中接受位置

```
@Override
public void onActivityReenter(int resultCode, Intent data) {
    super.onActivityReenter(resultCode, data);
    transitionState = new Bundle(data.getExtras());
}
```

再通过setExitSharedElementCallback更新**MainActivity**内部的 **sharedElement **与**sharedElementName**，就可以统一两个页面的元素达到正常的展示效果。

```
setExitSharedElementCallback(new SharedElementCallback() {
    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        if (transitionState != null) {
            int index = transitionState.getInt("index", 0);
            String url = getBackTransitionName(index);
            View view = getBackTransitionView(index);
            sharedElements.clear();
            sharedElements.put(url, view);
            transitionState = null;
        }
    }
});
```

这样我们就可以实现多图片的展示功能；

> 上下滑动关闭当前页面

主要的功能实现是根据ViewDragHelper类进行操作，计算滑动的y轴距离，来更换背景色的透明度，达到位置后执行**Activity.finishAfterTransition()**完成关闭动画。

其中需要提到一点，为了使图片双击放大，使用了[PhotoView](https://github.com/chrisbanes/PhotoView)第三方库，在多指触碰的时候会出现异常，因此我们需要对`onInterceptTouchEvent`进行处理

```
public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
        return this.dragger.shouldInterceptTouchEvent(ev);
    } catch (Exception e) {
        return false;
    }
}
```

同时提一下，ViewPager也会出现此问题，处理方法同上。自定义一个ViewPager然后对**onInterceptTouchEvent**进行异常包裹。

功能提取自：**[馒头先生](https://github.com/oxoooo/mr-mantou-android)**

=======
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
>>>>>>> feature_picasso
