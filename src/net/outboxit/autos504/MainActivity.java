package net.outboxit.autos504;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText license;
	TextView fabricante, color, periodo, dei, municipal, total;
	String jsonStr;
	
    // URL to get contacts JSON
    private static String url = "http://autos504.herokuapp.com/query/index?license=";
 
    // JSON Node names
    private static final String TAG_MAKER = "fabricante";
    private static final String TAG_COLOR = "color";
    private static final String TAG_PERIOD = "periodo";
    private static final String TAG_DEI = "dei";
    private static final String TAG_MUNICIPAL = "municipal";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button b=(Button)findViewById(R.id.btnSendRequest);
		license    = (EditText)findViewById(R.id.edtLicense);
		fabricante = (TextView)findViewById(R.id.lblMakerVal);
		color      = (TextView)findViewById(R.id.lblColorVal);
		periodo    = (TextView)findViewById(R.id.lblPeriodVal);
		dei        = (TextView)findViewById(R.id.lblDEIVal);
		municipal  = (TextView)findViewById(R.id.lblMunicipalVal);
		total      = (TextView)findViewById(R.id.lblTotalVal);
		b.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			fabricante.setText("");
			color.setText("");
			periodo.setText("");
			dei.setText("");
			municipal.setText("");
			
			if ( license.length()>=7 && license.length()<=9) {
				new GetInformation().execute();
				Log.d("Response: ", "> " + jsonStr);
			}
		}
	};
	
	public class GetInformation extends AsyncTask<Void, Void, Void>{
		
		protected void onPreExecute(){
			super.onPreExecute();
			Toast.makeText(MainActivity.this, "Obteniendo datos...", Toast.LENGTH_LONG).show();
		}
		
		protected Void doInBackground(Void... arg0){
			ServiceHandler sh = new ServiceHandler();
			
			jsonStr = sh.makeServiceCall(url+license.getText().toString(), ServiceHandler.GET);
			Log.d("Response AsyncTask: ", "> " + jsonStr);	
			return null;
		}
		
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					fabricante.setText(jsonObj.get(TAG_MAKER).toString());
					color.setText(jsonObj.get(TAG_COLOR).toString());
					periodo.setText(jsonObj.get(TAG_PERIOD).toString());
					dei.setText(jsonObj.get(TAG_DEI).toString());
					municipal.setText(jsonObj.get(TAG_MUNICIPAL).toString());
					
					/*
					Float deiVal=Float.parseFloat(jsonObj.get(TAG_DEI).toString());
					Float muniVal=Float.parseFloat(jsonObj.get(TAG_MUNICIPAL).toString());
					Float totalVal = deiVal+muniVal;
					DecimalFormat df = new DecimalFormat("0.00");
					df.setMaximumFractionDigits(2);
					total.setText( df.format(totalVal) );
					*/
				} catch (JSONException e) {
					Toast.makeText(MainActivity.this, "Ocurrio un error, por favor intente de nuevo.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(MainActivity.this, "No se logro obtener la informacion, intente de nuevo.", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
