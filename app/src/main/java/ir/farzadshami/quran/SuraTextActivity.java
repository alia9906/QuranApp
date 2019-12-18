package ir.farzadshami.quran;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.farzadshami.quran.adapters.SuraTextAdapter;
import ir.farzadshami.quran.helpers.ADataBase;
import ir.farzadshami.quran.helpers.RecyclerItemClickListener;
import ir.farzadshami.quran.helpers.SoundPlayer;
import ir.farzadshami.quran.helpers.SqliteDbHelper;
import ir.farzadshami.quran.models.DbModel;
import ir.farzadshami.quran.models.Download;
import ir.farzadshami.quran.models.Favorite;
import ir.farzadshami.quran.models.SoundToPlay;
import ir.farzadshami.quran.models.Sura;
import ir.farzadshami.quran.widget.AddBookmarDialog;
import ir.farzadshami.quran.widget.OnBookMarkSelectedListener;

public class SuraTextActivity extends AppCompatActivity {
    private SuraTextAdapter adapter;
    private RecyclerView rv;
    private String position;
    private String versePosition;
    private int suraAyehPosition;
    private ArrayList<DbModel> suraText;
    public static SoundToPlay sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sura_text);
        position = GetExtras(savedInstanceState, "suraPosition");
        versePosition = GetExtras(savedInstanceState, "versePosition");
        SqliteDbHelper dbHelper = SqliteDbHelper.getInstance(getApplicationContext());
        dbHelper.openDatabase();
        suraText = dbHelper.getDetails("SuraId", position);
        rv = findViewById(R.id.rvText);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SuraTextAdapter(this, suraText);
        rv.setAdapter(adapter);
        if (versePosition != null) {
            int vPosition = Integer.valueOf(versePosition);
            rv.smoothScrollToPosition(vPosition);
            suraAyehPosition = vPosition + 1;
        }

        SettingsActivity.context = SuraTextActivity.this;
        sound = new SoundToPlay((TextView) findViewById(R.id.seek_text) , (SeekBar) findViewById(R.id.audio_seek));
        sound.setSuraId(Integer.valueOf(position));
        if(versePosition != null)
            sound.setVerseId(Integer.valueOf(versePosition));
        else
            sound.setVerseId(0);

        findViewById(R.id.stop_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.stop();
            }
        });

        findViewById(R.id.shuffle).setTag("once");
        findViewById(R.id.shuffle).setOnClickListener(shiffleListener);

        findViewById(R.id.play_pause).setTag("play");
        findViewById(R.id.play_pause).setOnClickListener(playPauseListener);
        findViewById(R.id.sound_type).setOnClickListener(tartilTahdirlistener);
        if(sound.getSoundType() == SoundToPlay.SoundType.TAHDIR)
            findViewById(R.id.sound_type).callOnClick();
        findViewById(R.id.sound_settings_in_sura).setOnClickListener(soundSettingsListener);

        ImageButton fullScreen = findViewById(R.id.go_full_screen);
        fullScreen.setTag("full");
        fullScreen.setOnClickListener(handleFullScreenListener);
        fullScreen = findViewById(R.id.exit_full_screen);
        fullScreen.setTag("out");
        fullScreen.setOnClickListener(handleFullScreenListener);

        final TextView current_aye_position = findViewById(R.id.cur_aye_num);
        current_aye_position.setOnClickListener(go_to_aye_listener);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                suraAyehPosition = getCurrentAyehPosition();
                current_aye_position.setText(String.valueOf(suraAyehPosition + 1));
            }
        });

        rv.addOnItemTouchListener(new RecyclerItemClickListener(SuraTextActivity.this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*
            */
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                new AlertDialog.Builder(SuraTextActivity.this)
                        .setTitle("گزینه ها")
                        .setItems(R.array.sure_text_options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    DbModel sura = suraText.get(position);
                                    String shareBody = "سوره " + "" + "آیه " + (position + 1) + "\n" + sura.getArabicText() + "\n" + sura.getFarsiText();
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "آیه");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری"));
                                    return;
                                }
                                if(i == 1){
                                    ClipboardManager cm = (ClipboardManager) SuraTextActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                    DbModel sura = suraText.get(position);
                                    String textToClipBoard = "سوره " + "" + "آیه " + (position + 1) + "\n" + sura.getArabicText() + "\n" + sura.getFarsiText();
                                    ClipData clip = ClipData.newPlainText("آیه", textToClipBoard);
                                    cm.setPrimaryClip(clip);
                                    Toast.makeText(SuraTextActivity.this, "آیه موزد نظر شما در حافظه کپی شد.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(i == 2){
                                    final Favorite favorite = new Favorite(null, null, String.valueOf(suraText.get(0).getSuraId() - 1), position);
                                    ADataBase.expectedNullReturn = new ArrayList<String>();
                                    String condition = "SuraId = " + (suraText.get(0).getSuraId() + 1) + " AND VerseId";
                                    ArrayList<DbModel> aye = SqliteDbHelper.getInstance(SuraTextActivity.this).getDetails(condition , String.valueOf(position + 1));
                                    if(!aye.get(0).getFavorite()) {
                                        ADataBase db = new ADataBase("Groupes", SuraTextActivity.this);
                                        ArrayList<String> groupes = (ArrayList<String>) db.readArray("groupesid");
                                        new AddBookmarDialog(SuraTextActivity.this, onBookMarkSelectedListener, favorite, groupes).show();
                                    }else{
                                        String sql = "UPDATE quran SET Favorite = 0 WHERE " +
                                                ("SuraId = " + (suraText.get(0).getSuraId() + 1) + " AND VerseId = " + (position + 1));
                                        SqliteDbHelper.getInstance(SuraTextActivity.this).setDetails(sql);
                                        ADataBase db = new ADataBase("Groupes" , SuraTextActivity.this);
                                        ArrayList<String> groupes = (ArrayList<String>) db.readArray("groupesid");

                                        for(int j =0 ; j < groupes.size() ; j++){
                                            ArrayList<String> current = (ArrayList<String>) db.readArray(groupes.get(i));
                                            current.remove(favorite.getSuraName() + "_" + favorite.getVerseId());
                                            db.writeArray(groupes.get(i) , current , 1);
                                        }

                                        Toast.makeText(SuraTextActivity.this , "حذف شد.",Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                            }
                        })
                        .show();

            }
        }));

    }

    private int getCurrentAyehPosition() {
       LinearLayoutManager manager = (LinearLayoutManager) rv.getLayoutManager();
       return manager.findFirstVisibleItemPosition();
    }


    private String GetExtras(Bundle savedInstanceState, String value) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                return null;
            return extras.getString(value);
        } else {
            return (String) savedInstanceState.getSerializable(value);
        }
    }

    private OnBookMarkSelectedListener onBookMarkSelectedListener = new OnBookMarkSelectedListener() {
        @Override
        public void onBookMarkSelected(Favorite favorite, String groupname) {
            ADataBase.expectedNullReturn = new ArrayList<String>();
            ADataBase db = new ADataBase("Groupes" , SuraTextActivity.this);
            ArrayList<String> current = (ArrayList<String>) db.readArray(groupname);
            Log.d("s" , favorite.getSuraName());
            if(!current.contains(favorite.getSuraName() + "_" + favorite.getVerseId()))
                current.add(favorite.getSuraName() + "_" + favorite.getVerseId());
            db.writeArray(groupname , current , 1);
            String sql = "UPDATE quran SET Favorite = 1 WHERE " +
                    ("SuraId = " + (suraText.get(0).getSuraId() + 1) + " AND VerseId = " + (position + 1));
            SqliteDbHelper.getInstance(SuraTextActivity.this).setDetails(sql);
        }

        @Override
        public boolean onGroupeCreated(String groupename) {
            ADataBase.expectedNullReturn = new ArrayList<String>();
            ADataBase db = new ADataBase("Groupes" , SuraTextActivity.this);
            ArrayList<String>  current = (ArrayList<String>) db.readArray("groupesid");
            current.add(groupename);
            db.writeArray("groupesid" , current , 1);
            return true;
        }
    };

    private View.OnClickListener go_to_aye_listener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final EditText et = new EditText(SuraTextActivity.this);
            et.setHint("شماره آیه");
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            et.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            new AlertDialog.Builder(SuraTextActivity.this)
                    .setTitle(R.string.go_to_aye)
                    .setView(et)
                    .setPositiveButton(R.string.chhose, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            int max = rv.getAdapter().getItemCount();
                            String cur = et.getText().toString();
                            for(int i =0 ; i < cur.length() ; i++)
                                if(cur.charAt(i) == '0'){
                                    cur = cur.substring(1);
                                    i--;
                                }else
                                    break;
                            if(cur.length() > 0)
                            if(Integer.valueOf(cur) <= max){
                                dialogInterface.cancel();
                                rv.scrollToPosition(Integer.valueOf(cur) - 1);
                                ((TextView) view).setText(cur);
                            }else{
                                Toast.makeText(SuraTextActivity.this , "شماره آیه بین : " + max + " - 1", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }
    };

    private View.OnClickListener handleFullScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("view" , view.getTag().toString());
            if("full".equals(view.getTag()))
                fullScreen();
            else
                outFullScreen();
        }
    };


    private void outFullScreen(){
        LinearLayout.LayoutParams rvParams = (LinearLayout.LayoutParams) rv.getLayoutParams();
        rvParams.weight = 11;
        rv.setLayoutParams(rvParams);
        findViewById(R.id.exit_full_screen).setVisibility(View.GONE);
        findViewById(R.id.top_pannel).setVisibility(View.VISIBLE);
        findViewById(R.id.audio_pannel).setVisibility(View.VISIBLE);
        findViewById(R.id.seek_bar).setVisibility(View.VISIBLE);
    }
    private void fullScreen(){
        findViewById(R.id.top_pannel).setVisibility(View.GONE);
        findViewById(R.id.audio_pannel).setVisibility(View.GONE);
        findViewById(R.id.seek_bar).setVisibility(View.GONE);
        LinearLayout.LayoutParams rvParams = (LinearLayout.LayoutParams) rv.getLayoutParams();
        rvParams.weight = 12;
        rv.setLayoutParams(rvParams);
        findViewById(R.id.exit_full_screen).setVisibility(View.VISIBLE);
    }

    private View.OnClickListener shiffleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageButton v = (ImageButton) view;
            if(v.getTag().equals("once")){
                sound.setShuffle(SoundToPlay.Shuffle.TWICE);
                v.setTag("twice");
                v.setImageResource(R.mipmap.shuffle_twice);
            }else{
                sound.setShuffle(SoundToPlay.Shuffle.ONCE);
                v.setTag("once");
                v.setImageResource(R.mipmap.shuffle_once);
            }
        }
    };
    private View.OnClickListener playPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().equals("play")){
                if(sound.play() == 200) {
                    view.setTag("pause");
                    ((ImageButton) view).setImageResource(R.mipmap.pause);
                    sound.setOnStopListener(listener);
                }else{
                    new AlertDialog.Builder(SuraTextActivity.this)
                            .setTitle(R.string.download)
                            .setMessage(R.string.download_message)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    String jozzs = Sura.getJozInSura(Integer.valueOf(position) - 1);
                                    ArrayList<Integer> jozids = new ArrayList<Integer>();

                                    int start = 0;
                                    while (start < jozzs.length()){
                                        int index = jozzs.indexOf('|' , start);
                                        if(index == -1)
                                            index = jozzs.length();
                                        String val = jozzs.substring(start , index);
                                        if("".equals(val))
                                            break;
                                        jozids.add(Integer.valueOf(val));
                                        start = index + 1;
                                    }

                                    Integer[] last = new Integer[jozids.size()];
                                    for(int i =0 ; i < last.length ; i++)
                                        last[i] = jozids.get(i);
                                    Download.Type type = sound.getSoundType() == SoundToPlay.SoundType.TARTIL ? Download.Type.TARTL : Download.Type.TAHDIR;

                                    new OnlyDownloadActivity(type , last);
                                    startActivityForResult(new Intent(SuraTextActivity.this , OnlyDownloadActivity.class) , OnlyDownloadActivity.RESULT_ID);

                                    dialogInterface.cancel();

                                }
                            })
                            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            }else{
                sound.pause();
                ((ImageButton) view).setImageResource(R.mipmap.play);
                view.setTag("play");
            }
        }
    };
    private View.OnClickListener tartilTahdirlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView v = (TextView) view;
            String text  = v.getText().toString();
            if(text.equals(getString(R.string.tartil))){
                sound.setSoundType(SoundToPlay.SoundType.TAHDIR);
                v.setText(getString(R.string.tahdir));
            }else {
                sound.setSoundType(SoundToPlay.SoundType.TARTIL);
                v.setText(getString(R.string.tartil));
            }
        }
    };
    private View.OnClickListener soundSettingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View root = LayoutInflater.from(SuraTextActivity.this).inflate(R.layout.in_sura_sound_settings , null , false);
            final SeekBar seekBar = ((SeekBar) root.findViewById(R.id.in_sura_loudness));
            final CheckBox checkBox = ((CheckBox) root.findViewById(R.id.in_sura_back_play));
            seekBar.setProgress(sound.getVolume());
            checkBox.setChecked(sound.getBackPlay());
            new AlertDialog.Builder(SuraTextActivity.this)
                    .setTitle("تنظیمات صدا")
                    .setView(root)
                    .setPositiveButton(R.string.chhose, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sound.setBackPlay(checkBox.isChecked());
                            sound.setVolume(seekBar.getProgress());
                            dialogInterface.cancel();
                        }
                    })
                    .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    };

    private SoundPlayer.OnStopListener listener = new SoundPlayer.OnStopListener() {
        @Override
        public void onStop(boolean byUser) {
            if(!byUser)
                sound.goToNextAye();
        }
    };

    @Override
    protected void onPause() {
        if(!sound.getBackPlay() && sound.isPlaying())
            findViewById(R.id.play_pause).callOnClick();
        super.onPause();
    }
}
