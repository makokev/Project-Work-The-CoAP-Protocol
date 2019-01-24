package coap.mediator;

// A synchronized counter
public class SynchronizedCounter{
	private int count = 0;
	
	synchronized public int GetCount(){
		return count;
	}
	
	synchronized public void IncrementCount(){
		count ++;
	}
	
}