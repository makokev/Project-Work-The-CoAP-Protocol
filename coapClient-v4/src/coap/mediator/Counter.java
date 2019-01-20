package coap.mediator;

import java.util.concurrent.Semaphore;

// A synchronized counter
public class Counter{
	private int count;
	private Semaphore s;
	
	public Counter() {
		s = new Semaphore(1);
		count = 0;
	}
	
	public int GetCount(){
		try {
			s.acquire();
			int c = count;
			s.release();
			return c;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Counter: GetCount() error.");
			System.exit(-1);
		}
		return -1;
	}
	
	public void IncrementCount(){
		try {
			s.acquire();
			count ++;
			s.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Counter: IncrementCount() error.");
			System.exit(-1);
		}	
	}
}