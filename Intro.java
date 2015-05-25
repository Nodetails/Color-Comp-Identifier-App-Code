package app.compcoloridentifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.compcoloridentifier.R;

public class Intro extends Activity implements OnClickListener {

	Button startApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		startApp = (Button)findViewById(R.id.bStartApp);
		startApp.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent openMenu = new Intent("app.compcoloridentifier.MENU");
		startActivity(openMenu); // needs an Intent
	}
	
	

}
