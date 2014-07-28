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

		// 获取按钮
		Button but_help_feedback = (Button) findViewById(R.id.but_help_feedback);
		help_feedback = (EditText) findViewById(R.id.help_feedback);

		// 添加点击事件 ，保存文本信息，并生成提示，同时跳转到主界面
		but_help_feedback.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String Context = help_feedback.getText().toString();
				;
				// 保存
				try {
					// 调用网络接口，实现登陆指令
//					Boolean flags = UserDataServiceHelper.SendFeedBack(
//							new UserDataReadHelper(Help.this).GetUserNiceName(),
//							Context);

					Toast.makeText(FeedBackActivity.this, "感谢您的反馈,我们会尽快处理您的意见。",
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
