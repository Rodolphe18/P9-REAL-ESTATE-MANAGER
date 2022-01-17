package com.francotte.realestatemanager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.Utils;

import java.text.DecimalFormat;

public class LoanCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAmount, etRate, etPeriod;
    private TextView tvMonthlyPayment, tvTotalPayment;
    private Button btCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);

        initActivity();

    }

    private void initActivity() {

        etAmount = findViewById(R.id.et_amount_calculator_activity);
        etRate = findViewById(R.id.et_rate_calculator_activity);
        etPeriod = findViewById(R.id.et_period_calculator_activity);
        tvMonthlyPayment = findViewById(R.id.tv_result_payment_calculator_activity);
        tvTotalPayment = findViewById(R.id.tv_cost_calculator_activity);
        btCalculate = findViewById(R.id.bt_calculator_activity);
        btCalculate.setOnClickListener(this);
    }

    public void showLoanPayments() {

        double loanAmount = Integer.parseInt(etAmount.getText().toString());
        double interestRate = Double.parseDouble(String.valueOf(etRate.getText()));
        double loanPeriod = Integer.parseInt(etPeriod.getText().toString()) * 12;

        double monthlyPayment = Utils.calculateMonthlyPayment(interestRate, loanPeriod, loanAmount);
        double totalPayment = Utils.calculateTotalPayment(monthlyPayment, loanPeriod);

        tvMonthlyPayment.setText("Montant de vos mensualités : " + (new DecimalFormat("##.##").format(monthlyPayment)) + " €");
        tvTotalPayment.setText("Coût du crédit : " + (new DecimalFormat("##.##").format(totalPayment)) + " €");
    }

    @Override
    public void onClick(View view) {
        showLoanPayments();
    }
}