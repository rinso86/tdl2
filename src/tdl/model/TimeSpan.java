package tdl.model;

import java.util.Date;

public class TimeSpan {

	private Date start;
	private Date end;
	
	public TimeSpan() {
		this(null, null);
	}
	

	public TimeSpan(Date start) {
		this(start, null);
	}
	
	public TimeSpan(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public boolean isComplete() {
		return (end != null);
	}
	
	public boolean isRunning() {
		return (start != null && ! isComplete());
	}
	
	public void begin() {
		if(! isRunning()) {
			start = new Date();
		}
	}
	
	
	public void complete() {
		if(! isComplete() ) {
			end = new Date();
		}
	}
	
	public Long getDuration() {
		if(isComplete()) {
			return ( end.getTime() - start.getTime() ) / 1000;
		} else {
			return null;
		}
	}
	
}
