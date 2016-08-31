package moe.koibito.theeasy;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Debug;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;


public class MainActivity extends Activity implements View.OnTouchListener {
    private LinearLayout basePanel;
    private boolean isTyping = false;
    private TextView inputText;
    private ImageButton typeSpeak;
    private ImageButton typeBack;
    private TextToSpeech ttsManager;
    private int speakCount = 0;
    private int preTextCount = 0;
    private int backCount = 0;
    private boolean isLock = false;
    private ArrayList<String> elementList = new ArrayList<>();
    private ArrayList<LinearLayout> pannels = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private boolean isScanning = false;
    private int scanningTime = 0;
    private Timer mTimer;
    private int[] ColorList = {0xefff5350, 0xffff9700, 0xfffedc3b, 0xff66bb6a, 0xff42a5f5, 0xffab47bc};
    private String[] textList = {"ㅁ", "ㄴ", "ㄹ", "ㅇ", "ㅎ", "ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "?", "―", "ㅏ", "ㅣ", "ㅡ", "ㅗ", "ㅜ", "ㅓ", "ㅑ", "ㅐ", "ㅔ", "ㅛ", "ㅠ", "ㅕ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private TimerTask mTask;
    private int _preCurrentScanIndex = -3;
    private int _currentScanIndex = -2;
    private int timeCounter = 0;
    private TimerTask focusTask;
    private Timer focustTimer;

    private final Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getString("message").equals("scan")) {
                if (isScanning) {
                    timeCounter += 1;
                    isAction = false;
                    if (scanningTime <= timeCounter) {
                        timeCounter = 0;
                        final int preCurrentScanIndex = _preCurrentScanIndex;
                        final int currentScanIndex = _currentScanIndex;
                        if (isTyping) {
                            if (preCurrentScanIndex != -3) {
                                if (preCurrentScanIndex == -2)
                                    ((GradientDrawable) ((LayerDrawable) typeSpeak.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);
                                if (currentScanIndex == -2)
                                    ((GradientDrawable) ((LayerDrawable) typeSpeak.getBackground()).getDrawable(1)).setStroke(5, 0xffff0000);
                                if (preCurrentScanIndex == -1)
                                    ((GradientDrawable) ((LayerDrawable) typeBack.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);
                                if (currentScanIndex == -1)
                                    ((GradientDrawable) ((LayerDrawable) typeBack.getBackground()).getDrawable(1)).setStroke(5, 0xffff0000);
                            }
                            if (-1 <= preCurrentScanIndex) {
                                if (preCurrentScanIndex != -1&&preCurrentScanIndex<buttons.size())
                                    ((GradientDrawable) ((LayerDrawable) buttons.get(preCurrentScanIndex).getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);
                                if(-1<currentScanIndex&&currentScanIndex<buttons.size())
                                    ((GradientDrawable) ((LayerDrawable) buttons.get(currentScanIndex).getBackground()).getDrawable(1)).setStroke(8, 0xffff0000);
                            }
                            _preCurrentScanIndex = currentScanIndex;
                            _currentScanIndex += 1;
                            if (buttons.size()-1 <= preCurrentScanIndex)
                                _preCurrentScanIndex = -2;
                            if (buttons.size()-1 <= currentScanIndex)
                                _currentScanIndex = -2;
                            Log.d("index", preCurrentScanIndex + "/" + currentScanIndex);
                        } else {
                            if (preCurrentScanIndex == -2)
                                ((GradientDrawable) ((LayerDrawable) typeSpeak.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);
                            if (currentScanIndex == -2)
                                ((GradientDrawable) ((LayerDrawable) typeSpeak.getBackground()).getDrawable(1)).setStroke(5, 0xffff0000);
                            if (preCurrentScanIndex == -1)
                                ((GradientDrawable) ((LayerDrawable) typeBack.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);
                            if (currentScanIndex == -1)
                                ((GradientDrawable) ((LayerDrawable) typeBack.getBackground()).getDrawable(1)).setStroke(5, 0xffff0000);
                            if (-1 <= preCurrentScanIndex || 0 <= currentScanIndex) {
                                if (preCurrentScanIndex != -1&&preCurrentScanIndex<pannels.size())
                                    ((GradientDrawable) ((LayerDrawable) pannels.get(preCurrentScanIndex).getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);
                                if (-1 < currentScanIndex&&currentScanIndex<pannels.size())
                                    ((GradientDrawable) ((LayerDrawable) pannels.get(currentScanIndex).getBackground()).getDrawable(1)).setStroke(8, 0xffff0000);
                            }
                            _preCurrentScanIndex = currentScanIndex;
                            _currentScanIndex += 1;
                            if (pannels.size()-1 <= preCurrentScanIndex)
                                _preCurrentScanIndex = -2;
                            if (pannels.size()-1 <= currentScanIndex)
                                _currentScanIndex = -2;
                            ((GradientDrawable) ((LayerDrawable) getDrawable(R.drawable.xml_lines)).getDrawable(1)).setStroke(8, 0x00ff0000);
                            Log.d("index", preCurrentScanIndex + "/" + currentScanIndex);
                        }
                    }
                }
            } else if (msg.getData().getString("message").equals("focus")) {
                String baseText = (String) inputText.getText();
                if (0<baseText.length()&&String.valueOf(baseText.charAt(baseText.length()-1)).equals("|")) {
                    baseText = baseText.substring(0, baseText.length() - 1);
                } else {
                    baseText = baseText + "|";
                }
                inputText.setText(baseText);
            }
            super.handleMessage(msg);
        }
    };
    private boolean isAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        basePanel = (LinearLayout) findViewById(R.id.base_panel);
        inputText = (TextView) findViewById(R.id.input_field);
        typeSpeak = (ImageButton) findViewById(R.id.type_speakbutton);
        typeBack = (ImageButton) findViewById(R.id.type_backbutton);
        basePanel.setOnTouchListener(this);
        inputText.setOnTouchListener(this);
        typeBack.setOnTouchListener(this);
        typeSpeak.setOnTouchListener(this);
        final ImageView icLock = (ImageView) findViewById(R.id.ic_lock);
        final MainActivity activity = this;
        mTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("message", "scan");
                msg.setData(bundle);
                UIHandler.sendMessage(msg);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 1000, 1000);
        focusTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("message", "focus");
                msg.setData(bundle);
                UIHandler.sendMessage(msg);
            }
        };
        focustTimer = new Timer();
        focustTimer.schedule(focusTask, 500, 500);
        ttsManager = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    ttsManager.setLanguage(Locale.KOREAN);
                }
            }
        });
        typeSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id = ++speakCount;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (id < speakCount || speakCount < id)
                            return;
                        if (id == speakCount) {
                            if (speakCount == 2 && backCount == 2) {
                                new ScanningDialog(activity, activity).show();
                                speakCount = 0;
                                backCount = 0;
                            } else if (speakCount == 1 && backCount == 1) {
                                isLock = !isLock;
                                if(isLock)
                                    icLock.setVisibility(View.VISIBLE);
                                else
                                    icLock.setVisibility(View.INVISIBLE);
                                speakCount = 0;
                                backCount = 0;
                            } else if (speakCount == id) {
                                String utteranceId = this.hashCode() + "";
                                ttsManager.speak(inputText.getText(), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                inputText.setText("");
                                elementList.clear();
                                preTextCount = 0;
                                speakCount = 0;
                                backCount = 0;
                            }
                        }
                    }
                }, 500);
            }
        });
        typeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id = ++backCount;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (id < backCount || backCount < id)
                            return;
                        if (id == backCount) {
                            if (speakCount == 2 && backCount == 2) {
                                new ScanningDialog(activity, activity).show();
                                speakCount = 0;
                                backCount = 0;
                            } else if (speakCount == 1 && backCount == 1) {
                                isLock = !isLock;
                                if(isLock)
                                    icLock.setVisibility(View.VISIBLE);
                                else
                                    icLock.setVisibility(View.INVISIBLE);
                                speakCount = 0;
                                backCount = 0;
                            } else if (backCount == id) {
                                String baseText = (String) inputText.getText();
                                if (0 < baseText.length()) {
                                    baseText = baseText.substring(0, baseText.length() - 1);
                                }
                                inputText.setText(baseText);
                                elementList.clear();
                                preTextCount = 0;
                                speakCount = 0;
                                backCount = 0;
                            }
                        }
                    }
                }, 500);
            }
        });
        inputText.setText("");
        LinearLayout typePanel = (LinearLayout) basePanel.getChildAt(0);
        typePanel.setOnTouchListener(this);
        LinearLayout typeTop = (LinearLayout) typePanel.getChildAt(0);
        typeTop.setOnTouchListener(this);
        LinearLayout typeBottom = (LinearLayout) typePanel.getChildAt(1);
        typeBottom.setOnTouchListener(this);
        pannels.clear();
        int characterCount = 0;
        for (int i = 0; i < 3; i++) {
            LinearLayout child = (LinearLayout) typeTop.getChildAt(i);
            child.setOnTouchListener(this);
            final int finalI1 = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isScanning) return;
                    if (isLock) return;
                    OnKeyboardSet(finalI1 + 1);
                }
            });
            LayerDrawable drawable = (LayerDrawable) child.getBackground();
            GradientDrawable grDrawable = (GradientDrawable) drawable.getDrawable(0);
            grDrawable.setColor(ColorList[0 + i]);
            pannels.add(child);
            LinearLayout childTop = (LinearLayout) child.getChildAt(0);
            LinearLayout childBottom = (LinearLayout) child.getChildAt(1);
            switch (childTop.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        Button button = (Button) childTop.getChildAt(1 + index);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnTouchListener(this);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 1));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;
                    }
                    break;
                default:
                    for (int index = 0; index < childTop.getChildCount(); index++) {
                        Button button = (Button) childTop.getChildAt(index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 1));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;

                    }
                    break;
            }
            switch (childBottom.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        Button button = (Button) childBottom.getChildAt(1 + index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 1));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;

                    }
                    break;
                default:
                    for (int index = 0; index < childBottom.getChildCount(); index++) {
                        Button button = (Button) childBottom.getChildAt(index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 1));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;
                    }
                    break;
            }
        }
        for (int i = 0; i < 3; i++) {
            LinearLayout child = (LinearLayout) typeBottom.getChildAt(i);
            child.setOnTouchListener(this);
            final int finalI1 = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isScanning) return;
                    if (isLock) return;
                    OnKeyboardSet(finalI1 + 4);
                }
            });
            LayerDrawable drawable = (LayerDrawable) child.getBackground();
            GradientDrawable grDrawable = (GradientDrawable) drawable.getDrawable(0);
            grDrawable.setColor(ColorList[3 + i]);
            pannels.add(child);
            LinearLayout childTop = (LinearLayout) child.getChildAt(0);
            LinearLayout childBottom = (LinearLayout) child.getChildAt(1);
            switch (childTop.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        Button button = (Button) childTop.getChildAt(1 + index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 4));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;
                    }
                    break;
                default:
                    for (int index = 0; index < childTop.getChildCount(); index++) {
                        Button button = (Button) childTop.getChildAt(index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 4));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;

                    }
                    break;

            }
            switch (childBottom.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        Button button = (Button) childBottom.getChildAt(1 + index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 4));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;

                    }
                    break;
                default:
                    for (int index = 0; index < childBottom.getChildCount(); index++) {
                        Button button = (Button) childBottom.getChildAt(index);
                        button.setOnTouchListener(this);
                        final int finalI = i;
                        final int finalCharacterCount = characterCount;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isScanning) return;
                                if (isLock) {
                                    String text = textList[finalCharacterCount];
                                    typeString(text, finalCharacterCount);
                                } else {
                                    OnKeyboardSet((finalI + 4));
                                }
                            }
                        });
                        button.setText(textList[characterCount]);
                        buttons.add(button);
                        characterCount += 1;
                    }
                    break;

            }
        }
        ((GradientDrawable) ((LayerDrawable) getDrawable(R.drawable.xml_lines)).getDrawable(0)).setColor(0xffffffff);
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        focustTimer.cancel();
        super.onDestroy();
    }

    void OnKeyboardSet(final int keypadnum) {
        for (int i = 0; i < basePanel.getChildCount(); i++)
            basePanel.removeView(basePanel.getChildAt(i));
        isTyping = true;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.input_panel, (ViewGroup) findViewById(R.id.input_panel));
        basePanel.addView(child);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;

        child.setLayoutParams(params);

        LinearLayout inputSlot = (LinearLayout) child.findViewById(R.id.keypad_slot);
        for (int i = 0; i < inputSlot.getChildCount(); i++)
            inputSlot.removeView(inputSlot.getChildAt(i));
        LinearLayout keypad;
        int startNum = 0;
        int keypadCount = 0;
        if (keypadnum <= 2) {
            keypad = (LinearLayout) inflater.inflate(R.layout.button_panel, (ViewGroup) findViewById(R.id.button_pannel1));
            keypadCount = 5;
        } else if (keypadnum == 6) {
            keypad = (LinearLayout) inflater.inflate(R.layout.button_panel3, (ViewGroup) findViewById(R.id.button_pannel3));
            keypadCount = 10;
        } else {
            keypad = (LinearLayout) inflater.inflate(R.layout.button_panel2, (ViewGroup) findViewById(R.id.button_pannel2));
            keypadCount = 6;
        }
        inputSlot.addView(keypad);

        LinearLayout.LayoutParams keypadParams = (LinearLayout.LayoutParams) keypad.getLayoutParams();

        keypadParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        keypadParams.height = LinearLayout.LayoutParams.MATCH_PARENT;

        keypad.setLayoutParams(keypadParams);

        ((GradientDrawable) ((LayerDrawable) keypad.getBackground()).getDrawable(0)).setColor(ColorList[keypadnum - 1]);
        ((GradientDrawable) ((LayerDrawable) getDrawable(R.drawable.xml_lines)).getDrawable(0)).setColor(0xffffffff);

        switch (keypadnum) {
            case 1:
                startNum = 0;
                break;
            case 2:
                startNum = 5;
                break;
            case 3:
                startNum = 10;
                break;
            case 4:
                startNum = 16;
                break;
            case 5:
                startNum = 22;
                break;
            case 6:
                startNum = 28;
                break;
        }
        LinearLayout childTop = (LinearLayout) keypad.getChildAt(0);
        LinearLayout childBottom = (LinearLayout) keypad.getChildAt(1);
        switch (childTop.getChildCount()) {
            case 4:
                for (int index = 0; index < 2; index++) {
                    final Button button = (Button) childTop.getChildAt(1 + index);
                    final String msg = textList[startNum];
                    button.setText(msg);
                    startNum += 1;
                    final int finalStartNum = startNum - 1;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            String text = msg;
                            typeString(text, finalStartNum);
                        }
                    });
                }
                break;
            default:
                for (int index = 0; index < childTop.getChildCount(); index++) {
                    final Button button = (Button) childTop.getChildAt(index);
                    final String msg = textList[startNum];
                    button.setText(msg);
                    startNum += 1;
                    final int finalStartNum1 = startNum - 1;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            String text = msg;
                            typeString(text, finalStartNum1);
                        }
                    });

                }
                break;
        }
        switch (childBottom.getChildCount()) {
            case 4:
                for (int index = 0; index < 2; index++) {
                    final Button button = (Button) childBottom.getChildAt(1 + index);
                    final String msg = textList[startNum];
                    button.setText(msg);
                    startNum += 1;
                    final int finalStartNum2 = startNum - 1;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            String text = msg;
                            typeString(text, finalStartNum2);
                        }
                    });

                }
                break;
            default:
                for (int index = 0; index < childBottom.getChildCount(); index++) {
                    final Button button = (Button) childBottom.getChildAt(index);
                    final String msg = textList[startNum];
                    button.setText(msg);
                    startNum += 1;
                    final int finalStartNum3 = startNum - 1;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            String text = msg;
                            typeString(text, finalStartNum3);
                        }
                    });
                }
                break;

        }
        ImageButton homeButton = (ImageButton) child.findViewById(R.id.btn_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < basePanel.getChildCount(); i++)
                    basePanel.removeView(basePanel.getChildAt(i));
                isTyping = false;
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View typeChild = inflater.inflate(R.layout.type_panel, (ViewGroup) findViewById(R.id.type_panel));
                basePanel.addView(typeChild);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) typeChild.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                typeChild.setLayoutParams(params);
                LinearLayout typePanel = (LinearLayout) typeChild;
                LinearLayout typeTop = (LinearLayout) typePanel.getChildAt(0);
                LinearLayout typeBottom = (LinearLayout) typePanel.getChildAt(1);
                int characterCount = 0;
                pannels.clear();
                for (int i = 0; i < 3; i++) {
                    LinearLayout child = (LinearLayout) typeTop.getChildAt(i);
                    final int finalI1 = i;
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            OnKeyboardSet(finalI1 + 1);
                        }
                    });
                    LayerDrawable drawable = (LayerDrawable) child.getBackground();
                    GradientDrawable grDrawable = (GradientDrawable) drawable.getDrawable(0);
                    grDrawable.setColor(ColorList[0 + i]);
                    pannels.add(child);
                    LinearLayout childTop = (LinearLayout) child.getChildAt(0);
                    LinearLayout childBottom = (LinearLayout) child.getChildAt(1);
                    switch (childTop.getChildCount()) {
                        case 4:
                            for (int index = 0; index < 2; index++) {
                                Button button = (Button) childTop.getChildAt(1 + index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 1));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;
                            }
                            break;
                        default:
                            for (int index = 0; index < childTop.getChildCount(); index++) {
                                Button button = (Button) childTop.getChildAt(index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 1));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;

                            }
                            break;
                    }
                    switch (childBottom.getChildCount()) {
                        case 4:
                            for (int index = 0; index < 2; index++) {
                                Button button = (Button) childBottom.getChildAt(1 + index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 1));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;

                            }
                            break;
                        default:
                            for (int index = 0; index < childBottom.getChildCount(); index++) {
                                Button button = (Button) childBottom.getChildAt(index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 1));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;
                            }
                            break;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    LinearLayout child = (LinearLayout) typeBottom.getChildAt(i);
                    final int finalI1 = i;
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isLock) return;
                            OnKeyboardSet(finalI1 + 4);
                        }
                    });
                    LayerDrawable drawable = (LayerDrawable) child.getBackground();
                    GradientDrawable grDrawable = (GradientDrawable) drawable.getDrawable(0);
                    grDrawable.setColor(ColorList[3 + i]);
                    pannels.add(child);
                    LinearLayout childTop = (LinearLayout) child.getChildAt(0);
                    LinearLayout childBottom = (LinearLayout) child.getChildAt(1);
                    switch (childTop.getChildCount()) {
                        case 4:
                            for (int index = 0; index < 2; index++) {
                                Button button = (Button) childTop.getChildAt(1 + index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 4));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;
                            }
                        default:
                            for (int index = 0; index < childTop.getChildCount(); index++) {
                                Button button = (Button) childTop.getChildAt(index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 4));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;

                            }

                    }
                    switch (childBottom.getChildCount()) {
                        case 4:
                            for (int index = 0; index < 2; index++) {
                                Button button = (Button) childBottom.getChildAt(1 + index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 4));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;

                            }
                        default:
                            for (int index = 0; index < childBottom.getChildCount(); index++) {
                                Button button = (Button) childBottom.getChildAt(index);
                                final int finalI = i;
                                final int finalCharacterCount = characterCount;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isLock) {
                                            String text = textList[finalCharacterCount];
                                            typeString(text, finalCharacterCount);
                                        } else {
                                            OnKeyboardSet((finalI + 4));
                                        }
                                    }
                                });
                                button.setText(textList[characterCount]);
                                buttons.add(button);
                                characterCount += 1;
                            }

                    }
                }
                ((GradientDrawable) ((LayerDrawable) getDrawable(R.drawable.xml_lines)).getDrawable(0)).setColor(0xffffffff);
            }
        });
        ((GradientDrawable) ((LayerDrawable) getDrawable(R.drawable.xml_lines)).getDrawable(0)).setColor(0xffffffff);
    }

    void typeString(String text, int code) {
        String baseString = inputText.getText().toString();
        if (0<baseString.length()&&String.valueOf(baseString.charAt(baseString.length()-1)).equals("|")) {
            baseString = baseString.substring(0, baseString.length() - 1);
        }
        String elementString = text;
        Log.d("asdf", elementList.size() + "");
        if (elementList.size() > 0) {
            if (!combineVowels(elementList.get(elementList.size() - 1), elementString).equals(elementString)) {
                elementString = combineVowels(elementList.get(elementList.size() - 1), elementString);
                if (baseString.substring(baseString.length() - 1).matches(".*[ㄱ-ㅎ]+.*"))
                    baseString = baseString.substring(0, baseString.length() - 1);
                elementList.remove(elementList.size() - 1);
            }
        }
        if (elementString.matches(".*[ㅏ-ㅣ]+.*") && elementList.size() != 0) {
            String[] devideText = devideVowels(elementList.get(elementList.size() - 1));
            elementList.remove(elementList.size() - 1);
            for (String t : devideText) {
                elementList.add(t);
            }
        }
        if (code == 15)
            elementString = " ";
        elementList.add(elementString);
        try {
            String hangul = HangulParser.getInstance().assemble(elementList);
            baseString = baseString.substring(0, preTextCount);
            inputText.setText(baseString + hangul);
        } catch (HangulParserException e) {
            e.printStackTrace();
            inputText.setText(baseString + elementString);
            if (code == 14 || code == 15 || 28 <= code) {
                elementList.clear();
                preTextCount = baseString.length();
                char[] charArr = baseString.toCharArray();
                int numCount = 0;
                for (char c : charArr) {
                    String s = new String(c + "");
                    if (s.matches(".*[0-9]+.*")) {
                        numCount++;
                    }
                }
                preTextCount -= numCount / 2;
                preTextCount += numCount;
            }
        }
    }

    String combineVowels(String s1, String s2) {
        if (s1.equals("ㅜ")) {
            if (s2.equals("ㅓ"))
                return "ㅝ";
            if (s2.equals("l"))
                return "ㅟ";
            if (s2.equals("ㅔ"))
                return "ㅞ";
        } else if (s1.equals("ㅗ")) {
            if (s2.equals("ㅣ"))
                return "ㅚ";
            if (s2.equals("ㅐ"))
                return "ㅙ";
            if (s2.equals("ㅏ"))
                return "ㅘ";
        } else if (s1.equals("ㅡ")) {
            if (s2.equals("ㅣ"))
                return "ㅢ";
        } else if (s1.equals("ㅅ") && s2.equals("ㅅ")) {
            return "ㅆ";
        } else if (s1.equals("ㄱ") && s2.equals("ㄱ")) {
            return "ㄲ";
        } else if (s1.equals("ㅂ") && s2.equals("ㅂ")) {
            return "ㅃ";
        } else if (s1.equals("ㄷ") && s2.equals("ㄷ")) {
            return "ㄸ";
        } else if (s1.equals("ㅈ") && s2.equals("ㅈ")) {
            return "ㅉ";
        } else if (s1.equals("ㄹ") && s2.equals("ㄱ")) {
            return "ㄺ";
        } else if (s1.equals("ㄴ") && s2.equals("ㅈ")) {
            return "ㄵ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅁ")) {
            return "ㄻ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅂ")) {
            return "ㄼ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅅ")) {
            return "ㄽ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅌ")) {
            return "ㄾ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅍ")) {
            return "ㄿ";
        } else if (s1.equals("ㄹ") && s2.equals("ㅎ")) {
            return "ㅀ";
        }
        return s2;
    }

    String[] devideVowels(String s) {
        if (s.equals("ㄺ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㄱ";
            return array;
        } else if (s.equals("ㄵ")) {
            String[] array = new String[2];
            array[0] = "ㄴ";
            array[1] = "ㅈ";
            return array;
        } else if (s.equals("ㄻ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㅁ";
            return array;
        } else if (s.equals("ㄼ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㅂ";
            return array;
        } else if (s.equals("ㄽ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㅅ";
            return array;
        } else if (s.equals("ㄾ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㅌ";
            return array;
        } else if (s.equals("ㅀ")) {
            String[] array = new String[2];
            array[0] = "ㄹ";
            array[1] = "ㅎ";
            return array;
        }
        String[] array = new String[1];
        array[0] = s;
        return array;
    }

    public void OnDialogResult(boolean _isScanning, int _scanningTime) {
        this.isScanning = _isScanning;
        this.scanningTime = _scanningTime;
        if(isTyping){
            this.isScanning=false;
            Toast.makeText(getApplicationContext(),"Lock 상태에서만 스캐닝을 사용할 수 있습니다",Toast.LENGTH_LONG).show();
        }
        if(!isScanning){
            for(Button b : buttons)
                ((GradientDrawable) ((LayerDrawable) b.getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);

            for(LinearLayout p : pannels)
                ((GradientDrawable) ((LayerDrawable) p.getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);

            ((GradientDrawable) ((LayerDrawable) typeSpeak.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);

            ((GradientDrawable) ((LayerDrawable) typeBack.getBackground()).getDrawable(1)).setStroke(5, 0x00ff0000);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(motionEvent.getAction() == MotionEvent.ACTION_DOWN)) return false;
        if (!isScanning) return false;
        if (isAction) return false;
        final int currentScanIndex = _currentScanIndex;
        if (isTyping) {
            for (Button button : buttons) {
                ((GradientDrawable) ((LayerDrawable) button.getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);
            }
            if (currentScanIndex < 1) {
                if (currentScanIndex == -0) {
                    inputText.setText("");
                    isTyping = false;
                } else if (currentScanIndex == -1) {
                    String utteranceId = this.hashCode() + "";
                    ttsManager.speak(inputText.getText(), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    inputText.setText("");
                    isTyping = false;
                }
                return false;
            }
            final Button button = buttons.get(currentScanIndex - 1);
            final String text = (String) button.getText();
            int code = 0;
            for (String s : textList) {
                if (s.equals(text))
                    break;
                code += 1;
            }
            typeString(text, code);
            isAction = true;
            isTyping = false;
        } else {
            buttons.clear();
            if (currentScanIndex < 1) {
                if (currentScanIndex == -0) {
                    inputText.setText("");
                    isTyping = false;
                } else if (currentScanIndex == -1) {
                    String utteranceId = this.hashCode() + "";
                    ttsManager.speak(inputText.getText(), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    inputText.setText("");
                    isTyping = false;
                }
                return false;
            }
            LinearLayout keypad = pannels.get(currentScanIndex - 1);
            LinearLayout childTop = (LinearLayout) keypad.getChildAt(0);
            LinearLayout childBottom = (LinearLayout) keypad.getChildAt(1);
            switch (childTop.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        final Button button = (Button) childTop.getChildAt(1 + index);
                        buttons.add(button);
                    }
                    break;
                default:
                    for (int index = 0; index < childTop.getChildCount(); index++) {
                        final Button button = (Button) childTop.getChildAt(index);
                        buttons.add(button);
                    }
                    break;
            }
            switch (childBottom.getChildCount()) {
                case 4:
                    for (int index = 0; index < 2; index++) {
                        final Button button = (Button) childBottom.getChildAt(1 + index);
                        buttons.add(button);
                    }
                    break;
                default:
                    for (int index = 0; index < childBottom.getChildCount(); index++) {
                        final Button button = (Button) childBottom.getChildAt(index);
                        buttons.add(button);
                    }
                    break;

            }
            for (LinearLayout pannel : pannels) {
                ((GradientDrawable) ((LayerDrawable) pannel.getBackground()).getDrawable(1)).setStroke(8, 0x00ff0000);
            }
            _preCurrentScanIndex = -3;
            _currentScanIndex = -2;
            isAction = true;
            isTyping = true;
        }
        return false;
    }
}
