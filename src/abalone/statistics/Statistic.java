package abalone.statistics;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.jCharts.Chart;
import org.jCharts.axisChart.ScatterPlotAxisChart;
import org.jCharts.chartData.ScatterPlotDataSeries;
import org.jCharts.chartData.ScatterPlotDataSet;
import org.jCharts.encoders.PNGEncoder;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.ScatterPlotProperties;
import org.jCharts.test.TestDataGenerator;

import com.trolltech.qt.core.QFile;

public class Statistic 
{
	
	private String xAxisTitle; // name printed on the x Axis
	private String yAxisTitle; // name of the y axis
	private String statName; // Title of the whole chart
	private String subjectName; //  name of variable thats being mapped in the legend
	private int width;
	private int height;
	private double step; // steps along the x-axis
	private StatisticGenerator statGen;
	private ArrayList<Point2D.Double> stats;
	
	private static int append = 1;
	
	/**
	 * Create a new statistic of a certain object that implements
	 * the StatisticGenerator interface
	 * @param gen an object that implements StatisticGenerator
	 */
	public Statistic(StatisticGenerator a)
	{
		stats = new ArrayList<Point2D.Double>();
		statGen = a;
		xAxisTitle = "x axis";
		yAxisTitle = "y axis";
		statName = "generic statistic";
		subjectName = "some dude";
		width = 500;
		height = 400;
	}
	
	/**
	 * Set the name for the x-axis (what kind of steps do you take?)
	 */
	public void setXaxisName(String name)
	{
		xAxisTitle = name;
	}
	
	/**
	 * Set the name for the y-axis (what info are you gathering)
	 */
	public void setYAxisName(String name)
	{
		yAxisTitle = name;
	}
	
	/**
	 * Sets the title of the whole statistic
	 */
	public void setStatName(String name)
	{
		statName = name;
	}
	
	/**
	 * Sets the name of the variable being plotted
	 */
	public void setSubjectName(String name)
	{
		subjectName = name;
	}
	
	/**
	 * Returns the name of the variable that is being plotted
	 */
	public String getSubjectName()
	{
		return subjectName;
	}
	
	/**
	 * A ticker that lets the "time" or whatever you're plotting against, pass
	 */
	public void step()
	{
		step++;
	}
	
	/**
	 * Setting the step value manualy
	 * -Warning- A step can be set backwards, there are no checks!
	 */
	public void setStep(double i)
	{
		step = i;
	}
	
	/**
	 * Sets a custom size for picture file 
	 * @param width the width in pixels
	 * @param height the height in pixels 
	 */
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Call this every time you need the statistic to be recorded
	 */
	public void stat()
	{
		double state = statGen.getCurrentState();
		Point2D.Double point = new Point2D.Double(step, state);
		stats.add(point);
	}
	
	/**
	 * Call this when the statistic gathering is done and
	 * the statistic will be saved to a file
	 */
	public void save()
	{
		Shape[] shapes= { PointChartProperties.SHAPE_DIAMOND};
		Stroke[] strokes= { LineChartProperties.DEFAULT_LINE_STROKE };
		
		ScatterPlotProperties scatterPlotProperties = new ScatterPlotProperties(strokes, shapes);
		ScatterPlotDataSet scatterPlotDataSet= new ScatterPlotDataSet( scatterPlotProperties);
		Point2D.Double[] test = new Point2D.Double[1];
		Point2D.Double[] data = stats.toArray(test);
		Paint paint[] = TestDataGenerator.getRandomPaints( 1 );
		scatterPlotDataSet.addDataPoints(data, paint[0], subjectName);
		
		ScatterPlotDataSeries dataSeries = new ScatterPlotDataSeries( scatterPlotDataSet, xAxisTitle, yAxisTitle, statName );
		ChartProperties chartProperties= new ChartProperties();
		AxisProperties axisProperties= new AxisProperties(new DataAxisProperties(),new DataAxisProperties());
		LegendProperties legendProperties= new LegendProperties();
		ScatterPlotAxisChart scatterChart= new ScatterPlotAxisChart( dataSeries, chartProperties, axisProperties, legendProperties, width, height );
		
		//Now to print it all to a file
		writer(scatterChart, statName);
	}
	
	/**
	 * Combines 2 different statistics and saves them to 1 file
	 * -Note- The statName (Title of the statistic) of this statistic will be used (not "other")
	 * -warning- There is NO check wether the 2 statistics are related!
	 * @param a the other statistic you want to add
	 * @param name the name the other variable will have in the chart-legend
	 */
	public void combineAndSave(Statistic otherStat)
	{
		Shape[] shapes= { PointChartProperties.SHAPE_DIAMOND, PointChartProperties.SHAPE_SQUARE };
		Stroke[] strokes= { LineChartProperties.DEFAULT_LINE_STROKE, LineChartProperties.DEFAULT_LINE_STROKE};
		
		ScatterPlotProperties scatterPlotProperties = new ScatterPlotProperties(strokes, shapes);
		ScatterPlotDataSet scatterPlotDataSet= new ScatterPlotDataSet( scatterPlotProperties);
		Point2D.Double[] test = new Point2D.Double[1]; //For ArrayList to return actual Point2D.Double[] with .toArray it needs a proper example that is too small.
		Point2D.Double[] data = stats.toArray(test);  //So I don't know the outcome if you have only 1 data stored (probably mayhem everywhere)
		ArrayList<Point2D.Double> stats2 = otherStat.getData();
		Point2D.Double[] data2 = stats2.toArray(test);
		Paint paint[] = TestDataGenerator.getRandomPaints( 2 );
		scatterPlotDataSet.addDataPoints(data, paint[0], subjectName);
		scatterPlotDataSet.addDataPoints(data2, paint[1], otherStat.getSubjectName());
		
		ScatterPlotDataSeries dataSeries = new ScatterPlotDataSeries( scatterPlotDataSet, xAxisTitle, yAxisTitle, statName );
		ChartProperties chartProperties= new ChartProperties();
		AxisProperties axisProperties= new AxisProperties(new DataAxisProperties(),new DataAxisProperties());
		LegendProperties legendProperties= new LegendProperties();
		ScatterPlotAxisChart scatterChart= new ScatterPlotAxisChart( dataSeries, chartProperties, axisProperties, legendProperties, width, height );
		
		//Now to print it all to a file
		writer(scatterChart, statName);
	}
	
	private ArrayList<Point2D.Double> getData()
	{
		return stats;
	}
	
	private void writer(Chart chart, String filename)
	{
		String extension= ".png";
		FileOutputStream fileOutputStream;
		
		if(!QFile.exists(filename + append + extension)) // Static method of QFile which checks wether there already is such a file
		{
			try
			{
				fileOutputStream= new FileOutputStream( filename + append + extension );
				PNGEncoder.encode( chart, fileOutputStream );
				fileOutputStream.flush();
				fileOutputStream.close();
			}
			catch( Throwable throwable )
			{
				System.out.println("Statistic Error: Can't print *.png");
				throwable.printStackTrace();
			}
		}
		else
		{
			append++;
			writer(chart, filename);
		}
	}
}
