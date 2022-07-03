package com.freshfastfood.activity;

import static com.freshfastfood.fragment.ItemListFragment.itemListFragment;
import static com.freshfastfood.fragment.OrderSumrryFragment.isorder;
import static com.freshfastfood.utils.SessionManager.currncy;
import static com.freshfastfood.utils.SessionManager.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freshfastfood.R;
import com.freshfastfood.database.DatabaseHelper;
import com.freshfastfood.fragment.CardFragment;
import com.freshfastfood.fragment.HomeFragment;
import com.freshfastfood.fragment.MainFragment;
import com.freshfastfood.fragment.MyOrderFragment;
import com.freshfastfood.fragment.OrderSumrryFragment;
import com.freshfastfood.fragment.ProfileFragment;
import com.freshfastfood.fragment.ReferalFragment;
import com.freshfastfood.fragment.WalletFragment;
import com.freshfastfood.model.User;
import com.freshfastfood.utils.CustPrograssbar;
import com.freshfastfood.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPageActivity extends AppCompatActivity {
    EditText locSearch;
    ImageView locImgSearch, closeLay;
    TextView useCurrentLocation, addAddress;
    ImageView imgCart, imgNoti;
    RelativeLayout rltCart, rltNoti;
    TextView txtCountcard, txtActiontitle;
    Toolbar toolbar;

    User user;
    public static TextView txtCountcard1;
    public static MainPageActivity mainPageActivity = null;
    public static TextView txtNoti;
    public static CustPrograssbar custPrograssbar;
    Fragment fragment1 = null;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;
    Fragment fragment;
    ImageView iv_fruitZone,iv_search,iv_valet,iv_referral,iv_account;
    TextView tv_fruitZone,tv_search,tv_valet,tv_referral,tv_account;
    private ViewGroup hiddenPanel;
    private boolean isPanelShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);


//        setupNavigationView();
        //init imageview
        iv_fruitZone=findViewById(R.id.imageFruit);
        iv_search=findViewById(R.id.image_search);
        iv_valet=findViewById(R.id.image_valet);
        iv_referral=findViewById(R.id.image_referral);
        iv_account=findViewById(R.id.image_account);
        //init textview
        tv_fruitZone=findViewById(R.id.textFruit);
        tv_search=findViewById(R.id.text_search);
        tv_valet=findViewById(R.id.text_valet);
        tv_referral=findViewById(R.id.text_referral);
        tv_account=findViewById(R.id.text_account);
        imgNoti = findViewById(R.id.img_noti);
        imgCart = findViewById(R.id.img_cart);
        rltCart = findViewById(R.id.rlt_cart);
        rltNoti = findViewById(R.id.rlt_noti);
        txtCountcard = findViewById(R.id.txt_countcard);
        txtActiontitle = findViewById(R.id.txt_actiontitle);
        txtNoti = findViewById(R.id.txt_noti);
        toolbar = findViewById(R.id.toolbar);

        locImgSearch = findViewById(R.id.loc_img_search);
        locSearch = findViewById(R.id.loc_search);
        closeLay = findViewById(R.id.img_close_lay);
        useCurrentLocation = findViewById(R.id.txt_uselocation);
        addAddress = findViewById(R.id.txt_addlocation);

        hiddenPanel = (ViewGroup) findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        isPanelShown = false;

        custPrograssbar = new CustPrograssbar();
        databaseHelper = new DatabaseHelper(MainPageActivity.this);
        sessionManager = new SessionManager(MainPageActivity.this);
        user = sessionManager.getUserDetails("");
        mainPageActivity = this;
        setDrawer();
        fruitZone1();
    }

    public static MainPageActivity getInstance() {
        return mainPageActivity;
    }

    public void showMenu() {
        rltNoti.setVisibility(View.GONE);
        rltCart.setVisibility(View.VISIBLE);
    }
//
//    public void setFrameMargin(int top) {
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lvlMainhome.getLayoutParams();
//        params.setMargins(0, top, 0, 0);
//        lvlMainhome.setLayoutParams(params);
//    }

//    public void setTxtWallet(String wallet) {
//        if (sessionManager.getBooleanData(login)) {
//            txtWallet.setVisibility(View.VISIBLE);
//        } else {
//            txtWallet.setVisibility(View.GONE);
//        }
//        txtWallet.setText(sessionManager.getStringData(currncy) + wallet);
//    }

    @SuppressLint("SetTextI18n")
    private void setDrawer() {
        char first = user.getName().charAt(0);
        Log.e("first", "-->" + first);
        titleChange();
        txtCountcard1 = findViewById(R.id.txt_countcard);
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txtCountcard1.setText("0");
        } else {
            txtCountcard1.setText("" + res.getCount());
        }
    }



    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);

        if (fragment instanceof HomeFragment && fragment.isVisible()) {
        finish();

        } else if (fragment instanceof OrderSumrryFragment && fragment.isVisible() && isorder) {
            isorder = false;
            Intent i = new Intent(this, MainPageActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(0, 0);
        } else if (fragment instanceof MyOrderFragment && fragment.isVisible()) {
            Intent i = new Intent(this, MainPageActivity.class);startActivity(i);
            overridePendingTransition(0, 0);
        }

        else{  if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else
            super.onBackPressed();

    }


        if (itemListFragment == null) {
            rltNoti.setVisibility(View.VISIBLE);
            rltCart.setVisibility(View.VISIBLE);
        }

    }

    public void setdata() {
        rltNoti.setVisibility(View.VISIBLE);
        rltCart.setVisibility(View.VISIBLE);
    }


    public void titleChange(String s) {
        txtActiontitle.setText(s);
    }

    public void titleChange() {
        txtActiontitle.setText("Hello " + user.getName());
    }


    public void callFragment(Fragment fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentClass).addToBackStack("adds").commit();
        //drawerLayout.closeDrawer(GravityCompat.START);
    }


    @SuppressLint("SetTextI18n")
//    @OnClick({R.id.img_close, R.id.myprofile, R.id.myoder, R.id.address, R.id.feedback, R.id.contect, R.id.logout, R.id.about, R.id.privecy, R.id.img_search, R.id.img_cart, R.id.img_noti, R.id.lvl_home, R.id.share, R.id.termcondition, R.id.mywallet, R.id.refer})
    @OnClick({R.id.img_close_lay, R.id.img_cart, R.id.img_noti})

    public void onViewClicked(View view) {
        Fragment fragment;
        Bundle args;
        switch (view.getId()) {

            case R.id.img_cart:
                txtActiontitle.setVisibility(View.VISIBLE);
                rltNoti.setVisibility(View.GONE);
                rltCart.setVisibility(View.VISIBLE);
                txtActiontitle.setText("MyCart");
                fragment = new CardFragment();
              //  callFragment(fragment);
                pushFragment(fragment);
                break;
            case R.id.img_noti:
                titleChange();
                startActivity(new Intent(MainPageActivity.this, NotificationActivity.class));
                break;
            case R.id.img_close_lay:
                Animation bottomDown = AnimationUtils.loadAnimation(this,
                        R.anim.bottom_down);

                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public static void notificationCount(int i) {
        if (i <= 0) {
            txtNoti.setVisibility(View.GONE);
        } else {
            txtNoti.setVisibility(View.VISIBLE);
            txtNoti.setText("" + i);
        }
    }

//    private void setupNavigationView() {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        if (bottomNavigationView != null) {
//
//            // Select first menu item by default and show Fragment accordingly.
//            Menu menu = bottomNavigationView.getMenu();
//            selectFragment(menu.getItem(0));
//
//            // Set action to perform when any menu-item is selected.
//            bottomNavigationView.setOnNavigationItemSelectedListener(
//                    new BottomNavigationView.OnNavigationItemSelectedListener() {
//                        @Override
//                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                            selectFragment(item);
//                            return true;
//                        }
//                    });
//        }
//    }

    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
//    @SuppressLint("NonConstantResourceId")
//    protected void selectFragment(MenuItem item) {
//
//        Animation bottomDown;
//        item.setChecked(true);
//
//        switch (item.getItemId()) {
//            case R.id.action_app:
//                // Action to perform when Home Menu item is selected.
////                pushFragment(new MainFragment());
//                fragment = new MainFragment();
//                pushFragment(fragment);
//                break;
//            case R.id.action_search:
//                // Action to perform when Home Menu item is selected.
//                Animation bottomUp = AnimationUtils.loadAnimation(this,
//                        R.anim.bottom_up);
//                hiddenPanel.startAnimation(bottomUp);
//                hiddenPanel.setVisibility(View.VISIBLE);
//                break;
//            case R.id.action_wallet:
//                // Action to perform when Home Menu item is selected.
//                // pushFragment(new WalletFragment());
//                fragment = new WalletFragment();
//                pushFragment(fragment);
//                break;
//            case R.id.action_referal:
//                // Action to perform when Bag Menu item is selected.
//                fragment = new ReferalFragment();
//                pushFragment(fragment);
//                break;
//            case R.id.action_account:
//                // Action to perform when Account Menu item is selected.
//                fragment = new ProfileFragment();
//                pushFragment(fragment);
//                break;
//        }
//    }



    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (ft != null) {
                    ft.replace(R.id.rootLayout, fragment);
                    ft.commit();
                }
            }
        }
    }
public void fruitZone1()
{
    fragment = new MainFragment();
    pushFragment(fragment);
    tv_fruitZone.setTextColor(Color.parseColor("#009345"));
    tv_search.setTextColor(Color.GRAY);
    tv_referral.setTextColor(Color.GRAY);
    tv_account.setTextColor(Color.GRAY);
    tv_valet.setTextColor(Color.GRAY);
    iv_fruitZone.setBackgroundResource(R.drawable.app_icon_main);
    iv_search.setBackgroundResource(R.drawable.search_icon);
    iv_referral.setBackgroundResource(R.drawable.referal);
    iv_account.setBackgroundResource(R.drawable.save_as);
}
    public void fruitZone(View view) {

        // Action to perform when Home Menu item is selected.
//                pushFragment(new MainFragment());
        fragment = new MainFragment();
        pushFragment(fragment);
        tv_fruitZone.setTextColor(Color.parseColor("#009345"));
        tv_search.setTextColor(Color.GRAY);
        tv_referral.setTextColor(Color.GRAY);
        tv_account.setTextColor(Color.GRAY);
        tv_valet.setTextColor(Color.GRAY);
        iv_search.setBackgroundResource(R.drawable.search_icon);
        iv_fruitZone.setBackgroundResource(R.drawable.app_icon_main);
        iv_referral.setBackgroundResource(R.drawable.referal);
        iv_account.setBackgroundResource(R.drawable.save_as);

    }

    public void search(View view) {
        Animation bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.VISIBLE);
        tv_search.setTextColor(Color.parseColor("#009345"));
        tv_fruitZone.setTextColor(Color.GRAY);
        tv_referral.setTextColor(Color.GRAY);
        tv_account.setTextColor(Color.GRAY);
        tv_valet.setTextColor(Color.GRAY);
        iv_fruitZone.setBackgroundResource(R.drawable.fr_zone);
        iv_search.setBackgroundResource(R.drawable.search_green);
        iv_referral.setBackgroundResource(R.drawable.referal);
        iv_account.setBackgroundResource(R.drawable.save_as);
    }

    public void valet(View view) {
        // Action to perform when Home Menu item is selected.
        // pushFragment(new WalletFragment());
        fragment = new WalletFragment();
        pushFragment(fragment);
        tv_valet.setTextColor(Color.parseColor("#009345"));
        tv_fruitZone.setTextColor(Color.GRAY);
        tv_referral.setTextColor(Color.GRAY);
        tv_account.setTextColor(Color.GRAY);
        tv_search.setTextColor(Color.GRAY);
        iv_fruitZone.setBackgroundResource(R.drawable.fr_zone);

        iv_search.setBackgroundResource(R.drawable.search_icon);

        iv_referral.setBackgroundResource(R.drawable.referal);
        iv_account.setBackgroundResource(R.drawable.save_as);
    }

    public void referral(View view) {
        // Action to perform when Bag Menu item is selected.
        fragment = new ReferalFragment();
        pushFragment(fragment);
        tv_referral.setTextColor(Color.parseColor("#009345"));
        tv_fruitZone.setTextColor(Color.GRAY);
        tv_search.setTextColor(Color.GRAY);
        tv_account.setTextColor(Color.GRAY);
        tv_valet.setTextColor(Color.GRAY);
        iv_search.setBackgroundResource(R.drawable.search_icon);
        iv_fruitZone.setBackgroundResource(R.drawable.fr_zone);
        iv_referral.setBackgroundResource(R.drawable.referral_green);
        iv_account.setBackgroundResource(R.drawable.save_as);
    }

    public void account(View view) {
        // Action to perform when Account Menu item is selected.
        fragment = new ProfileFragment();
        pushFragment(fragment);
        tv_account.setTextColor(Color.parseColor("#009345"));
        tv_fruitZone.setTextColor(Color.GRAY);
        tv_referral.setTextColor(Color.GRAY);
        tv_search.setTextColor(Color.GRAY);
        tv_valet.setTextColor(Color.GRAY);
        iv_fruitZone.setBackgroundResource(R.drawable.fr_zone);

        iv_search.setBackgroundResource(R.drawable.search_icon);
        iv_referral.setBackgroundResource(R.drawable.referal);
       iv_account.setBackgroundResource(R.drawable.account_green);

    }
}