package ir.farzadshami.quran.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ir.farzadshami.quran.R;

public class MultiSwitch extends LinearLayout {

    private String[] switchTexts;
    private Button[] switches;
    private int[] weights;
    private SwitchToggleListener listener;
    private int currentSwitch = 0;
    private enum STATE{
        ENABLED,
        DISABLED
    }


    public MultiSwitch(Context context) {
        super(context);
    }

    public MultiSwitch(Context context , AttributeSet attributeSet){
        super(context , attributeSet);
    }

    public void setSwitcherlistener(SwitchToggleListener listener){
        this.listener = listener;
    }

    public void initialize(String[] switchTexts , int[] weights , SwitchToggleListener listener) {
        setSwitcherlistener(listener);
        this.switchTexts = switchTexts;
        switches = new Button[switchTexts.length];

        setOrientation(LinearLayout.HORIZONTAL);
        this.weights = weights;
        int sum = 0;
        for(int i =0 ; i < weights.length ; i++)
            sum+=weights[i];
        setWeightSum(sum);

        this.setLayoutDirection(LAYOUT_DIRECTION_RTL);

        buildSwitches();
    }

    private void buildSwitches(){
        for(int i =0 ; i < switchTexts.length; i ++){
            buildButton(i);
        }
    }

    public interface SwitchToggleListener {
        void onSwitchToggle(int which);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildButton(int which) {
        if(which < 0 || which > switchTexts.length - 1)
            return;
        Button leftButton = new Button(getContext());
        leftButton.setTag(which);
        leftButton.setOnClickListener(switcherlistener);
        leftButton.setLayoutParams(getButtonLayoutParams(which));
        leftButton.setGravity(Gravity.CENTER);
        leftButton.setText(switchTexts[which]);
        leftButton.setTextColor(getResources().getColor(R.color.white));

        if(which == 0)
            updateView(leftButton , STATE.ENABLED);
        else
            updateView(leftButton , STATE.DISABLED);

        addView(leftButton);
        switches[which] = leftButton;
    }

    private LayoutParams getButtonLayoutParams(int i) {
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = weights[i];
        return layoutParams;
    }


    private OnClickListener switcherlistener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            applySwitch((Integer) view.getTag());
            if(listener != null)
                listener.onSwitchToggle((Integer) view.getTag());
        }
    };

    private void applySwitch(int newstate){
        Button cur = switches[currentSwitch];
        Button now = switches[newstate];
        updateView(cur , STATE.DISABLED);
        updateView(now , STATE.ENABLED);
        currentSwitch = newstate;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateView(Button button , STATE state){
        int which = (Integer) button.getTag();
        switch(state){
            case ENABLED:
                if(which == switchTexts.length - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_left_enabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_right_enabled));
                }else if(which == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_right_enabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_right_enabled));
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_middle_enabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_middle_enabled));
                }
                button.setTextColor(getResources().getColor(R.color.white));
                break;
            case DISABLED:
                if(which == switchTexts.length - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_left_disabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_left_disabled));
                }else if(which == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_right_disabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_right_disabled));
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackground(getContext().getDrawable(R.drawable.switch_middle_disabled));
                    else
                        button.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.switch_middle_disabled));
                }
                button.setTextColor(getResources().getColor(R.color.green));
                break;
        }
    }

    public void updateButtonText(int which , String newtext){
        switches[which].setText(newtext);
    }

    public void setCurrentSwitch(int which){
        applySwitch(which);
        if(switcherlistener != null)
            switcherlistener.onClick(switches[which]);
    }
    public int getCurrentSwitch(){
        return currentSwitch;
    }
}
