package com.example.ecommercephone.Login_CreateAccount_2Factor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommercephone.R;
import java.sql.Timestamp;
import java.util.Date;
//******************UNDER CONSTRUCTION*******************//
public class Create_User_Account extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_account);

        //Create a Date object
        Date date = new Date(); // current date and time
        //Convert Date to Timestamp for SQL compatibility
        Timestamp timestamp = new Timestamp(date.getTime());

        //Create instances

        EditText inputtedFirstname = (EditText) findViewById(R.id.firstname_input);
        EditText inputtedLastname = (EditText) findViewById(R.id.lastname_input);
        EditText inputtedEmail = (EditText) findViewById(R.id.email_input);
        EditText inputtedPassword = (EditText) findViewById(R.id.password_input);
        Button createAccount = (Button) findViewById(R.id.Create_Account_button);
        String manufacturer = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        Context context = this;

        try{
            createAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("You just clicked the submit button");
                    //Declare variable
                    String firstname = inputtedFirstname.getText().toString();
                    String lastname = inputtedLastname.getText().toString();
                    String username = inputtedEmail.getText().toString();
                    String password = inputtedPassword.getText().toString();;

                    if (firstname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your Firstname", Toast.LENGTH_SHORT).show();
                    }
                    else if (lastname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your Lastname", Toast.LENGTH_SHORT).show();
                    }
                    else if (username.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please set your Password", Toast.LENGTH_SHORT).show();

                    }else {
                        com.example.ecommercephone.Login_CreateAccount_2Factor.LoginCredential loginInfo = new com.example.ecommercephone.Login_CreateAccount_2Factor.LoginCredential(
                                firstname,
                                lastname,
                                username,
                                password,
                                manufacturer,
                                model
                        );
                    }
                }
            });
        }catch(Exception e){
            System.out.println("Something went wrong. Error: " + e.getMessage());
        }

    }

}