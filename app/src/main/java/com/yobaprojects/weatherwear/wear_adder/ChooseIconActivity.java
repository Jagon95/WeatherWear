package com.yobaprojects.weatherwear.wear_adder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yobaprojects.weatherwear.R;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseIconActivity extends Activity {

    private static Typeface lobster;
    private static GridLayout gridLayout;
    private static EditText searchText;
    private static boolean isTimerForSearchButtonWork = false;
    private static boolean isSearchButtonVisible = false;
    private static boolean isSearchActive = false;
    private static WearTemplate wearTemplates [] = null;

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons_table);

        gridLayout = (GridLayout) findViewById(R.id.grid_layout_icons_table);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int numberOfColumns = (int) ((displayMetrics.widthPixels * DisplayMetrics.DENSITY_DEFAULT / displayMetrics.xdpi - 40) / 80);
        gridLayout.setColumnCount(numberOfColumns);



        lobster = Typeface.createFromAsset(getAssets(), "fonts/hallheroes.ttf");
        wearTemplates = WearTemplate.GetAll(getAssets(), "WearTemplates/" + getString(R.string.lang) + ".json");
        drawIcons ();



        searchText = (EditText)findViewById(R.id.search_text);
        searchText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
                if (isSearchActive)
                    findIcons(s.toString());
            }
        });



        ButtonHideTimer mMyTimerTask = new ButtonHideTimer();

        if (isSearchActive)
            showSearchArea();

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                hideSearchButton();
                if (isTimerForSearchButtonWork) {
                    mTimer.cancel();
                    mTimer.purge();
                }
                if (!isSearchActive) {
                    mTimer = new Timer(true);
                    mTimer.schedule(new ButtonHideTimer(), 800);
                    isTimerForSearchButtonWork = true;
                }
            }
        });

    }


    void drawIcons () {
        LinearLayout linearLayout;
        Button tmpButton;
        gridLayout.removeAllViews();

        TextView tmpTextView;
        //TODO: Правильнее переделать с Fragments и Adapter. Будет ЕЩЕ КРУЧЕ!
        for (int i = 0; i < wearTemplates.length; i++) {

            linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_wear_icon, gridLayout, false);
            tmpButton = (Button) linearLayout.getChildAt(0);
            tmpButton.setText(Character.toString(wearTemplates[i].getIcon()));
            tmpButton.setTypeface(lobster);
            tmpButton.setId(i);

            tmpTextView = (TextView) linearLayout.getChildAt(1);
            tmpTextView.setText(wearTemplates[i].getName());

            gridLayout.addView(linearLayout);
        }
    }

    void findIcons (String subString) {
        gridLayout.removeAllViews();
        wearTemplates = WearTemplate.findByName(subString);
        drawIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_icons_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickSearch(View view) {
        showSearchArea();
    }

    private void showSearchArea () {
        FrameLayout searchArea = (FrameLayout) findViewById(R.id.search_area);
        searchArea.setVisibility(View.VISIBLE);
        hideSearchButton();
        isSearchActive = true;

        if (searchText.length() > 0)
            findIcons(searchText.getText().toString());
    }

    private void hideSearchArea () {
        FrameLayout searchArea = (FrameLayout) findViewById(R.id.search_area);
        searchArea.setVisibility(View.GONE);
        isSearchActive = false;
    }

    private void hideSearchButton () {
        if (isSearchButtonVisible) {
            ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
            Animation searchButtonHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.search_button_hide);
            searchButton.startAnimation(searchButtonHide);
            searchButton.setVisibility(View.GONE);
            searchButton.setClickable(false);
            isSearchButtonVisible = false;
        }
    }
    private void showSearchButton () {
        if (!isSearchButtonVisible) {
            ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
            Animation searchButtonShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.search_button_show);
            searchButton.startAnimation(searchButtonShow);
            searchButton.setVisibility(View.VISIBLE);
            searchButton.setClickable(true);
            isSearchButtonVisible = true;
        }
    }

    public void onClickCloseSearch(View view) {
        wearTemplates = WearTemplate.GetAll();
        drawIcons();
        hideSearchArea();
        searchText.setText("");
    }

    private class ButtonHideTimer extends TimerTask {

        @Override

        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showSearchButton();
                    isTimerForSearchButtonWork = false;
                }
            });
        }
    }

    public void onClickSetIcon (View view) {

/*
        int color = picker.getColor();
        picker.setOldCenterColor(color);
        textViewColorName.setText (colorName.GetName(color, 0));
        Intent thisIntent = getIntent();
        Bundle bundle = thisIntent.getExtras();
        bundle.putInt("wearColor", picker.getColor());
        Intent nextIntent = new Intent(getApplicationContext(), WearEditActivity.class);
        nextIntent.putExtras(bundle);
        startActivity(nextIntent);*/

        WearTemplate item = wearTemplates [view.getId()];
//        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
//        intent.putExtra("wearItem", item);
        Intent thisIntent = getIntent();
        Intent nextIntent = new Intent(this, ChooseColorActivity.class);
        Bundle bundle = thisIntent.getExtras();
        if (bundle != null) {
            bundle.putSerializable("wearItem", item);
            nextIntent.putExtras (bundle);
        } else {
            nextIntent.putExtra("wearItem", item);
        }
        startActivity(nextIntent);
//        Toast.makeText(getApplicationContext(), Integer.toString(view.getId()), Toast.LENGTH_SHORT).show();
    }
}
