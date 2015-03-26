package de.jofa.flickrapp.imageAdmin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;;

public class ImageAdmin {//extends BaseAdapter{
	private ImagePage imagePage;
	private int linesPerBlock;
	//private String[] imgPathNames;
	private String url, baseUrl;
	
    private HttpGet request;
    private HttpResponse response;
    private String url_tag;
    private int url_perPage, url_page;
    private Activity activity;
    private int heightPerLine, imgPerLine, width, lastUsedId;
    private GridLayout grid;
	
	public ImageAdmin(Activity activity, int heightPerLine, int width, GridLayout grid){
		this(activity, heightPerLine, 3, width, 3, grid);
		
	}
	
	public ImageAdmin(Activity activity, int heightPerLine, int imgPerLine, int width, int linesPerBlock, GridLayout grid){
		this.activity = activity;
		this.baseUrl = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=b36c712b9e9bacd93a74b725fdfb928d&format=json";
		this.url_tag = "funny";
		this.url_perPage = 20;
		this.url_page = 0;
		this.heightPerLine = heightPerLine;
		this.imgPerLine = imgPerLine;
		this.width = width;
		this.grid = grid;
		
		adjustGrid();
	}
	
	public GridLayout getGrid(){
		return grid;
	}
	
	public GridLayout fillGrid(){
		imagePage.fillGridView();
		return grid;
	}
	
	public void loadNext() throws GetImageException, GetJSONException{
		this.loadNext(this.url_tag);
	}
	
	public void loadNext(String tag) throws GetJSONException, GetImageException{
		if(tag.equalsIgnoreCase(this.url_tag)){
			this.url_page++;
			fillImgPathNames(tag, this.url_page);
		}else{
			this.url_page = 1;
			this.url_tag = tag;
			fillImgPathNames(tag, this.url_page);
			}
		//grid = fillGrid();
	}
	
	public int getPageNumber(){
		return this.url_page;
	}
	
	private void fillImgPathNames(String tag, int page) throws GetJSONException, GetImageException{
		adjustUrl(tag, page);
		String result = "";
		JSONArray jArray = null;
		List<String> photoList = new ArrayList<String>();
		HttpClient client;
		client = new DefaultHttpClient();
		request = new HttpGet();
		String[] imgPathNames;
		int begin = 0;
		try {
			request.setURI(new URI(url));
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			throw new GetJSONException("ClientProtocolException", e);
		} catch (IOException e) {
			throw new GetJSONException("IOException", e);
		} catch(URISyntaxException use){
			throw new GetJSONException("URISyntaxException", use);
		}
        try {
			result = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			throw new GetJSONException("ParseException", e);
		} catch (IOException e) {
			throw new GetJSONException("IOException", e);
		}
        try {
        	JSONObject answer = new JSONObject(result.substring(14));
			JSONObject photos = answer.getJSONObject("photos");
			jArray = photos.getJSONArray("photo");
		
			int n = jArray.length();
			for (int i = 0; i < n; i++) {
				JSONObject jo = jArray.getJSONObject(i);
				long id = jo.getLong("id");
				int server = jo.getInt("server");
				int farm = jo.getInt("farm");
				String secret = jo.getString("secret");
				String photourl = "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
				photoList.add(photourl);
			}
        } catch (JSONException e) {
        	throw new GetJSONException("JSONException", e);
		}
        if(page == 1){
        	imagePage = new ImagePage((Context)this.activity, this.heightPerLine, this.width, this.grid);
        }
        imgPathNames = new String[photoList.size()];
        for (String s: photoList) {
        		imgPathNames[begin] = this.getPicture(s, begin+this.lastUsedId);
        		begin++;
        	}
        lastUsedId += begin;
        imagePage.addNewImages(imgPathNames);
	}		
	
	public String getUrl(){
		return this.url;
	}
	
	public void adjustUrl(String tag, int perPage, int page){
		this.url_page = page;
		this.url_perPage = perPage;
		this.url_tag = tag;
		//StringBuffer bufferUrl = new StringBuffer(baseUrl);
		String newUrl = this.baseUrl+"&tags="+tag+"&per_page="+perPage+"&page="+page;
		//bufferUrl.append("&tags="+tag);
		//bufferUrl.append("&per_page="+perPage);
		//bufferUrl.append("&page="+page);
		//this.url = bufferUrl.toString();
		this.url = newUrl;
	}
	
	private void adjustUrl(){
		this.adjustUrl(this.url_tag, this.url_perPage, this.url_page);
	}
	
	private void adjustUrl(String tag){
		this.adjustUrl(tag, this.url_perPage, 1);
	}
	
	private void adjustUrl(String tag, int page){
		this.adjustUrl(tag, this.url_perPage, page);
	}
	
	private String getPicture(String imageurl, int id) throws GetImageException{
		String ergebnis = "";
		File file = null;
		InputStream in = null;
		try {

		    URL url = new URL(imageurl);
		    URLConnection urlConn = url.openConnection();

		    HttpURLConnection httpConn = (HttpURLConnection) urlConn;

		    httpConn.connect();

		    in = httpConn.getInputStream();

		   }
		catch (MalformedURLException e) {
			throw new GetImageException("MalformedURLException", e);
		}
		catch (IOException e) {
			throw new GetImageException("IOException", e);
		}
	    Bitmap bmpimg = BitmapFactory.decodeStream(in);
	    try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    bmpimg.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
			File direc = new File(activity.getFilesDir(), "pics");
		    direc.mkdir();
		    file = new File(activity.getFilesDir() + File.separator + "pics" + File.separator + id + ".jpg");
		    file.createNewFile();
		    FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes.toByteArray());
			fos.close();
		} catch (Exception e) {
			throw new GetImageException("Exception", e);
		}
	    ergebnis = file.getAbsolutePath();
		
		return ergebnis;
	}

	public void adjustGrid() {
		if(imagePage != null){
			LayoutParams params = new LayoutParams();
			int amountOfImages = imagePage.images.length;
			//GridLayout gl = new GridLayout((Context) this.activity);
			grid.setColumnCount(3);
			grid.setRowCount((int)( amountOfImages / 3));
			grid.setPadding(0, 0, 0, 0);
			grid.setVisibility(GridLayout.VISIBLE);
			grid.setLayoutParams(params);
		}
	}
}
