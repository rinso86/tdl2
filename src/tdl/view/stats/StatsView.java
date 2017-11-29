package tdl.view.stats;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.utils.statmod.renderers.ModRenderer;

public class StatsView  implements Recipient {
	
	private Controller controller;
	private ModRenderer renderer;
	private JPanel jpInner;
	private JScrollPane scroller;
	private JPanel jp;
	
	public StatsView(Controller controller, ModRenderer renderer) {
		this.controller = controller;
		this.renderer = renderer;
		this.jp = new JPanel();
		this.jpInner = renderer.render();
		this.scroller = new JScrollPane(this.jpInner);
		this.jp.add(scroller);
	}

	@Override
	public void receiveMessage(Message message) {
		System.out.println("StatsView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case UPDATED_TASK:
			update();
			break;
		default:
			break;
		}
	}
	
	private void update() {
		this.jpInner = renderer.render();
	}

	public JPanel getPanel() {
		return jp;
	}

}
