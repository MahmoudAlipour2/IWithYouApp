package ir.iwithyou.app.features.login.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import ir.iwithyou.app.R;
import ir.iwithyou.app.features.Menu.View.MainMenuActivity;
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
    TextView tv_RegisterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Hawk.init(getApplicationContext()).build();

        if (Hawk.contains("token")) {
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
        } else {
            init();
            login_btn.setOnClickListener(v -> {
                String lEmail = log_Email.getText().toString();
                String lPassword = log_Password.getText().toString();

                if (lEmail.isEmpty() || lPassword.isEmpty()) {
                    Toast.makeText(this, "باکس خالی را پر کنید!", Toast.LENGTH_SHORT).show();
                } else {
                    final SendModeltoLogin sendModeltoLogin = new SendModeltoLogin(
                            lEmail,
                            lPassword
                    );
                    ClientLogin client = RetrofitGeneratorLogin.createService(ClientLogin.class);
                    Call<Login> login = client.login(sendModeltoLogin);
                    login.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.isSuccessful()) {
                                deleteHawk();
                                Boolean status = response.body().getStatus();
                                Hawk.put("status", status);
                                if (status==true){
                                    String id = response.body().getResponse().getId();
                                    Hawk.put("id", id);
                                    String firstName = response.body().getResponse().getFirstName();
                                    Hawk.put("firstName", firstName);
                                    String lastName = response.body().getResponse().getLastName();
                                    Hawk.put("lastName", lastName);
                                    String userName = response.body().getResponse().getUserName();
                                    Hawk.put("userName", userName);
                                    String token = response.body().getResponse().getToken();
                                    Hawk.put("token", token);
                                    Toast.makeText(LoginActivity.this, "خوش آمدید!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                    startActivity(intent) ;
                                }else {
                                    String message = response.body().getMessage();
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "اطلاعات ورودی صحیح نمی باشد!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            tv_RegisterIntent.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    }

    public void init() {
        log_Email = findViewById(R.id.log_Email);
        log_Password = findViewById(R.id.log_Password);
        login_btn = findViewById(R.id.login_btn);
        tv_RegisterIntent = findViewById(R.id.tv_RegisterIntent);
    }

    public void deleteHawk() {
        Hawk.delete("status");
        Hawk.delete("id");
        Hawk.delete("firstName");
        Hawk.delete("lastName");
        Hawk.delete("userName");
        Hawk.delete("token");

    }
}
