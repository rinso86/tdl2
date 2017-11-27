package tdl.utils.statmod.renderers;

import javax.swing.JPanel;

import tdl.utils.statmod.Buvs;

public class BuvsRenderer implements ModRenderer {
	
	private Buvs model;

	public BuvsRenderer(Buvs buvs) {
		model = buvs;
	}

	@Override
	public JPanel render() {
		return new JPanel();
	}

}
