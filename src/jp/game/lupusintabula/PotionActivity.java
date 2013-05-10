/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class PotionActivity extends Activity {
	private static final String Tag = "PotionActivity";
	private GameRuleClass GameData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_potion);

			GameData = GameRuleClass.getInstance();
			Intent intent = getIntent();
			String player = intent.getStringExtra("player");
			RadioButton radio_hilling = (RadioButton) findViewById(R.id.potion_radio_hilling);
			RadioButton radio_poison = (RadioButton) findViewById(R.id.potion_radio_poison);
			if (!GameData.hasHilling(player))
				radio_hilling.setVisibility(View.GONE);
			if (!GameData.hasPoison(player))
				radio_poison.setVisibility(View.GONE);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			// 画面の終了
			Intent intent = new Intent();
			RadioButton radio_hilling = (RadioButton) findViewById(R.id.potion_radio_hilling);
			RadioButton radio_poison = (RadioButton) findViewById(R.id.potion_radio_poison);
			intent.putExtra("hilling", radio_hilling.isChecked());
			intent.putExtra("poison", radio_poison.isChecked());
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
