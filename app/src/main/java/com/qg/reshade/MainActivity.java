package com.qg.reshade;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;
import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {
    static { System.loadLibrary("qgreshade"); }
    private native String probeKernel();
    
    private static final int SHIZUKU_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setTextSize(18);
        tv.setPadding(50, 50, 50, 50);
        setContentView(tv);

        // 申请 Shizuku 提权
        if (!Shizuku.isPreV11() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(SHIZUKU_REQUEST_CODE);
        } else {
            runKernelProbe(tv);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SHIZUKU_REQUEST_CODE) {
            runKernelProbe((TextView) findViewById(android.R.id.content));
        }
    }

    private void runKernelProbe(TextView tv) {
        String res = probeKernel();
        tv.setText("=== QG ReShade 内核演化 ===\n\n" + res + "\n\n(已在底层挂载超弦算子)");
    }
}
