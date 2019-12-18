package ir.farzadshami.quran.models;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import ir.farzadshami.quran.MainActivity;
import ir.farzadshami.quran.SettingsActivity;
import ir.farzadshami.quran.helpers.SoundPlayer;
import ir.farzadshami.quran.helpers.SoundPreparer;
import ir.farzadshami.quran.helpers.Utilities;
import ir.farzadshami.quran.widget.SoundPushNotification;

public class SoundToPlay {

    public enum SoundType {
        TARTIL , TAHDIR
    }
    public enum Shuffle{
        ONCE , TWICE
    }
    private SeekBar.OnSeekBarChangeListener seekSoundListener = new SeekBar.OnSeekBarChangeListener() {
        boolean touchStarted = false;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(!isSeekChanging)
                seekTo(seekBar.getProgress() * 1000);
            if(sp != null)
                soundProps.setText(Utilities.changeToPersionNumbers(sp.getCurrentSeekStatus()));
            else {
                soundProps.setText(Utilities.changeToPersionNumbers("00:00"));
                seekBar.setProgress(0);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekChanging = false;
            touchStarted = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekChanging = true;
            touchStarted = false;
        }
    };
    private SoundType soundType;
    private SoundPushNotification spn;
    private Shuffle shuffle;
    private long[] indexes;
    private SoundPlayer sp;
    private int suraId;
    private int verseId;
    private int volume;
    private boolean backPlay;
    private TextView soundProps;
    private SeekBar controller;
    private boolean isInitilized = false;
    private boolean isSeekChanging = false;
    private Thread seekChanger;

    public SoundToPlay(final TextView soundProps , final SeekBar controller){

        this.soundProps = soundProps;
        this.controller = controller;
        controller.setOnSeekBarChangeListener(seekSoundListener);

        soundType = SettingsActivity.getSetting(SettingsActivity.Settings.DEFAULT_SOUND_TYPE).equals(SettingsActivity.Settings.TARTIL.toString())?
                SoundType.TARTIL : SoundType.TAHDIR;
        shuffle = Shuffle.ONCE;
        volume = Integer.valueOf(SettingsActivity.getSetting(SettingsActivity.Settings.LOUDNESS));
        backPlay  = SettingsActivity.getSetting(SettingsActivity.Settings.BACKGROUND_PLAY).equals(SettingsActivity.Settings.TRUE.toString());
        spn = new SoundPushNotification(this , MainActivity.mainActivity, ((Activity) soundProps.getContext()).getIntent());
        setBackPlay(backPlay);

        final Handler handler = new Handler();

        seekChanger = new Thread(new Runnable() {
            @Override
            public void run() {
                isSeekChanging = true;
                while (true){
                    if(sp != null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(isSeekChanging)
                                    controller.setProgress(sp.getCurrent() / 1000);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void setShuffle(Shuffle shuffle) {
        this.shuffle = shuffle;
        if(sp != null) sp.setLooping(shuffle == Shuffle.ONCE ? 1 : 2);
    }
    public void setSoundType(SoundType soundType) {
        this.soundType = soundType;
        isInitilized = false;
        _init_();
    }
    public SoundType getSoundType() {
        return soundType;
    }
    public void setBackPlay(boolean backPlay) {
        this.backPlay = backPlay;
        if(spn != null)
        if(backPlay) {
            spn.show();
        }else {
            spn.clear();
        }
    }
    public void setSuraId(int id){
        suraId = id;
    }
    public int getSuraId() {
        return suraId;
    }
    public int getVerseId() {
        return verseId;
    }
    public void setVerseId(int verseId) {
        this.verseId = verseId;
    }
    public boolean getBackPlay() {
       return backPlay;
    }
    public void setVolume(int volume) {
        this.volume = volume;
        if(sp != null)
            sp.setVolume(volume);
    }
    public int getVolume() {
        return volume;
    }
    private boolean _init_(){
        if(isInitilized)
            return true;
        SoundProperties props = SoundPreparer.getSoundFileAndIndexes(soundProps.getContext() , suraId , verseId , soundType);
        if (props == null)
            return false;
        else{
            indexes = props.indexes;
            stop();
            sp = new SoundPlayer(soundProps.getContext() , props.filePath , indexes[verseId] , indexes[verseId+1]);
            controller.setMax(sp.getMax() / 1000);
            if(!seekChanger.isAlive())
            seekChanger.start();
            isInitilized = true;
            return true;
        }
    }
    private boolean _init_(boolean changeToNextJoz){
        Log.d("changeToNext" , "started");
        verseId = 0;
        if(changeToNextJoz) {
            isInitilized = false;
            SoundProperties props = SoundPreparer.getSoundFileAndIndexes(soundProps.getContext(), suraId, verseId , soundType);
            if (props == null)
                return false;
            else {
                indexes = props.indexes;
                sp.changeFile(props.filePath, indexes[verseId], indexes[verseId + 1]);
                controller.setMax(sp.getMax() / 1000);
                isInitilized = true;
                return true;
            }
        }else
            return true;
    }
    public void goToNextAye(){
        if(verseId  + 1>= indexes.length)
            _init_(true);
        verseId++;
        goToAye(verseId);
    }
    public void goToPreviousAye(){
        if(verseId == 0)
            return;
        verseId--;
        goToAye(verseId);
    }
    public void goToAye(int verseId){
        this.verseId = verseId;
        if(indexes != null)
            if(verseId  + 1 >=  indexes.length)
                _init_(true);
        if(isInitilized){
            sp.changeOffSets(indexes[verseId] , indexes[verseId + 1]);
        }
    }
    public int play(){
        //okay == 200
        //need to download == 100
        //sp = null 1000
        if(!_init_())
            return 100;
        //play
        if(sp != null) {
            sp.play();
            return 200;
        }
        return  1000;
    }
    public void stop(){
        if(sp != null)
        sp.stop();
    }
    public void pause(){
        if(sp != null)
        sp.pause();
    }
    public void seekTo(int seek){
        if(sp != null)
        sp.seekTo(seek);
    }
    public boolean isPlaying(){
        if(sp == null)
            return false;
        return sp.isPlaying();
    }
    public void setOnStopListener(SoundPlayer.OnStopListener listener){
        sp.setOnStopListener(listener);
    }
    public void stopAllSoundServices(){
        if(sp != null)
            sp.stop();
        if(spn != null)
            spn.clear();
    }
}
