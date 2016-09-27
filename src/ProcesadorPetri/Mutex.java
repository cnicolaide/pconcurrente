package ProcesadorPetri;

public class Mutex {

	private int key = 0;

	public int acquire() {
		return key;
				
	}
	
	public void setKey(){
		key++;
	}
	
	public void release (){
		key--;
	}

}
