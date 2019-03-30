package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.string.no;
import static android.R.string.yes;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public NavigationView navigationView;

    public static TextView tvPageTitle;

    final FragmentManager fm = getSupportFragmentManager();


    SharedPreferences pref;

    private static final int TIME_INTERVAL = 2500; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        NewProductFragment newFragment = (NewProductFragment)fm.findFragmentByTag("NEWPRODUCT");
        RestockFragment restockFragment = (RestockFragment)fm.findFragmentByTag("RESTOCKPRODUCT");
        EbaySetupFragment ebaySetUpFragment = (EbaySetupFragment)fm.findFragmentByTag("EBAYSETUP");
        AmazonSetupFragment amazonSetUpFragment = (AmazonSetupFragment)fm.findFragmentByTag("AMAZONSETUP");
        EtsySetupFragment etsySetUpFragment = (EtsySetupFragment)fm.findFragmentByTag("ETSYSETUP");
        MagentoSetupFragment magentoSetUpFragment = (MagentoSetupFragment)fm.findFragmentByTag("MAGENTOSETUP");
        ShopifySetupFragment shopifySetUpFragment = (ShopifySetupFragment)fm.findFragmentByTag("SHOPIFYSETUP");
        RakutenSetupFragment rakutenSetUpFragment = (RakutenSetupFragment)fm.findFragmentByTag("RAKUTENSETUP");
        BigcommerceSetupFragment bigcommerceSetUpFragment = (BigcommerceSetupFragment)fm.findFragmentByTag("BIGCOMMERCESETUP");
        ResetUsernameFragment resetUsernameFragment = (ResetUsernameFragment)fm.findFragmentByTag("RESETUSERNAME");
        ResetPasswordFragment resetPasswordFragment = (ResetPasswordFragment)fm.findFragmentByTag("RESETPASSWORD");
        CreateNewAccountFragment createNewAccountFragment = (CreateNewAccountFragment)fm.findFragmentByTag("CREATENEWACCOUNT");
        CloseAccountFragment closeAccountFragment = (CloseAccountFragment) fm.findFragmentByTag("CLOSEACCOUNT");
        SetPermissionsFragment setPermissionsFragment = (SetPermissionsFragment) fm.findFragmentByTag("SETPERMISSIONS");

        if(newFragment != null || restockFragment != null) {
                fm.beginTransaction().replace(R.id.containerView, new AddProductFragment()).commit();
                tvPageTitle.setText("");
            } else if (ebaySetUpFragment != null || amazonSetUpFragment != null || etsySetUpFragment != null
                || magentoSetUpFragment != null || shopifySetUpFragment != null || rakutenSetUpFragment != null
                || bigcommerceSetUpFragment != null) {
            fm.beginTransaction().replace(R.id.containerView, new NewListingFragment()).commit();
            tvPageTitle.setText(R.string.where_do_you_want_to_list);
        } else if (resetUsernameFragment != null || resetPasswordFragment != null || createNewAccountFragment != null
                || closeAccountFragment != null|| setPermissionsFragment != null) {
            fm.beginTransaction().replace(R.id.containerView, new SettingsFragment()).commit();
            tvPageTitle.setText(R.string.Settings);
        }
        else {
            navigationView.setCheckedItem(R.id.nav_item_home);
            tvPageTitle.setText(R.string.home);
            FragmentTransaction fragmentTransaction1 = FM.beginTransaction();
            fragmentTransaction1.replace(R.id.containerView, new HomeFragment()).commit();
            drawerLayout.closeDrawer(navigationView);

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_exit)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                Toast.makeText(getBaseContext(), R.string.tap_back_again_to_exit, Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }

    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    DrawerLayout drawerLayout;
    FragmentManager FM;
    FragmentTransaction FT;

    public void setTitle (String actionBarTitle) {


        textSwitcher.setText(actionBarTitle);

    }

    TextSwitcher textSwitcher;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         navigationView = (NavigationView) findViewById(R.id.shitstuff);

         pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

         final int adminPermission = pref.getInt("adminPermission", 0);

         tvPageTitle = (TextView) findViewById(R.id.tvPageTitle);

         View headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, navigationView, false);
         navigationView.addHeaderView(headerView);

         TextView tvLogout = (TextView) headerView.findViewById(R.id.tvLogout);

         Intent intent = getIntent();

         final String companyName = intent.getStringExtra("companyName");
         final String username = intent.getStringExtra("username");

         textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView switcherTextView = new TextView(getApplicationContext());
                switcherTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/avenir.ttf"));
                switcherTextView.setTextSize(24);
                switcherTextView.setTextColor(Color.rgb(128,128,128));
                switcherTextView.setText(R.string.welcome);
                return switcherTextView;
            }
        });

        Animation animationOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        Animation animationIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        textSwitcher.setOutAnimation(animationOut);
        textSwitcher.setInAnimation(animationIn);

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 if (companyName != null || username != null) {
                     setTitle(companyName + " / " + username);
                 }
                 else {
                     setTitle(pref.getString("companyName","") + " / " + pref.getString("username",""));
                 }
             }
         }, 2200);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerLayout.openDrawer(navigationView);
        navigationView.setCheckedItem(R.id.nav_item_home);
        tvPageTitle.setText(R.string.home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(navigationView);            }
        }, 1700);

        FM= getSupportFragmentManager();
        FT= FM.beginTransaction();
        FT.replace(R.id.containerView, new HomeFragment()).commit();

          tvLogout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                  builder.setTitle(R.string.app_name);
                  builder.setIcon(R.mipmap.ic_launcher);
                  builder.setMessage(R.string.logout_confirm)
                          .setCancelable(false)
                          .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  pref.edit().clear().commit();
                                  Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                                  MainActivity.this.startActivity(logoutIntent);
                                  finish();
                              }
                          })
                          .setNegativeButton(no, new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  dialog.cancel();
                              }
                          });
                  AlertDialog alert = builder.create();
                  alert.show();

                  }
          });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                if (item.getItemId()== R.id.nav_item_communication) {
                    tvPageTitle.setText(R.string.Communication);
                    navigationView.setCheckedItem(R.id.nav_item_communication);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new CommunicationFragment()).commit();
                    }

                if (item.getItemId()==R.id.nav_item_home) {
                    tvPageTitle.setText(R.string.home);
                    navigationView.setCheckedItem(R.id.nav_item_home);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new HomeFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_prof) {
                    tvPageTitle.setText(R.string.profile);
                    navigationView.setCheckedItem(R.id.nav_item_prof);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new ProfileFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_reminders) {
                    tvPageTitle.setText(R.string.reminders);
                    navigationView.setCheckedItem(R.id.nav_item_reminders);
                    FragmentTransaction fragmentTransaction= FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new RemindersFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_reports) {
                    if(adminPermission == 1) {
                        tvPageTitle.setText(R.string.Reports);
                        navigationView.setCheckedItem(R.id.nav_item_reports);
                        FragmentTransaction fragmentTransaction = FM.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, new ReportsFragment()).commit();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.no_auth_access_reports, Toast.LENGTH_LONG).show();
                    }
                    }

                if (item.getItemId()== R.id.nav_item_about) {
                    tvPageTitle.setText(R.string.About);
                    navigationView.setCheckedItem(R.id.nav_item_about);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new AboutFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_settings) {
                    tvPageTitle.setText(R.string.Settings);
                    navigationView.setCheckedItem(R.id.nav_item_settings);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new SettingsFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_help) {
                    tvPageTitle.setText(R.string.Help);
                    navigationView.setCheckedItem(R.id.nav_item_help);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new HelpFragment()).commit();
                    }

                if (item.getItemId()== R.id.nav_item_upload) {
                    if(adminPermission == 1) {
                        tvPageTitle.setText(R.string.Upload);
                        navigationView.setCheckedItem(R.id.nav_item_upload);
                        FragmentTransaction fragmentTransaction=FM.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,new UploadFragment()).commit();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.no_auth_access_uploads, Toast.LENGTH_LONG).show();
                    }
                }

                if (item.getItemId()== R.id.nav_item_feedback) {
                    tvPageTitle.setText(R.string.Feedback);
                    navigationView.setCheckedItem(R.id.nav_item_feedback);
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new FeedbackFragment()).commit();
                }
                return false;
            }
        });


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }
}