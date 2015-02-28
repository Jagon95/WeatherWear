package com.yobaprojects.weatherwear.wear_adder;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yobaprojects.weatherwear.MainActivity;
import com.yobaprojects.weatherwear.R;
import com.yobaprojects.weatherwear.wear_item.Layer;
import com.yobaprojects.weatherwear.wear_item.WearItem;


public class WearEditActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private static EditText wearNameView;
	private WearTemplate wearTemplate;
	private int wearColor;
	private byte [] NFCID;
	private float thermalInsulation = 0;
	private Layer layer;

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_edit);
	    resolveIntent(getIntent());

	    final SeekBar seekbar = (SeekBar)findViewById(R.id.seekBarLayer);
	    seekbar.setOnSeekBarChangeListener(this);

	    mAdapter = NfcAdapter.getDefaultAdapter(this);
	    if (mAdapter == null) {
		    Toast.makeText (getApplicationContext (), "NFC is disable!", Toast.LENGTH_SHORT).show ();
	    }

	    RatingBar thermalInsulationBar = (RatingBar) findViewById (R.id.thermalInsulation);

	    thermalInsulationBar.setOnRatingBarChangeListener (new RatingBar.OnRatingBarChangeListener () {
		    @Override
		    public void onRatingChanged (RatingBar ratingBar, float rating, boolean fromUser) {
			    thermalInsulation = rating;
		    }
	    });


	    mPendingIntent = PendingIntent.getActivity (this, 0,
			    new Intent (this, getClass ()).addFlags (Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				showWirelessSettingsDialog();
			}
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch (this);
		}
	}

	private void showWirelessSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setMessage(R.string.nfc_disabled);
		builder.setMessage("NFC disable");

		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				finish();
			}
		});
		builder.create().show();
		return;
	}

	private void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			NFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

			fillNFCField (NFCID);
		}
		fillField (intent.getExtras ());
	}

	private void fillField (Bundle bundle) {
		if (bundle.get (WearItem.NAME) != null && bundle.get ("wearColor") != null) {
			Typeface iconsFont = Typeface.createFromAsset(getAssets(), "fonts/hallheroes.ttf");

			Button wearIconButton = (Button) findViewById (R.id.wear_icon);
			wearNameView = (EditText) findViewById(R.id.wear_name);
			ColorName colorName = new ColorName (getAssets (), "ColorNames/" + getString (R.string.lang) + ".json");
			wearTemplate = (WearTemplate) bundle.getSerializable (WearItem.NAME);

			wearColor = bundle.getInt ("wearColor");

			wearIconButton.setTypeface (iconsFont);
			wearIconButton.setText (Character.toString (wearTemplate.getIcon ()));
			wearIconButton.setTextColor (wearColor);
			wearIconButton.setShadowLayer (5, 0, 0, ColorName.shadowComputing (wearColor));

			wearNameView.setText(colorName.GetName (wearColor, wearTemplate.getGenus ()) + " " + wearTemplate.getName());

			layer = wearTemplate.getLayer ();
			onLayerChanged (layer, true);

		}
	}


	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState (outState);
		outState.putSerializable (WearItem.NAME, wearTemplate);
		outState.putInt ("wearColor", wearColor);
	}

	@Override
	protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState (savedInstanceState);
		fillField (savedInstanceState);
	}

	private String getHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (int i : bytes)
			sb.append (String.format ("%02x ", i & 0xff));

		return sb.toString();
	}

    public void onClickChangeIcon(View view) {
        Toast.makeText(getApplicationContext(), "Change icon", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), String.format("%08x", ColorName.shadowComputing(wearColor)), Toast.LENGTH_SHORT).show();
    }

	private void fillNFCField (byte [] NFCID) {
		((TextView) findViewById (R.id.NFCID)).setText (getHex (NFCID));
		findViewById (R.id.clear_nfc_button).setVisibility (View.VISIBLE);
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

	    resultItem.setLayer (layer);
	    resultItem.setNFCID (NFCID);
	    resultItem.setThermalInsulation (thermalInsulation);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(WearItem.NAME, resultItem);
        startActivity(mainIntent);
    }

	public void onNewIntent(Intent intent) {
		setIntent(intent);
		resolveIntent(intent);
	}

	public void onClickClearNFCButton (View view) {
		NFCID = null;
		findViewById (R.id.clear_nfc_button).setVisibility (View.GONE);
		((TextView) findViewById (R.id.NFCID)).setText (getString (R.string.text_view_nfcid));
	}

	@Override
	public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId ()) {
			case R.id.seekBarLayer:
				layer = Layer.values () [roundSeekBarValue (seekBar, Layer.values ().length)];
				onLayerChanged (layer, false);
				break;
		}
	}

	@Override
	public void onStartTrackingTouch (SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch (SeekBar seekBar) {
		switch (seekBar.getId ()) {
			case R.id.seekBarLayer:
				layer = Layer.values () [roundSeekBarValue (seekBar, Layer.values ().length)];
				onLayerChanged (layer, true);
				break;
		}

	}

	private void onLayerChanged (Layer layer, boolean isSeekBarChanged) {

		SeekBar seekBar = (SeekBar) findViewById (R.id.seekBarLayer);
		String layerName = getString (getResources ().getIdentifier ("layer_" + layer.toString (), "string", getPackageName ()));
		((TextView) findViewById (R.id.textViewLayerValue)).setText (layerName);

		if (isSeekBarChanged)
			seekBar.setProgress ((int) Math.round (layer.ordinal () * (seekBar.getMax () / (Layer.values ().length - 1.))));
	}

	public int roundSeekBarValue (SeekBar seekBar, int variants) {
		return (int) Math.round ((double) seekBar.getProgress () / seekBar.getMax () * (variants - 1));
	}


}
