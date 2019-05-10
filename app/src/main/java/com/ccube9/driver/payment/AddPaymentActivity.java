package com.ccube9.driver.payment;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccube9.driver.R;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddPaymentActivity extends AppCompatActivity {
    ArrayList<String> listOfPattern=new ArrayList<String>();
    Button btn_credit_card;
    Button btn_paypal,btn_submit;
    EditText et_cvv,et_expiry_date,et_card_no;
    SweetAlertDialog dialog;
    TextView tv_title;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        initView();
        cardValidation();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }//onCreateClose

    public void cardValidation()
    {
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);



     //   et_card_no.addTextChangedListener(new CreditCardTextWatcher());

        et_card_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long number=Long.parseLong(s.toString());
               // if (isValid(number) ? "valid" : "invalid");
                Toast.makeText(AddPaymentActivity.this, ""+(isValid(number) ? "valid" : "invalid"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_expiry_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidDate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }//cardValidation


    public static boolean isValid(long number)
    {
        Log.i("dsdsdsd",""+(sumOfDoubleEvenPlace(number)));
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    public static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    public static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    public static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

    public static boolean isValidDate(String cardValidity) {
        if (!TextUtils.isEmpty(cardValidity) && cardValidity.length() == 5)
        {
            String month=cardValidity.substring(0,2);
            String year=cardValidity.substring(3,5);

            int monthpart=-1,yearpart=-1;

            try
            {
                monthpart=Integer.parseInt(month)-1;
                yearpart=Integer.parseInt(year);

                Calendar current = Calendar.getInstance();
                current.set(Calendar.DATE,1);
                current.set(Calendar.HOUR,12);
                current.set(Calendar.MINUTE,0);
                current.set(Calendar.SECOND,0);
                current.set(Calendar.MILLISECOND,0);

                Calendar validity=Calendar.getInstance();
                validity.set(Calendar.DATE,1);
                validity.set(Calendar.HOUR,12);
                validity.set(Calendar.MINUTE,0);
                validity.set(Calendar.SECOND,0);
                validity.set(Calendar.MILLISECOND,0);

                if(monthpart>-1&&monthpart<12&&yearpart>-1)
                {
                    validity.set(Calendar.MONTH,monthpart);
                    validity.set(Calendar.YEAR,yearpart+2000);
                }
                else
                    return false;

                Log.d("Util", "isValidDate: "+current.compareTo(validity));

                if(current.compareTo(validity)<=0)
                    return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public class CreditCardTextWatcher implements TextWatcher {

        public static final char SPACING_CHAR = '-'; // Using a Unicode character seems to stuff the logic up.

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }

        @Override
        public void afterTextChanged(final Editable s) {
            if (s.length() > 0) {
                long number = Long.parseLong(s.toString());

                System.out.println(number + " is " +
                        (isValid(number) ? "valid" : "invalid"));
                Toast.makeText(AddPaymentActivity.this, ""+(isValid(number) ? "valid" : "invalid"), Toast.LENGTH_SHORT).show();
                // Any changes we make to s in here will cause this method to be run again.  Thus we only make changes where they need to be made,
                // otherwise we'll be in an infinite loop.

            /*    // Delete any spacing characters that are out of place.
                for (int i=s.length()-1; i>=0; --i) {
                    if (s.charAt(i) == SPACING_CHAR  // There is a spacing char at this position ,
                            && (i+1 == s.length()    // And it's either the last digit in the string (bad),
                            || (i+1) % 5 != 0)) {    // Or the position is not meant to contain a spacing char?

                        s.delete(i,i+1);
                    }
                }

                // Insert any spacing characters that are missing.
                for (int i=14; i>=4; i-=5) {
                    if (i < s.length() && s.charAt(i) != SPACING_CHAR) {
                        s.insert(i, String.valueOf(SPACING_CHAR));
                    }
                }*/
            }
        }
    }
    public void initView()
    {
        tv_title=findViewById(R.id.tv_title);
        iv_back=findViewById(R.id.iv_back);
        tv_title.setText(getResources().getString(R.string.payment));
        btn_credit_card=findViewById(R.id.btn_credit_card);
        btn_paypal=findViewById(R.id.btn_paypal);
        et_cvv=findViewById(R.id.et_cvv);
        btn_submit=findViewById(R.id.btn_submit);
        et_expiry_date=findViewById(R.id.et_expiry_date);
        et_card_no=findViewById(R.id.et_card_no);
        dialog=new SweetAlertDialog(this);
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(getResources().getString(R.string.add_payment));
        dialog.setConfirmText(getResources().getString(R.string.yes));
        dialog.setCancelText(getResources().getString(R.string.skip));
        dialog.show();
       dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
           @Override
           public void onClick(SweetAlertDialog sweetAlertDialog) {
               dialog.dismiss();
           }
       });
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });


    }//initViewClose
}
