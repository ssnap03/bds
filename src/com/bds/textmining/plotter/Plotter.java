package com.bds.textmining.plotter;

import org.jfree.chart.ChartFactory;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;
import java.util.List;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.ChartPanel;

public class Plotter {
	public static void plot2d(double[][] input, List<String> labels) {
	    
	    JFrame f = new JFrame("JDBCTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        
        JFreeChart chart = ChartFactory.createScatterPlot(
	            "Scatter Plot", 
	            "X", 
	            "Y", 
	            createDataset(input),
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	            );
        XYPlot plot = chart.getXYPlot();
        plot.setDomainAxis(new DateAxis("Date"));
        f.add(new ChartPanel(chart));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
		
		
	}
	
	private static XYDataset createDataset(double[][] input) {

		final XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeries series = new XYSeries("Chrome");
		
		int m = input.length;
		  
	    for(int i = 0; i < m; i++)
	    		series.add(input[i][0],input[i][1]);

		dataset.addSeries(series);
		
		return dataset;

	}
}