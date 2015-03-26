package uni.flickrapp;

import de.jofa.flickrapp.imageAdmin.GetImageException;
import de.jofa.flickrapp.imageAdmin.GetJSONException;
import de.jofa.flickrapp.imageAdmin.ImageAdmin;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;


public class MainActivity 	extends 	Activity
							implements 	OnClickListener{
	
	Button searchButton;
	Button reloadButton;
    ImageAdmin imageAdmin;
    EditText searchTextEdit;
    //LinearLayout imagesFrame;
    GridLayout grid = null;
    ProgressBar pbar;
    boolean loading;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		searchButton = (Button) findViewById(R.id.searchButton);
		
		reloadButton = (Button) findViewById(R.id.reloadButton);
		
		searchTextEdit = (EditText) findViewById(R.id.searchTextEdit);
		//imagesFrame = (LinearLayout) findViewById(R.id.ImagesFrame);
		//imagesFrame = imageAdmin.getPageAsLinearLayout();
		//LayoutParams imagesFrameLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//imagesFrameLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		//this.addContentView(imagesFrame, imagesFrameLayout);
		grid = (GridLayout) findViewById(R.id.imageGrid);
		grid.setColumnCount(3);
		//gridView.setAdapter(imageAdmin);
		imageAdmin = new ImageAdmin(this, 100, 3, 300, 3, grid);
		setClickListeners();
		loading = false;
		pbar = (ProgressBar) findViewById(R.id.loadingProgress);
		pbar.setVisibility(ProgressBar.INVISIBLE);
		grid.setVisibility(GridLayout.VISIBLE);
	}
	
	private void setClickListeners() {
		searchButton.setOnClickListener(this);
		reloadButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if(!loading){
			pbar.setVisibility(ProgressBar.VISIBLE);
			Thread t;
			if(v == searchButton){
				t = new Thread( new LoadNextThread((Context) this, searchTextEdit.getText().toString(), imageAdmin));
				//t.setPriority(Thread.MAX_PRIORITY);
				t.start();
				searchTextEdit.setText(R.string.searchTextField_Hint);
				setContentView(R.layout.activity_main);
				System.out.println("GridView is visible: "+grid.isShown());
			}else if (v == reloadButton){
				t = new Thread( new LoadNextThread((Context) this, imageAdmin));
				//t.setPriority(Thread.MAX_PRIORITY);
				t.start();
			}
			//this.addContentView(imageAdmin.getView(0, null, null), null);
			pbar.setVisibility(ProgressBar.INVISIBLE);
		}
	}
	
	
	class LoadNextThread implements Runnable{
		private String tag;
		private ImageAdmin imgAdmin;
		private Context context;
		LoadNextThread(Context context, String tag, ImageAdmin imgAdmin){
			this.tag = tag;
			this.imgAdmin = imgAdmin;
			this.context = context;
		}
		LoadNextThread(Context context, ImageAdmin imgAdmin){
			this.tag = null;
			this.context = context;
			this.imgAdmin = imgAdmin;
		}
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			loading = true;
			//Toast toast;
			try{
				if(tag == null){
					imgAdmin.loadNext();
					
				}else{
					imgAdmin.loadNext(tag);
				}
			}catch(GetImageException gie){
				gie.printStackTrace();
				//toast = Toast.makeText(context, "Error while loading Image", Toast.LENGTH_SHORT);
				//toast.show();
			}
			catch(GetJSONException gje){
				gje.printStackTrace();
				//toast = Toast.makeText(context , "Error while parsing JSON", Toast.LENGTH_SHORT);
				//toast.show();
			}
			loading = false;
		}
		
		
	}
}

