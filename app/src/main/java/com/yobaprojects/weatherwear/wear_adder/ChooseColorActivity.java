package com.yobaprojects.weatherwear.wear_adder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
