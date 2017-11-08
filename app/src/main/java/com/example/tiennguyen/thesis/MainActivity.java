package com.example.tiennguyen.thesis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tiennguyen.thesis.fragment.AlbumFm;
import com.example.tiennguyen.thesis.fragment.CategoryFm;
import com.example.tiennguyen.thesis.fragment.ContentFm;
import com.example.tiennguyen.thesis.fragment.HomeFm;
import com.example.tiennguyen.thesis.fragment.InformationFm;
import com.example.tiennguyen.thesis.fragment.PlaylistFm;
import com.example.tiennguyen.thesis.fragment.StyleFm;
import com.example.tiennguyen.thesis.fragment.UserFm;
import com.example.tiennguyen.thesis.interfaces.Resourceble;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.model.SlideMenuItem;
import com.example.tiennguyen.thesis.util.Constants;
import com.example.tiennguyen.thesis.util.ViewAnimator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewAnimator.ViewAnimatorListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    // private ContentFragment contentFragment;
    private ViewAnimator viewAnimator;
    //private int res = R.drawable.content_music;
    private LinearLayout linearLayout;

    private ScreenShotable screenShot;
    private Fragment fragment = null;
    MaterialSearchView searchView;

    //------------------------
    // Constants
    private Constants CONSTANTS;

    // Search property
    private FrameLayout flSearchProperty;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        event();
    }

    private void initView(){
        refView();
        initialNewClass();
        setClickEvent();
        setActionBar();
        createMenuList();

        screenShot = new InformationFm();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, (Fragment) screenShot)
                .commit();

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        viewAnimator = new ViewAnimator<>(this, list, screenShot, drawerLayout, this);
        //viewAnimator = new ViewAnimator<>(this, list, drawerLayout, this);
    }

    private void event() {
        flSearchProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySearchDialog();
            }
        });
    }

    private void refView() {
        //-----------drawer menu-------------
        //contentFragment = ContentFragment.newInstance("Home");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        flSearchProperty = (FrameLayout) findViewById(R.id.fl_search_property);

    }

    private void initialNewClass() {
        CONSTANTS = new Constants();
    }

    private  void setClickEvent() {
        linearLayout.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.left_drawer: {
                drawerLayout.closeDrawers();
            };break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();break;

        }
    }

    private void createMenuList() {

        SlideMenuItem menuItem0 = new SlideMenuItem(Constants.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(Constants.FRAGMENT_USER, R.drawable.icons8_user_50);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(Constants.FRAGMENT_HOME, R.drawable.icons8_home_page_50);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(Constants.FRAGMENT_ALBUM, R.drawable.icons8_rhythm_50);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(Constants.FRAGMENT_CATEGORY, R.drawable.icons8_music_folder);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(Constants.FRAGMENT_STYLE, R.drawable.icons8_treble_clef);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(Constants.FRAGMENT_PLAYLIST, R.drawable.icons8_playlist);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(Constants.FRAGMENT_INFORMATION, R.drawable.icons8_info);
        list.add(menuItem7);
    }


    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        searched(searchView);
        return true;
    }

    public void searched(final MaterialSearchView searchView) {
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setHint("Searching for Song");
                flSearchProperty.setVisibility(View.VISIBLE);
                // displaySearchDialog();
            }

            @Override
            public void onSearchViewClosed() {
                flSearchProperty.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition, String name) {
        //this.res = this.res == R.drawable.content_music ? R.drawable.content_films : R.drawable.content_music;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        if (name == "Searching") {
            animator = ViewAnimationUtils.createCircularReveal(view, 370, 0, 0, finalRadius);
        }
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        //ContentFragment contentFragment = ContentFragment.newInstance(name);
        screenShot = new HomeFm();
        switch(name) {
            case Constants.FRAGMENT_USER : screenShot = new UserFm(); break;
            case Constants.FRAGMENT_HOME : screenShot = new HomeFm(); break;
            case Constants.FRAGMENT_ALBUM :screenShot = new AlbumFm(); break;
            case Constants.FRAGMENT_CATEGORY :screenShot = new CategoryFm(); break;
            case Constants.FRAGMENT_STYLE :screenShot = new StyleFm(); break;
            case Constants.FRAGMENT_PLAYLIST :screenShot = new PlaylistFm(); break;
            case Constants.FRAGMENT_INFORMATION : screenShot = new InformationFm(); break;
            //case "Searching": screenShot = new SearchingFm(); break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, (Fragment) screenShot)
                .commit();
        return screenShot;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFm.CLOSE:
                return screenShotable;
            default: {
                String fragmentName = slideMenuItem.getName();
                return replaceFragment(screenShotable, position, fragmentName);
            }
        }
    }


    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    private void displaySearchDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_search_title, null);
        final RadioButton song = (RadioButton) dialogLayout.findViewById(R.id.rb_song);
        final RadioButton album = (RadioButton) dialogLayout.findViewById(R.id.rb_album);
        final RadioButton artist = (RadioButton) dialogLayout.findViewById(R.id.rb_artist);
        final RadioButton composer = (RadioButton) dialogLayout.findViewById(R.id.rb_composer);

        AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
        searchDialog.setView(dialogLayout);
        searchDialog.setTitle(CONSTANTS.SEARCH_TITLE);
        searchDialog.setPositiveButton(CONSTANTS.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String search = "Searching for ";
                if (song.isChecked()) {
                    searchView.setHint(search + song.getText());
                } else if (album.isChecked()) {
                    searchView.setHint(search + album.getText());
                } else if (artist.isChecked()) {
                    searchView.setHint(search + artist.getText());
                } else if (composer.isChecked()) {
                    searchView.setHint(search + composer.getText());
                }
            }
        });
        searchDialog.setNegativeButton(CONSTANTS.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                searchView.closeSearch();
            }
        });
        searchDialog.create().show();
    }


}
