package com.yobaprojects.weatherwear.wear_adder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.yobaprojects.weatherwear.R;


public class ChooseColorActivity extends ActionBarActivity {

    private static ColorPicker picker;
    private static SaturationBar saturationBar;
    private static ValueBar valueBar;
    private static TextView textViewColorName;
    private static ColorName colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);


        picker = (ColorPicker) findViewById(R.id.picker);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        valueBar = (ValueBar) findViewById(R.id.valuebar);
        textViewColorName = (TextView) findViewById(R.id.colorName);
        colorName = new ColorName(getAssets(), "ColorNames/" + getString(R.string.lang) + ".json");


        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setShowOldCenterColor(false);

        picker.setOnColorChangedListener (new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                textViewColorName.setText (colorName.GetName(i));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickSetColor (View view) {
//        textViewColorName.setText (colorName.GetName(color, 0));
        Intent thisIntent = getIntent();
        Bundle bundle = thisIntent.getExtras();
        bundle.putInt("wearColor", picker.getColor());
        Intent nextIntent = new Intent(getApplicationContext(), WearEditActivity.class);
        nextIntent.putExtras(bundle);
        startActivity(nextIntent);
//        Toast.makeText(getApplicationContext(), bundle.getString("wearName"), Toast.LENGTH_SHORT).show();
    }
}
