package ir.farzadshami.quran;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import ir.farzadshami.quran.models.Download;

public class OnlyDownloadActivity extends FragmentActivity {

    private static final int container_id = 89723489;
    public static final int RESULT_ID = 98;
    private static Download.Type type;
    private static Integer[] josz;
    private boolean isDownloadFinished = false;

    public OnlyDownloadActivity(Download.Type type , Integer[] jozs){
        this.type = type;
        this.josz = jozs;
    }
    public OnlyDownloadActivity(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout v = new LinearLayout(this);
        v.setId(container_id);
        setContentView(v);

        DownloadMangerActivity fragment = new DownloadMangerActivity();
        getSupportFragmentManager().beginTransaction().
                replace(container_id , fragment)
                .commit();

        fragment.setInitialDowload(type , josz);
    }

    @Override
    protected void onDestroy() {
        Intent data =new Intent();
        data.putExtra("downloaded" , isDownloadFinished);
        setResult(RESULT_ID , data);
        super.onDestroy();
    }

    public void setDownloadFinished(boolean downloadFinished) {
        isDownloadFinished = downloadFinished;
    }
}
