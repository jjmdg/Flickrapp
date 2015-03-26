package de.jofa.flickrapp.imageAdmin;

public class GetJSONException extends Exception {
	private Exception cause;
	
	public GetJSONException(String origin, Exception cause){
		super("GetJSONException - "+origin);
	}
	
	public void printStackTrace(){
		if(cause != null){
			cause.printStackTrace();
		}else this.printStackTrace();
	}
}
