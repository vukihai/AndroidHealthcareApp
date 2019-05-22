package duong.huy.huong.healthcare.SleepRecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecorder extends Thread {
    private boolean stopped = false;
    private static AudioRecord recorder = null;
    private static int N = 0;
    private NoiseModel noiseModel;
    private short[] buffer;
    private FeatureExtractor featureExtractor;


    public AudioRecorder(NoiseModel noiseModel) {

        this.noiseModel = noiseModel;
        this.featureExtractor = new FeatureExtractor(noiseModel);
    }

    @Override
    public void run() {

        capture();

    }

    private void capture() {
        int i = 0;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        if(buffer == null) {
            buffer  = new short[1600];
        }

        if(N == 0 || (recorder == null || recorder.getState() != AudioRecord.STATE_INITIALIZED)) {
            N = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if(N < 1600) {
                N = 1600;
            }
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    16000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    N);
        }
        recorder.startRecording();
        while(!this.stopped) {
            N = recorder.read(buffer, 0, buffer.length);

            process(buffer);
        }
        recorder.stop();
        recorder.release();

    }

    private void process(short[] buffer) {
        featureExtractor.update(buffer);
    }
    public void close() {
        stopped = true;
    }

}
