package abalone.exec;

import abalone.adt.KeyValuePair;
import java.util.ArrayList;
import java.lang.Long;
import java.util.Properties;

import java.awt.*;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jCharts.chartData.*;
import org.jCharts.properties.*;
import org.jCharts.properties.ScatterPlotProperties;
import org.jCharts.axisChart.*;
import org.jCharts.types.ChartType;
import org.jCharts.chartData.*;
import org.jCharts.test.TestDataGenerator;
import org.jCharts.Chart;
import org.jCharts.encoders.PNGEncoder;

public class Statistic 
{
	
	private String xAxisTitle;
	private String yAxisTitle;
	private String statName;
	private String subjectName;
	private int width;
	private int height;
	private double step;
	private StatisticGenerator statGen;
	private ArrayList<Point2D.Double> stats;
	private Properties props;
	
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
	
	public void setStatName(String name)
	{
		statName = name;
	}
	
	public void setSubjectName(String name)
	{
		subjectName = name;
	}
	
	/**
	 * A ticker that lets the "time" or whatever you're plotting against, pass
	 */
	public void step()
	{
		step++;
	}
	
	public void setStep(double i)
	{
		step = i;
	}
	
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
		String extension= ".png";
		FileOutputStream fileOutputStream;

		try
		{
			fileOutputStream= new FileOutputStream( statName + extension );
			PNGEncoder.encode( scatterChart, fileOutputStream );
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		catch( Throwable throwable )
		{
			throwable.printStackTrace();
		}
	}
}
