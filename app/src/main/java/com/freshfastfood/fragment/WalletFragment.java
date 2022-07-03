package com.freshfastfood.fragment;

import static com.freshfastfood.fragment.OrderSumrryFragment.paymentsucsses;
import static com.freshfastfood.utils.SessionManager.currncy;
import static com.freshfastfood.utils.SessionManager.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.freshfastfood.R;
import com.freshfastfood.activity.PaypalActivity;
import com.freshfastfood.activity.RazerpayActivity;
import com.freshfastfood.model.Payment;
import com.freshfastfood.model.PaymentItem;
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

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment  implements GetResult.MyListener, View.OnClickListener {
    CardView oneMonth,threeMonth;
    LinearLayout oneMonthLay,threeMonthLay;
    ImageView oneMonthImg,threeMonthImg;
    TextView txtWallet;
    TextView month,amount;
    String edAmount;
    Spinner spinner;
    TextView txtPay;
    String payment = "";
    int postionss;
    CustPrograssbar custPrograssbar;
    List<PaymentItem> paymentList;
    User user;
    SessionManager sessionManager;
    ProfileFragment profileFragment=new ProfileFragment();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        txtWallet=view.findViewById(R.id.txt_wallet);
        //edAmount=view.findViewById(R.id.ed_amount);
        spinner=view.findViewById(R.id.spinner);
        txtPay=view.findViewById(R.id.txt_pay);

        oneMonth=view.findViewById(R.id.oneMonth);
        oneMonthLay=view.findViewById(R.id.oneMonthLay);
        oneMonthImg=view.findViewById(R.id.oneMonthImg);
        threeMonth=view.findViewById(R.id.threeMonth);
        threeMonthLay=view.findViewById(R.id.threeMonthLay);
        threeMonthImg=view.findViewById(R.id.threeMonthImg);
        month=view.findViewById(R.id.txt_month);
        amount=view.findViewById(R.id.txt_amount);

        ButterKnife.bind(requireActivity());
        sessionManager = new SessionManager(requireActivity());
        user = sessionManager.getUserDetails("");

        custPrograssbar = new CustPrograssbar();
       //  txtWallet.setText(getIntent().getStringExtra("wallat"));

        edAmount="129";
        month.setText("3 month");
        amount.setText("₹ 129");


        txtPay.setOnClickListener(this);

        oneMonth.setOnClickListener(v -> {
            oneMonthLay.setBackgroundResource(R.drawable.plain_card);
            threeMonthLay.setBackgroundResource(R.drawable.plain_card_empty);
            oneMonthImg.setImageResource(R.drawable.ic_baseline_check_circle_24);
            threeMonthImg.setImageResource(R.drawable.circle);
            month.setText("1 month");
            amount.setText("₹ 69");
            edAmount="69";
        });
        threeMonth.setOnClickListener(v -> {
            threeMonthLay.setBackgroundResource(R.drawable.plain_card);
            oneMonthLay.setBackgroundResource(R.drawable.plain_card_empty);
            threeMonthImg.setImageResource(R.drawable.ic_baseline_check_circle_24);
            oneMonthImg.setImageResource(R.drawable.circle);
            month.setText("3 month");
            amount.setText("₹ 129");
            edAmount="129";
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment = spinner.getItemAtPosition(position).toString();
                postionss = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getPayment();

        return view;
    }

    public void setWallet(String wallet) {
        if (sessionManager.getBooleanData(login)) {
            txtWallet.setVisibility(View.VISIBLE);
        } else {
            txtWallet.setVisibility(View.GONE);
        }
        txtWallet.setText(sessionManager.getStringData(currncy) + wallet);
    }

    private void getPayment() {
        custPrograssbar.prograssCreate(requireActivity());
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getpaymentgateway((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    private void sendPyment() {
        try {
            custPrograssbar.prograssCreate(requireActivity());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            jsonObject.put("wallet", edAmount);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().walletUpdate((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View view) {
        if (view.getId() == R.id.txt_pay) {

//            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                if (payment.equalsIgnoreCase("Razorpay")) {
                    int temtoal = Integer.parseInt(edAmount);
                    startActivity(new Intent(requireActivity(), RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentList.get(postionss)));
                } else if (payment.equalsIgnoreCase("Paypal")) {
                    Double totle = Double.parseDouble(edAmount);
                    startActivity(new Intent(requireActivity(), PaypalActivity.class).putExtra("amount", totle).putExtra("detail", paymentList.get(postionss)));
//                }
//            } else {
//                edAmount.setError("Enter Amount");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < payment.getData().size(); i++) {
                    if (payment.getData().get(i).getwShow().equalsIgnoreCase("1")) {
                        arealist.add(payment.getData().get(i).getTitle());
                    }
                }
                paymentList = payment.getData();
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinnercode_layout, arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);

            }
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(requireActivity(), restResponse.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
//               HomeActivity.getInstance().txtWallet.setText(sessionManager.getStringData(currncy) + restResponse.getWallet());
                    profileFragment.txtWallet.setText(sessionManager.getStringData(currncy) + restResponse.getWallet());
                    txtWallet.setText(sessionManager.getStringData(currncy) + restResponse.getWallet());
                   // edAmount.setText("");
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "" + e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            sendPyment();
        }
    }
}