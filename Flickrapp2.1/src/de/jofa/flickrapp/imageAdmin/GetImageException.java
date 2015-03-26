package de.jofa.flickrapp.imageAdmin;

public class GetImageException extends Exception{
	private Exception cause;
	public GetImageException(String origin, Exception cause){
		super("GetImageException - "+origin);
		this.cause = cause;
	}
	
	public void printStackTrace(){
		if(cause != null){
			cause.printStackTrace();
		}else this.printStackTrace();
	}
}
