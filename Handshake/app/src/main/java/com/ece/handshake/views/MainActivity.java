package com.ece.handshake.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.handshake.events.NewAccountEvent;
import com.ece.handshake.events.TwitterLoginEvent;
import com.ece.handshake.helper.MediaPlatformHelper;
import com.ece.handshake.R;
import com.ece.handshake.helper.SharedPreferencesManager;
import com.ece.handshake.helper.UriDeserializer;
import com.ece.handshake.helper.UriSerializer;
import com.ece.handshake.model.data.Connection;
import com.ece.handshake.model.data.NfcTransfer;
import com.ece.handshake.model.data.PhoneContact;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.model.db.AccountsDataSource;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NfcAdapter.CreateNdefMessageCallback, GoogleApiClient.ConnectionCallbacks{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "SScUHc4jfNsporiWTQcKMerBx";
    private static final String TWITTER_SECRET = "acMW4LyCZpcL0AWGmgfu1MhYkVMQM2ETn7KCeuzKygD2JC1DIi";

    private GoogleApiClient mGoogleApiClient;

    public CallbackManager mCallbackManager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private NavigationView mNavView;
    private void initPlatforms() {
        FacebookSdk.sdkInitialize(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlatformHelper.initializePlatformImgMapping(this);
        checkIsLoggedIn();
        initPlatforms();
        initCallbacks();

        getFragmentManager().beginTransaction().replace(R.id.container, new BumpFragment()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);

        mNavView = (NavigationView) findViewById(R.id.navigation);
        mNavView.setNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //MapFragment frag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
       // frag.getMapAsync(this);

        /************ NFC Setup ****************/
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    private void initCallbacks() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile prof2 = Profile.getCurrentProfile();

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                        Profile.setCurrentProfile(newProfile);
                        if (newProfile != null) {
                            Toast.makeText(getApplicationContext(), "Succesfull Facebook Login", Toast.LENGTH_LONG).show();
                            Profile profile = Profile.getCurrentProfile();
                            SMAccount account = new SMAccount(profile.getFirstName(), profile.getLastName(), getString(R.string.platform_name_facebook), profile.getLinkUri(), profile.getProfilePictureUri(64, 64), AccessToken.getCurrentAccessToken().getToken());
                           // EventBus.getDefault().post(new NewAccountEvent(account));
                            addAccount(account);

                        }
                        this.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled Facebook Login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Failed Facebook Login: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
//64206
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO:Replace hardcoded requestcodes
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addAccount(SMAccount account) {
        AccountsDataSource source = new AccountsDataSource(this);
        source.insertConnectedAccount(account);
    }

    TwitterLoginButton mTwitterLoginButton;

    @SuppressWarnings("unused")
    public void onEvent(TwitterLoginEvent event) {
        mTwitterLoginButton = new TwitterLoginButton(this);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(getApplicationContext(), "Successful Twitter login", Toast.LENGTH_LONG).show();
                Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        User user = result.data;
                        final String[] name = user.name.split(" ");
                        String firstName, lastName;
                        firstName = name[0];

                        if(name.length == 2)
                            lastName = name[1];
                        else
                            lastName = "";

                        final Uri profileUri = Uri.parse(String.format("twitter://user?user_id=%s", Long.toString(user.getId())));
                        final Uri profilePicUri = Uri.parse(user.profileImageUrl);
                        SMAccount account = new SMAccount(firstName, lastName, getString(R.string.platform_name_twitter), profileUri, profilePicUri, "");
                        addAccount(account);
                        //EventBus.getDefault().post(new NewAccountEvent(account));
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getApplicationContext(), "Failed Twitter login", Toast.LENGTH_LONG).show();

            }
        });
        mTwitterLoginButton.performClick();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        AccountsDataSource source = new AccountsDataSource(this);
        ArrayList<SMAccount> accounts = source.getConnectedAccounts();

        PhoneContact contact = new PhoneContact("Zeki", "Sherif", "5195691196", "zsherif@gmail.com", null);
        NfcTransfer transfer = new NfcTransfer(accounts, contact);


        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create();
        String data = gson.toJson(transfer);

        return new NdefMessage(
                new NdefRecord[] { createMimeRecord("application/vnd.com.ece.handshake.beam", data.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                         */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());

            Intent i = new Intent();
            i.setAction("do_nothing");
            setIntent(i);
        }
    }
    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        System.out.println(new String(msg.getRecords()[0].getPayload()));


        String data = new String(msg.getRecords()[0].getPayload());
        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create();

        Type fooType = new TypeToken<NfcTransfer>() {}.getType();

        NfcTransfer recievedTransfer = gson.fromJson(data, fooType);

        //ArrayList<Connection> pendingConnections = new ArrayList<>();
        //pendingConnections.addAll(Arrays.asList((gson.fromJson(data, Connection[].class))));
        /*AccountsDataSource source = new AccountsDataSource(this);
        source.insertPendingConnection(pendingConnections);*/
        AccountsDataSource source = new AccountsDataSource(this);
        ArrayList<Connection> connections = new ArrayList<>();
        connections.addAll(recievedTransfer.accounts);
        connections.add(recievedTransfer.contact);
        source.insertPendingConnection(connections);
        //int i = 4;
    }

    /**
     * Creates a custom MIME type encapsulated in an NDEF record
     *
     * @param mimeType : type of data
     */
    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        FragmentManager fragmentManager = getFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.drawer_item_home:
                fragmentManager.beginTransaction().replace(R.id.container, new BumpFragment()).commit();
                break;
            case R.id.drawer_item_connected_accounts:
                fragmentManager.beginTransaction().replace(R.id.container, new AccountsFragment()).commit();
                break;
            case R.id.drawer_item_nfc_profiles:
                fragmentManager.beginTransaction().replace(R.id.container, new ProfileTabFragment()).commit();
                break;
            case R.id.drawer_item_pending_connections:
                fragmentManager.beginTransaction().replace(R.id.container, new PendingConnectionsFragment()).commit();
                break;
            case R.id.drawer_item_maps:
                fragmentManager.beginTransaction().replace(R.id.container, new GMapsFragment()).commit();
                break;
            case R.id.drawer_item_settings:
                //TODO: Make Settings page
                getFragmentManager().beginTransaction().replace(R.id.container, new PreferencesFragment()).commit();
                break;
            case R.id.drawer_item_logout:
                //TODO: Logout of gplus and take user to login page
                break;
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {/*
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.handshake, menu);
            restoreActionBar();
            return true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    private void checkIsLoggedIn() {
        if (!SharedPreferencesManager.isUserLoggedIn(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Plus.API)
                .addScope(new Scope("profile"))
                .build();
        mGoogleApiClient.connect();
    }

    public String getDeviceId() {
        final String deviceId = Settings.Secure.getString( this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }


    private void facebookHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ece.handshake",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        final Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        CircleImageView profilePic = (CircleImageView) mNavView.findViewById(R.id.profilePic);
        TextView email = (TextView) mNavView.findViewById(R.id.email);
        TextView name = (TextView) mNavView.findViewById(R.id.name);
        final RelativeLayout background = (RelativeLayout) mNavView.findViewById(R.id.cover);

        Person.Cover cover = person.getCover();
        if(person.hasCover()) {
            Picasso.with(this).load(person.getCover().getCoverPhoto().getUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    background.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        name.setText(person.getName().getGivenName() + person.getName().getFamilyName());
        email.setText(Plus.AccountApi.getAccountName(mGoogleApiClient));
        Picasso.with(this).load(person.getImage().getUrl()).into(profilePic);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}
