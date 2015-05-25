package app.compcoloridentifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import app.compcoloridentifier.R;


public class ImageColorAverager extends Activity implements OnClickListener {

	float[] hsv = new float[3];
	Bitmap test;
	private Uri myUri;
	final int PIC_CROP = 2;
	Button goToComp;
	ImageView iv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagecoloraverager);
		ImageView iv = (ImageView)findViewById(R.id.ivTestImage);
		Intent i = getIntent();
		myUri = Uri.parse(i.getStringExtra("imageUri"));
		try {
			test = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
            iv.setImageBitmap(Bitmap.createScaledBitmap(test, 2000, 2000, false));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		goToComp = (Button)findViewById(R.id.bGoToComp);
		iv.setOnClickListener(this);
		goToComp.setOnClickListener(this);	
	}
	
	private void performCrop() {
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			cropIntent.setDataAndType(myUri, "image/*");
			cropIntent.putExtra("crop", "true");
	//		cropIntent.putExtra("aspectX", 1);
	//		cropIntent.putExtra("aspectY", 1);
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, PIC_CROP);
			
		} catch (ActivityNotFoundException anfe) {
			String errorMess = "Cannot crop image.";
			Toast toast = Toast.makeText(this, errorMess, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	    	if (requestCode == PIC_CROP) {
	    		
	    		Bundle extras = data.getExtras();
	    		test = extras.getParcelable("data");
	    		ImageView picView = (ImageView)findViewById(R.id.ivTestImage);
	    		picView.setImageBitmap(test);
	    	}
	         
	    }
	}
	
	private int getRGBImage () {
		int red=0, green=0, blue = 0;
		int total = test.getHeight()*test.getWidth();

		for (int i=0 ; i < test.getHeight() ; i++) {
			for (int j=0; j < test.getWidth(); j++) {
				
				int pixel = test.getPixel(j, i);
			    red += Color.red(pixel);
			    green += Color.green(pixel);
			    blue += Color.blue(pixel);
			}
		}
		
		red /= total;
		green /= total;
		blue /= total;
		
		return Color.rgb(red,green,blue);
		
	}
	
	private int getComplimentaryColorImage(int RGB_Image) {
		int Comp_red = 0, Comp_green = 0, Comp_blue = 0;
		Color.colorToHSV(RGB_Image, hsv);
		
	    float H = (float) (hsv[0]);

	    if (H <= 180) {
	    	hsv[0] = H + 180;
	    } else {
	    	hsv[0] = H - 180;
	    }
	    
	    Comp_red = Color.red(Color.HSVToColor(hsv));
	    Comp_green = Color.green(Color.HSVToColor(hsv));
	    Comp_blue = Color.blue(Color.HSVToColor(hsv));
	    
	    return Color.rgb(Comp_red, Comp_green, Comp_blue);
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.ivTestImage:
				myUri = getImageUri(this,test);
				performCrop();
				break;
			case R.id.bGoToComp:
				int RGB_Image = getRGBImage();
			    int Comp_RGB_Image = getComplimentaryColorImage(RGB_Image);
			    Intent i = new Intent("app.compcoloridentifier.COMPARECOLORS");
			    i.putExtra("avgRGBvalue", RGB_Image);
			    i.putExtra("compRGBvalue", Comp_RGB_Image);
			    startActivity(i);
			    break;
		}
		
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
      }


    // taken method from Android http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    // taken method from Android http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
