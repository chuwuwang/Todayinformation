package com.todayinfo.ui.component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinghua.todayinformation.R;
import com.todayinfo.controller.NetWorkCenter;

public class DateDialog {

	public static Dialog dialog;
	private static int START_YEAR = 1900, END_YEAR = 2100, END_MONTH, END_DATE;
	static TextView etTemp;
	static int year, month, day;

	public void showDatePicker(final Context con, final TextView textView) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		END_YEAR = Integer.parseInt(timeStamp.substring(0, 4));
		END_MONTH = Integer.parseInt(timeStamp.substring(4, 6));
		END_DATE = Integer.parseInt(timeStamp.substring(6, 8));

		etTemp = textView;
		if (etTemp.getText().toString().length() > 0) {
			String[] time = etTemp.getText().toString().split("-");
			if (time.length == 1 && time[0].equals(etTemp.getText().toString())) {
				time = etTemp.getText().toString().split("\\.");
			}
			year = Integer.parseInt(time[0]);
			month = Integer.parseInt(time[1]) - 1;
			day = Integer.parseInt(time[2]);
		} else {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DATE);
		}

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		dialog = new Dialog(con, R.style.FullHeightDialog);
		// dialog.setTitle("请选择日期");
		// 找到dialog的布局文件
		LayoutInflater inflater = LayoutInflater.from(con);
		View view = inflater.inflate(R.layout.pop_date, null);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);

		// 年
		final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(false);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						wv_day.setCurrentItem(0);
					} else {
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
						wv_day.setCurrentItem(0);
					}
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					wv_day.setCurrentItem(0);
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					wv_day.setCurrentItem(0);
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						wv_day.setCurrentItem(0);
					} else {
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
						wv_day.setCurrentItem(0);
					}
				}
			}
		};

		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大
		wv_day.TEXT_SIZE = (int) con.getResources().getDimension(R.dimen.dp45);
		wv_month.TEXT_SIZE = (int) con.getResources().getDimension(R.dimen.dp45);
		wv_year.TEXT_SIZE = (int) con.getResources().getDimension(R.dimen.dp45);

		Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
		
		// 确定
		btn_sure.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String day1 = String.valueOf(wv_day.getCurrentItem() + 1);
				String year1 = String.valueOf(wv_year.getCurrentItem() + START_YEAR);
				String month1 = String.valueOf(wv_month.getCurrentItem() + 1);
				if (month1.length() < 2) {
					month1 = 0 + month1;
				}
				if (day1.length() < 2) {
					day1 = 0 + day1;
				}

				if (END_YEAR > Integer.parseInt(year1)
						|| END_YEAR == Integer.parseInt(year1)
						&& END_MONTH > Integer.parseInt(month1)
						|| END_YEAR == Integer.parseInt(year1)
						&& END_MONTH == Integer.parseInt(month1)
						&& END_DATE >= Integer.parseInt(day1)) {
					if (!NetWorkCenter.isNetworkConnected(con)) {
						Toast.makeText(con, "请检查你的网络!", 1).show();
						return;
					}
					etTemp.setText(year1 + "-" + month1 + "-" + day1);
					if (listener != null) {
						listener.pressDateBtn(R.id.ok_button);
					}
					dialog.dismiss();
				} else {
					Toast.makeText(con, "亲，你选择的日期不能晚于今天哦", 1).show();
				}
			
			}
		});
		
		// 取消
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		
		// 设置dialog的布局,并显示
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();

		Window win = dialog.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}

	private DateBtnListener listener;

	public interface DateBtnListener {
		void pressDateBtn(int id);
	}

	public void setDateBtnListener(DateBtnListener dateListener) {
		this.listener = dateListener;
	}

}
