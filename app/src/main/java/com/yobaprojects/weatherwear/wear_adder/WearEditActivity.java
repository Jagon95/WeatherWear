package com.yobaprojects.weatherwear.wear_adder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yobaprojects.weatherwear.MainActivity;
import com.yobaprojects.weatherwear.R;
import com.yobaprojects.weatherwear.wear_item.WearItem;


public class WearEditActivity extends ActionBarActivity {

//    private static TextView textView;
    private static EditText wearNameView;
    private static ColorName colorName;
    private static Button wearIconButton;
    private static int wearColor;
	private WearTemplate wearTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_edit);

//        textView = (TextView) findViewById(R.id.text_view);
        wearNameView = (EditText) findViewById(R.id.wear_name);
        wearIconButton = (Button) findViewById(R.id.wear_icon);
        Typeface iconsFont = Typeface.createFromAsset(getAssets(), "fonts/hallheroes.ttf");

        colorName = new ColorName(getAssets(), "ColorNames/" + getString(R.string.lang) + ".json");
        Intent thisIntent = getIntent();
        wearColor = thisIntent.getIntExtra("wearColor", 0);
        wearTemplate = (WearTemplate) thisIntent.getSerializableExtra("wearItem");
        wearNameView.setText(colorName.GetName(wearColor, wearTemplate.getGenus()) + " " + wearTemplate.getName());

        wearIconButton.setTypeface (iconsFont);
        wearIconButton.setText(Character.toString(wearTemplate.getIcon()));
        wearIconButton.setTextColor(wearColor);
	    wearIconButton.setShadowLayer(5, 0, 0, ColorName.shadowComputing(wearColor));
//        Toast.makeText(getApplicationContext(), String.format("%08x", wearColor), Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wear_edit, menu);
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

    public void onClickChangeIcon(View view) {
        Toast.makeText(getApplicationContext(), "Change icon", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), String.format("%08x", ColorName.shadowComputing(wearColor)), Toast.LENGTH_SHORT).show();
    }

    public void onClickAddNFC(View view) {
        Toast.makeText(getApplicationContext(), "Adding new NFC...", Toast.LENGTH_LONG).show();
    }

    public void onClickEndUpWearGenerate(View view) {
	    if (wearNameView.getText ().toString ().isEmpty ()) {
		    Toast.makeText (getApplicationContext (), "Name is empty!", Toast.LENGTH_SHORT).show ();
		    return;
	    }
	    WearItem resultItem = new WearItem ();
	    resultItem.setIconColor (wearColor);
	    resultItem.setName (wearNameView.getText ().toString ());
	    resultItem.setStandardWearValues (wearTemplate);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("wearItem", resultItem);
        startActivity(mainIntent);
    }
}
