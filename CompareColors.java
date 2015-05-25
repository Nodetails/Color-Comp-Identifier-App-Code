package app.compcoloridentifier;


import java.io.InputStream;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import app.compcoloridentifier.R;


public class CompareColors extends FragmentActivity implements OnClickListener {

	TabHost th;
	Button goBack_A, goBack_B;
	TextView AvgRGB_tv, CompRGB_tv;
	ImageView AvgRGB_img, CompRGB_img;
	int avgRGB, compRGB; 
	Bitmap AvgReplace, CompReplace;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.comparecolors);
		

		
		th = (TabHost)findViewById(R.id.tabhost);
		goBack_A = (Button)findViewById(R.id.bGoBack1);
		goBack_B = (Button)findViewById(R.id.bGoBack2);
		AvgRGB_tv = (TextView)findViewById(R.id.tvRGBavg);
		CompRGB_tv = (TextView)findViewById(R.id.tvRGBcomp);
		AvgRGB_img = (ImageView)findViewById(R.id.ivAvg);
		CompRGB_img = (ImageView)findViewById(R.id.ivComp); 
		
		th.setup();
		TabSpec specs = th.newTabSpec("tag1"); //(85) tag is needed, but might have no purpose
		specs.setContent(R.id.tab1);
		specs.setIndicator("Avg RGB"); //what tab includes
		th.addTab(specs);
		specs = th.newTabSpec("tag2"); //(85) tag is needed, but might have no purpose
		specs.setContent(R.id.tab2);
		specs.setIndicator("Comp RGB"); //what tab includes
		th.addTab(specs);
		
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			avgRGB = extras.getInt("avgRGBvalue");
			compRGB = extras.getInt("compRGBvalue");
		} 

		InputStream is = getResources().openRawResource(+ R.drawable.red);
		AvgReplace = BitmapFactory.decodeStream(is);
		AvgReplace = replaceColor(AvgReplace,Color.rgb(Color.red(avgRGB),Color.green(avgRGB), Color.blue(avgRGB))); //input color from other activity
		AvgRGB_img.setImageBitmap(AvgReplace);
		InputStream iss = getResources().openRawResource(+ R.drawable.red);
		CompReplace = BitmapFactory.decodeStream(iss);
		CompReplace=replaceColor(CompReplace ,Color.rgb(Color.red(compRGB),Color.green(compRGB), Color.blue(compRGB)));
		CompRGB_img.setImageBitmap(CompReplace); 
		
		String Avg = "  R:  " + Color.red(avgRGB) + "  G:  " + Color.green(avgRGB) + "  B:  " + Color.blue(avgRGB);
		String Comp = "  R:  " + Color.red(compRGB) + "  G:  " + Color.green(compRGB) + "  B:  " + Color.blue(compRGB);
		
		
		AvgRGB_tv.setText(Avg);
		CompRGB_tv.setText(Comp);
		
		goBack_A.setOnClickListener(this);
		goBack_B.setOnClickListener(this);
		

		
	}




	@Override
	public void onClick(View v) {
		Intent openMenu = new Intent("app.compcoloridentifier.MENU");
		startActivity(openMenu); 
	}
	
	public Bitmap replaceColor(Bitmap src, int targetColor) { //credit goes to http://shaikhhamadali.blogspot.com/2013/08/changereplacementremove-pixel-colors-in.html
        if(src == null) {
            return null;
        }
     // Source image size 
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        //get pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height);
  
        for(int x = 0; x < pixels.length; x++) {
            pixels[x] = targetColor;
        }
     // create result bitmap output 
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        //set pixels
        result.setPixels(pixels, 0, width, 0, 0, width, height);
  
        return result;
    }

}
