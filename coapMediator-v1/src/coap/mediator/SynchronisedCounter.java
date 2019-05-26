package coap.mediator;

import java.util.concurrent.Semaphore;

// A synchronised counter
public class SynchronisedCounter{
	private int count;
	private Semaphore mutex;
	
	public SynchronisedCounter(){
		this(0);
	}
	
	public SynchronisedCounter(int startingCount){
		if(startingCount < 0)
			throw new IllegalArgumentException("startingCount must be >= 0.");
		this.count = startingCount;
		this.mutex = new Semaphore(1);
	}
	
	public int GetCount(){
		try {
			mutex.acquire();
			int c = count;
			mutex.release();
			return c;
		} catch (InterruptedException e) {
			System.out.println("SynchronizedCounter exception.");
			e.printStackTrace();
			return -1;
		}
	}
	
	public void IncrementCount(){
		try {
			mutex.acquire();
			count ++;
			mutex.release();
		} catch (InterruptedException e) {
			System.out.println("SynchronizedCounter exception.");
			e.printStackTrace();
		}
	}
	
}