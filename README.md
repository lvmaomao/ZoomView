
# 模仿即刻点击图片的效果
![mulit image](https://github.com/yyBetter/ZoomView/blob/master/gifs/zoom_mulit_view.gif)

![single image](https://github.com/yyBetter/ZoomView/blob/master/gifs/zoom_single_view.gif)

#ZoomViewHepler

**图片放大：放大动画，滑动关闭**


> `ImageView`的放大利用的就是`ActivityOptions`类，其中关键的两个元素`transitionView`和`transitionName` 将前后Activity元素设置相同就可以进行放大和缩小的位移动画了。

***

##使用方法

想要使用当前动画需要对Theme进行设置

```
<item name="android:windowContentTransitions">true</item>
```


ViewerActivity是图片展示的activity，需要在`AndroidManifest.xml`中注册；

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

### Activity多个ImageView放大
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

##补充

> 之前一直没有考虑到使用RecyclerView和Fragment下使用转场动画来实现图片放大的功能，现在补充一下：

### Fragment下Imageview放大问题
1，Fragment没有想Activity更换共享元素的方法。尝试使用Activity内的setExitSharedElementCallback（）方法去进行修改，但是发现callback并没有被回调。具体原因不是很清楚，所在使用Fragment作为缩略图页面。只能用父Activity持有Fragment的对象，在设置共享元素的时候让Fragment提供；（恶略的解决方案。只能解决简单的使用场景）

2，多层Fragment嵌套。如果还是用Activity持有的话代码入侵太严重，并且冗余的厉害，看下面的代码你害怕吗？

```
    eg：MainActivity->ParentFragment->ViewPager->ChildFragmentA
                                               ->ChildFragmentB
                                               ->ChildFragmentC
                                               ...
```
### RecyclerView下Imageview放大问题
正常情况下并没有问题，但是如果你需要展示的item比较多，并且在屏幕没无法展示的情况下就会遇到问题，由于缩略图页面和展示页面的元素相同才可以进行转场动画，这个时候，如果你去取reyclerView内item内的ImageView就会为null，recyclerView将不展示的item回收掉了。

***所以暂时的解决方式是，在Fragment和RecyclerView内Item过多的情况下，建议只使用放大功能，不关注关闭后缩回。
具体可以看一下demo***

