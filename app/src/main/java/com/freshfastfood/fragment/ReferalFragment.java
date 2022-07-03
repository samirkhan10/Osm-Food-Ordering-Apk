package com.freshfastfood.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.freshfastfood.BuildConfig;
import com.freshfastfood.R;
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

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReferalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferalFragment extends Fragment implements GetResult.MyListener,View.OnClickListener {
    TextView txtT1;
    TextView txtT2;
    TextView txtT3;
    TextView txtCode;
    TextView txtShare;
    TextView txtCopy;
    TextView inviteWhatsapp;
    TextView seeOffer;

    User user;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReferalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferalFragment newInstance(String param1, String param2) {
        ReferalFragment fragment = new ReferalFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_referal, container, false);
        txtT1=view.findViewById(R.id.txt_t1);
        txtT2=view.findViewById(R.id.txt_t2);
        txtT3=view.findViewById(R.id.txt_t3);
        txtCode=view.findViewById(R.id.txt_code);
        txtShare=view.findViewById(R.id.txt_share);
        txtCopy=view.findViewById(R.id.txt_copy);
        inviteWhatsapp=view.findViewById(R.id.invite_whatsapp);
        seeOffer=view.findViewById(R.id.see_offer);

        ButterKnife.bind(requireActivity());
        sessionManager = new SessionManager(requireActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        getData();


        txtCode.setOnClickListener(this);
        txtShare.setOnClickListener(this);
        txtCopy.setOnClickListener(this);
        inviteWhatsapp.setOnClickListener(this);
        seeOffer.setOnClickListener(this);

    return  view;
    }
    private void getData() {
        try {
            custPrograssbar.prograssCreate(requireActivity());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getDta((JsonObject) jsonParser.parse(jsonObject.toString()));
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
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    txtT2.setText("Friends get " + sessionManager.getStringData(SessionManager.currncy) + restResponse.getRefercredit() + " on their first Order");
                    txtT3.setText("You get " + sessionManager.getStringData(SessionManager.currncy) + restResponse.getSignupcredit() + " on your wallet");
                    txtCode.setText("" + restResponse.getCode());

                }

            }
        } catch (Exception e) {
            Log.e("Error",""+e.toString());
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "Hey! Now use our app to share with your family or friends. User will get wallet amount on your 1st successful order. Enter my referral code *" + txtCode.getText().toString() + "* & Enjoy your shopping !!!" ;
                    shareMessage = shareMessage + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Log.e("error", Objects.requireNonNull(e.getMessage()));
                }
                break;
            case R.id.see_offer:

                break;

            case R.id.txt_code:

                break;

            case R.id.invite_whatsapp:
                String link = "https://play.google.com/store/apps/details?id=";

                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link.toString()+"My Referral Code is " +txtCode.getText().toString());
                intent.putExtra(Intent.EXTRA_TITLE, "Fruit Zone");
                startActivity(Intent.createChooser(intent, "Share Link"));
                break;

            case R.id.txt_copy:
                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                ClipboardManager cm = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(txtCode.getText().toString());
                break;
            default:
                break;
        }
    }
}