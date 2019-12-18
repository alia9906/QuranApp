package ir.farzadshami.quran.helpers;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

public class SoundPlayer {
    private MediaPlayer player;
    private String fileToPlay;
    private long startOffSet = 0l;
    private long endOffSet = -1l;
    private long currentTime = 0l;
    private int user_loop_count = 1;
    private Context context;
    private boolean wasPlaying = false;
    private Thread stopperThread;
    private boolean runStopperThread = false;
    private int loop_count = 1;
    private OnStopListener listener;

    public SoundPlayer(final Context context , String filepath , final long startOffSet , final long endOffSet){
        this.fileToPlay = filepath;
        this.startOffSet = startOffSet;
        this.endOffSet = endOffSet;
        this.context = context;

        player = new MediaPlayer();
        try {

            FileInputStream fis = new FileInputStream(fileToPlay);
            player.setDataSource(fis.getFD());
            fis.close();
            player.prepare();

        }catch (IOException e){
            try {
                player.setDataSource(context, Uri.parse(fileToPlay));
                player.prepare();
            } catch (IOException e2) {
                Log.e("file" , "file not valid : \'" + fileToPlay + "\'");
                player = null;
            }
        }

        stopperThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (endOffSet > 0l) {
                        if (runStopperThread) {
                            if (loop_count == 0) {
                                stop();
                                runStopperThread = false;
                                loop_count = user_loop_count;
                            }
                        }
                        if (runStopperThread) {
                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            currentTime += 10;
                            if (currentTime >= endOffSet - startOffSet) {
                                pause();
                                loop_count--;
                                reset();
                                try{Thread.sleep(500);}catch (Exception e){e.printStackTrace();}
                                play();
                            }
                        }

                    }
                }
            }
        });
        if(player != null) stopperThread.start();
    }

    public String getCurrentSeekStatus(){
        if(player == null)
            return Utilities.changeToPersionNumbers("00:00");
        if(endOffSet < 0l) {
            int duration = player.getDuration() / 1000;
            int current = player.getCurrentPosition() / 1000;
            String retval = "";
            retval += current / 10 == 0 ? "0" + current : current + "";
            retval += duration / 10 == 0 ? ":0" + duration : ":" + duration;
            return retval;
        }else{
            int duration = getMax() / 1000;
            int current = getCurrent() / 1000;
            String retval = "";
            retval += current / 10 == 0 ? "0" + current : current + "";
            retval += duration / 10 == 0 ? ":0" + duration : ":" + duration;
            return retval;
        }
    }
    public void play(){
        if(player == null)
            return;
        if(!player.isPlaying()) {
            if(wasPlaying)
                player.start();
            else{
                player.seekTo((int) startOffSet);
                currentTime = 0;
                player.start();
                wasPlaying = true;
            }
        }
        runStopperThread = true;
    }
    public void pause(){
        if(player == null)
            return;
        if(player.isPlaying())
            player.pause();
        runStopperThread = false;
    }
    public void stop(){
        if(player == null)
            return;
        if(player.isPlaying()) {
            player.stop();
            if(listener != null)
                if(currentTime >= endOffSet - startOffSet)
                    listener.onStop(false);
                else
                    listener.onStop(true);
        }
        reset();
    }
    public void seekTo(int seek){
        if(player == null)
            return;
        seek += startOffSet;
        if(seek > endOffSet && endOffSet > 0)
            stop();
        if(seek < startOffSet)
            stop();
        player.seekTo(seek);
        if (endOffSet > 0l)
            currentTime = seek - startOffSet;
        else
            currentTime = seek;
    }
    public void setVolume(int volume){
        player.setVolume(volume , volume);
    }
    public void changeFile(String fileToPlay , long startOffSet , long endOffSet){
        this.fileToPlay = fileToPlay;
        this.startOffSet = startOffSet;
        this.endOffSet = endOffSet;
        stop();
        reset();
        resetPlayer();
    }
    public void changeOffSets(long startOffSet , long endOffSet){
        this.startOffSet = startOffSet;
        this.endOffSet = endOffSet;
        stop();
        reset();
        resetPlayer();
    }
    public int getMax(){
        if(player == null)
            return -1;
        if(endOffSet > 0l)
            return (int) (endOffSet - startOffSet);
        else
            return player.getDuration();
    }
    public int getCurrent(){
        if(player == null)
            return -1;
        if(endOffSet > 0l)
            return (int) currentTime;
        else
            return player.getCurrentPosition();
    }
    public boolean isPlaying(){
        if(player== null)
            return false;
        return player.isPlaying();
    }
    public void setLooping(int how_much){
        loop_count = how_much;
        user_loop_count = how_much;
    }
    public void setOnStopListener(OnStopListener listener){
        this.listener = listener;
    }
    public interface OnStopListener{
        void onStop(boolean byUser);
    }
    private void reset(){
        runStopperThread = false;
        wasPlaying = false;
        currentTime = 0;
    }
    private void resetPlayer(){
        if(player == null)
            return;
        player.reset();
    }
}
