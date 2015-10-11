package casual;

public class Stopwatch{

	private long totalElapsed = 0;
	private long lastStartedTime = 0;

	public void start(){
		lastStartedTime = System.currentTimeMillis();
	}

	public void stop(){
		totalElapsed = System.currentTimeMillis() - lastStartedTime;
	}

	public void reset(){
		totalElapsed = 0;
	}

	public void restart(){
		reset();
		start();
	}

	public long getElapsedTime(){
		return totalElapsed;
	}
	
	public void report(){
		System.out.println("Elapsed time: " + this.getElapsedTime() + " ms");
	}
}
