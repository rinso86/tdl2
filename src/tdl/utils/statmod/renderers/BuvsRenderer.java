package tdl.utils.statmod.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

import javax.swing.JPanel;

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
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tdl.utils.statmod.Buvs;



public class BuvsRenderer implements ModRenderer {
	
	private static int nrGroupsTime = 7;
	private static int nrGroupsChildren = 7;
	private static int imgWidth = 300;
	private static int imgHeight = 200;
	private Buvs model;

	public BuvsRenderer(Buvs buvs) {
		model = buvs;
	}

	@Override
	public JPanel render() {
		
		int treeDepth = model.getTreeDepth();
		JPanel jp = new JPanel(new GridLayout(treeDepth - 1, 4));
		
		HashMap<Integer, ArrayList<Integer>> childCounts = model.getChildCounts();
		HashMap<Integer, ArrayList<Double>> netTimes = model.getNetTimes();
		HashMap<Integer, PoissonDistribution> poisDs = model.getPoisDs();
		HashMap<Integer, GammaDistribution> gamDs = model.getGamDs();
		
		for(int i = 1; i < treeDepth; i++) { // starting at 1: root will never be finished. 
			
			ChartPanel childPanel = createChildGraph(i, childCounts.get(i), poisDs.get(i));
			ChartPanel timePanel = createTimeGraph(i, netTimes.get(i), gamDs.get(i));
			ChartPanel childConditional = createChildCondExGraph(i, childCounts.get(i), poisDs.get(i));
			ChartPanel timeConditional = createTimeCondExGraph(i, netTimes.get(i), gamDs.get(i));
			
			childPanel.setPreferredSize(new Dimension(imgWidth,  imgHeight));
			timePanel.setPreferredSize(new Dimension(imgWidth,  imgHeight));
			timeConditional.setPreferredSize(new Dimension(imgWidth,  imgHeight));
			childConditional.setPreferredSize(new Dimension(imgWidth,  imgHeight));
			
			jp.add(childPanel);
			jp.add(childConditional);
			jp.add(timePanel);
			jp.add(timeConditional);
		}
		
		return jp;
	}

	private ChartPanel createTimeCondExGraph(int depth, ArrayList<Double> netTimes, GammaDistribution gd) {
		if(netTimes == null || gd == null) return new ChartPanel(null);
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		XYSeries t0Series = new XYSeries("t0");
		XYSeries e0Series = new XYSeries("E[t]");
		XYSeries eCondSeries = new XYSeries("E[t|t>t0]");
		double t0Max = Collections.max(netTimes);
		int steps = 20;
		double delta = t0Max / steps;
		for(int s = 0; s < steps; s++) {
			double t0 = delta * s;
			t0Series.add(t0, t0);
			e0Series.add(t0, gd.getNumericalMean());
			eCondSeries.add(t0, model.getEstimateMeanNetTimeCond(depth, (long) t0));
		}
		
		dataset.addSeries(t0Series);
		dataset.addSeries(e0Series);
		dataset.addSeries(eCondSeries);
		
		JFreeChart chart = ChartFactory.createXYLineChart("expected time on " + depth, "t0", "E[t|t>c0]", dataset, PlotOrientation.VERTICAL, true, false, false);
		XYPlot plot = (XYPlot) chart.getPlot();

		// Getting frame
		ChartFrame cf = new ChartFrame("E on " + depth, chart);
		ChartPanel cp = cf.getChartPanel();
		
		return cp;
	}

	private ChartPanel createChildCondExGraph(int depth, ArrayList<Integer> childCounts, PoissonDistribution pd) {
		if(pd == null) return new ChartPanel(null);
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		XYSeries c0Series = new XYSeries("c0");
		XYSeries e0Series = new XYSeries("E[c]");
		XYSeries eCondSeries = new XYSeries("E[c|c>c0]");
		int c0Max = 10;
		for(int c0 = 0; c0 < c0Max; c0++) {
			c0Series.add(c0, c0);
			e0Series.add(c0, pd.getNumericalMean());
			eCondSeries.add(c0, model.getExpectedChildCountCond(pd.getNumericalMean(), c0));
		}
		
		dataset.addSeries(c0Series);
		dataset.addSeries(e0Series);
		dataset.addSeries(eCondSeries);
		
		JFreeChart chart = ChartFactory.createXYLineChart("expected children on " + depth, "c0", "E[c|c>c0]", dataset, PlotOrientation.VERTICAL, true, false, false);
		XYPlot plot = (XYPlot) chart.getPlot();

		// Getting frame
		ChartFrame cf = new ChartFrame("E on " + depth, chart);
		ChartPanel cp = cf.getChartPanel();
		
		return cp;
	}

	private ChartPanel createTimeGraph(int depth, ArrayList<Double> netTimes, GammaDistribution gd) {
		if(netTimes == null || netTimes.size() <= 1) return new ChartPanel(null);
		
		// Histogram
		double[] data = toPrimitiveDouble(netTimes);
		data = mapOnDouble(data, a -> a/60);
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.SCALE_AREA_TO_1);
		dataset.addSeries("Times", data, nrGroupsTime);

		// Pdf
		XYSeries xyseries = new XYSeries("pdf");
		if(gd != null) {			
			int steps = 20;
			double maxT = Collections.max(netTimes) + Math.sqrt(gd.getNumericalVariance());
			double delta = maxT / steps;
			for(int i = 1; i < steps; i++) { // strikt positiv; darf nicht bei 0 beginnen
				double t = delta * i;
				double pt = gd.density(t);
				xyseries.add(t/60, pt * 100);
			}
		}
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(xyseries);
		XYItemRenderer pdfRenderer = new DefaultXYItemRenderer();
		
		// Creating chart with dataset 1
		JFreeChart barChart = ChartFactory.createHistogram("times on " + depth, "minutes", "prob/count", dataset, PlotOrientation.VERTICAL, true, false, false);
		
		// Getting plot 
		XYPlot plt = (XYPlot) barChart.getPlot();
		
		// Adding dataset 2
		plt.setDataset(1, dataset2);
		plt.setRenderer(1, pdfRenderer);
		plt.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );
		
		// Adding mean
		if(gd != null) {
			ValueMarker marker = new ValueMarker(gd.getNumericalMean());  // position is the value on the axis
			marker.setPaint(Color.black);
			marker.setLabel("mean");
			plt.addDomainMarker(marker);
		}
			
		
		// Getting frame
		ChartFrame cf = new ChartFrame("times on " + depth, barChart);
		ChartPanel cp = cf.getChartPanel();
		
		return cp;
	}



	private ChartPanel createChildGraph(int depth, ArrayList<Integer> childCounts, PoissonDistribution pd) {
		if(childCounts == null || childCounts.size() <= 1) return new ChartPanel(null);
		
		// Histogram
		double[] data = toPrimitiveInt(childCounts);
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.SCALE_AREA_TO_1);
		dataset.addSeries("hist", data, nrGroupsChildren);

		// Dataset 2
		XYSeries xyseries = new XYSeries("pdf");
		if(pd != null) {
			int steps = Collections.max(childCounts) + 1;
			for(int t = 0; t < steps; t++) {
				double pt = pd.probability(t);
				xyseries.add(t, pt);
			}			
		}
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(xyseries);
		XYItemRenderer pdfRenderer = new DefaultXYItemRenderer();
		
		// Creating chart with dataset 1
		JFreeChart barChart = ChartFactory.createHistogram("children on " + depth, "children", "count/prob", dataset, PlotOrientation.VERTICAL, true, false, false);
		
		// Getting plot 
		XYPlot plt = (XYPlot) barChart.getPlot();
		
		// Adding dataset 2
		plt.setDataset(1, dataset2);
		plt.setRenderer(1, pdfRenderer);
		plt.setDatasetRenderingOrder( DatasetRenderingOrder.FORWARD );
		
		// Adding mean
		if(pd != null) {			
			ValueMarker marker = new ValueMarker(pd.getNumericalMean());  // position is the value on the axis
			marker.setPaint(Color.black);
			marker.setLabel("mean");
			plt.addDomainMarker(marker);
		}
		
		// Getting frame
		ChartFrame cf = new ChartFrame("children on " + depth, barChart);
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


	private double[] mapOnDouble(double[] data, Function<Double, Double> f) {
		int size = data.length;
		double[] out = new double[size];
		for(int i = 0; i < size; i++) {
			out[i] = f.apply(data[i]);
		}
		return out;
	}
}
