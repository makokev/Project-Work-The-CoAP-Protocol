package coap.mediator;

import java.util.concurrent.Semaphore;

// A synchronized counter
public class SynchronizedCounter{
	private int count;
	private Semaphore mutex;
	
	public SynchronizedCounter(){
		this(0);
	}
	
	public SynchronizedCounter(int count){
		this.count = count;
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