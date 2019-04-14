
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.PI;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class graph extends JFrame
{
    double numOfWaves = 2;
    //double AudioFrame.frequency = 10;
    double waveRes = 20;
    double median = ((AudioFrame.frequency*numOfWaves)/2);
    int equidistant = (int)(numOfWaves*waveRes);
    boolean going = true;
    boolean left = true;
    XYSeriesCollection dataset = new XYSeriesCollection();
    Point2D.Double[] points;

graph()
{
    XYSeries buoys = new XYSeries("Buoys");
    XYSeries series = new XYSeries("Wave");
    points = new Point2D.Double[equidistant];
    for(int i=0; i<equidistant; i++)
    {
        Point2D.Double temp = new Point2D.Double();
        temp.x = i*(AudioFrame.frequency/waveRes);
        temp.y = AudioFrame.amplitude *Math.sin((2*PI)/AudioFrame.frequency*temp.x);
        points[i] = temp;
        series.add(points[i].x, points[i].y);
        if(i==(equidistant/2)-1)
            buoys.add(median, points[i].y);
    }

    buoys.add(median, 0);
    dataset.addSeries(series);
    dataset.addSeries(buoys);
    JFreeChart chart = ChartFactory.createXYLineChart(
    "Sine Wave", // chart title
    "Frequency", // x axis label
    "Amplitude", dataset, // data
    PlotOrientation.VERTICAL,
    false, // include legend
    false, // tooltips
    false // urls
    );
    XYPlot plot = (XYPlot)chart.getPlot();
    XYLineAndShapeRenderer renderer
    = (XYLineAndShapeRenderer) plot.getRenderer();
    renderer.setSeriesShapesFilled(1, true);
    renderer.setSeriesShapesVisible(1, true);
    NumberAxis domain = (NumberAxis)plot.getDomainAxis();
    domain.setRange(0.00, AudioFrame.frequency*numOfWaves);
    NumberAxis range = (NumberAxis)plot.getRangeAxis();
    range.setRange(-AudioFrame.amplitude*1.3, AudioFrame.amplitude*1.3);
    ChartPanel cp = new ChartPanel(chart);
    cp.setPreferredSize(new Dimension(500, 270));
    setContentPane(cp);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    WaveRunner run = new WaveRunner();
}

class WaveRunner extends Thread
{
    WaveRunner()
    {
        this.start();
    }

    public void run()
    {
        double[] temp = new double[equidistant];
        XYSeries temp2, temp3;
        while(going)
        {
            temp2 = dataset.getSeries(0);
            temp3 = dataset.getSeries(1);
            temp2.delete(0, equidistant-1);
            temp3.delete(0, 1);
            for(int i=0; i<equidistant; i++)
            {
                if(left){
                try{
                    temp[i] = points[i+1].y;
                }
                catch(java.lang.ArrayIndexOutOfBoundsException exc){
                    temp[equidistant-1] = points[0].y;
                }}
                else{
                try{
                    temp[i] = points[i-1].y;
                }
                catch(java.lang.ArrayIndexOutOfBoundsException exc){
                    temp[0] = points[equidistant-1].y;
                }}
            }
            for(int i=0; i<equidistant; i++)
            {
                points[i].y = temp[i];
                temp2.add(points[i].x, points[i].y);
                if(i==(equidistant/2))
                {
                    temp3.add(median, points[i].y);
                    temp3.add(median, 0);
                }
            }
            try
            {
                Thread.sleep(50);
            }
            catch(InterruptedException exc)
            {}
        }
    }
}

public static void main(String[] args)
{
    graph test = new graph();
}
}