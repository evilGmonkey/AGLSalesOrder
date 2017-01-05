package org.uhurulabs.aglsalesorder;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    public final static String PRODUCT = "com.example.aglsalesorder.PRODUCT";
    public final static String QUANTITY = "com.example.aglsalesorder.QUANTITY";

    private RadioGroup radioProductGroup;
    private RadioButton radioProductButton;
    private RadioGroup radioExGroup;
    private RadioButton radioExButton;

  //  private EditText quantity;
   // private EditText quality;


    EditText shipmentdate;
    DatePickerDialog datePickerDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws ActivityNotFoundException {

                Boolean validOrder = true;
                int entryLength = 0;
                StringBuilder errMsg = new StringBuilder("Please make sure you provide: ");
                errMsg.append("\n");

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                String todaysdate = df.format(c.getTime());

                Snackbar.make(view, "Validating data....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();




                radioProductGroup = (RadioGroup) findViewById(R.id.radioProduct);
                int selectedId = radioProductGroup.getCheckedRadioButtonId();
                radioProductButton = (RadioButton) findViewById(selectedId);
                String product = radioProductButton.getText().toString();

                radioExGroup = (RadioGroup) findViewById(R.id.EX_Group);
                int exSelectedId = radioExGroup.getCheckedRadioButtonId();
                radioExButton = (RadioButton) findViewById(exSelectedId);
                String ex = radioExButton.getText().toString();



                EditText dateText = (EditText) findViewById(R.id.shipmentdate);
                String shipmentdate = dateText.getText().toString();


                EditText quantityText = (EditText) findViewById(R.id.quantity);
                int quantity = Integer.parseInt(quantityText.getText().toString());
                if (quantity <=0)
                {
                    validOrder = false;
                    errMsg.append("Quantity must be greater than 0").append("\n");
                }

                EditText qualityText = (EditText) findViewById(R.id.quality);
                int quality = Integer.parseInt(qualityText.getText().toString());
                if (quality <=0)
                {
                    validOrder = false;
                    errMsg.append("Quality must be greater than 0").append("\n");
                }

                EditText buyerText = (EditText) findViewById(R.id.buyer);
                String buyer = buyerText.getText().toString();
                entryLength = buyer.length();
                if (entryLength >= 50 || entryLength <= 2) {
                    validOrder = false;
                    errMsg.append("Invalid buyer name").append("\n");
                }

                EditText rateText = (EditText) findViewById(R.id.rate);
                int rate = Integer.parseInt(rateText.getText().toString());
                if (rate <=0)
                {
                    validOrder = false;
                    errMsg.append("Rate must be greater than 0").append("\n");
                }


                EditText podText = (EditText) findViewById(R.id.pod);
                String pod =  podText.getText().toString();
                entryLength = pod.length();
                if (entryLength != 3) {
                    validOrder = false;
                    errMsg.append("POD must be 3 characters").append("\n");
                }

                if(validOrder)  {
                    Toast.makeText(MainActivity.this, "Creating Email", Toast.LENGTH_SHORT).show();
                    // Setup email
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"freddie@uhurunet.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sales Order");


                    StringBuilder emailBody = new StringBuilder("Product: ");
                    emailBody.append(product).append("\n");
                    emailBody.append("Order Date: ").append(todaysdate).append("\n");
                    emailBody.append("Quantity: ").append(quantity).append("\n");
                    emailBody.append("Quality: ").append(quality).append("\n");
                    emailBody.append("Rate: ").append(rate).append("\n");
                    emailBody.append("EX").append(ex).append("\n");
                    emailBody.append("Pod: ").append(pod).append("\n");
                    emailBody.append("Shipment Date: ").append(shipmentdate).append("\n");

                    i.putExtra(Intent.EXTRA_TEXT, emailBody.toString());
                    startActivity(Intent.createChooser(i, "Send mail..."));

                } else {
                    Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // initiate the date picker and a button
        shipmentdate = (EditText) findViewById(R.id.shipmentdate);
        // perform click event on edit text
        shipmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final  String[] months = {
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May"
                };
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                shipmentdate.setText(dayOfMonth + "/" + months[monthOfYear] + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
