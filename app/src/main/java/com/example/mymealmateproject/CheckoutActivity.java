package com.example.mymealmateproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private EditText userName, userAddress, userCity, userZip;
    private RadioGroup paymentGroup, deliveryGroup;
    private RadioButton selectedPayment, selectedDelivery;
    private NumberPicker quantityPicker1, quantityPicker2;
    private TextView foodName1, foodName2, foodPrice1, foodPrice2, deliveryChargeValue, totalPriceValue;
    private Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Views
        userName = findViewById(R.id.user_name);
        userAddress = findViewById(R.id.user_address);
        userCity = findViewById(R.id.user_city);
        userZip = findViewById(R.id.user_zip);
        paymentGroup = findViewById(R.id.paymentGroup);
        deliveryGroup = findViewById(R.id.deliveryGroup);
        quantityPicker1 = findViewById(R.id.quantity_picker_1);
        quantityPicker2 = findViewById(R.id.quantity_picker_2);
        foodName1 = findViewById(R.id.food_name_1);
        foodName2 = findViewById(R.id.food_name_2);
        foodPrice1 = findViewById(R.id.food_price_1);
        foodPrice2 = findViewById(R.id.food_price_2);
        deliveryChargeValue = findViewById(R.id.delivery_charge_value);
        totalPriceValue = findViewById(R.id.total_price_value);
        placeOrderButton = findViewById(R.id.place_order_button);

        // Setting default values
        foodName1.setText("Margherita Pizza");
        foodName2.setText("Caesar Salad");
        foodPrice1.setText("$12.99");
        foodPrice2.setText("$6.99");
        deliveryChargeValue.setText("$5.00");

        // Set default quantities
        quantityPicker1.setValue(1);
        quantityPicker2.setValue(1);

        // Calculate Total Price
        calculateTotalPrice();

        // Place Order Button Action
        placeOrderButton.setOnClickListener(v -> {
            if (isInputValid()) {
                String address = userAddress.getText().toString();
                String city = userCity.getText().toString();
                String zip = userZip.getText().toString();

                // Get selected payment and delivery method
                int selectedPaymentId = paymentGroup.getCheckedRadioButtonId();
                selectedPayment = findViewById(selectedPaymentId);
                String paymentMethod = selectedPayment.getText().toString();

                int selectedDeliveryId = deliveryGroup.getCheckedRadioButtonId();
                selectedDelivery = findViewById(selectedDeliveryId);
                String deliveryMethod = selectedDelivery.getText().toString();

                // Handle Order Placement (You can send the order to a server or database here)
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate the input fields
    private boolean isInputValid() {
        return !userName.getText().toString().isEmpty() &&
                !userAddress.getText().toString().isEmpty() &&
                !userCity.getText().toString().isEmpty() &&
                !userZip.getText().toString().isEmpty() &&
                paymentGroup.getCheckedRadioButtonId() != -1 &&
                deliveryGroup.getCheckedRadioButtonId() != -1;
    }

    // Method to calculate the total price
    private void calculateTotalPrice() {
        // Get item quantities and prices
        int quantity1 = quantityPicker1.getValue();
        int quantity2 = quantityPicker2.getValue();
        double price1 = 12.99;  // Margherita Pizza price
        double price2 = 6.99;   // Caesar Salad price
        double deliveryCharge = 5.00;

        // Calculate the total price
        double totalPrice = (quantity1 * price1) + (quantity2 * price2) + deliveryCharge;

        // Update the total price in the UI
        totalPriceValue.setText("$" + totalPrice);
    }
}
