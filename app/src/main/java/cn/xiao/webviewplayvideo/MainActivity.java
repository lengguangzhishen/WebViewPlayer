package cn.xiao.webviewplayvideo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

    private static final String TAG = "WebviewMainActivity";
    private WebView webView;
	private FrameLayout video_fullView;
	private View xCustomView;
	private ProgressDialog waitdialog = null;
	private WebChromeClient.CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;
    private WindowManager.LayoutParams mWindowAttrs;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.e(TAG, "oncreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

        mWindowAttrs = getWindow().getAttributes();

		waitdialog = new ProgressDialog(this);
		waitdialog.setTitle("加载");
		waitdialog.setMessage("加载");
		waitdialog.setIndeterminate(true);
		waitdialog.setCancelable(true);
		waitdialog.show();
		webView = (WebView) findViewById(R.id.webView);
		video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(false);
		ws.setLoadWithOverviewMode(false);
		ws.setSavePassword(true);
		ws.setSaveFormData(true);
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
		ws.setDomStorageEnabled(true);
		ws.setSupportMultipleWindows(false);
		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		webView.setWebViewClient(new myWebViewClient());
//		webView.loadUrl("http://172.16.10.87/m/player.html?uu=hd2o0wzypt&vu=a3c43de967");
		webView.loadUrl("http://yuntv.letv.com/bcloud.html?uu=hd2o0wzypt&vu=a3c43de967");
//		webView.loadUrl("http://172.16.10.87/m/kjcy.html");
	}
	public class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			waitdialog.dismiss();
		}
	}
	public class myWebChromeClient extends WebChromeClient {
		private View xprogressvideo;
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {

//            goneBar();
//
//            mWindowAttrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            getWindow().setAttributes(mWindowAttrs);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			webView.setVisibility(View.INVISIBLE);
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			video_fullView.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.setVisibility(View.VISIBLE);
		}
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)
				return;

//            showBar();
//
//            mWindowAttrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(mWindowAttrs);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
//			webView.setVisibility(View.VISIBLE);
		}
		@Override
		public View getVideoLoadingProgressView() {
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater
						.from(MainActivity.this);
				xprogressvideo = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}
	}
	public boolean inCustomView() {
		return (xCustomView != null);
	}
	public void hideCustomView() {
		xwebchromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
		webView.resumeTimers();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
		webView.pauseTimers();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		video_fullView.removeAllViews();
		webView.loadUrl("about:blank");
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.destroy();
		webView = null;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (inCustomView()) {
                // webViewDetails.loadUrl("about:blank");
				hideCustomView();
				return true;
			} else {
				webView.loadUrl("about:blank");
				MainActivity.this.finish();
			}
		}
		return false;
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged");
    }
}
