package de.jofa.flickrapp.imageAdmin;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;



public class ImagePage{
	//private ImageLine[] lines;
	protected Image[] images;
	private Context context;
	private String[] imgPathNames;
	private int heightPerImg;
	private int imgPerLine;
	private int width;
	private GridLayout grid;
	
	public ImagePage(Context context, 
			int heightPerImg, int width, GridLayout grid){//, ImageAdmin imageAdmin) {
		System.out.println("Anfang Konstruktor ImagePage");
		//this.imageAdmin = imageAdmin;
		this.context = context;
		this.heightPerImg = heightPerImg;
		this.width = width;
		this.grid = grid;
		grid.invalidate();
		System.out.println("Ende Konstruktor ImagePage");
	}
	
	public void fillGridView(){
		for(Image img: images){
			grid.addView(img, this.width, this.heightPerImg);
		}
		//grid.draw((Canvas)context);
	}
	
	public void fillGridViewWithNew(Image img){
		grid.addView(img, this.width, this.heightPerImg);
	}
	
	public void initImagesArray(String[] newImagePaths){
		addNewImages(newImagePaths);
	}
	
	public void addNewImages(String[] newImagePaths){
		Image[] newImg = new Image[newImagePaths.length];
		Image[] both   = null;  
		String[] bothPaths = null;
		for(int i = 0; i < newImagePaths.length; i++){
			newImg[i] = new Image(context, newImagePaths[i], this.heightPerImg, this.width);
			fillGridViewWithNew(newImg[i]);
		}
		
		//expand images array by new images
		if(images == null){
			images = newImg;
		}else{
			both = new Image[newImagePaths.length + images.length];
			System.arraycopy(images, 0, both, 0, images.length);
			System.arraycopy(newImg, 0, both, images.length, newImg.length);
			images = both;
		}
		
		//expand imagePathNames array by new path names
		if(imgPathNames == null){
			imgPathNames = newImagePaths;
		}else{
			bothPaths =  new String[imgPathNames.length + newImagePaths.length];
			System.arraycopy(imgPathNames, 0, bothPaths, 0, imgPathNames.length);
			System.arraycopy(newImagePaths, 0, bothPaths, imgPathNames.length, newImagePaths.length);
			imgPathNames = bothPaths;
		}
		
		
		
	}
}
