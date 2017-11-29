package tdl.utils.statmod.renderers;

import java.awt.Color;

import javax.swing.JPanel;

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
		
		// Dataset 1
		double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Hist", data, 5);

		// Dataset 2
		XYSeries xyseries = new XYSeries("pdf");
		xyseries.add(1, 1);
		xyseries.add(2,2);
		xyseries.add(1, 1);
		xyseries.add(3,3);
		xyseries.add(1, 1);
		xyseries.add(4,4);
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(xyseries);
		XYItemRenderer pdfRenderer = new DefaultXYItemRenderer();
		
		// Creating chart with dataset 1
		JFreeChart barChart = ChartFactory.createHistogram("Histogram", "X Axis", "Y Axis", dataset, PlotOrientation.VERTICAL, true, false, false);
		
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
		ChartFrame cf = new ChartFrame("The histogram", barChart);
		ChartPanel cp = cf.getChartPanel();
		JPanel jp = new JPanel();
		jp.add(cp);
		return jp;
	}

}
