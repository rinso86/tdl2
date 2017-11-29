package tdl.utils.statmod.renderers;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tdl.utils.statmod.Buvs;


/**
 * https://stackoverflow.com/questions/12344465/how-to-overlay-fitted-distribution-and-histogram-using-jfreechart
 * https://github.com/ngadde/playground/blob/master/com.iis.sample1/src/main/java/demo/PriceVolumeDemo1.java
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
		
		int treeDepth = model.getTreeDepth();
		JPanel jp = new JPanel(new GridLayout(treeDepth - 1, 2));
		
		HashMap<Integer, ArrayList<Integer>> childCounts = model.getChildCounts();
		HashMap<Integer, ArrayList<Double>> netTimes = model.getNetTimes();
		HashMap<Integer, PoissonDistribution> poisDs = model.getPoisDs();
		HashMap<Integer, GammaDistribution> gamDs = model.getGamDs();

		
		for(int i = 1; i < treeDepth; i++) { // starting at 1: root will never be finished. 
			String childHeading = "Children on level " + i;
			String timeHeading = "Time on level " + i;
			ChartPanel childPanel = createChildGraph(childHeading, childCounts.get(i), poisDs.get(i));
			ChartPanel timePanel = createTimeGraph(timeHeading, netTimes.get(i), gamDs.get(i));
			jp.add(childPanel);
			jp.add(timePanel);
		}
		
		return jp;
	}

	private ChartPanel createTimeGraph(String heading, ArrayList<Double> netTimes, GammaDistribution gd) {
		if(netTimes == null) return new ChartPanel(null);
		
		// Histogram
		double[] data = toPrimitiveDouble(netTimes);
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Times", data, 20);

		// Pdf
		XYSeries xyseries = new XYSeries("pdf");
		if(gd != null) {			
			int steps = 100;
			double maxT = gd.getNumericalMean() * 20;
			double delta = maxT / steps;
			for(int i = 0; i < steps; i++) {
				double t = delta * i;
				double pt = gd.density(t);
				xyseries.add(t, pt);
			}
		}
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(xyseries);
		XYItemRenderer pdfRenderer = new DefaultXYItemRenderer();
		
		// Creating chart with dataset 1
		JFreeChart barChart = ChartFactory.createHistogram(heading, "X Axis", "Y Axis", dataset, PlotOrientation.VERTICAL, true, false, false);
		
		// Getting plot 
		XYPlot plt = (XYPlot) barChart.getPlot();
		
		// Adding dataset 2
		plt.setDataset(1, dataset2);
		plt.setRenderer(1, pdfRenderer);
		plt.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );
		
		// Adding line
		ValueMarker marker = new ValueMarker(5);  // position is the value on the axis
		marker.setPaint(Color.black);
		marker.setLabel("here");
		plt.addDomainMarker(marker);
		
		// Getting frame
		ChartFrame cf = new ChartFrame(heading, barChart);
		ChartPanel cp = cf.getChartPanel();
		
		return cp;
	}


	private ChartPanel createChildGraph(String heading, ArrayList<Integer> childCounts, PoissonDistribution pd) {
		if(childCounts == null) return new ChartPanel(null);
		
		// Histogram
		double[] data = toPrimitiveInt(childCounts);
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Hist", data, 5);

		// Dataset 2
		XYSeries xyseries = new XYSeries("pdf");
		if(pd != null) {
			int steps = 100;
			double maxT = pd.getNumericalMean() * 20;
			int delta = (int) (maxT / steps);
			for(int i = 0; i < steps; i++) {
				int t = delta * i;
				double pt = pd.probability(t);
				xyseries.add(t, pt);
			}			
		}
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(xyseries);
		XYItemRenderer pdfRenderer = new DefaultXYItemRenderer();
		
		// Creating chart with dataset 1
		JFreeChart barChart = ChartFactory.createHistogram(heading, "X Axis", "Y Axis", dataset, PlotOrientation.VERTICAL, true, false, false);
		
		// Getting plot 
		XYPlot plt = (XYPlot) barChart.getPlot();
		
		// Adding dataset 2
		plt.setDataset(1, dataset2);
		plt.setRenderer(1, pdfRenderer);
		plt.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );
		
		// Adding line
		ValueMarker marker = new ValueMarker(5);  // position is the value on the axis
		marker.setPaint(Color.black);
		marker.setLabel("here");
		plt.addDomainMarker(marker);
		
		// Getting frame
		ChartFrame cf = new ChartFrame(heading, barChart);
		ChartPanel cp = cf.getChartPanel();
		
		return cp;
	}
	
	
	

	private double[] toPrimitiveDouble(ArrayList<Double> list) {
		int size = list.size();
		double[] rawData = new double[size];
		for(int i = 0; i < size; i++) {
			rawData[i] = list.get(i);
		}
		return rawData;
	}
	
	private double[] toPrimitiveInt(ArrayList<Integer> list) {
		int size = list.size();
		double[] rawData = new double[size];
		for(int i = 0; i < size; i++) {
			rawData[i] = list.get(i);
		}
		return rawData;
	}

}
