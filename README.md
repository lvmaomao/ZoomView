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

