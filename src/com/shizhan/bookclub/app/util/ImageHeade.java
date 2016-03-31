package com.shizhan.bookclub.app.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageHeade {
	
	private String imageUrl;
	private ImageView personHead;
	
	public static final int SHOW_RESPONSE = 0;

	private Handler handler = new Handler() {

		// 进行UI操作，将头像显示出来
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				Bitmap bitmap = (Bitmap) msg.obj;
				personHead.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		}
	};
	
	public ImageHeade(String imageUrl, ImageView personHead) {
		this.imageUrl = imageUrl;
		this.personHead = personHead;
	}



	// 下载服务器上的头像并显示
	public void setImageHead() {
		// 开启线程来发起网络请求
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(imageUrl);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(in);

					Message message = new Message();
					message.what = SHOW_RESPONSE;
					message.obj = bitmap; // 将服务器返回的结果存放到Message中
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}
