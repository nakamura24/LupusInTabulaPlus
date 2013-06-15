package jp.game.lupusintabula;

import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TextToSpeechClass implements TextToSpeech.OnInitListener {
	private static final String Tag = "TextToSpeechClass";
	private static TextToSpeechClass instance = new TextToSpeechClass();
	private static TextToSpeech    mTextToSpeech = null;
	private static int mStatus = TextToSpeech.ERROR;
	private static Context mContext;

	private TextToSpeechClass(){}
	
	public static TextToSpeechClass getInstance(Context context) {  
		mContext = context.getApplicationContext();
		// TextToSpeechオブジェクトの生成
		if(mTextToSpeech == null) {
			mTextToSpeech = new TextToSpeech(mContext, instance);
		}
        return instance;  
    }

	@Override
	public void onInit(int status) {
		mStatus = status;
		if (TextToSpeech.SUCCESS == status) {
			Locale locale = Locale.JAPANESE;
			if (mTextToSpeech.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
				mTextToSpeech.setLanguage(locale);
			} else {
				Log.e(Tag, "Error SetLocale");
			}
		} else {
			Log.e(Tag, "Error Init");
		}
	}

	public int getStatus() {
		return mStatus;
	}
	
	public boolean isSpeaking() {
		return mTextToSpeech.isSpeaking();
	}

	public void speechText(int resid) {
		Resources res = mContext.getResources();
		String string = res.getString(resid);
		speechText(string);
	}

	public void speechText(String string) {
		if (TextToSpeech.SUCCESS == mStatus) {
			if (0 < string.length()) {
				if (mTextToSpeech.isSpeaking()) {
					// 読み上げ中なら止める
					mTextToSpeech.stop();
				}
				// 読み上げ開始
				mTextToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
			}
		}
	}

	public void speechStop() {
		if (TextToSpeech.SUCCESS == mStatus) {
			if (mTextToSpeech.isSpeaking()) {
				// 読み上げ中なら止める
				mTextToSpeech.stop();
			}
		}
	}
}
