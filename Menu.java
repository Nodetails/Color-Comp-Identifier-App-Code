package app.compcoloridentifier;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.compcoloridentifier.R;
import android.util.Log;

public class Menu extends Activity implements OnClickListener  {
	
	private static final int IMAGE_PICKER_SELECT = 1;
	private static final int CAMERA_IMAGE = 2;
    private static final String TAG = Activity.class.getSimpleName();

	Button openImageGallery, openCamera;
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		openImageGallery = (Button)findViewById(R.id.bSelectFromImageGallery);
		openCamera = (Button)findViewById(R.id.bCamera);
		openImageGallery.setOnClickListener(this);
		openCamera.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.bSelectFromImageGallery:
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, IMAGE_PICKER_SELECT);  
			break;
			
		case R.id.bCamera:		
			Intent s = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(s,CAMERA_IMAGE);
			break;
			
		default:
			break;
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    super.onActivityResult(requestCode, resultCode, data); 

	    switch(requestCode) { 
	    case IMAGE_PICKER_SELECT:
	        if(resultCode == RESULT_OK){
	            uri = data.getData();
				Intent k = new Intent("app.compcoloridentifier.IMAGECOLORAVERAGER");
				k.putExtra("imageUri", uri.toString());
				startActivity(k);
	        } break;
	    case CAMERA_IMAGE:
	    	if(resultCode == RESULT_OK) {
	    		Bundle extras = data.getExtras();
	    		Bitmap photo = (Bitmap) extras.get("data");

                BitmapFactory.Options options = new BitmapFactory.Options();


	    		uri = getImageUri(this,photo);
				Intent k = new Intent("app.compcoloridentifier.IMAGECOLORAVERAGER");
				k.putExtra("imageUri", uri.toString());
				startActivity(k);
	    	} break;           
	    }
	}
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
      }



//    mImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));

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

