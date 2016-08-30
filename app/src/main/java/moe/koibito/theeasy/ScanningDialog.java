package moe.koibito.theeasy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ScanningDialog extends Dialog {

    private Button cancelButton;
    private Button acceptButton;
    private Button minusButton;
    private Button plusButton;
    private TextView secondText;
    private int scanningTime=1;
    private MainActivity activity;

    public ScanningDialog(Context context,MainActivity _activity) {
        super(context);
        activity=_activity;
    }

    public ScanningDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.scanning_dialog);

        cancelButton = (Button) findViewById(R.id.btn_cancel);
        acceptButton = (Button) findViewById(R.id.btn_accept);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.plus_button);
        secondText = (TextView) findViewById(R.id.scanning_time);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanningTime-=1;
                if(scanningTime<1)
                    scanningTime=1;
                refreshTextView();
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanningTime+=1;
                refreshTextView();
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.OnDialogResult(true,scanningTime);
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.OnDialogResult(false,0);
                dismiss();
            }
        });
    }

    public void refreshTextView(){
        secondText.setText(scanningTime+"초");
    }
}