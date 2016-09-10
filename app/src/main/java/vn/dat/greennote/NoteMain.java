package vn.dat.greennote;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import vn.dat.greennote.NoteMenu.About;
import vn.dat.greennote.NoteMenu.Help;
import vn.dat.greennote.NoteMenu.NoteList;
import vn.dat.greennote.NoteMenu.Search;
import vn.dat.greennote.NoteMenu.Setting;
import vn.dat.greennote.NoteMenu.Statistics;

/**
 * Created by Alone on 04/16/2016.
 */
public class NoteMain extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new NoteList()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navAllNote:
                        toolbar.setTitle("Green Note");
                        fragmentTransaction.replace(R.id.containerView, new NoteList()).commit();
                        break;
                    case R.id.navSearch:
                        toolbar.setTitle("Search");
                        fragmentTransaction.replace(R.id.containerView, new Search()).commit();
                        break;
                    case R.id.navStatistics:
                        toolbar.setTitle("Statistics");
                        fragmentTransaction.replace(R.id.containerView, new Statistics()).commit();
                        break;
                    case R.id.navAbout:
                        toolbar.setTitle("About");
                        fragmentTransaction.replace(R.id.containerView, new About()).commit();
                        break;

                    case R.id.navHelp:
                        toolbar.setTitle("Help");
                        fragmentTransaction.replace(R.id.containerView, new Help()).commit();
                        break;
                    case R.id.navSetting:
                        toolbar.setTitle("Setting");
                        fragmentTransaction.replace(R.id.containerView, new Setting()).commit();
                        break;

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    public void exit() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(getApplicationContext(), "Press again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Exit Green Note?");
//        alertDialogBuilder
//                .setMessage("Click yes to exit!")
//                .setCancelable(false)
//                .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                moveTaskToBack(true);
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
//                            }
//                        })
//
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }
}