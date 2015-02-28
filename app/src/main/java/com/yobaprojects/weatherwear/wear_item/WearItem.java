package com.yobaprojects.weatherwear.wear_item;

import com.yobaprojects.weatherwear.wear_adder.WearTemplate;

import java.io.Serializable;

public class WearItem implements Serializable{
	public final static String NAME = WearItem.class.getSimpleName ();
    public final static int EMPTY_GENUS = -1;
    private String name;
	private int genus = EMPTY_GENUS;		//род, в лингвистическом смысле
	private Layer layer;
	private Level level;
	private char icon;
	private int iconColor;
	private byte [] NFCID;
	private float thermalInsulation;

    public WearItem () {}

	public void setStandardWearValues (WearTemplate standardWearValues) {
		icon = standardWearValues.getIcon ();
		genus = standardWearValues.getGenus ();
		layer = standardWearValues.getLayer ();
		level = standardWearValues.getLevel ();
	}
	public void setIconColor (int color) {
		 this.iconColor = color;
	 }
	public void setName (String name) {
		this.name = name;
	}
	public void setGenus (int genus) {
		this.genus = genus;
	}
	public void setLayer (Layer layer) {
		this.layer = layer;
	}
	public void setLayer (String layer) {
		setLayer (Layer.valueOf (layer));
	}
	public void setLevel (Level level) {
		this.level = level;
	}
	public void setLevel (String level) {
		setLevel (Level.valueOf (level));
	}
	public void setIcon (char icon) {
		this.icon = icon;
	}
	public void setIcon (String icon) {
		setIcon (icon.charAt (0));
	}
	public void setNFCID (byte [] NFCID) {
		this.NFCID = NFCID;
	}
	public void setThermalInsulation (float thermalInsulation) {
		this.thermalInsulation = thermalInsulation;
	}

	public char getIcon() {
		return icon;
	}
	public String getName() {
		return name;
	}
	public int getGenus() {
		return genus;
	}
	public int getColor() {
		return iconColor;
	}
	public Layer getLayer() {
		return layer;
	}
	public Level getLevel() {
		return level;
	}
	public byte [] getNFCID () {
		return NFCID;
	}
	public String getStringNFCID () {
		if (NFCID != null) {
			StringBuilder sb = new StringBuilder ();

			for (int i : NFCID)
				sb.append (String.format ("%02x ", i & 0xff));

			return sb.toString ();
		}
		else return "";
	}
	public float getThermalInsulation () {
		return thermalInsulation;
	}
}
