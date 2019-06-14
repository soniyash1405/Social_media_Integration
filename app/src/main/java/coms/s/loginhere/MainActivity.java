package coms.s.loginhere;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import android.util.Log;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginbtn;
    private CallbackManager clbck;
    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);
        loginbtn =  findViewById(R.id.loginbutton);

        clbck = CallbackManager.Factory.create();
        loginbtn.setReadPermissions(Arrays.asList("email", "public_profile"));
        checkLoginStatus();
        loginbtn.registerCallback(clbck, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                new TwitterAuthClient().requestEmail(session, new Callback<String>() {

                    @Override
                    public void success(Result<String> result) {
                        final String emailid = result.data;
                        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(false,true,false).enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                User data=result.data;
                               String profileurlhttp= data.profileImageUrl.replace("_normal","");
                              String profileurl= profileurlhttp.replace("http","https");
                                login(session,emailid,profileurl);
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Toast.makeText(MainActivity.this, "Cant get profilepic", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(MainActivity.this, "Cant get email", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(TwitterSession session, String data, String profileurl)
    {
        String username = session.getUserName();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("email", data);
        intent.putExtra("profileurl", profileurl);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        clbck.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(currentAccessToken==null){
                Toast.makeText(MainActivity.this,"User Logged Out",Toast.LENGTH_LONG).show();
            }
            else
                loadProfile(currentAccessToken);
        }
    };

    private void loadProfile(AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String newemail=object.getString("email");
                    String id=object.getString("id");
                    String imgURL="https://graph.facebook.com/"+id+ "/picture?type=normal";

                    Intent intent2 = new Intent(MainActivity.this, HomeFacebook.class);
                    intent2.putExtra("firstnamefb",first_name );
                    intent2.putExtra("lastnamefb",last_name);
                    intent2.putExtra("emailfb",newemail );
                    intent2.putExtra("imageurlfb",imgURL );
                    startActivity(intent2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadProfile(AccessToken.getCurrentAccessToken());
        }
    }
}
