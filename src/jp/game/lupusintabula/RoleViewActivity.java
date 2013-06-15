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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RoleViewActivity extends Activity {
	private static final String Tag = "RoleViewActivity";
	private GameRuleClass GameData;
	private String Player;
	private static final int[] drawables_male = new int[] {
			R.drawable.werewolf_male, R.drawable.villager_male,
			R.drawable.seer, R.drawable.medium, R.drawable.possessed_male,
			R.drawable.bodyguard, R.drawable.owlman, R.drawable.freemasons,
			R.drawable.werehamster, R.drawable.mythomaniac, R.drawable.devil,
			R.drawable.hunter, R.drawable.apothecary, R.drawable.matchmaker,
			R.drawable.witch, R.drawable.scryer, R.drawable.oracle,
			R.drawable.hierophant, R.drawable.magistrate, R.drawable.scryer,
			R.drawable.crone, R.drawable.warlock, R.drawable.necromancer,
			R.drawable.thief, R.drawable.toughgay, R.drawable.cult_leader,
			R.drawable.cultist, R.drawable.mystic, R.drawable.fool, };
	private static final int[] drawables_female = new int[] {
			R.drawable.werewolf_female, R.drawable.villager_female,
			R.drawable.seer, R.drawable.medium, R.drawable.possessed_female,
			R.drawable.bodyguard, R.drawable.owlman, R.drawable.freemasons,
			R.drawable.werehamster, R.drawable.mythomaniac, R.drawable.devil,
			R.drawable.hunter, R.drawable.apothecary, R.drawable.matchmaker,
			R.drawable.witch, R.drawable.scryer, R.drawable.oracle,
			R.drawable.hierophant, R.drawable.magistrate, R.drawable.scryer,
			R.drawable.crone, R.drawable.sorceress, R.drawable.necromancer,
			R.drawable.thief, R.drawable.toughgay, R.drawable.cult_leader,
			R.drawable.cultist, R.drawable.mystic, R.drawable.fool, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_roleview);

			GameData = GameRuleClass.getInstance();
			Intent intent = getIntent();
			Player = intent.getStringExtra("player");
			int role = intent.getIntExtra("role", GameData.getRole(Player)
					.ordinal());
			boolean message = intent.getBooleanExtra("message", true);
			Resources resource = getResources();
			String[] role_names = resource.getStringArray(R.array.role_names);
			String[] role_descriptions = resource
					.getStringArray(R.array.role_descriptions);
			TextView textView_name = (TextView) findViewById(R.id.roleview_textView_name);
			TextView textView_description = (TextView) findViewById(R.id.roleview_textView_description);
			ImageView imageView_role = (ImageView) findViewById(R.id.roleview_imageView_role);
			TextView textView_message = (TextView) findViewById(R.id.roleview_textView_message);
			textView_name.setText(Player + " - (" + role_names[role] + ")");
			textView_description.setText(role_descriptions[role]);
			imageView_role.setImageResource(getImageId(role,
					GameData.getGender(Player)));
			// 　仲間を表示するためのメッセージ
			if (message)
				textView_message.setText(getGroupMassage(Player));
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	// 画像ＩＤを取得
	public int getImageId(int role, boolean gender) {
		try {
			if (gender) {
				return drawables_male[role];
			} else {
				return drawables_female[role];
			}
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
		return -1;
	}

	// 仲間を表示するためのメッセージ
	public String getGroupMassage(String player) {
		try {
			String message = "";
			switch (GameData.getRole(player)) {
			case Werewolf:
				for (String werewolf : GameData.getWerewolves()) {
					message += werewolf + " ";
				}
				break;
			case Freemason:
				for (String mason : GameData.getMasons()) {
					message += mason + " ";
				}
				break;
			case CultLeader:
			case Cultist:
				for (String cultist : GameData.getCultists()) {
					message += cultist + " ";
				}
				break;
			default:
				break;
			}
			for (String lover : GameData.getLovers()) {
				if (lover.equals(player)) {
					message += lover + " ";
				}
			}
			return message.trim();
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
		return null;
	}

	public void onClickRoleImage(View view) {
		Log.i(Tag, "onClickRoleImage");
		try {
			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
