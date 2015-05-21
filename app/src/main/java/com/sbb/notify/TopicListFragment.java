package com.sbb.notify;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sbb.notify.swipe.SwipeRefreshLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbb on 2015/5/21.
 */
public class TopicListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView listview;
    SwipeRefreshLayout swipe;
    List<TopicListBean> list = new ArrayList<>();
    TopicAdapter adapter = new TopicAdapter();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(adapter);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic;
    }

    @Override
    public void doRefresh() {
        swipe.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(getActivity(), "http://iguba.eastmoney.com/5384013884094322", null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String sub = responseString.substring(responseString.indexOf("var itemdata =") + "var itemdata =".length()).trim();
                String sub1 = sub.substring(0, sub.indexOf("};") + 1);
                try {
                    JSONObject response = new JSONObject(sub1);
                    JSONArray array = response.optJSONArray("re");
                    if (array != null) {
                        StringBuilder sb = new StringBuilder();
                        Gson gson = new Gson();
                        list = new ArrayList<TopicListBean>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.optJSONObject(i);
                            TopicListBean bean = gson.fromJson(jo.toString(), new TypeToken<TopicListBean>() {
                            }.getType());
                            list.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(sub1);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipe.setRefreshing(false);
            }
        });
    }

    class TopicAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        class ViewHolder{
            TextView name,code,time,title,content;

            public ViewHolder(View view){
                name = (TextView) view.findViewById(R.id.name);
                code = (TextView) view.findViewById(R.id.code);
                time = (TextView) view.findViewById(R.id.time);
                title= (TextView) view.findViewById(R.id.title);
                content= (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
