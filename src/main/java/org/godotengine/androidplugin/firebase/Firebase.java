/**
 * Copyright 2020 Yalcin Ata. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.godotengine.androidplugin.firebase;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.FirebaseApp;

import org.godotengine.godot.Dictionary;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Firebase extends GodotPlugin {

    // private static Activity activity = null;
    private static Godot godot = null;
    private static JSONObject firebaseConfig = new JSONObject();
    private FirebaseApp firebaseApp = null;
    private FrameLayout layout = null;

    public static final String TAG = "Firebase";

    public Firebase(Godot godot) {
        super(godot);
        this.godot = godot;
    }

    public static JSONObject getConfig() {
        return firebaseConfig;
    }

    @Override
    public String getPluginName() {
        return "Firebase";
    }

    @Override
    public List<String> getPluginMethods() {
        List<String> methods = new ArrayList<String>();

        methods.add("init");

        // ===== AdMob
        methods.add("admob_banner_is_loaded");
        methods.add("admob_banner_show");
        methods.add("admob_banner_get_size");
        methods.add("admob_interstitial_show");
        methods.add("admob_rewarded_video_show");
        methods.add("admob_rewarded_video_request_status");

        // ===== Analytics
        methods.add("analytics_send_custom");
        methods.add("analytics_send_events");

        // ===== Authentication
        methods.add("authentication_get_id_token");
        // ===== Google
        methods.add("authentication_google_sign_in");
        methods.add("authentication_google_sign_out");
        methods.add("authentication_google_is_connected");
        methods.add("authentication_google_get_user");

        // ===== Firestore
        methods.add("firestore_load_document");
        methods.add("firestore_add_document");
        methods.add("firestore_set_document_data");

        // ===== Storage
        methods.add("storage_upload");
        methods.add("storage_download");

        // ===== InAppMessaging
        // Nothing to implement, just adding implementation 'com.google.firebase:firebase-inappmessaging-display:19.0.3' to gradle.conf enables it, done!

        // ===== Cloud Messaging
        methods.add("cloudmessaging_subscribe_to_topic");
        methods.add("cloudmessaging_unsubscribe_from_topic");

        return methods;
    }

    public void init(final int script_id) {

        Utils.logDebug("Firebase.init() called");

        godot.runOnUiThread(new Runnable() {
            public void run() {
                String fileName = "res://godot-firebase-config.json";
                String data = Utils.readFromFile(fileName, godot);
                data = data.replaceAll("\\s+", "");
                if (data == null || data.isEmpty()) {
                    Utils.logDebug("read data null or empty? " + data);
                } else {
                    Utils.logDebug("read data not empty");
                }

                Utils.setScriptInstance(script_id);
                initFirebase(data);
            }
        });
    }

    private void initFirebase(final String data) {
        Utils.logDebug("Firebase initializing");

        JSONObject config = null;
        firebaseApp = FirebaseApp.initializeApp(godot);

        if (data.length() <= 0) {
            Utils.logDebug("Firebase initialized.");
            return;
        }

        try {
            config = new JSONObject(data);
            firebaseConfig = config;
        } catch (JSONException e) {
            Utils.logDebug("JSON Parse error: " + e.toString());
        }

        // ===== AdMob
        if (config.optBoolean("AdMob", false)) {
            Utils.logDebug("AdMob initializing");
            AdMob.getInstance(godot).init(firebaseApp, layout);
        }

        // ===== Analytics
        if (config.optBoolean("Analytics", false)) {
            Utils.logDebug("Analytics initializing");
            Analytics.getInstance(godot).init(firebaseApp);
        }

        // ===== Authentication
        if (config.optBoolean("Authentication", false)) {
            Utils.logDebug("Authentication initializing");
            Authentication.getInstance(godot).init(firebaseApp);
        }

        // ===== Firestore
        if (config.optBoolean("Firestore", false)) {
            Utils.logDebug("Firestore initializing");
            Firestore.getInstance(godot).init(firebaseApp);
        }

        // ===== Storage
        if (config.optBoolean("Storage", false)) {
            Utils.logDebug("Storage initializing");
            Storage.getInstance(godot).init(firebaseApp, godot);
        }

        // ===== InAppMessaging
        // Just adding implementation 'com.google.firebase:firebase-inappmessaging-display:19.0.3' to gradle.conf enables it, done!
        {
            Utils.logDebug("In-App Messaging initialized");
        }

        // ===== Cloud Messaging
        if (config.optBoolean("CloudMessaging", false)) {
            Utils.logDebug("CloudMessaging initializing");
            CloudMessaging.getInstance(godot).init(firebaseApp);
        }

        Utils.logDebug("Firebase initialized");
    }

    // ===== AdMob
    public boolean admob_banner_is_loaded() {
        return AdMob.getInstance(godot).bannerIsLoaded();
    }

    public void admob_banner_show(final boolean show) {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                AdMob.getInstance(godot).bannerShow(show);
            }
        });
    }

    public Dictionary admob_banner_get_size() {
        return AdMob.getInstance(godot).bannerGetSize();
    }

    public void admob_interstitial_show() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                AdMob.getInstance(godot).interstitialShow();
            }
        });
    }

    public void admob_rewarded_video_show() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                AdMob.getInstance(godot).rewardedVideoShow();
            }
        });
    }

    public void admob_rewarded_video_request_status() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                AdMob.getInstance(godot).rewardedVideoRequestStatus();
            }
        });
    }
    // ===== AdMob ====================================================================================================

    // ===== Analytics
    public void analytics_send_custom(final String key, final String value) {
        if (key.length() <= 0 || value.length() <= 0) {
            return;
        }

        godot.runOnUiThread(new Runnable() {
            public void run() {
                Analytics.getInstance(godot).sendCustom(key, value);
            }
        });
    }

    public void analytics_send_events(final String key, final Dictionary data) {
        if (key.length() <= 0) {
            return;
        }

        godot.runOnUiThread(new Runnable() {
            public void run() {
                Analytics.getInstance(godot).sendEvents(key, data);
            }
        });
    }
    // ===== Analytics ================================================================================================

    // ===== Authentication
    public void authentication_get_id_token() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                Authentication.getInstance(godot).getIdToken();
            }
        });
    }

    // ----- Google
    public void authentication_google_sign_in() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                Authentication.getInstance(godot).signIn();
            }
        });
    }

    public void authentication_google_sign_out() {
        godot.runOnUiThread(new Runnable() {
            public void run() {
                Authentication.getInstance(godot).signOut();
            }
        });
    }

    public boolean authentication_google_is_connected() {
        return Authentication.getInstance(godot).isConnected();
    }

    public String authentication_google_get_user() {
        return Authentication.getInstance(godot).getUserDetails();
    }
    // ===== Authentication ===========================================================================================

    // ===== Firestore
    public void firestore_add_document(final String name, final Dictionary data) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Firestore.getInstance(godot).addDocument(name, data);
            }
        });
    }

    public void firestore_load_document(final String name) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Firestore.getInstance(godot).loadDocuments(name, -1);
            }
        });
    }

    public void firestore_set_document_data(final String colName, final String docName, final Dictionary data) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Firestore.getInstance(godot).setDocumentData(colName, docName, data);
            }
        });
    }
    // ===== Firestore ================================================================================================

    // ===== Storage
    public void storage_upload(final String fileName) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Storage.getInstance(godot).upload(fileName);
            }
        });
    }

    public void storage_download(final String fileName) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Storage.getInstance(godot).download(fileName);
            }
        });
    }
    // ===== Storage ==================================================================================================

    // ===== Cloud Messaging
    public void cloudmessaging_subscribe_to_topic(final String topicName) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CloudMessaging.getInstance(godot).subscribeToTopic(topicName);
            }
        });
    }

    public void cloudmessaging_unsubscribe_from_topic(final String topicName) {
        godot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CloudMessaging.getInstance(godot).unsubscribeFromTopic(topicName);
            }
        });
    }
    // ===== Cloud Messaging ==========================================================================================

    // Forwarded callbacks you can reimplement, as SDKs often need them.
    public void onMainActivityResult(int requestCode, int resultCode, Intent data) {
        Authentication.getInstance(godot).onActivityResult(requestCode, resultCode, data);
    }

    public void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public void onMainPause() {
        AdMob.getInstance(godot).onPause();
        Authentication.getInstance(godot).onPause();
    }

    public void onMainResume() {
        AdMob.getInstance(godot).onResume();
        Authentication.getInstance(godot).onResume();
    }

    public void onMainDestroy() {
        AdMob.getInstance(godot).onStop();
        Authentication.getInstance(godot).onStop();
    }

    @Override
    public View onMainCreate(Activity activity) {
        layout = new FrameLayout(activity);
        return layout;
    }

    public void onGLDrawFrame(GL10 gl) {
    }

    public void onGLSurfaceChanged(GL10 gl, int width, int height) {
    } // Singletons will always miss first 'onGLSurfaceChanged' call.
}
