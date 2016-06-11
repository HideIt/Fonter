package reknew.fonter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

//	private static final int NORMAL = 0;
//
//	private static final int Thin = 12212;
//	private static final int ThinItalic = 1;
//	private static final int Light = 2;
//	private static final int LightItalic = 3;
//	private static final int normal = 4;    //  √
//	private static final int italic = 5;    //  √
//	private static final int Medium = 6;
//	private static final int MediumItalic = 7;
//	private static final int Black = 8;
//	private static final int BlackItalic = 9;
//	private static final int bold = 10;     //  √
//	private static final int BoldItalic = 11;

	private int type = 0;

	private TextView endEditBtn;

	private TextView content;

	private TextView sizeText;

	private TextView fontText;

	private TextView typeText;

	private EditText editText;

	List<String> key;
	Map<String, Typeface> sSystemFontMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		content = (TextView) findViewById(R.id.text_content);
		assert content != null;
		content.setOnClickListener(this);

//		String source = "<b>粗体格式</b><br>";
//		source += "<i>斜体格式</i><br>";
//		source += "<b><i>粗斜体格式</i></b><be>";
//      content.setText(Html.fromHtml(source));

		//TODO 更换更多系统字体样式
		//text.setSpan(Typeface.create("serif", Typeface.BOLD), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		editText = (EditText) findViewById(R.id.edit_text_content);
		assert editText != null;
		editText.setText(content.getText());
		editText.setOnClickListener(this);

		endEditBtn = (TextView) findViewById(R.id.end_edit_btn);
		assert endEditBtn != null;
		endEditBtn.setOnClickListener(this);

		sizeText = (TextView) findViewById(R.id.text_size);
		typeText = (TextView) findViewById(R.id.text_type_bar);

		SeekBar sizeCtrl = (SeekBar) findViewById(R.id.seek_bar_size);
		assert sizeCtrl != null;
		sizeCtrl.setMax(56);
		sizeCtrl.setProgress(8);
		sizeCtrl.setOnSeekBarChangeListener(this);

		getSSystemFontMap();
		SeekBar typeCtrl = (SeekBar) findViewById(R.id.seek_bar_type);
		assert typeCtrl != null;
		typeCtrl.setMax(key.size() - 1);
		typeCtrl.setProgress(0);
		typeCtrl.setOnSeekBarChangeListener(this);

		//TODO 检测锁屏时钟的冒号显示是否正常
		//但需要先指定字体是 "sans-serif" && weight = 100 && style = normal
		try {
			Toast.makeText(this, "\uEE01", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Typeface typeface = sizeText.getTypeface();
		List<String> key = valueGetKey(sSystemFontMap, typeface);
		Utils.d("**********IMPORTANT**********");
		for (String s : key) {
			Utils.d(s + "\n");
		}
		Utils.d("**********IMPORTANT**********");
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
			case R.id.seek_bar_size:
				sizeText.setText("size : " + String.valueOf(progress + 8));
				content.setTextSize(progress + 8);
				break;
			case R.id.seek_bar_type:
				//typeText.setText("type : " + String.valueOf(progress));
				typeText.setText("type : " + key.get(progress));
				setContent(progress, content.getText());
				break;
			default:
				break;
		}
	}

	private void setContent(int type, CharSequence cs) {
//		int style = 0;
//		switch (type) {
//			case 0:
//				style = Typeface.NORMAL;
//				break;
//			case 1:
//				style = Typeface.BOLD;
//				break;
//			case 2:
//				style = Typeface.ITALIC;
//				break;
//			case 3:
//				style = Typeface.BOLD_ITALIC;
//				break;
//		}
		SpannableString ss = new SpannableString(cs);
		Typeface typeface = sSystemFontMap.get(key.get(type));
		//清除属性
		for (Object obj : ss.getSpans(0, ss.length(), Object.class)) ss.removeSpan(obj);
		Utils.d(typeface);
		ss.setSpan(typeface, 0, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		this.content.setText(ss);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch (seekBar.getId()) {
			case R.id.seek_bar_size:
				type = seekBar.getProgress();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_content:
				content.setVisibility(View.GONE);
				editText.setVisibility(View.VISIBLE);
				endEditBtn.setVisibility(View.VISIBLE);
				break;
			case R.id.end_edit_btn:
				setContent(type, editText.getText());
				content.setVisibility(View.VISIBLE);
				editText.setVisibility(View.GONE);
				endEditBtn.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

//	protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			Map<String, Typeface> newMap = new HashMap<>();
//			newMap.put("sans-serif", newTypeface);
//			try {
//				final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
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


	@SuppressWarnings("all")
	protected void getSSystemFontMap() {
		try {
			Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
			Field f = Typeface.class.getDeclaredField("sSystemFontMap");
			f.setAccessible(true);
			sSystemFontMap = (Map<String, Typeface>) f.get(typeface);
			key = new ArrayList<>();
			for (Map.Entry<String, Typeface> entry : sSystemFontMap.entrySet()) {
				Utils.d(entry.getKey() + " ---> " + entry.getValue() + "\n");
				key.add(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.e("getSSystemFontMap() Exception");
		}
	}

	private static List<String> valueGetKey(Map<String, Typeface> map, Typeface value) {
		Set<Map.Entry<String, Typeface>> set = map.entrySet();//新建一个不可重复的集合
		List<String> arr = new ArrayList<>();//新建一个集合
		for (Object aSet : set) {
			Map.Entry entry = (Map.Entry) aSet;//找到所有key-value对集合
			if (entry.getValue().equals(value)) {//通过判断是否有该value值
				String str = (String) entry.getKey();//取得key值
				arr.add(str);
			}
		}
		return arr;
	}

/*
	public static final Typeface DEFAULT;
	public static final Typeface DEFAULT_BOLD;
	public static final Typeface SANS_SERIF;
	public static final Typeface SERIF;
	public static final Typeface MONOSPACE;

	//style
	public static final int NORMAL = 0;
	public static final int BOLD = 1;
	public static final int ITALIC = 2;
	public static final int BOLD_ITALIC = 3;
*/
}