package com.qg.reshade;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;
import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {
    static { 
        try {
            System.loadLibrary("qgreshade"); 
        } catch (Exception e) {
            // 防止.so加载失败导致闪退
        }
    }
    private native String probeKernel();
    
    private static final int SHIZUKU_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setTextSize(18);
        tv.setPadding(50, 50, 50, 50);
        setContentView(tv);
        tv.setText("=== QG ReShade 启动中 ===\n等待 Shizuku 响应...");

        try {
            // 检查 Shizuku 是否存活
            if (Shizuku.pingBinder()) {
                if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                    runKernelProbe(tv);
                } else {
                    tv.setText("⚠️ 需要 Shizuku 授权，请点击同意...");
                    Shizuku.requestPermission(SHIZUKU_REQUEST_CODE);
                }
            } else {
                tv.setText("❌ Shizuku 服务未启动！\n请先去后台打开 Shizuku App 启动服务。");
            }
        } catch (Throwable t) {
            tv.setText("⚠️ Shizuku 接口异常: " + t.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SHIZUKU_REQUEST_CODE) {
            TextView tv = new TextView(this);
            tv.setTextSize(18);
            tv.setPadding(50, 50, 50, 50);
            setContentView(tv);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runKernelProbe(tv);
            } else {
                tv.setText("❌ 授权被拒绝，内核演化无法进行。");
            }
        }
    }

    private void runKernelProbe(TextView tv) {
        try {
            String res = probeKernel();
            tv.setText("=== QG ReShade 内核演化 ===\n\n" + res + "\n\n(已在底层挂载超弦算子)");
        } catch (Throwable t) {
            tv.setText("💥 JNI 底层探针崩溃:\n" + t.toString());
        }
    }
}
