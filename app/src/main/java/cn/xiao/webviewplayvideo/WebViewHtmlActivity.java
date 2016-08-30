package cn.xiao.webviewplayvideo;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView加载网页
 * Created by joe.xiang on 2016/3/22.
 */
public class WebViewHtmlActivity extends Activity {

    private  WebView webView;
    private  WebSettings webSettings;
    private static final String URL="http://www.2345.com/?hz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_html);
        initview();
    }


    /**
     * 初始化webview相应的参数
     */
    public void initview(){
        webView = (WebView)findViewById(R.id.mwebview);
        // 获取webview的相关配置
        webSettings = webView.getSettings();
        //webview的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        //自动缩放
        webSettings.setSupportZoom(true);
        //设置webview的插件转状态
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //允许与js交互
        webSettings.setJavaScriptEnabled(true);
        //设置默认的字符编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //webveiw加载网页的方式
//        webView.loadDataWithBaseURL();
//        webView.loadData();
          webView.loadUrl(URL);

        //为了防止和过滤掉一些其他的网页地址我们可以重写shouldOverrideUrlLoading
        //来覆盖掉之前的url加载路径
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return  true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            /** 你可以在出话之前加载一些资源*/
            @Override
            public void onLoadResource(WebView view, String url) {

            }
        });
    }

}
