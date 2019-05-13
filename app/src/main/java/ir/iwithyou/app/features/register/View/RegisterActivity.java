package ir.iwithyou.app.features.register.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ir.iwithyou.app.R;
import ir.iwithyou.app.features.register.Model.ClientRegister;
import ir.iwithyou.app.features.register.Model.RetrofitGeneratorRegister;
import ir.iwithyou.app.features.register.Model.SendModelForRegister;
import ir.iwithyou.app.pojo.register.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText reg_Email;
    EditText reg_Password;
    EditText reg_FirstName;
    EditText reg_LastName;
    EditText reg_PhoneNumber;
    Button register_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_Email = findViewById(R.id.reg_Email);
        reg_Password = findViewById(R.id.reg_Password);
        reg_FirstName = findViewById(R.id.reg_FirstName);
        reg_LastName = findViewById(R.id.reg_LastName);
        reg_PhoneNumber = findViewById(R.id.reg_PhoneNumber);
        register_btn = findViewById(R.id.register_btn);


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SendModelForRegister sendModelForRegister = new SendModelForRegister(
                        reg_Email.getText().toString(),
                        reg_Password.getText().toString(),
                        reg_FirstName.getText().toString(),
                        reg_LastName.getText().toString(),
                        reg_PhoneNumber.getText().toString()

                );
                ClientRegister client = RetrofitGeneratorRegister.createService(ClientRegister.class);

                final Call<Register> registerCall = client.register(sendModelForRegister);
                registerCall.enqueue(new Callback<Register>() {
                    @Override
                    public void onResponse(Call<Register> call, Response<Register> response) {



                        String message = response.body().getMessage();
                        String rspns = response.body().getResponse().toString();
                        String status = response.body().getStatus().toString();
                        String statusCode = response.body().getStatusCode().toString();

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                        //TODO: IF statusCode=0 , Active: Intent to Camera activity.
                    }

                    @Override
                    public void onFailure(Call<Register> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }
}
