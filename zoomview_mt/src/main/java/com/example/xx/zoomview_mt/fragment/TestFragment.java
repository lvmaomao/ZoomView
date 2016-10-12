package com.example.xx.zoomview_mt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xx.zoomview_mt.R;
import com.yy.www.libs.TransitionFragmentMultiHelper;
import com.yy.www.libs.manager.Event;
import com.yy.www.libs.manager.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyu on 16/10/12.
 */
public class TestFragment extends Fragment implements TestAdapter.onImageViewClickListener {

    TransitionFragmentMultiHelper helper;
    LinearLayoutManager manager;

    RecyclerView rvImage;

    List<String> urls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        rvImage = (RecyclerView) view.findViewById(R.id.rvImage);
        initDummy();
        initRv();
        helper = new TransitionFragmentMultiHelper();
        setExitSharedElementCallback(helper.sharedElementCallback);
        EventManager.setEventListener(event);
        return view;
    }

    private void initDummy() {
        if (urls == null)
            urls = new ArrayList<>();
        urls.add("http://www.5djiaren.com/uploads/2016-06/08-165944_676.jpg");
        urls.add("http://www.5djiaren.com/uploads/2016-06/08-165945_452.jpg");
        urls.add("http://p.ishowx.com/uploads/allimg/160819/486-160QZT454.jpg");
        urls.add("http://dmr.nosdn.127.net/1o51ADEcJbidc5Y2FFYnfA==/6896093022349149358.jpg");
        urls.add("http://p.ishowx.com/uploads/allimg/160907/486-160ZG40449.jpg");
        urls.add("http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M0B/21/86/wKhTlFe7xeOEJDV4AAAAAPbonyI938.jpg");
        urls.add("http://p.ishowx.com/uploads/allimg/160902/415-160Z2093517.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=1770407502,1713614648&fm=11&gp=0.jpg");
        urls.add("http://img5.duitang.com/uploads/item/201407/27/20140727202737_sZLAX.jpeg");
    }

    private void initRv() {
        manager = new LinearLayoutManager(getActivity());
        rvImage.setLayoutManager(manager);
        rvImage.setAdapter(new TestAdapter(getActivity(), this, urls));
    }

    @Override
    public void onImageClick(View v, int position) {
        helper.startViewerActivity(getActivity(), v, (ArrayList<String>) urls, position);
    }

    private Event event = new Event() {
        @Override
        public void onSomethingHappened(int postion) {
            helper.update(postion, new TransitionFragmentMultiHelper.UpdateTransitionListener() {
                @Override
                public View updateView(int position) {
                    View view = manager.findViewByPosition(position).findViewById(R.id.ivImg);
                    return view;
                }

                @Override
                public String updateName(int position) {
                    return urls.get(position);
                }
            });
        }
    };


}
