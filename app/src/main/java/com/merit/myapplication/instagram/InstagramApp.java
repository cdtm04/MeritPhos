package com.merit.myapplication.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.merit.myapplication.activities.MainActivity;
import com.merit.myapplication.loaddata.Utils;
import com.merit.myapplication.models.Caption;
import com.merit.myapplication.models.Comment;
import com.merit.myapplication.models.Image;
import com.merit.myapplication.models.Location;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 */
public class InstagramApp {
    public static InstagramApp instagramApp;

    private InstagramSession mSession;
    private DialogLoginInstagram mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;

    private HashMap<String, String> userInfo = new HashMap<String, String>();
    private ArrayList<Post> mAllRecentMedias = new ArrayList<>();
    private ArrayList<Post> mNewFeeds = new ArrayList<>();
    private ArrayList<User> mFollowers = new ArrayList<>();
    private String mNextPageFollowers;
    private ArrayList<User> mFollowings = new ArrayList<>();
    private String mNextPageFollowings;

    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private Context mCtx;

    private String mClientId;
    private String mClientSecret;

    public static int WHAT_FINALIZE = 0;
    public static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */

    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    private static final String TAG = "InstagramAPI";

    public static final String TAG_DATA = "data";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_COUNTS = "counts";
    public static final String TAG_FOLLOWS = "follows";
    public static final String TAG_FOLLOWED_BY = "followed_by";
    public static final String TAG_MEDIA = "media";
    public static final String TAG_FULL_NAME = "full_name";
    public static final String TAG_META = "meta";
    public static final String TAG_CODE = "code";

    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_LOWRESOLUTION = "low_resolution";
    public static final String TAG_STANDARDRESOLUTION = "standard_resolution";
    public static final String TAG_URL = "url";
    public static final String TAG_NEXT_URL = "next_url";
    public static final String TAG_CREATEDTIME = "created_time";
    public static final String TAG_CAPTION = "caption";
    public static final String TAG_LIKES = "likes";
    public static final String TAG_COUNT = "count";
    public static final String TAG_COMMENTS = "comments";
    public static final String TAG_TEXT = "text";
    public static final String TAG_FROM = "from";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_WIDTH = "width";
    public static final String TAG_HEIGHT = "height";
    public static final String TAG_FILTER = "filter";
    public static final String TAG_LINK = "link";
    public static final String TAG_TYPE = "type";
    public static final String TAG_USER = "user";
    public static final String TAG_NAME = "name";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_VIDEO = "videos";
    public static final String TAG_ISLIKED = "user_has_liked";
    public static final String TAG_PAGNINATION = "pagination";

    private boolean isfetchAllRecentMediasRunning = false;

    public InstagramApp(Context context, String clientId, String clientSecret, String callbackUrl) {
        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl
                + "&grant_type=authorization_code";
        mAuthUrl = AUTH_URL
                + "?client_id="
                + clientId
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&display=touch&scope=likes+comments+relationships";

        DialogLoginInstagram.OnDialogLoginInstagram listener = new DialogLoginInstagram.OnDialogLoginInstagram() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new DialogLoginInstagram(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    // URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    // urlConnection.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);
                    writer.flush();
                    String response = Utils.streamToString(urlConnection.getInputStream());
                    Log.i(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString(
                            "username");
                    String name = jsonObj.getJSONObject("user").getString(
                            "full_name");

                    mSession.storeAccessToken(mAccessToken, id, user, name);

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    // get Profile Informations
    public void fetchProfileInformation(final Handler handler) {
        final ProgressDialog mProgressFetchUserName = new ProgressDialog(MainActivity.mainContext);
        mProgressFetchUserName.setMessage("Loading info...");
        mProgressFetchUserName.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL(API_URL + "/users/" + mSession.getId()
                            + "/?access_token=" + mAccessToken);

                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection.getInputStream());
                    System.out.println(response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    // String name = jsonObj.getJSONObject("data").getString(
                    // "full_name");
                    // String bio =
                    // jsonObj.getJSONObject("data").getString("bio");
                    // Log.i(TAG, "Got name: " + name + ", bio [" + bio + "]");
                    JSONObject data_obj = jsonObj.getJSONObject(TAG_DATA);
                    userInfo.put(TAG_ID, data_obj.getString(TAG_ID));

                    userInfo.put(TAG_PROFILE_PICTURE,
                            data_obj.getString(TAG_PROFILE_PICTURE));

                    userInfo.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));

                    userInfo.put(TAG_BIO, data_obj.getString(TAG_BIO));

                    userInfo.put(TAG_WEBSITE, data_obj.getString(TAG_WEBSITE));

                    JSONObject counts_obj = data_obj.getJSONObject(TAG_COUNTS);

                    userInfo.put(TAG_FOLLOWS, counts_obj.getString(TAG_FOLLOWS));

                    userInfo.put(TAG_FOLLOWED_BY,
                            counts_obj.getString(TAG_FOLLOWED_BY));

                    userInfo.put(TAG_MEDIA, counts_obj.getString(TAG_MEDIA));

                    userInfo.put(TAG_FULL_NAME,
                            data_obj.getString(TAG_FULL_NAME));

                    JSONObject meta_obj = jsonObj.getJSONObject(TAG_META);

                    userInfo.put(TAG_CODE, meta_obj.getString(TAG_CODE));
                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }
                mProgressFetchUserName.dismiss();
                handler.sendMessage(handler.obtainMessage(what, 2, 0));
            }
        }.start();

    }

    // get posted media information from json
    private synchronized void getMediaFromJSON(Handler handler, String mUrl, ArrayList<Post> postArrayList) {
        int what = WHAT_FINALIZE;
        try {
            JSONParser jsonParser = new JSONParser();
                    /*
                    JSONObject jsonObject = jsonParser
                            .getJSONFromUrlByGet("https://api.instagram.com/v1/users/"
                                    + userInfo.get(InstagramApp.TAG_ID)
                                    + "/media/recent/?client_id="
                                    + ApplicationData.CLIENT_ID
                                    + "&count="
                                    + userInfo.get(InstagramApp.TAG_COUNTS));*/
            JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(mUrl + mSession.getAccessToken());
            String a = mUrl + mSession.getAccessToken();
            JSONArray data = jsonObject.getJSONArray(TAG_DATA);
            for (int data_i = 0; data_i < data.length(); data_i++) {
                JSONObject data_obj = data.getJSONObject(data_i);
                Post post = new Post();

                // get location
                try {
                    JSONObject location_obj = data_obj.getJSONObject(TAG_LOCATION);
                    String idLocation = location_obj.getString(TAG_ID);
                    String nameLocation = location_obj.getString(TAG_NAME);
                    double latitude = location_obj.getDouble(TAG_LATITUDE);
                    double longitude = location_obj.getDouble(TAG_LONGITUDE);
                    Location location = new Location(idLocation, nameLocation, latitude, longitude);
                    post.setLocationOfPost(location);
                } catch (Exception ex) {
                    post.setLocationOfPost(null);
                }


                // get comments
                try {
                    JSONObject comts_obj = data_obj.getJSONObject(TAG_COMMENTS);

                    // get count comment
                    post.setCountComments(comts_obj.getInt(TAG_COUNT));

                    JSONArray comts_data = comts_obj.getJSONArray(TAG_DATA);
                    ArrayList<Comment> comments = new ArrayList<Comment>();
                    for (int comts_data_i = 0; comts_data_i < comts_data.length(); comts_data_i++) {
                        JSONObject comt = comts_data.getJSONObject(comts_data_i);
                        String createdTime = comt.getString(TAG_CREATEDTIME);
                        String text = comt.getString(TAG_TEXT);
                        String id = comt.getString(TAG_ID);

                        JSONObject from = comt.getJSONObject(TAG_FROM);
                        String userName = from.getString(TAG_USERNAME);
                        String profilePicture = from.getString(TAG_PROFILE_PICTURE);
                        String fullName = from.getString(TAG_FULL_NAME);
                        String userId = from.getString(TAG_ID);

                        User user = new User(userName, profilePicture, userId, fullName, null, null);

                        Comment comment = new Comment(id, createdTime, text, user);
                        comments.add(comment);
                    }
                    post.setComments(comments);
                } catch (Exception ex) {
                    post.setComments(null);
                }

                // get filter
                String filter = data_obj.getString(TAG_FILTER);
                post.setFilter(filter);

                // get createdTime
                String createdTime = data_obj.getString(TAG_CREATEDTIME);
                long foo = Long.parseLong(createdTime) * 1000;
                Date date = new Date(foo);
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM/dd/yyyy");
                post.setCreatedTime(formatter.format(date));

                // get link
                String link = data_obj.getString(TAG_LINK);
                post.setLink(link);

                // get likes
                try {
                    JSONObject likes_obj = data_obj.getJSONObject(TAG_LIKES);

                    // get count likes
                    post.setCountLikes(likes_obj.getInt(TAG_COUNT));

                    JSONArray likes_data = likes_obj.getJSONArray(TAG_DATA);
                    ArrayList<User> likes = new ArrayList<User>();
                    for (int likes_data_i = 0; likes_data_i < likes_data.length(); likes_data_i++) {
                        JSONObject user = likes_data.getJSONObject(likes_data_i);
                        String userName = user.getString(TAG_USERNAME);
                        String profilePicture = user.getString(TAG_PROFILE_PICTURE);
                        String fullName = user.getString(TAG_FULL_NAME);
                        String userId = user.getString(TAG_ID);
                        User userLike = new User(userName, profilePicture, userId, fullName, null, null);
                        likes.add(userLike);
                    }
                    post.setLikes(likes);
                } catch (Exception ex) {
                    post.setLikes(null);
                }

                // get image
                JSONObject images_obj = data_obj.getJSONObject(TAG_IMAGES);
                JSONObject lowresolution_obj = images_obj.getJSONObject(TAG_STANDARDRESOLUTION);
                String url = lowresolution_obj.getString(TAG_URL);
                int width = lowresolution_obj.getInt(TAG_WIDTH);
                int height = lowresolution_obj.getInt(TAG_HEIGHT);
                Image image = new Image(url, height, width);
                post.setImage(image);

                // get video
                try {
                    JSONObject videos_obj = data_obj.getJSONObject(TAG_VIDEO);
                    JSONObject video_obj = videos_obj.getJSONObject(TAG_STANDARDRESOLUTION);
                    String video = video_obj.getString(TAG_URL);
                    post.setVideo(video);
                } catch (Exception ex) {
                    post.setVideo(null);
                }

                // get caption
                try {
                    JSONObject caption_obj = data_obj.getJSONObject(TAG_CAPTION);
                    String textOfCaption = caption_obj.getString(TAG_TEXT);
                    String createdTimeOfCaption = caption_obj.getString(TAG_CREATEDTIME);
                    String idOfCaption = caption_obj.getString(TAG_ID);
                    JSONObject userOfCaption_obj = caption_obj.getJSONObject(TAG_FROM);
                    User userOfCaption = new User(userOfCaption_obj.getString(TAG_USERNAME), userOfCaption_obj.getString(TAG_PROFILE_PICTURE), userOfCaption_obj.getString(TAG_ID), userOfCaption_obj.getString(TAG_FULL_NAME), null, null);
                    Caption captionOfPost = new Caption(idOfCaption, createdTimeOfCaption, textOfCaption, userOfCaption);
                    post.setCaptionOfPost(captionOfPost);
                } catch (Exception ex) {
                    post.setCaptionOfPost(null);
                }

                // get type
                String type = data_obj.getString(TAG_TYPE);
                post.setType(type);

                // get id
                String id = data_obj.getString(TAG_ID);
                post.setId(id);

                // get user
                JSONObject user_obj = data_obj.getJSONObject(TAG_USER);
                String userName = user_obj.getString(TAG_USERNAME);
                String profilePicture = user_obj.getString(TAG_PROFILE_PICTURE);
                String fullName = user_obj.getString(TAG_FULL_NAME);
                String userId = user_obj.getString(TAG_ID);
                User user = new User(userName, profilePicture, userId, fullName, null, null);
                post.setUserOfPost(user);

                // get isLiked
                try {
                    boolean isLiked = data_obj.getBoolean(TAG_ISLIKED);
                    post.setIsLiked(isLiked);
                } catch (Exception ex) {
                    post.setIsLiked(false);
                }


                postArrayList.add(post);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            what = WHAT_ERROR;
        }
        handler.sendEmptyMessage(what);
    }

    // get All Recent Medias
    public void fetchAllRecentMedias(final Handler handler) {
        final ProgressDialog mDialog = ProgressDialog.show(MainActivity.mainContext, "", "Loading media...");
        mDialog.setCancelable(true);
        final String mUrl = "https://api.instagram.com/v1/users/self/media/recent/?access_token=";
        mAllRecentMedias.clear();

        new Thread(new Runnable() {

            @Override
            public void run() {
                getMediaFromJSON(handler, mUrl, mAllRecentMedias);
                mDialog.dismiss();
            }

        }).start();

    }

    public void fetchNewFeeds(final Handler handler) {
        final ProgressDialog mDialog = ProgressDialog.show(MainActivity.mainContext, "", "Loading new feeds...");
        mDialog.setCancelable(true);
        final String mUrl = "https://api.instagram.com/v1/users/self/feed?access_token=";
        mNewFeeds.clear();

        new Thread(new Runnable() {

            @Override
            public void run() {
                getMediaFromJSON(handler, mUrl, mNewFeeds);
                mDialog.dismiss();
            }
        }).start();
    }

    // get list user information from JSON
    private synchronized void getUserFromJSON(Handler handler, String mUrl, boolean isGetFollower) {
        int what = WHAT_FINALIZE;
        try {
            // URL url = new URL(mTokenUrl + "&code=" + code);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(mUrl);

            // try to get next page url
            try {
                JSONObject pagination_obj = jsonObject.getJSONObject(TAG_PAGNINATION);
                if (isGetFollower) mNextPageFollowers = pagination_obj.getString(TAG_NEXT_URL);
                else mNextPageFollowings = pagination_obj.getString(TAG_NEXT_URL);
            } catch (Exception ex) {
                if (isGetFollower) mNextPageFollowers = null;
                else mNextPageFollowings = null;
            }

            // get datas
            JSONArray data = jsonObject.getJSONArray(TAG_DATA);
            for (int data_i = 0; data_i < data.length(); data_i++) {
                JSONObject data_obj = data.getJSONObject(data_i);
                String str_id = data_obj.getString(TAG_ID);
                String avatar = data_obj.getString(TAG_PROFILE_PICTURE);
                String fullName = data_obj.getString(TAG_FULL_NAME);
                String userName = data_obj.getString(TAG_USERNAME);
                User user = new User(userName, avatar, str_id, fullName, null, null);

                if (isGetFollower) mFollowers.add(user);
                else mFollowings.add(user);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            what = WHAT_ERROR;
        }
        mDialog.dismiss();
        handler.sendEmptyMessage(what);
    }

    public void fetchRelationship(final Handler handler, final boolean isGetFollower) {
        String url = "";
        String mDialogTitle = "";
        if (isGetFollower) {
            url = "https://api.instagram.com/v1/users/self/followed-by?access_token=" + mSession.getAccessToken();
            mDialogTitle = "Loading followers...";
            mFollowers.clear();
        } else {
            url = "https://api.instagram.com/v1/users/self/follows?access_token=" + mSession.getAccessToken();
            mDialogTitle = "Loading following...";
            mFollowings.clear();
        }
        final String mUrl = url;

        final ProgressDialog mDialog = ProgressDialog.show(MainActivity.mainContext, "", mDialogTitle);
        mDialog.setCancelable(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                getUserFromJSON(handler, mUrl, isGetFollower);
                mDialog.dismiss();
            }
        }).start();
    }

    public void fetchNextPageRelationship(final Handler handler, final boolean isGetFollower) {
        String url = "";
        if (isGetFollower) {
            url = mNextPageFollowers;
        } else {
            url = mNextPageFollowings;
        }
        final String mUrl = url;

        final ProgressDialog mDialog = ProgressDialog.show(MainActivity.mainContext, "", "Loading more...");
        mDialog.setCancelable(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                getUserFromJSON(handler, mUrl, isGetFollower);
                mDialog.dismiss();
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                // fetchUserName();
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };

    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public ArrayList<Post> getAllRecentMedias() {
        return mAllRecentMedias;
    }

    public ArrayList<Post> getNewFeeds() {
        return mNewFeeds;
    }

    public ArrayList<User> getFollowers() {
        return mFollowers;
    }

    public ArrayList<User> getFollowings() {
        return mFollowings;
    }

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }

    public String getId() {
        return mSession.getId();
    }

    public String getName() {
        return mSession.getName();
    }

    public String getTOken() {
        return mSession.getAccessToken();
    }

    public void authorize() {
        // Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        // webAuthIntent.setData(Uri.parse(AUTH_URL));
        // mCtx.startActivity(webAuthIntent);
        mDialog.show();
    }


    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }


}