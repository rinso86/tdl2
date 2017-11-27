package tdl.utils.statmod.renderers;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import tdl.utils.statmod.Buvs;


/**
 * https://stackoverflow.com/questions/12344465/how-to-overlay-fitted-distribution-and-histogram-using-jfreechart
 * @author Langbein_M
 *
 */

public class BuvsRenderer implements ModRenderer {
	
	private Buvs model;

	public BuvsRenderer(Buvs buvs) {
		model = buvs;
	}

	@Override
	public JPanel render() {
		
		double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Hist", data, 5);
		
		JFreeChart barChart = ChartFactory.createHistogram("Histogram", "X Axis", "Y Axis", dataset, PlotOrientation.VERTICAL, true, false, false);
		Plot plt = barChart.getPlot();
		
		ValueMarker marker = new ValueMarker(10);  // position is the value on the axis
		marker.setPaint(Color.black);
		
		
		ChartFrame cf = new ChartFrame("The histogram", barChart);
		ChartPanel cp = cf.getChartPanel();
		JPanel jp = new JPanel();
		jp.add(cp);
		return jp;
	}

}
