/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

import static jp.game.lupusintabula.Constant.*;

public class MainActivity extends Activity {
	private static final String Tag = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_main);
			
	        //キャッチされない例外により、スレッドが突然終了したときや、  
	        //このスレッドに対してほかにハンドラが定義されていないときに  
	        //呼び出されるデフォルトのハンドラを設定します。  
	        Thread.setDefaultUncaughtExceptionHandler(new ErrorReportClass(this));  
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
	  
    @Override  
    protected void onStart() {  
        super.onStart();  
        ErrorReportClass.SendBugReportDialog(this.getApplicationContext());  
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(Tag, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_PLAYER:
				break;
			case ACTIVITY_ROLE:
				break;
			case ACTIVITY_HELP:
				break;
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickStartButton(View view) {
		Log.i(Tag, "onClickStartButton");
		try {
			Intent intent = new Intent(this, OpeningActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickPlayerButton(View view) {
		Log.i(Tag, "onClickPlayerButton");
		try {
			Intent intent = new Intent(this, PlayerActivity.class);
			startActivityForResult(intent, ACTIVITY_PLAYER);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickRoleButton(View view) {
		Log.i(Tag, "onClickRoleButton");
		try {
			Intent intent = new Intent(this, RoleActivity.class);
			startActivityForResult(intent, ACTIVITY_ROLE);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickHelpButton(View view) {
		Log.i(Tag, "onClickHelpButton");
		try {
			Intent intent = new Intent(this, HelpActivity.class);
			startActivityForResult(intent, ACTIVITY_HELP);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
