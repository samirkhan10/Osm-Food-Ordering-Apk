package com.freshfastfood.fragment;

import static com.freshfastfood.utils.SessionManager.currncy;
import static com.freshfastfood.utils.SessionManager.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.freshfastfood.R;
import com.freshfastfood.activity.AboutsActivity;
import com.freshfastfood.activity.ContectusActivity;
import com.freshfastfood.activity.FeedBackActivity;
import com.freshfastfood.activity.LoginActivity;
import com.freshfastfood.activity.PrivecyPolicyActivity;
import com.freshfastfood.activity.ProfileActivity;
import com.freshfastfood.activity.TramsAndConditionActivity;
import com.freshfastfood.database.DatabaseHelper;
import com.freshfastfood.model.User;
import com.freshfastfood.utils.CustPrograssbar;
import com.freshfastfood.utils.SessionManager;

import java.util.Objects;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView txtMob,txtfirstl;
    TextView txtWallet;
    TextView txtEmail;

    LinearLayout myprofile;
    LinearLayout myoder;
    LinearLayout address;
    LinearLayout feedback;
    LinearLayout contect;
    LinearLayout logout;
    LinearLayout about;
    LinearLayout share;
    LinearLayout tramscondition;
    LinearLayout privecy;
    LinearLayout termcondition;


    User user;
    public static CustPrograssbar custPrograssbar;
    Fragment fragment1 = null;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        custPrograssbar = new CustPrograssbar();
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
        user = sessionManager.getUserDetails("");

        txtMob = view.findViewById(R.id.txt_mob);
        txtfirstl = view.findViewById(R.id.txtfirstl);
        txtWallet = view.findViewById(R.id.txt_wallet);
        txtEmail = view.findViewById(R.id.txt_email);
        myprofile = view.findViewById(R.id.myprofile);
        myoder = view.findViewById(R.id.myoder);
        address = view.findViewById(R.id.address);
        feedback = view.findViewById(R.id.feedback);
        contect = view.findViewById(R.id.contect);
        logout = view.findViewById(R.id.logout);
        share=view.findViewById(R.id.share);
        about = view.findViewById(R.id.about);
        tramscondition = view.findViewById(R.id.termcondition);
        privecy = view.findViewById(R.id.privecy);

        txtMob.setOnClickListener(this);
        txtfirstl.setOnClickListener(this);
        txtWallet.setOnClickListener(this);
        txtEmail.setOnClickListener(this);
        myprofile.setOnClickListener(this);
        myoder.setOnClickListener(this);
        address.setOnClickListener(this);
        feedback.setOnClickListener(this);
        contect.setOnClickListener(this);
        share.setOnClickListener(this);
        logout.setOnClickListener(this);
        about.setOnClickListener(this);
        tramscondition.setOnClickListener(this);
        privecy.setOnClickListener(this);

        setDrawer();
        return view;
    }


    public void setTxtWallet(String wallet) {
        if (sessionManager.getBooleanData(login)) {
            txtWallet.setVisibility(View.VISIBLE);
        } else {
            txtWallet.setVisibility(View.GONE);
        }
        txtWallet.setText(sessionManager.getStringData(currncy) + wallet);
    }

    @SuppressLint("SetTextI18n")
    private void setDrawer() {

        char first = user.getName().charAt(0);
        txtfirstl.setText("" + first);

        txtMob.setText("" + user.getMobile());
        txtEmail.setText("" + user.getEmail());


    }

    public void callFragment(Fragment fragmentClass) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentClass).addToBackStack("adds").commit();
    }




    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + requireActivity().getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        Bundle args;
        switch (v.getId()) {
            case R.id.myprofile:

                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(requireContext(), ProfileActivity.class));

                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                }
                break;
            case R.id.myoder:
                if (sessionManager.getBooleanData(login)) {
                    fragment = new MyOrderFragment();
                    callFragment(fragment);
                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                }
                break;
            case R.id.address:

                if (sessionManager.getBooleanData(login)) {
                    fragment = new AddressFragment();
                    callFragment(fragment);
                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                }
                break;
            case R.id.feedback:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(requireContext(), FeedBackActivity.class));

                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                }
                break;
            case R.id.contect:
                startActivity(new Intent(requireContext(), ContectusActivity.class));
                break;
            case R.id.logout:
                if (sessionManager.getBooleanData(login)) {
                    sessionManager.logoutUser();
                    databaseHelper.deleteCard();
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    requireActivity().finish();
                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    requireActivity().finish();
                }
                break;

            case R.id.share:
                shareApp();
                break;
            case R.id.about:

                startActivity(new Intent(requireContext(), AboutsActivity.class));
                break;
            case R.id.privecy:

                startActivity(new Intent(requireContext(), PrivecyPolicyActivity.class));
                break;
            case R.id.termcondition:
                startActivity(new Intent(requireContext(), TramsAndConditionActivity.class));
                break;
            default:
                break;
        }
    }
}