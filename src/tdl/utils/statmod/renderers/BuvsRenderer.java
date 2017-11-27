package tdl.utils.statmod.renderers;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import tdl.utils.statmod.Buvs;

public class BuvsRenderer implements ModRenderer {
	
	private Buvs model;

	public BuvsRenderer(Buvs buvs) {
		model = buvs;
	}

	@Override
	public JPanel render() {
		CategoryDataset dataset = null;
		JFreeChart barChart = ChartFactory.createBarChart("Histogram", "X Axis", "Y Axis", dataset, PlotOrientation.HORIZONTAL, true, true, false);
		ChartFrame cf = new ChartFrame("The histogram", barChart);
		ChartPanel cp = cf.getChartPanel();
		
		return new JPanel();
	}

}
