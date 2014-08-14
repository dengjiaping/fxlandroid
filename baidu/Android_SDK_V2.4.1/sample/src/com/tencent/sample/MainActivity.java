//
//package com.tencent.sample;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.tencent.connect.UserInfo;
//import com.tencent.connect.auth.QQAuth;
//import com.tencent.connect.avatar.QQAvatar;
//import com.tencent.connect.common.IUiListener;
//import com.tencent.connect.common.UiError;
//import com.tencent.connect.share.QQShare;
//import com.tencent.open.SocialConstants;
//import com.tencent.open.SocialApi;
//import com.tencent.open.TaskExecute;
//import com.tencent.qzone.Albums;
//import com.tencent.qzone.Albums.AlbumSecurity;
//import com.tencent.qzone.Shuoshuo;
//import com.tencent.sample.AlbumListDialog.GetAlbumIdListenner;
//import com.tencent.sample.GetInviteParamsDialog.OnGetInviteParamsCompleteListener;
//import com.tencent.sample.GetVoiceParamsDialog.OnGetVoiceParamsCompleteListener;
//import com.tencent.sample.weiyun.WeiyunMainActivity;
//import com.tencent.t.Weibo;
//import com.tencent.utils.SystemUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.FileNotFoundException;
//import java.util.Date;
//
//public class MainActivity extends Activity implements OnClickListener {
//
//    private static final String APP_ID = "222222";
//    // private static final String APP_ID = "100363349";
//
//    private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,upload_photo,"
//            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
//
//    // private static final String SCOPE = "all";
//
//    private static final int REQUEST_UPLOAD_PIC = 1000;
//    private static final int REQUEST_ADD_PIC_T = 1001;
//    public static final int REQUEST_VOICE_PIC = 1002;
//    private static final int REQUEST_SET_AVATAR = 2;
//
//    private static final String SERVER_PREFS = "ServerPrefs";
//    private static final String SERVER_TYPE = "ServerType";
//
//    private ImageView mLoginButton;
//
//    private Albums mAlbums;
//    private QQAuth mQQAuth;
//    private UserInfo mUserInfo;
//    private SocialApi mSocialApi;
//    private TaskExecute mTaskExecute;
//    
//    private TextView mBaseMessageText;
//
//    private TextView mMessageText;
//
//    private Handler mHandler;
//
//    private Dialog mProgressDialog;
//
//    // set to 1 for test params
//    private int mNeedInputParams = 1;
//
//    private EditText mEtAppid = null;
//    
//    
//    private String TAG = "SDKSample";
//    
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
//        // 固定竖屏
//        setContentView(R.layout.activity_main);
//        final Context ctxContext = this.getApplicationContext();
//        mEtAppid = new EditText(this);
//        new AlertDialog.Builder(this)
//                .setTitle("请输入APP_ID")
//                .setCancelable(false)
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setView(mEtAppid)
//                .setPositiveButton("Commit",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                    int which) {
//                                String appid = mEtAppid.getText().toString()
//                                        .trim();
//                                if (!("".equals(appid))) {
//                                    mQQAuth = QQAuth.createInstance(appid, ctxContext);
//                                } else {
//                                	mQQAuth = QQAuth.createInstance(AppConstants.APP_ID, ctxContext);
//                                }
//                                mHandler = new Handler();
//                                mProgressDialog = new ProgressDialog(
//                                        MainActivity.this);
//                                initViews();
//                            }
//                        })
//                .setNegativeButton("Use Default",
//                        new android.content.DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                    int which) {
//                                // TODO Auto-generated method stub
//                            	mQQAuth = QQAuth.createInstance(AppConstants.APP_ID, ctxContext);
//                                mHandler = new Handler();
//                                mProgressDialog = new ProgressDialog(
//                                        MainActivity.this);
//                                initViews();
//                            }
//                        }).show();
//    }
//
//    private void initViews() {
//        mBaseMessageText = (TextView) findViewById(R.id.base_message_tv);
//        mMessageText = (TextView) findViewById(R.id.message_tv);
//
//        mLoginButton = (ImageView) findViewById(R.id.login_btn);
//        updateLoginButton();
//
//        mLoginButton.setOnClickListener(this);
//        findViewById(R.id.invite_btn).setOnClickListener(this);
//        findViewById(R.id.send_story_btn).setOnClickListener(this);
//        findViewById(R.id.ask_gift_btn).setOnClickListener(this);
//        findViewById(R.id.user_info_btn).setOnClickListener(this);
//        findViewById(R.id.vip_info_btn).setOnClickListener(this);
//        findViewById(R.id.vip_rich_info_btn).setOnClickListener(this);
//        findViewById(R.id.list_album_btn).setOnClickListener(this);
//        findViewById(R.id.add_share_btn).setOnClickListener(this);
//        findViewById(R.id.add_topic_btn).setOnClickListener(this);
//        findViewById(R.id.upload_pic_btn).setOnClickListener(this);
//        findViewById(R.id.add_album_btn).setOnClickListener(this);
//        findViewById(R.id.select_server_btn).setOnClickListener(this);
//        findViewById(R.id.set_avatar_btn).setOnClickListener(this);
//        findViewById(R.id.nick_tips_btn).setOnClickListener(this);
//        findViewById(R.id.intimate_friends_btn).setOnClickListener(this);
//        findViewById(R.id.add_pic_url_t).setOnClickListener(this);
//        findViewById(R.id.test_qq_btn).setOnClickListener(this);
//        findViewById(R.id.pk_brag_btn).setOnClickListener(this);
//        findViewById(R.id.check_login_btn).setOnClickListener(this);
//        findViewById(R.id.weiyun).setOnClickListener(this);
//        findViewById(R.id.grade_btn).setOnClickListener(this);
//        findViewById(R.id.list_photo_btn).setOnClickListener(this);
//        findViewById(R.id.voice_btn).setOnClickListener(this);
//		findViewById(R.id.reward_btn).setOnClickListener(this);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
//    	
//    	Log.v("sample","onActivityResult:" + requestCode + ", resultCode:"+resultCode);
//        if (data != null) {
//            if (requestCode == REQUEST_UPLOAD_PIC) {
//                doUploadPic(data.getData());
//            } else if (requestCode == REQUEST_SET_AVATAR) {
//                doSetAvatar(data.getData());
//            } else if (requestCode == REQUEST_ADD_PIC_T) {
//            	doSendPicWeibo(data.getData());
//            } else if (requestCode == REQUEST_VOICE_PIC) {
//                doVoicePhotoSelected(data.getData());
//            } 
//        }
//    }
//
//    /**
//     * 根据选择的数据类型，进入下一步，选择操作类型
//     * @param actionType
//     */
//    private void startWeiyun() {
//        Intent intent = new Intent(this, WeiyunMainActivity.class);
//        startActivity(intent);
//    }
//
//    private void updateLoginButton() {
//        mLoginButton
//                .setImageResource(mQQAuth.isSessionValid() ? R.drawable.com_tencent_open_logout
//                        : R.drawable.com_tencent_open_login);
//    }
//
//    @Override
//    public void onClick(View v) {
//        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//        switch (v.getId()) {
//            case R.id.login_btn:
//                onClickLogin();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.invite_btn:
//                onClickInvite();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.send_story_btn:
//                onClickStory();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.set_avatar_btn:// 设置头像
//                onClickSetAvatar();
//                break;
//
//            case R.id.ask_gift_btn:
//                onClickAskGift();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.grade_btn:
//                onClickAppGrade();
//                v.startAnimation(shake);
//                break;
//                
//            case R.id.user_info_btn:
//                onClickUserInfo();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.vip_info_btn:
//                onClickVipInfo();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.vip_rich_info_btn:
//                onClickVipRichInfo();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.add_pic_url_t:
//                onClickAddPicUrlTweet();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.test_qq_btn:
//                onClickShareToQQ();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.list_album_btn:
//                onClickListAlbum();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.list_photo_btn:
//    			onClickListPhoto();
//    			v.startAnimation(shake);
//    			break;
//
//            case R.id.add_share_btn:
//                onClickAddShare();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.add_topic_btn:
//                onClickAddTopic();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.upload_pic_btn:
//                onClickUploadPic();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.add_album_btn:
//                onClickAddAlbum();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.select_server_btn:
//                onClickSelectServer();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.nick_tips_btn:
//                onClickNickTips();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.intimate_friends_btn:
//                onClickIntimateFriends();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.pk_brag_btn:
//                onClickPkBrag();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.check_login_btn:
//                onClickCheckLogin();
//                v.startAnimation(shake);
//                break;
//
//            case R.id.weiyun:
//                onClickWeiyun();
//                break;
//                
//            case R.id.voice_btn:
//                onClickVoice();
//                break;
//				
//			case R.id.reward_btn:
//            	onClickReward();
//            	break;
//
//            default:
//                break;
//        }
//    }
//
//    private void onClickReward() {
//    	if (ready()) {
//	    	if (mTaskExecute == null) {
//	    		mTaskExecute = new TaskExecute(this, mQQAuth.getQQToken());
//	    	}
//	    	mTaskExecute.getTaskInfo(this,null,null);
//    	}
//    	//mTaskExecute.showWindow();
//	}
//
//
//
//    private void onClickWeiyun(){
//        if (ready()) {
//            startWeiyun();
//        }
//    }
//
//    
//
//    private void onClickCheckLogin() {
//        IUiListener listener = new BaseUiListener() {
//            @Override
//            protected void doComplete(Object values) {
//                showResult("IRequestListener.onComplete:", values.toString());
//            }
//        };
//        mQQAuth.login(this, SCOPE, listener);
//    }
//
//    private void onClickLogin() {
//    	if (mQQAuth.isSessionValid()) {
//    		mQQAuth.logout(this);
//    		updateLoginButton();
//    	} else {
//	    	IUiListener listener = new BaseUiListener() {
//	    		@Override
//	    		protected void doComplete(Object values) {
//	    		    updateLoginButton();
//	    		}
//	    	};
//	    	mQQAuth.login(this, "all", listener);
//    	}
//    }
//
//    private Bundle voiceBundle = new Bundle();
//    private void onClickVoice() {
//        Log.i("sample", "onClickVoice");
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            
//            if (mNeedInputParams == 1) {
//                showVoiceDialog();
//            } else {
//                if(voiceBundle == null){
//                    voiceBundle = new Bundle();
//                    // TODO keywords.
//                    voiceBundle.putString(SocialConstants.PARAM_APP_ICON,
//                            "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//                    voiceBundle.putString(SocialConstants.PARAM_APP_DESC,
//                            "AndroidSdk_1_3: invite description!");
//                    voiceBundle.putString(SocialConstants.PARAM_ACT, "进入应用");
//                }
//                
//                mSocialApi.voice(MainActivity.this, voiceBundle, new BaseUiListener());
//            }
//
//        }
//    }
//    
//    private void doVoicePhotoSelected(Uri uri){
//        if(uri != null){
//            Bitmap bitmap = null;  
//            try {  
//                ContentResolver resolver = getContentResolver();
//                bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri));  
//                //bitmap = resizeImage(originalBitmap, 200, 200);  
//            } catch (FileNotFoundException e) {  
//                e.printStackTrace();
//            }
//            
//            if(bitmap != null){
//                voiceBundle.putParcelable(SocialConstants.PARAM_IMG_DATA, bitmap);
//                showVoiceDialog();
//            }
//        }
//    }
//    
//    /**  
//     * 图片缩放  
//     * @param originalBitmap 原始的Bitmap  
//     * @param newWidth 自定义宽度  
//     * @param newHeight自定义高度  
//     * @return 缩放后的Bitmap  
//     */  
//    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){  
//        int width = originalBitmap.getWidth();  
//        int height = originalBitmap.getHeight();  
//        //定义欲转换成的宽、高  
////          int newWidth = 200;  
////          int newHeight = 200;  
//        //计算宽、高缩放率  
//        float scanleWidth = (float)newWidth/width;  
//        float scanleHeight = (float)newHeight/height;  
//        //创建操作图片用的matrix对象 Matrix  
//        Matrix matrix = new Matrix();  
//        // 缩放图片动作  
//        matrix.postScale(scanleWidth,scanleHeight);  
//        //旋转图片 动作  
//        //matrix.postRotate(45);  
//        // 创建新的图片Bitmap  
//        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);  
//        return resizedBitmap;  
//    }  
//    
//    private void showVoiceDialog(){
//        new GetVoiceParamsDialog(this, new OnGetVoiceParamsCompleteListener() {
//
//            @Override
//            public void onGetParamsComplete(Bundle params) {
//                Log.i("sample", "onClickVoice complete");
//                mSocialApi.voice(MainActivity.this, params, new BaseUiListener());
//            }
//            
//            public void selectPhoto(Bundle params) {
//                Log.i("sample", "onClickVoice complete");
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_VOICE_PIC);
//            }
//        }, voiceBundle).show();
//    }
//    
//    private void onClickInvite() {
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            if (mNeedInputParams == 1) {
//                new GetInviteParamsDialog(this, new OnGetInviteParamsCompleteListener() {
//
//                    @Override
//                    public void onGetParamsComplete(Bundle params) {
//                        mSocialApi.invite(MainActivity.this, params, new BaseUiListener());
//                    }
//                }).show();
//            } else {
//                Bundle params = new Bundle();
//                // TODO keywords.
//                params.putString(SocialConstants.PARAM_APP_ICON,
//                        "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//                params.putString(SocialConstants.PARAM_APP_DESC ,
//                        "AndroidSdk_2_0: voice description!");
//                params.putString(SocialConstants.PARAM_ACT, "进入应用");
//                
//                mSocialApi.voice(MainActivity.this, params, new BaseUiListener());
//            }
//        }
//    }
//
//    private boolean ready() {
//    	boolean ready = mQQAuth.isSessionValid();
//        if (!ready)
//            Toast.makeText(this, "login and get openId first, please!",
//                    Toast.LENGTH_SHORT).show();
//        return ready;
//    }
//
//    private void onClickStory() {
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            if (mNeedInputParams == 1) {
//                new GetStoryParamsDialog(this,
//                        new GetStoryParamsDialog.OnGetStoryParamsCompleteListener() {
//
//                            @Override
//                            public void onGetParamsComplete(Bundle params) {
//                                mSocialApi.story(MainActivity.this, params, new BaseUiListener());
//                            }
//                        }).show();
//            } else {
//                Bundle params = new Bundle();
//
//                params.putString(SocialConstants.PARAM_TITLE,
//                        "AndroidSdk_1_3:UiStory title");
//                params.putString(SocialConstants.PARAM_COMMENT,
//                        "AndroidSdk_1_3: UiStory comment");
//                params.putString(SocialConstants.PARAM_IMAGE,
//                        "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//                params.putString(SocialConstants.PARAM_SUMMARY,
//                        "AndroidSdk_1_3: UiStory summary");
//                params.putString(
//                        SocialConstants.PARAM_PLAY_URL,
//                        "http://player.youku.com/player.php/Type/Folder/"
//                                + "Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf");
//                params.putString(SocialConstants.PARAM_ACT, "进入应用");
//                mSocialApi.story(MainActivity.this, params, new BaseUiListener());
//            }
//        }
//    }
//    
//    private void onClickAskGift() {
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            if (mNeedInputParams == 1) {
//                new GetAskGiftParamsDialog(
//                        this,
//                        new GetAskGiftParamsDialog.OnGetAskGiftParamsCompleteListener() {
//                            @Override
//                            public void onGetParamsComplete(Bundle params) {
//                                if ("request".equals(params.getString(SocialConstants.PARAM_TYPE))) {
//                                    mSocialApi.ask(MainActivity.this, params, new BaseUiListener());
//                                } else {
//                                    mSocialApi.gift(MainActivity.this, params, new BaseUiListener());
//                                }
//                            }
//                        }).show();
//            } else {
//                Bundle params = new Bundle();
//                params.putString(SocialConstants.PARAM_TITLE, "title字段测试");
//                params.putString(SocialConstants.PARAM_SEND_MSG, "msg字段测试");
//                params.putString(SocialConstants.PARAM_IMG_URL,"http://i.gtimg.cn/qzonestyle/act/qzone_app_img/app888_888_75.png");
//                mSocialApi.ask(this, params, new BaseUiListener());
//            }
//        }
//    }
//    
//    private void onClickPkBrag() {
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            if (mNeedInputParams == 1) {
//                new GetPkBragParamsDialog(
//                        this,
//                        new GetPkBragParamsDialog.OnGetPkBragParamsCompleteListener() {
//
//                            @Override
//                            public void onGetParamsComplete(Bundle params) {
//                                if ("pk".equals((String) params.get(SocialConstants.PARAM_TYPE))) {
//                                    mSocialApi.challenge(MainActivity.this, params, new BaseUiListener());
//                                } else if ("brag".equals((String) params
//                                        .get(SocialConstants.PARAM_TYPE))) {
//                                    mSocialApi.brag(MainActivity.this, params, new BaseUiListener());
//                                }
//                            }
//                        }).show();
//            } else {
//                Bundle params = new Bundle();
//                params.putString(SocialConstants.PARAM_RECEIVER, "3B599FF138EE42DD7FE2234D3B89C44B");
//                params.putString(SocialConstants.PARAM_SEND_MSG, "向某某某发起挑战");
//                params.putString(SocialConstants.PARAM_IMG_URL, "http://i.gtimg.cn/qzonestyle/act/qzone_app_img/app888_888_75.png");
//                mSocialApi.challenge(MainActivity.this, params, new BaseUiListener());
//            }
//        }
//    }
//    
//    private void onClickAppGrade() {
//        if (ready()) {
//            if(mSocialApi == null) {
//                mSocialApi = new SocialApi(MainActivity.this, mQQAuth.getQQToken());
//            }
//            if (mNeedInputParams == 1) {
//                new GetGradeParamsDialog(this, new GetGradeParamsDialog.OnGetGradeParamsCompleteListener() {
//
//                    @Override
//                    public void onGetParamsComplete(Bundle params) {
//                        mSocialApi.grade(MainActivity.this, params, new BaseUiListener());
//                    }
//                }).show();
//            } else {
//                Bundle params = new Bundle();
//                params.putString("comment", "亲，给个好评吧");
//                mSocialApi.grade(MainActivity.this, params, new BaseUiListener());
//            }
//
//        }
//    }
//
//    private void onClickUserInfo() {
//    	if (mUserInfo == null) {
//    		mUserInfo = new UserInfo(this, mQQAuth.getQQToken());
//    	}
//    	
//    	mUserInfo.getUserInfo(new BaseUiListener());
//
////    	Weibo weibo = new Weibo(this, mQQAuth.getQQToken());
////    	weibo.getWeiboInfo(new BaseApiListener("get_weibo_info", false), null);
//        mProgressDialog.show();
//    }
//
//    private void onClickVipInfo() {
//    	if (mUserInfo == null) {
//    		mUserInfo = new UserInfo(this, mQQAuth.getQQToken());
//    	}
//    	
//    	mUserInfo.getVipUserInfo(new BaseUiListener());
//
//        mProgressDialog.show();
//    }
//
//    private void onClickIntimateFriends() {
//        if (ready()) {
//            final EditText inputServer = new EditText(this);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("请输入要获取的个数：").setIcon(android.R.drawable.ic_dialog_info)
//                    .setView(inputServer)
//                    .setNegativeButton("取消", null);
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//                	Weibo weibo = new Weibo(MainActivity.this, mQQAuth.getQQToken());
//                	int value = Integer.parseInt(inputServer.getText().toString());
//                	weibo.atFriends(value, new BaseUiListener());
//                }
//            }).show();
//        }
//    }
//
//    private void onClickNickTips() {
//        if (ready()) {
//            final EditText inputServer = new EditText(this);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("请输入要提示的昵称：").setIcon(android.R.drawable.ic_dialog_info)
//                    .setView(inputServer)
//                    .setNegativeButton("取消", null);
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//                    final EditText inputServer1 = new EditText(MainActivity.this);
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//                    builder1.setTitle("请输入要获取的个数：").setIcon(android.R.drawable.ic_dialog_info)
//                            .setView(inputServer1)
//                            .setNegativeButton("取消", null);
//                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//                        	Weibo weibo = new Weibo(MainActivity.this, mQQAuth.getQQToken());
//                        	int num = Integer.parseInt(inputServer1.getText().toString());
//                        	weibo.nickTips( inputServer.getText().toString(), num, new BaseUiListener());
//                        }
//                    }).show();
//                }
//            });
//            builder.show();
//        }
//    }
//
//    private void onClickVipRichInfo() {
//    	if (mUserInfo == null) {
//    		mUserInfo = new UserInfo(this, mQQAuth.getQQToken());
//    	}
//    	
//    	mUserInfo.getVipUserRichInfo(new BaseUiListener());
//        mProgressDialog.show();
//    }
//
//    private void onClickAddPicUrlTweet() {
//    	 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//         intent.addCategory(Intent.CATEGORY_OPENABLE);
//         intent.setType("image/*");
//         startActivityForResult(intent, REQUEST_ADD_PIC_T);
//    }
//
//    private void doSendPicWeibo(Uri uri) {
//    	Weibo weibo = new Weibo(this, mQQAuth.getQQToken());
//    	String filePath = SystemUtils.getRealPathFromUri(this, uri);
//    	weibo.sendPicText("互联sdk测试发送微博", filePath, new BaseUiListener());
//        mProgressDialog.show();
//    }
//    
//    private void onClickShareToQQ() {
//        //if (ready()) {
//            final Activity context = MainActivity.this;
//            new GetShareToQQParamsDialog(context,
//                    new GetShareToQQParamsDialog.ShareToQQParamsListener() {
//
//                        @Override
//                        public void onComplete(Bundle params) {
//                        	
//                        	//两种方式使用分享:1主线程，2子线程
//                        	
//                        	/*主线程
//                        	doShareToQQ(params);
//                        	*/
//                        	/*子线程*/
//                        	shareParams = params;                        	
//                        	Thread thread = new Thread(shareThread);
//                			thread.start();
//                        }
//                    }).show();
//
//       // }
//    }
// //***********模拟线程里面调用QQ分享*************************************
//    
//    Bundle shareParams = null;
//    
//    Handler shareHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			Log.v(TAG, "handleMessage:"+msg.arg1);  			 
//		}
//    };
//        
//    
//    //线程类，该类使用匿名内部类的方式进行声明
//    Runnable shareThread = new Runnable(){ 	    	 
//    	
//		public void run() {	 
//			Log.v(TAG, "Begin Thread");  
//			doShareToQQ(shareParams);
//			 
//			  
//			Message msg = shareHandler.obtainMessage();  
//			
//			//将Message对象加入到消息队列当中
//			shareHandler.sendMessage(msg);
//			  	
//		 
//		}
//    };
//    
//    private void doShareToQQ(Bundle params){
//    	QQShare qqShare = new QQShare(this, mQQAuth.getQQToken());
//    	String title = params.getString("title");
//    	String imageUrl = params.getString("imageUrl");
//    	String targetUrl = params.getString("targetUrl");
//    	String summary = params.getString("summary");
//    	String appName = params.getString("appName");
//    	qqShare.shareToQQ(this, title, imageUrl, targetUrl, summary, appName, new BaseUiListener(){
//	      	 protected void doComplete(JSONObject values) {
//	  		 showResult("shareToQQ:", "onComplete");
//	       }
//	
//	       @Override
//	       public void onError(UiError e) {
//	           showResult("shareToQQ:", "onError code:" + e.errorCode + ", msg:"
//	                   + e.errorMessage + ", detail:" + e.errorDetail);
//	       }
//	       @Override
//	       public void onCancel() {
//	           showResult("shareToQQ", "onCancel");
//	       }
//	  });    	
//    }
//    
//    //*************************************************
//    
//    private void onClickSetAvatar() {
//        if (ready()) {
//            Intent intent = new Intent();
//            // 开启Pictures画面Type设定为image
//            intent.setType("image/*");
//            // 使用Intent.ACTION_GET_CONTENT这个Action
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            // 取得相片后返回本画面
//            startActivityForResult(intent, REQUEST_SET_AVATAR);
//            // 在 onActivityResult 中调用 doSetAvatar
//        }
//    }
//
//    private void doSetAvatar(Uri uri) {
//        QQAvatar qqAvatar = new QQAvatar(this, mQQAuth.getQQToken());
//        qqAvatar.setAvatar(this, uri, new BaseUiListener(), R.anim.zoomout);
//    }
//
//    private void onClickListAlbum() {
//    	if (mAlbums == null) {
//    		mAlbums = new Albums(this, mQQAuth.getQQToken());
//    	}
//    	mAlbums.listAlbum(new BaseUiListener());
//        mProgressDialog.show();
//
//    }
//
//    private void onClickAddShare() {
//        if (ready()) {
//        	Shuoshuo shuoshuo = new Shuoshuo(this, mQQAuth.getQQToken());
//        	shuoshuo.addShare("QQ登陆SDK：Add_Share测试", "http://www.qq.com" + "#" + System.currentTimeMillis(),
//        			"QQ登陆SDK：测试comment" + new Date(), "QQ登陆SDK：测试summary", 
//        			"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif", 
//        			"http://player.youku.com/player.php/Type/Folder/Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf", 
//        			new BaseUiListener());
//
//            mProgressDialog.show();
//        }
//    }
//
//    private void onClickAddTopic() {
//        Shuoshuo shuo = new Shuoshuo(this, mQQAuth.getQQToken());
//        shuo.sendShuoShuo("互联测试:心情变化", new BaseUiListener());
//        mProgressDialog.show();
//    }
//
//    private void onClickUploadPic() {
//        if (ready()) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*");
//            startActivityForResult(intent, REQUEST_UPLOAD_PIC);
//        }
//    }
//
//    private void doUploadPic(Uri uri) {
//    	if (mAlbums == null) {
//    		mAlbums = new Albums(this, mQQAuth.getQQToken());
//    	}
//    	mAlbums.uploadPicture(SystemUtils.getRealPathFromUri(this, uri), 
//    			"QQ登陆SDK：UploadPic测试" + new Date(), 
//    			null, 
//    		    "114.1234567", 
//    		    "36.11223344", 
//    		    new BaseUiListener());
//        mProgressDialog.show();
//    }
//
//    private void onClickAddAlbum() {
//    	if (mAlbums == null) {
//    		mAlbums = new Albums(this, mQQAuth.getQQToken());
//    	}
//    	mAlbums.addAlbum("testAlbum", "This is my test album测试用", AlbumSecurity.needQuestion, "1加2等于几", 
//    			"3", new BaseUiListener());
//        mProgressDialog.show();
//    }
//
//    private static int SELECT_SERVER_DIALOG = 1;
//
//    private void onClickSelectServer() {
//        // 屏蔽切换环境入口
//        //showDialog(SELECT_SERVER_DIALOG);
//        Toast.makeText(this, "This entry is not open now.", Toast.LENGTH_SHORT).show();
//    }
//	
//    /**
//	 * 显示相册的流程
//	 * 1.首先获取用户的相册目录
//	 * 2.更具相册的albumid来获取特定相册的相片集合
//	 */
//	public void onClickListPhoto() {
//		// 显示相册
//		if (mAlbums == null) {
//    		mAlbums = new Albums(this, mQQAuth.getQQToken());
//    	}
//		mAlbums.listAlbum(new BaseUiListener() {
//						@Override
//						protected void doComplete(Object response) {
//							super.doComplete(response);
//							try {
//								new AlbumListDialog(MainActivity.this, (JSONObject)response, 
//										new GetAlbumIdListenner(){
//
//									@Override
//									public void onGetId(String albumId) {
//										getPhotos(albumId);
//									}
//									
//								}).show();
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//
//					});
//		mProgressDialog.show();
//	}
//	/**
//	 * 此处演示如何获取特定相册的相片
//	 * @param albumId
//	 */
//	private void getPhotos(String albumId) {
//		Log.i(TAG, "getPhoto id ="+albumId);
//		if (mAlbums == null) {
//    		mAlbums = new Albums(this, mQQAuth.getQQToken());
//    	}
//		mAlbums.listPhotos(albumId,new BaseUiListener(){
//			@Override
//			protected void doComplete(Object response) {
//				super.doComplete(response);
//				new PhotoListDialog(MainActivity.this, (JSONObject)response).show();
//			}
//		});
//		mProgressDialog.show();
//	}
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        // TODO Auto-generated method stub
//        Dialog dialog = null;
//        final CharSequence[] serverList = {
//                "正式环境", "体验环境"
//        };
//        SharedPreferences sp = getSharedPreferences(SERVER_PREFS, 0);
//        int serverType = sp.getInt(SERVER_TYPE, 0);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Option")
//                .setCancelable(true)
//                .setPositiveButton("取消",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                    int id) {
//                                dismissDialog(SELECT_SERVER_DIALOG);
//                            }
//                        });
//        builder.setSingleChoiceItems(serverList, serverType,
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            dismissDialog(SELECT_SERVER_DIALOG);
//                        }
//                        if (which == 1) {
//                            dismissDialog(SELECT_SERVER_DIALOG);
//                        }
//                    }
//                });
//
//        dialog = builder.create();
//        return dialog;
//    }
//
//    private void showResult(final String base, final String msg) {
//        mHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
//                mBaseMessageText.setText(base);
//                mMessageText.setText(msg);
//            }
//        });
//    }
//
//    private class BaseUiListener implements IUiListener {
//
//        @Override
//        public void onComplete(Object response) {
//        	showResult("onComplete:", response.toString());
//            doComplete(response);
//        }
//
//        protected void doComplete(Object values) {
//        	
//        }
//
//        @Override
//        public void onError(UiError e) {
//            showResult("onError:", "code:" + e.errorCode + ", msg:"
//                    + e.errorMessage + ", detail:" + e.errorDetail);
//        }
//
//        @Override
//        public void onCancel() {
//            showResult("onCancel", "");
//        }
//    }
//    
//}
