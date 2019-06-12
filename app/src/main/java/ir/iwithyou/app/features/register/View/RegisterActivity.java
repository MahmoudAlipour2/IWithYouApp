package ir.iwithyou.app.features.register.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import ir.iwithyou.app.R;
import ir.iwithyou.app.features.login.View.LoginActivity;
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
    String rEmail;
    String rPassword;
    String rFirstName;
    String rLastName;
    String rPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        register_btn.setOnClickListener(v -> {
            rEmail = reg_Email.getText().toString();
            rPassword = reg_Password.getText().toString();
            rFirstName = reg_FirstName.getText().toString();
            rLastName = reg_LastName.getText().toString();
            rPhoneNumber = reg_PhoneNumber.getText().toString();
            if (rEmail.isEmpty() || rFirstName.isEmpty() || rLastName.isEmpty() || rPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "لطفا فیلدهای خالی را پر کنید!", Toast.LENGTH_SHORT).show();

            }else {
                final SendModelForRegister sendModelForRegister = new SendModelForRegister(
                        rEmail,
                        rPassword,
                        rFirstName,
                        rLastName,
                        rPhoneNumber
                );
                ClientRegister client = RetrofitGeneratorRegister.createService(ClientRegister.class);

                final Call<Register> registerCall = client.register(sendModelForRegister);
                registerCall.enqueue(new Callback<Register>() {
                    @Override
                    public void onResponse(Call<Register> call, Response<Register> response) {
                        String message = response.body().getMessage();
                        String rspn = response.body().getResponse().toString();
                        String status = response.body().getStatus().toString();
                        int statusCode = response.body().getStatusCode();
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                        switch (statusCode) {
                            case 0:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 100:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 101:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 102:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            case 103:
                                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                finish();
                                startActivity(getIntent());
                        }
                    }

                    @Override
                    public void onFailure(Call<Register> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Check your connection or proxy!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void init() {
        reg_Email = findViewById(R.id.reg_Email);
        reg_Password = findViewById(R.id.reg_Password);
        reg_FirstName = findViewById(R.id.reg_FirstName);
        reg_LastName = findViewById(R.id.reg_LastName);
        reg_PhoneNumber = findViewById(R.id.reg_PhoneNumber);
        register_btn = findViewById(R.id.register_btn);


    }
}
