package com.freshfastfood.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.freshfastfood.R;
import com.freshfastfood.database.DatabaseHelper;
import com.freshfastfood.model.User;
import com.freshfastfood.utils.CustPrograssbar;
import com.freshfastfood.utils.SessionManager;

import java.util.Objects;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment  implements  View.OnClickListener{
    EditText edSearch;
    ImageView imgSearch;
    LinearLayout lvlActionsearch, lvlMainhome, lvlHidecart;
    User user;
    public static MainFragment homeActivity = null;

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



    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        edSearch = view.findViewById(R.id.ed_search);
        imgSearch = view.findViewById(R.id.img_search);
        lvlActionsearch = view.findViewById(R.id.lvl_actionsearch);
        lvlMainhome = view.findViewById(R.id.lvl_mainhome);
        lvlHidecart = view.findViewById(R.id.lvl_hidecart);

        custPrograssbar = new CustPrograssbar();
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
        user = sessionManager.getUserDetails("");

        imgSearch.setOnClickListener(this);

        setDrawer();
        return view;
    }


    public void setFrameMargin(int top) {
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lvlMainhome.getLayoutParams();
//        params.setMargins(0, top, 0, 0);
//        lvlMainhome.setLayoutParams(params);
    }



    @SuppressLint("SetTextI18n")
    private void setDrawer() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack("HomePage").commit();
        addTextWatcher();

    }

    public EditText passThisEditText() {
        return edSearch;
    }

    private void addTextWatcher() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edSearch.getText().toString().trim().length() == 0) {
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_frame);
                    if (fragment instanceof HomeFragment && fragment.isVisible()) {
                        Log.e("no", "jsd");
                    } else {
                        getFragmentManager().popBackStackImmediate();

                    }
                }
            }
        });
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (edSearch.getText().toString().trim().length() != 0) {
                    Bundle args;
                    Fragment fragment;
                    args = new Bundle();
                    args.putInt("id", 0);
                    args.putString("search", edSearch.getText().toString().trim());
                    fragment = new ItemListFragment();
                    fragment.setArguments(args);
                    callingFragment(fragment);
                } else {
                    fragment1 = null;
                }
                return true;
            }
            return false;
        });
    }



    public void hideActionbar() {
//        appBarLayout.setVisibility(View.GONE);
        lvlHidecart.setVisibility(View.GONE);
//        drawer.setVisibility(View.GONE);
    }

    public void serchviewHide() {
        lvlActionsearch.setVisibility(View.GONE);
    }

    public void serchviewShow() {
//        lvlActionsearch.setVisibility(View.VISIBLE);
    }


    public void callingFragment(Fragment fragmentClass) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentClass).addToBackStack("adds").commit();
    }




//    @SuppressLint("SetTextI18n")
//    @OnClick({R.id.img_search})
//    public void onViewClicked(View view) {
//        Fragment fragment;
//        Bundle args;
//        if (view.getId() == R.id.img_search) {
//            if (edSearch.getText().toString().trim().length() != 0) {
//                args = new Bundle();
//                args.putInt("id", 0);
//                args.putString("search", edSearch.getText().toString().trim());
//                fragment = new ItemListFragment();
//                fragment.setArguments(args);
//                callFragment(fragment);
//            } else {
//                fragment1 = null;
//            }
//        }
//    }


    @Override
    public void onClick(View v) {
        Fragment fragment;
        Bundle args;
        if (v.getId() == R.id.img_search) {
            if (edSearch.getText().toString().trim().length() != 0) {
                args = new Bundle();
                args.putInt("id", 0);
                args.putString("search", edSearch.getText().toString().trim());
                fragment = new ItemListFragment();
                fragment.setArguments(args);
                callingFragment(fragment);
            } else {
                fragment1 = null;
            }
        }
    }
}