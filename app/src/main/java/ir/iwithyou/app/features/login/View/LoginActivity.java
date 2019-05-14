package ir.iwithyou.app.features.login.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ir.iwithyou.app.R;
import ir.iwithyou.app.features.login.Model.ClientLogin;
import ir.iwithyou.app.features.login.Model.RetrofitGeneratorLogin;
import ir.iwithyou.app.features.login.Model.SendModeltoLogin;
import ir.iwithyou.app.features.register.Model.ClientRegister;
import ir.iwithyou.app.features.register.Model.RetrofitGeneratorRegister;
import ir.iwithyou.app.features.register.Model.SendModelForRegister;
import ir.iwithyou.app.features.register.View.RegisterActivity;
import ir.iwithyou.app.pojo.login.Login;
import ir.iwithyou.app.pojo.register.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText log_Email;
    EditText log_Password;
    Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_Email = findViewById(R.id.log_Email);
        log_Password = findViewById(R.id.log_Password);
        login_btn = findViewById(R.id.login_btn);

        //TODO: Intent to Register Activity
        //TODO: Link to Forgot Passwordk?
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SendModeltoLogin sendModeltoLogin = new SendModeltoLogin(
                        log_Email.getText().toString(),
                        log_Password.getText().toString()

                );
                ClientLogin client = RetrofitGeneratorLogin.createService(ClientLogin.class);

                final Call<Login> login = client.login(sendModeltoLogin);
                login.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {

                        String foo = response.body().getStatus().toString();
                        Toast.makeText(LoginActivity.this, foo, Toast.LENGTH_SHORT).show();

                        //TODO: USE FIREBASE, faranesh.com
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {

                    }
                });


            }
        });


    }
}
