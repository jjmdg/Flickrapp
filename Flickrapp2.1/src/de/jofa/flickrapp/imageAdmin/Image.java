package de.jofa.flickrapp.imageAdmin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class Image extends ImageView{
	private String pathName;
	public Image(Context context, String pathName, int height, int width){
		super(context);
		this.pathName = pathName;
		super.setMaxHeight(height);
		super.setMaxWidth(width);
		super.setMinimumHeight(height);
	}
	
	public void setImageDrawable(){
		super.setImageDrawable(Drawable.createFromPath(pathName));
	}
	
}
