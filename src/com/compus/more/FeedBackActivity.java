package com.compus.more;

import com.compus.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends Activity {
	EditText help_feedback = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity_feedback);

		// ��ȡ��ť
		Button but_help_feedback = (Button) findViewById(R.id.but_help_feedback);
		help_feedback = (EditText) findViewById(R.id.help_feedback);

		// ��ӵ���¼� �������ı���Ϣ����������ʾ��ͬʱ��ת��������
		but_help_feedback.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String Context = help_feedback.getText().toString();
				;
				// ����
				try {
					// ��������ӿڣ�ʵ�ֵ�½ָ��
//					Boolean flags = UserDataServiceHelper.SendFeedBack(
//							new UserDataReadHelper(Help.this).GetUserNiceName(),
//							Context);

					Toast.makeText(FeedBackActivity.this, "��л���ķ���,���ǻᾡ�촦�����������",
							Toast.LENGTH_SHORT).show();
					finish();;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
			}
		});

	}

}
