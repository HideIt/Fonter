package reknew.fonter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

	private int currentFont = 0;
	private int currentType = 0;

	private TextView content;

	private EditText editText;

	private Button endEditButton;

	private TextView sizeText;
	private TextView fontText;
	private TextView typeText;

	Map<String, Typeface> fontMap;

	List<String> fontList;

	List<String> typeList;

	@SuppressWarnings("all")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initMapnList();

		content = (TextView) findViewById(R.id.text_content);
		assert content != null;
		content.setOnClickListener(this);

		editText = (EditText) findViewById(R.id.edit_text_content);
		assert editText != null;
		editText.setText(content.getText());

		endEditButton = (Button) findViewById(R.id.button_end_edit);
		assert endEditButton != null;
		endEditButton.setOnClickListener(this);

		sizeText = (TextView) findViewById(R.id.text_size);
		sizeText.setText("SIZE : " + 16);
		fontText = (TextView) findViewById(R.id.text_font);
		fontText.setText("FONT : " + fontList.get(0));
		typeText = (TextView) findViewById(R.id.text_type);
		typeText.setText("TYPE : " + typeList.get(0));

		SeekBar sizeCtrl = (SeekBar) findViewById(R.id.seek_bar_size);
		assert sizeCtrl != null;
		sizeCtrl.setMax(56);
		sizeCtrl.setProgress(8);
		sizeCtrl.setOnSeekBarChangeListener(this);

		SeekBar typeCtrl = (SeekBar) findViewById(R.id.seek_bar_type);
		assert typeCtrl != null;
		typeCtrl.setMax(3);
		typeCtrl.setProgress(0);
		typeCtrl.setOnSeekBarChangeListener(this);

		SeekBar fontCtrl = (SeekBar) findViewById(R.id.seek_bar_font);
		assert fontCtrl != null;
		fontCtrl.setMax(fontList.size() - 1);
		fontCtrl.setProgress(0);
		fontCtrl.setOnSeekBarChangeListener(this);


		//TODO 改变text字重 以及更多type


		//TODO 检测锁屏时钟的冒号能否正常显示
		//但需要先指定字体是 "sans-serif" && weight = 100 && style = normal
/*		try {
			//TODO 需要指定字体为sans-serif Light
			Toast.makeText(this, "\uEE01", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		//获取文本框应用的字体名
/*		Typeface typeface = sizeText.getTypeface();
		List<String> fontList = valueGetKey(fontMap, typeface);
		Utils.d("**********IMPORTANT**********");
		for (String s : fontList) {
			Utils.d(s + "\n");
		}
		Utils.d("**********IMPORTANT**********");*/


		//Typeface lucida = Typeface.createFromAsset(getAssets(), "fonts/lucida.ttf");
		//Typeface lucida = Typeface.createFromFile("/storage/emulated/0/lucida.ttf");

		Utils.d(Environment.getExternalStorageState());
		try {
			Typeface lucida = Typeface.createFromFile(new File(Environment.getExternalStorageDirectory(), "font.ttf"));
			content.setTypeface(lucida, Typeface.NORMAL);
			content.setTextSize(32);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.e("load font Exception");
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
			case R.id.seek_bar_size:
				sizeText.setText("SIZE : " + String.valueOf(progress + 8));
				content.setTextSize(progress + 8);
				break;
			case R.id.seek_bar_type:
				typeText.setText("TYPE : " + typeList.get(progress));
				setContent(currentFont, progress, content.getText());
				break;
			case R.id.seek_bar_font:
				fontText.setText("FONT : " + fontList.get(progress));
				setContent(progress, currentType, content.getText());
			default:
				break;
		}
	}

	private void setContent(int fontLocation, int typeLocation, CharSequence text) {
//		Typeface typeface = Typeface.create(fontMap.get(fontList.get(fontLocation)), typeLocation);
//		SpannableString ss = new SpannableString(text);
//		ss.setSpan(new StyleSpan(typeLocation), 0, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//		for (Object obj : ss.getSpans(0, ss.length(), Object.class)) ss.removeSpan(obj);
//		content.setText(ss);

		content.setText(text);
		content.setTypeface(fontMap.get(fontList.get(fontLocation)), typeLocation);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch (seekBar.getId()) {
			case R.id.seek_bar_font:
				currentFont = seekBar.getProgress();
				break;
			case R.id.seek_bar_type:
				currentType = seekBar.getProgress();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_content:
				content.setVisibility(View.GONE);
				editText.setVisibility(View.VISIBLE);
				endEditButton.setVisibility(View.VISIBLE);
				break;
			case R.id.button_end_edit:
				setContent(currentFont, currentType, editText.getText());
				content.setVisibility(View.VISIBLE);
				editText.setVisibility(View.GONE);
				endEditButton.setVisibility(View.GONE);
				break;
		}
	}

//	protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			Map<String, Typeface> newMap = new HashMap<>();
//			newMap.put("sans-serif", newTypeface);
//			try {
//				final Field staticField = Typeface.class.getDeclaredField("fontMap");
//				staticField.setAccessible(true);
//				staticField.set(null, newMap);
//			} catch (NoSuchFieldException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
//				staticField.setAccessible(true);
//				staticField.set(null, newTypeface);
//			} catch (NoSuchFieldException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	private static List<String> valueGetKey(Map<String, Typeface> map, Typeface value) {
//		Set<Map.Entry<String, Typeface>> set = map.entrySet();//新建一个不可重复的集合
//		List<String> arr = new ArrayList<>();//新建一个集合
//		for (Object aSet : set) {
//			Map.Entry entry = (Map.Entry) aSet;//找到所有key-value对集合
//			if (entry.getValue().equals(value)) {//通过判断是否有该value值
//				String str = (String) entry.getKey();//取得key值
//				arr.add(str);
//			}
//		}
//		return arr;
//	}

	@SuppressWarnings("all")
	private void initMapnList() {
		try {
			Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
			Field f = Typeface.class.getDeclaredField("sSystemFontMap");
			f.setAccessible(true);
			fontMap = (Map<String, Typeface>) f.get(typeface);
			fontList = new ArrayList<>();
			for (Map.Entry<String, Typeface> entry : fontMap.entrySet()) {
				//Utils.d(entry.getKey() + " ---> " + entry.getValue() + "\n");
				fontList.add(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.e("initMapnList() Exception");
		}

		typeList = new ArrayList<>();
		typeList.add("NROMAL");
		typeList.add("BOLD");
		typeList.add("ITALIC");
		typeList.add("BOLD_ITALIC");
	}

/*
	public static final Typeface DEFAULT;
	public static final Typeface DEFAULT_BOLD;
	public static final Typeface SANS_SERIF;
	public static final Typeface SERIF;
	public static final Typeface MONOSPACE;
*/
}