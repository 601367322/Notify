package com.sbb.notify;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
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
    boolean isTopic = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
            isTopic = getArguments().getBoolean("isTopic");
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
        if (isTopic) {
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
                            Gson gson = new Gson();
                            list = new ArrayList<>();
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
        } else {
            httpClient.post(getActivity(), "http://iguba.eastmoney.com/action.aspx?action=getuserreply&uid=5384013884094322&rnd=1432015773727", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    JSONArray array = response.optJSONArray("re");
                    if (array != null) {
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.optJSONObject(i);
                            TopicListBean bean = gson.fromJson(jo.toString(), new TypeToken<TopicListBean>() {
                            }.getType());
                            list.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    swipe.setRefreshing(false);
                }
            });
        }
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
            ViewHolder hodler;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.topic_list_item, parent, false);
                hodler = new ViewHolder(convertView);
            } else {
                hodler = (ViewHolder) convertView.getTag();
            }
            TopicListBean bean = (TopicListBean) getItem(position);
            hodler.name.setText("[" + bean.sn + "]");
            if (bean.mc != null && !"".equals(bean.mc))
                hodler.code.setText("[" + bean.mc + "]");
            else {
                hodler.code.setVisibility(View.GONE);
            }
            hodler.title.setText(bean.tt);
            hodler.time.setText(bean.ti);
            hodler.content.setText(Html.fromHtml(bean.te));
            return convertView;
        }

        class ViewHolder {
            TextView name, code, time, title, content;

            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.name);
                code = (TextView) view.findViewById(R.id.code);
                time = (TextView) view.findViewById(R.id.time);
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.content);
                view.setTag(this);
            }
        }
    }
}
