package duong.huy.huong.healthcare.SleepRecorder;

/**
 * Nguồn: http://dbis.eprints.uni-ulm.de/1183/1/BA_Mohr_15.pdf.
 */
class FeatureExtractor {
    private NoiseModel noiseModel;
    private float[] lowFreq;
    private float[] highFreq;

    public FeatureExtractor(NoiseModel noiseModel) {
        this.noiseModel = noiseModel;
    }

    /**
     * tính RLH, RMS, VAR và update vào noiseModel.
     * @param buffer
     */
    public void update(short[] buffer) {
        lowFreq = new float[buffer.length];
        highFreq = new float[buffer.length];
        noiseModel.addRLH(calculateRLH(buffer));
        noiseModel.addRMS(calculateRMS(buffer));
        noiseModel.addVAR(calculateVar(buffer));
        noiseModel.calculateFrame();
    }

    /**
     * Hàm tính RMS.
     * @param buffer
     * @return
     */
    private double calculateRMS(short[] buffer) {
        long sum = 0;
        for(int i=0;i<buffer.length;i++) {
            sum += Math.pow(buffer[i],2);
        }
        return Math.sqrt(sum / buffer.length);
    }

    /**
     * Hàm tính RMS.
     * @param buffer
     * @return
     */
    private double calculateRMS(float[] buffer) {
        long sum = 0;
        for(int i=0;i<buffer.length;i++) {
            sum += Math.pow(buffer[i],2);
        }
        return Math.sqrt(sum / buffer.length);
    }

    /**
     * tính low RMS.
     * @param buffer
     * @return
     */
    private double calculateLowFreqRMS(short[] buffer) {
        lowFreq[0] = 0;

        float a = 0.25f;

        for(int i=1;i<buffer.length;i++) {
            lowFreq[i] = lowFreq[i-1] + a * (buffer[i] - lowFreq[i-1]);
        }

        return calculateRMS(lowFreq);
    }

    /**
     * Hàm tính high RMS.
     * @param buffer
     * @return
     */
    private double calculateHighFreqRMS(short[] buffer) {
        highFreq[0] = 0;
        float a = 0.25f;
        for(int i=1;i<buffer.length;i++) {
            highFreq[i] = a * (highFreq[i-1] + buffer[i] - buffer[i-1]);
        }

        return calculateRMS(highFreq);
    }

    /**
     * Hàm tính RLH.
     * @param buffer
     * @return
     */
    private double calculateRLH(short[] buffer) {
        double rmsh = calculateHighFreqRMS(buffer);
        double rmsl = calculateLowFreqRMS(buffer);
        if(rmsh == 0) return 0;
        if(rmsl == 0) return 0;
        return  rmsl / rmsh;
    }

    /**
     * Hàm tính VAR.
     *
     * @param buffer
     * @return
     */
    private double calculateVar(short[] buffer) {

        double mean = calculateMean(buffer);
        double var = 0;
        for(short s: buffer) {
            var += Math.pow(s - mean,2);
        }
        return var / buffer.length;
    }

    /**
     * MEAN.
     *
     * @param buffer
     * @return
     */
    private double calculateMean(short[] buffer) {
        double mean = 0;
        for(short s: buffer) {
            mean += s;
        }
        return mean / buffer.length;
    }
}
