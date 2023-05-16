package ua.moyseienko.cargoapp.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import ua.moyseienko.cargoapp.MainActivity;
import ua.moyseienko.cargoapp.R;
import ua.moyseienko.cargoapp.services.externaldb.ExternalCreateOrder;
import ua.moyseienko.cargoapp.services.externaldb.ExternalSelectOrders;
import ua.moyseienko.cargoapp.services.localdb.LocalSelectUser;

public class CreateOrder extends AppCompatActivity {
    EditText addressFrom;
    EditText addressTo;
    Button btnSendedAt;
    Button btnRecievedAt;
    EditText price;
    EditText distance;
    EditText packSize;
    EditText packType;
    EditText packWeight;
    Button btnCreate;
    Button btnCancel;

    String sendedAt;
    String recievedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        addressFrom = (EditText) findViewById(R.id.etAddressFrom);
        addressTo = (EditText) findViewById(R.id.etAddressTo);
        btnSendedAt = (Button) findViewById(R.id.btnSendedAt);
        btnRecievedAt = (Button) findViewById(R.id.btnRecievedAt);
        price = (EditText) findViewById(R.id.etPrice);
        distance = (EditText) findViewById(R.id.etDistance);
        packSize = (EditText) findViewById(R.id.etPackageSize);
        packType = (EditText) findViewById(R.id.etPackageType);
        packWeight = (EditText) findViewById(R.id.etPackageWeight);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(clickListener);
        btnCreate.setOnClickListener(clickListener);
        btnRecievedAt.setOnClickListener(clickListener);
        btnSendedAt.setOnClickListener(clickListener);
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnSendedAt:
                    // on below line we are getting
                    // the instance of our calendar.
                    final Calendar c = Calendar.getInstance();

                    // on below line we are getting
                    // our day, month and year.
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    // on below line we are creating a variable for date picker dialog.
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            // on below line we are passing context.
                            CreateOrder.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // on below line we are setting date to our text view.
                                    sendedAt = year  + "-" + monthOfYear + "-"+ (dayOfMonth + 1)  ;

                                }
                            },
                            // on below line we are passing year,
                            // month and day for selected date in our date picker.
                            year, month, day);
                    // at last we are calling show to
                    // display our date picker dialog.
                    datePickerDialog.show();
                    break;
                case R.id.btnRecievedAt:
                    final Calendar c1 = Calendar.getInstance();

                    // on below line we are getting
                    // our day, month and year.
                    int year1 = c1.get(Calendar.YEAR);
                    int month1 = c1.get(Calendar.MONTH);
                    int day1 = c1.get(Calendar.DAY_OF_MONTH);

                    // on below line we are creating a variable for date picker dialog.
                    DatePickerDialog datePickerDialog1 = new DatePickerDialog(
                            // on below line we are passing context.
                            CreateOrder.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year1,
                                                      int monthOfYear1, int dayOfMonth1) {
                                    // on below line we are setting date to our text view.
                                    recievedAt = year1  + "-" + monthOfYear1 + "-" + ( dayOfMonth1 + 1)  ;

                                }
                            },
                            // on below line we are passing year,
                            // month and day for selected date in our date picker.
                            year1, month1, day1);
                    // at last we are calling show to
                    // display our date picker dialog.
                    datePickerDialog1.show();
                    break;

                case R.id.btnCancel:
                    startActivity(new Intent(CreateOrder.this, MainActivity.class));
                    finish();
                    break;
                case R.id.btnCreate:
                    String addressFromTxt = addressFrom.getText().toString();
                    String addressToTxt = addressTo.getText().toString();
                    String sendedAtTxt = sendedAt;
                    String recievedAtTxt = recievedAt;
                    String priceTxt = price.getText().toString();
                    if(!isInt(priceTxt)){
                        Toast.makeText(CreateOrder.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    }
                    String distanceTxt = distance.getText().toString();
                    if(!isInt(distanceTxt)){
                        Toast.makeText(CreateOrder.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    }
                    String packSizeTxt = packSize.getText().toString();
                    String packTypeTxt = packType.getText().toString();
                    String packWeightTxt = packWeight.getText().toString();
                    if(!isInt(packWeightTxt)){
                        Toast.makeText(CreateOrder.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    }
                    createOrder(addressFromTxt, addressToTxt,
                                sendedAtTxt, recievedAtTxt,
                                priceTxt, distanceTxt,
                                packSizeTxt, packTypeTxt, packWeightTxt);

                    break;
            }
        }
    };

    public void createOrder(
            String addressFrom, String addressTo,
            String sentAt, String recievedAt,
            String price, String distance,
            String packSize, String packType, String packWeight){
        ExternalCreateOrder createOrder = new ExternalCreateOrder();
        String email ="";
        LocalSelectUser selectUser = new LocalSelectUser();
        ArrayList<HashMap<String, String>> resultSelect =  selectUser.selectUser(CreateOrder.this);
        System.out.println("Result select = "+ resultSelect);
        HashMap<String, String> oneUser=  resultSelect.get(0);
        email = oneUser.get("email");
        System.out.println("Email from local db: " + email);

        createOrder.createOrder(addressFrom,addressTo,sentAt, recievedAt, price, distance, packSize, packType, packWeight, email);



        startActivity(new Intent(CreateOrder.this, MainActivity.class));
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}