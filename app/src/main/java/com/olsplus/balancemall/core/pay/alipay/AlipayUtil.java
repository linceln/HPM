package com.olsplus.balancemall.core.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.olsplus.balancemall.core.pay.util.ThreadPool;



public class AlipayUtil {

	private static final int SDK_PAY_FLAG = 1;

	private static AlipayUtil instance;
	private static Object lock = new Object();
	private Activity mActivity;

	private IPayCallBack callBack;

	private AlipayUtil(Activity activity) {
		this.mActivity = activity;
	}

	public static AlipayUtil getInstance(Activity activity) {
		synchronized (lock) {
			if (instance == null) {
				instance = new AlipayUtil(activity);
			}
			return instance;
		}
	}

	public void setCallBack(IPayCallBack callBack) {
		this.callBack = callBack;
	}



	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG:
				AlipayResult resultObj = new AlipayResult((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为"9000"则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {

					if (callBack != null) {
						callBack.onPaySuccess();
					}
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						// ToastUtils.showToast(mActivity, "支付结果确认中",
						// Toast.LENGTH_SHORT);
					} else {
						// ToastUtils.showToast(mActivity,
						// R.string.pay_alipay_result_failed,
						// Toast.LENGTH_SHORT);
						if (callBack != null) {
							callBack.onPayFailed();
						}
					}
				}
				break;
			default:
				break;
			}
		};
	};


	public void startPay(String payInfo) {
		ThreadPool.newInstance().execute(new PayRunnable(payInfo));
	}

	class PayRunnable implements Runnable {
		String payInfo;

		public PayRunnable(String payInfo) {
			this.payInfo = payInfo;
		}

		@Override
		public void run() {
			// 构造PayTask 对象
			PayTask alipay = new PayTask(mActivity);
			// 调用支付接口
			String result = alipay.pay(payInfo);
			// Log.i("result = " + result);
			Message msg = new Message();
			msg.what = SDK_PAY_FLAG;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	};


}
