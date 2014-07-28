package com.compus.function.schedule;

import java.util.List;











import com.compus.R;
import com.compus.domain.Schedule;
import com.compus.tools.SPUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��viewPager�ϲ�ͬ����,��һ������
 * 
 * @author chl
 * 
 */
public class GetSchedule {
	private static String[] days1 = { "����һ", "���ڶ�", "������", "������", "������", "������",
			"������" };
	private TextView tv0, tv1_2, tv1_3, tv2_2, tv2_3, tv3_2, tv3_3, tv4_2,
			tv4_3, tv5_2, tv5_3, tv6_2, tv6_3;
//	private String[] course = new String[7];
//	private String[] add = new String[7];
//	private Cursor mCursor;
	private Context context;
	private TableRow tr1,tr2,tr3,tr4,tr5,tr6;
	TableLayout tl;
	PopupWindow popup ;
	SPUtil sp;
	public GetSchedule(Context context) {
		this.context = context;
		sp = new SPUtil(context);
	}

	public View getScheduleView(int week) {// 1Ϊ����һ

		// View view=View.inflate(this, R.layout.app_schedule_show_page, null);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View myView = mInflater.inflate(R.layout.app_schedule_show_page, null);

		tv0 = (TextView) myView.findViewById(R.id.show_tv0);

		tv1_2 = (TextView) myView.findViewById(R.id.show_tv1_2);
		tv1_3 = (TextView) myView.findViewById(R.id.show_tv1_3);

		tv2_2 = (TextView) myView.findViewById(R.id.show_tv2_2);
		tv2_3 = (TextView) myView.findViewById(R.id.show_tv2_3);

		tv3_2 = (TextView) myView.findViewById(R.id.show_tv3_2);
		tv3_3 = (TextView) myView.findViewById(R.id.show_tv3_3);

		tv4_2 = (TextView) myView.findViewById(R.id.show_tv4_2);
		tv4_3 = (TextView) myView.findViewById(R.id.show_tv4_3);

		tv5_2 = (TextView) myView.findViewById(R.id.show_tv5_2);
		tv5_3 = (TextView) myView.findViewById(R.id.show_tv5_3);
		
		tv6_2 = (TextView) myView.findViewById(R.id.show_tv6_2);
		tv6_3 = (TextView) myView.findViewById(R.id.show_tv6_3);

		// Log.i("GetSchedule", week+"");

		// SQLiteDatabase db;
		// toDoDB=new ToDoDB(context);
		// db=toDoDB.getReadableDatabase();
		// String
		// sql="select * from todo_schedule where todo_week="+week;//1Ϊ����һ
		// mCursor =db.rawQuery(sql, null);
		// Log.i("", sql);
		// //�ж��α��Ƿ�Ϊ��
		// if(mCursor !=null ){
		// int i=0,n=mCursor .getCount();
		// mCursor .moveToFirst();
		// Log.i("", "mCursor !=null");
		// Log.i("n=?", n+"");
		//
		// //�����α� 11.
		// while (!mCursor .isAfterLast()) {
		//
		// //���ID
		// //int id = mCursor .getInt(0);
		// //����û���
		// course[i]=mCursor .getString(3);
		// Log.i("", mCursor .getString(3));
		// //�������
		// add[i]=mCursor .getString(4);
		// i++;
		// mCursor .moveToNext();
		// }
		// }

		tv0.setText(days1[week - 1]);

		// tv1_2.setText(course[0]);
		// tv1_3.setText(add[0]);
		//
		// tv2_2.setText(course[1]);
		// tv2_3.setText(add[1]);
		//
		// tv3_2.setText(course[2]);
		// tv3_3.setText(add[2]);
		//
		// tv4_2.setText(course[3]);
		// tv4_3.setText(add[3]);
		//
		// tv5_2.setText(course[4]);
		// tv5_3.setText(add[4]);
		//
		//
		// Log.i("tv", "������tv");
		// mCursor .close();
		// toDoDB.close();
		return myView;
	}

	public View loadDataFromNet(View myView, int week, List<Schedule> list) {// 1Ϊ����һ
		tv0 = (TextView) myView.findViewById(R.id.show_tv0);

		tv1_2 = (TextView) myView.findViewById(R.id.show_tv1_2);
		tv1_3 = (TextView) myView.findViewById(R.id.show_tv1_3);

		tv2_2 = (TextView) myView.findViewById(R.id.show_tv2_2);
		tv2_3 = (TextView) myView.findViewById(R.id.show_tv2_3);

		tv3_2 = (TextView) myView.findViewById(R.id.show_tv3_2);
		tv3_3 = (TextView) myView.findViewById(R.id.show_tv3_3);

		tv4_2 = (TextView) myView.findViewById(R.id.show_tv4_2);
		tv4_3 = (TextView) myView.findViewById(R.id.show_tv4_3);

		tv5_2 = (TextView) myView.findViewById(R.id.show_tv5_2);
		tv5_3 = (TextView) myView.findViewById(R.id.show_tv5_3);
		
		tv6_2 = (TextView) myView.findViewById(R.id.show_tv6_2);
		tv6_3 = (TextView) myView.findViewById(R.id.show_tv6_3);
		
		tr1 = (TableRow) myView.findViewById(R.id.tb1);
		tr2 = (TableRow) myView.findViewById(R.id.tb2);
		tr3 = (TableRow) myView.findViewById(R.id.tb3);
		tr4 = (TableRow) myView.findViewById(R.id.tb4);
		tr5 = (TableRow) myView.findViewById(R.id.tb5);
		tr6 = (TableRow) myView.findViewById(R.id.tb6);
		
		tl = (TableLayout) myView.findViewById(R.id.TLSS);
		
		tv0.setText(days1[week - 1]);
		 tv1_2.setText("");
		 tv1_3.setText("");
		
		 tv2_2.setText("");
		 tv2_3.setText("");
		
		 tv3_2.setText("");
		 tv3_3.setText("");
		
		 tv4_2.setText("");
		 tv4_3.setText("");
		
		 tv5_2.setText("");
		 tv5_3.setText("");

//		 myView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {Toast.makeText(context, "--66->", 1).show();
//				if(popup!= null && popup.isShowing()){
//					popup.dismiss();Toast.makeText(context, "--2222->", 1).show();
//					return true;
//				}
//				return false;
//			}
//		});
		for (int i = 0; i < list.size(); i++) {
			Schedule schedule = list.get(i);
			if ("һ".equals(schedule.getJc())) {
				tv1_2.setText(schedule.getName());
				tv1_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr1);
			}

			if ("��".equals(schedule.getJc())) {
				tv2_2.setText(schedule.getName());
				tv2_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr2);
			}

			if ("��".equals(schedule.getJc())) {
				tv3_2.setText(schedule.getName());
				tv3_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr3);
			}

			if ("��".equals(schedule.getJc())) {
				tv4_2.setText(schedule.getName());
				tv4_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr4);
			}

			if ("��".equals(schedule.getJc())) {
				tv5_2.setText(schedule.getName());
				tv5_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr5);
			}
			
			if ("��".equals(schedule.getJc())) {
				tv6_2.setText(schedule.getName());
				tv6_3.setText(schedule.getBuilding()+schedule.getRoom());
				showPopup(tl, week, schedule, tr6);
			}

		}

//		tv1_2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Log.i(Constants.TAG, "---->"+days1[tt - 1]+((TextView)v).getText());
//				Toast.makeText(context, "---->"+days1[tt - 1]+((TextView)v).getText(), 1).show();
//			}
//		});
		// Log.i("tv", "������tv");
		// mCursor .close();
		// toDoDB.close();
		return myView;
	}

	private void showPopup(TableLayout myView, int week, Schedule schedule, TableRow trr) {
		final Schedule s = schedule;
		final View mymyView = myView;
		final TableRow tr = trr;
		final TableRow trLayout = tr1;
		final int w = week;
		tr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {mymyView.setVisibility(View.INVISIBLE);
				//tr.setEnabled(false);tr.setClickable(false);
				View view = LayoutInflater.from(context).inflate(R.layout.schedule_detail_dialog, null);
				popup = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				popup.showAsDropDown(trLayout);
				popup.setFocusable(false);
				popup.setOutsideTouchable(true);
				popup.showAtLocation(trLayout, Gravity.CENTER, 0, 0);
				ColorDrawable dw = new ColorDrawable(-00000);
				popup.setBackgroundDrawable(dw);
				
				final Button btnSetting = (Button)view.findViewById(R.id.btnSetting);
				if(!(sp.getValue(w+s.getJc(), true))) {
					btnSetting.setText(R.string.schedule_add);
				}
				
				Button btnDetail = (Button)view.findViewById(R.id.btnDetail);
				Button btnExam = (Button)view.findViewById(R.id.btnExam);
				Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
				popup.update();
				btnSetting.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(sp.getValue(w+s.getJc(), true)) {
							sp.setValue(w+s.getJc(), false);
							btnSetting.setText(R.string.schedule_add);
							Toast.makeText(context, "ȡ���ɹ�", Toast.LENGTH_SHORT).show();
						}else {
							sp.setValue(w+s.getJc(), true);
							btnSetting.setText(R.string.schedule_setting_cancel);
							Toast.makeText(context, "��ӳɹ�", Toast.LENGTH_SHORT).show();
						}
					}
				});
				btnDetail.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						StringBuilder ss = new StringBuilder();
						ss.append("�γ̺ţ�"+s.getScheduleid());
						ss.append("\n�γ����ƣ�"+s.getName());
						ss.append("\nѧ�֣�"+s.getCredit());
						ss.append("\n�γ����ԣ�"+s.getAttrubute());
						ss.append("\n�������ͣ�"+s.getExam());
						ss.append("\n��ʦ��"+s.getTeacher());
						if("full".equals(s.getType())){
							ss.append("\n�ܴΣ�"+s.getBeginweeek()+"��-��"+s.getEndweek()+"��");
						}
						if("odd".equals(s.getType())){
							ss.append("\n�ܴΣ�"+s.getBeginweeek()+"��-��"+s.getEndweek()+"�ܣ������Ͽ�");
						}
						if("even".equals(s.getType())){
							ss.append("\n�ܴΣ�"+s.getBeginweeek()+"��-��"+s.getEndweek()+"�ܣ�˫���Ͽ�");
						}
						getDialogAbout(context, "�γ�����", ss.toString());
					}
				});
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {//Toast.makeText(context, "--4444->"+s.getName()+s.getType(), 0).show();
					if(popup!= null && popup.isShowing()){
						popup.dismiss();//Toast.makeText(context, "--ss->"+s.getName()+s.getType(), 0).show();
						mymyView.setVisibility(View.VISIBLE);
					}

					}
				});
				btnExam.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(context, "��δ������", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	public static Dialog getDialogAbout(Context context,String title,String mess) {
		  final Activity activit = (Activity)context;
		  LayoutInflater inflater =LayoutInflater.from(activit);
		  //R.layout.dialog_xml  dialog�����ļ�
		  View view = inflater.inflate(R.layout.dialog_detail, (ViewGroup)activit.findViewById(R.id.dialog));
		  // �ı��ؼ�
		  TextView text1 = (TextView)view.findViewById(R.id.texit1);
		  text1.setText(mess);
		  AlertDialog.Builder builder = new AlertDialog.Builder(activit);
		  builder.setIcon(R.drawable.ic_launcher);
		  builder.setTitle(title);
		//  builder.setMessage(mess);
		  //��Ӳ���
		  builder.setView(view);
//		  builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//		   @Override
//		   public void onClick(DialogInterface arg0, int arg1) {
//			  builder.
//		   }
//		  });
		  builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		  return builder.show();
		}
}
