import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;


public class AudioFrame extends JPanel
{
    static double[] sines;
    static int vol;
    static double amplitude;
    static double frequency ;
    static int equidistant;

    public void play_Audio(int[] frequency,int volume, JButton amplitudeButton) throws LineUnavailableException
    {


        System.out.println("audio frame ");
        int Hertz = frequency[0];
        float rate = 44100;
        byte[] buf;
        buf = new byte[1];
        sines = new double[(int)rate];
        vol=volume;

        AudioFormat audioF;
        audioF = new AudioFormat(rate,8,1,true,false);

        SourceDataLine sourceDL = AudioSystem.getSourceDataLine(audioF);
        sourceDL = AudioSystem.getSourceDataLine(audioF);
        sourceDL.open(audioF);
        sourceDL.start();

        for(int i=0; i<rate; i++){
          double angle = (i/rate)*Hertz*2.0*Math.PI;
          buf[0]=(byte)(Math.sin(angle)*vol);
          sourceDL.write(buf,0,1);

          sines[i]=(double)(Math.sin(angle)*vol);
        }

        sourceDL.drain();
        sourceDL.stop();
        sourceDL.close();

        /*if(amplitudeButton.getModel().isPressed()) {
            FrequencyGraph graph = new FrequencyGraph();
            graph.fre_graph(sines);
        }*/

      }


    public void file_audio(File audioFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
       /* System.out.println("playing audio file");
        AudioInputStream audioIn;
        audioIn = AudioSystem.getAudioInputStream(audioFile);

        //Allocate inputAudio clip
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);

        //start the audio playing
        clip.start();
        clip.loop(0); // repeat no times*/





        //Create a global buffer size
        final int EXTERNAL_BUFFER_SIZE = 128000;

        //Load the Audio Input Stream from the file
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Get Audio Format information
        AudioFormat audioFormat = audioInputStream.getFormat();

        //Handle opening the line
        SourceDataLine	line = null;
        DataLine.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Start playing the sound
        line.start();

        //Write the sound to an array of bytes
        int nBytesRead = 0;
        byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);

            } catch (IOException e) {
                    e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                    int nBytesWritten = line.write(abData, 0, nBytesRead);
            }

        }

        //close the line
        line.drain();
        line.close();


        //Calculate the sample rate
        float sample_rate = audioFormat.getSampleRate();
        System.out.println("sample rate = "+sample_rate);

        //Calculate the length in seconds of the sample
        float T = audioInputStream.getFrameLength() / audioFormat.getFrameRate();
        System.out.println("T = "+T+ " (length of sampled sound in seconds)");

        //Calculate the number of equidistant points in time
        int equidistant = (int) (T * sample_rate) / 2;
        System.out.println("n = "+equidistant+" (number of equidistant points)");

        //Calculate the time interval at each equidistant point
        float h = (T / equidistant);
        System.out.println("h = "+h+" (length of each time interval in seconds)");


        //Determine the original Endian encoding format
        boolean isBigEndian = audioFormat.isBigEndian();

        //this array is the value of the signal at time i*h
        int x[] = new int[equidistant];

        //convert each pair of byte values from the byte array to an Endian value
        for (int i = 0; i < equidistant*2; i+=2) {
            int b1 = abData[i];
            int b2 = abData[i + 1];
            if (b1 < 0) b1 += 0x100;
            if (b2 < 0) b2 += 0x100;

            int value;

            //Store the data based on the original Endian encoding format
            if (!isBigEndian) value = (b1 << 8) + b2;
            else value = b1 + (b2 << 8);
            x[i/2] = value;
        }


        //do the DFT for each value of x sub j and store as f sub j
        double f[] = new double[equidistant/2];
        for (int j = 0; j < equidistant/2; j++) {

            double firstSummation = 0;
            double secondSummation = 0;

            for (int k = 0; k <equidistant ; k++) {
                    double twoPInjk = ((2 * Math.PI) / equidistant) * (j * k);
                    firstSummation +=  x[k] * Math.cos(twoPInjk);
                    secondSummation += x[k] * Math.sin(twoPInjk);
            }

                f[j] = Math.abs( Math.sqrt(Math.pow(firstSummation,2) +
                Math.pow(secondSummation,2)) );

            amplitude = 2 * f[j]/equidistant;
            frequency = j * h / T * sample_rate;
            //System.out.println("frequency = "+frequency+", amp = "+amplitude);

        }

            }
        }


