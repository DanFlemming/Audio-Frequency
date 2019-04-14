import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;
import java.io.*;


public class FirstFrame {

    File audioFile;
    JFileChooser chooser = new JFileChooser();
    int[] frequency = new int[1];


    private void display() {
        System.out.println("Displaying graph window");
        JFrame frame = new JFrame("Audio Frequency");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Audio Frequency");

        //Make the layout
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Size the frame
        frame.pack();
        frame.setLocation(400, 100);
        frame.setSize(900, 700);

        //Create label and add it to window
        JLabel label = new JLabel("Audio Frequency Program");
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        frame.add(label, c);

        //Add graph image
        JLabel image = new JLabel(new ImageIcon("C:\\Users\\DAN\\iCloudDrive\\School\\Advanced Programming Java\\Audio Frequency\\Pictures\\eye.jpg"));
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 1;
        frame.add(image, c);

        //Directions text
        JLabel dLabel = new JLabel("Enter a Frequency or Select a Wav File.");
        dLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        frame.add(dLabel, c);

        //Make buttons
        JButton inputButton = new JButton("Input Frequency");
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(25, 100, 0, 0);
        inputButton.setPreferredSize(new Dimension(125, 30));
        frame.add(inputButton, c);

        //openButton
        JButton openButton = new JButton("Open file");
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(25, 0, 0, 100);
        openButton.setPreferredSize(new Dimension(125, 30));
        frame.add(openButton, c);

         //Make amplitudeButton
        JButton amplitudeButton = new JButton("Plot Amplitude");
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(25, 100, 0, 0);
        amplitudeButton.setPreferredSize(new Dimension(125, 30));
        frame.add(amplitudeButton, c);
        amplitudeButton.setVisible(false);

        //frequencyButton
        JButton frequencyButton = new JButton("Plot Frequency");
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(25, 0, 0, 100);
        frequencyButton.setPreferredSize(new Dimension(125, 30));
        frame.add(frequencyButton, c);
        frequencyButton.setVisible(false);

        JButton playButton = new JButton("Play Audio Tone");
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 5;
        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(25, 0, 0, 0);
        playButton.setPreferredSize(new Dimension(125, 30));
        frame.add(playButton, c);
        playButton.setVisible(false);


        //Make the First frame visible
        frame.setVisible(true);

       //Add action listener for InputButton
        inputButton.addActionListener(arg0 -> {
            System.out.println("Input button was clicked");
            frequency[0] = Integer.parseInt(JOptionPane.showInputDialog("Input Frequency"));
            playButton.setVisible(true);
            amplitudeButton.setVisible(true);
            frequencyButton.setVisible(true);
        });//End of inputButton action listener


        //Add file chooser for openButton
        openButton.addActionListener(arg0 -> {
          System.out.println("Open file button was clicked");
          chooser.setCurrentDirectory(new File("."));
          int retVal = chooser.showOpenDialog(frame);

          if (retVal == JFileChooser.APPROVE_OPTION) {
                audioFile = chooser.getSelectedFile();
                frequency[0]=0; //Set to 0 for checking which value is entered
                System.out.println("File = " + audioFile);

                //Set the other buttons to visible
                playButton.setVisible(true);
                amplitudeButton.setVisible(true);
                frequencyButton.setVisible(true);
          }
          else{
              System.out.println("The cancel button was clicked");
          }
        });// End of openButton action listener



         playButton.addActionListener(arg0 -> { //(new actionListener()
             if (frequency[0]>0) {
                 if (frequency[0]<10){
                     JOptionPane.showMessageDialog(frame, "Frequency too low to play");
                 }

                 else{
                     AudioFrame playAudio = new AudioFrame(); //make instance of class

                 try {
                     playAudio.play_Audio(frequency,100,amplitudeButton); //call the class method

                 } catch (LineUnavailableException e) {
                     e.printStackTrace();
                 }
                 }//End nested if/else

             }
             else if (frequency[0]==0) {
                 AudioFrame fileAudio = new AudioFrame();
                 try {
                     fileAudio.file_audio(audioFile);
                 } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                     System.out.println("something when wrong when calling file_audio function");
                     e.printStackTrace();
                 }

             } else {
                 JOptionPane.showMessageDialog(frame, "No file or Frequency chosen");

             }
         });//End of playButton action listener


         //Action button for Amplitute button
        amplitudeButton.addActionListener(arg0 -> { //(new actionListener()
            System.out.println("Displaying Amplitude Graph");
            JFrame frame2 = new JFrame("Amplitude Graph");
            frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame2.add(new AmplitudeGraph());
            frame2.setSize(800,300);
            frame2.setLocation(450,500);
            frame2.setVisible(true);
        });//End of amplitudeButton action listener

        //Action button for Frequency graph
        frequencyButton.addActionListener(arg0 -> { //(new actionListener()
            System.out.println("Displaying Frequency Graph");
            JFrame frame1 = new JFrame("Frequency Graph");
            frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame1.add(new FrequencyGraph());
            frame1.setSize(800,300);
            frame1.setLocation(450,500);
            frame1.setVisible(true);



           //new graph();   // to call graph
        });//End of frequencyButton action listener


        frame.revalidate();
        frame.repaint();
 }//end display method



    public static void main(String[] args) {
        FirstFrame f = new FirstFrame();
        f.display();
    }//End of graph
}

