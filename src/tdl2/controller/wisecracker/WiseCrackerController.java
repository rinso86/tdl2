package tdl2.controller.wisecracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.Timer;

import tdl2.model.WiseCracker;

public class WiseCrackerController implements ActionListener {

	private JFrame jf;
	private WiseCracker wc;
	private Timer timer;
	
	
	public WiseCrackerController(JFrame jf) {
		this.jf = jf;
		this.wc = new WiseCracker();
		this.timer = new Timer(20000, this);
		timer.setInitialDelay(0);
		timer.start(); 
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		int imax = this.wc.wiseStuffCount();
		int i = ThreadLocalRandom.current().nextInt(0, imax);
		this.jf.setTitle(this.wc.getWiseStuff(i));
	}
	
}
