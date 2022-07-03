package com.freshfastfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.freshfastfood.R;
import com.freshfastfood.activity.HomeActivity;
import com.freshfastfood.activity.MyOrderListActivity;
import com.freshfastfood.model.Order;
import com.freshfastfood.model.OrderDatum;
import com.freshfastfood.model.RestResponse;
import com.freshfastfood.model.User;
import com.freshfastfood.retrofit.APIClient;
import com.freshfastfood.retrofit.GetResult;
import com.freshfastfood.utils.CustPrograssbar;
import com.freshfastfood.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.freshfastfood.utils.SessionManager.currncy;


public class MyOrderFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.lvl_mycard)
    LinearLayout lvlMycard;
    Unbinder unbinder;
    @BindView(R.id.txt_notfound)
    TextView txtNotfound;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    MainFragment mainFragment=new MainFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SessionManager sessionManager;
    User user;
    List<OrderDatum> orderData;
    CustPrograssbar custPrograssbar;
    int positionOrd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        orderData = new ArrayList<>();
//        HomeActivity.getInstance().setFrameMargin(0);
        mainFragment.setFrameMargin(0);
        getHistry();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void getHistry() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getHistory((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            if(callNo.equalsIgnoreCase("1")){
                Gson gson = new Gson();
                Order order = gson.fromJson(result.toString(), Order.class);
                if (order.getResult().equals("true")) {
                    orderData = new ArrayList<>();
                    orderData.addAll(order.getData());
                    if (!orderData.isEmpty()) {
                        setJoinPlayrList(lvlMycard, orderData);
                    } else {
                        custPrograssbar.closePrograssBar();
                        lvlNotfound.setVisibility(View.VISIBLE);
                        txtNotfound.setText("" + order.getResponseMsg());
                    }
                }else {
                    custPrograssbar.closePrograssBar();
                    lvlNotfound.setVisibility(View.VISIBLE);
                    txtNotfound.setText("" + order.getResponseMsg());
                }

            }else if (callNo.equalsIgnoreCase("2")) {
                Log.e("Response", "-->" + result);
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(getActivity(), response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                    OrderDatum datum=orderData.get(positionOrd);
                    datum.setStatus("cancelled");
                    orderData.set(positionOrd,datum);
                    setJoinPlayrList(lvlMycard, orderData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setJoinPlayrList(LinearLayout lnrView, List<OrderDatum> orderData) {
        if (lnrView == null) {
            return;
        }
        lnrView.removeAllViews();
        int a = 0;
        if (orderData != null && !orderData.isEmpty()) {
            for (int i = 0; i < orderData.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_oder, null);
                TextView txtOrderid = view.findViewById(R.id.txt_orderid);
                TextView txtInfo = view.findViewById(R.id.txt_info);
                TextView txtOrdcancel = view.findViewById(R.id.txt_ordcancel);
                LinearLayout lvlCancel = view.findViewById(R.id.lvl_cancel);
                TextView txtStatus = view.findViewById(R.id.txt_status);
                TextView txtTotal = view.findViewById(R.id.txt_total);
                txtOrderid.setText("Order ID:" + orderData.get(i).getId());
                txtTotal.setText(sessionManager.getStringData(currncy) + orderData.get(i).getTotalamt());
                if (orderData.get(i).getStatus().equalsIgnoreCase("completed")) {
                    txtStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                if (orderData.get(i).getStatus().equalsIgnoreCase("pending") || orderData.get(i).getStatus().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                    lvlCancel.setVisibility(View.VISIBLE);
                } else {
                    lvlCancel.setVisibility(View.GONE);
                }
                txtStatus.setText("" + orderData.get(i).getStatus());
                lnrView.addView(view);
                int finalI = i;
                txtOrdcancel.setOnClickListener(v -> {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getActivity());
                    //Setting message manually and performing action on button click
                    builder.setMessage("Are you sure cancel this order ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {

                                positionOrd=finalI;
                                getOdercancle(orderData.get(finalI).getId());
                            })
                            .setNegativeButton("No", (dialog, id) -> {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                });
                txtInfo.setOnClickListener(v -> getActivity().startActivity(new Intent(getActivity(), MyOrderListActivity.class).putExtra("oid", orderData.get(finalI).getId())));
            }
            custPrograssbar.closePrograssBar();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onResume() {
        super.onResume();
//        HomeActivity.getInstance().serchviewHide();
//        HomeActivity.getInstance().setFrameMargin(0);
        mainFragment.serchviewHide();
        mainFragment.setFrameMargin(0);
        if (!orderData.isEmpty()) {
            getHistry();
        }
    }
    private void getOdercancle(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", id);
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getOdercancle((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
