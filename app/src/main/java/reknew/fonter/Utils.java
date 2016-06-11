package reknew.fonter;

import android.util.Log;

public class Utils {

	private static final String TAG = "MINE";

	public static void d(Object obj) {
		Log.d(TAG, String.valueOf(obj));
	}

	public static void e(Object obj) {
		Log.e(TAG, String.valueOf(obj));
	}
}