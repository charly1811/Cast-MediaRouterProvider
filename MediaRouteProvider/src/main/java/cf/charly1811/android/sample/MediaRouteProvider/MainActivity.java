/*
   Copyright 2014 Charles-Eugene LOUBAO

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package cf.charly1811.android.sample.MediaRouteProvider;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;


public class MainActivity extends ActionBarActivity {
    /**
     * This sample shows how to add the Cast Button to the action bar using MediaRouterProvider
     * @see "https://developers.google.com/cast/docs/android_sender"
     */
    public static final String TAG = MainActivity.class.getSimpleName();
    String APP_ID = "REPLACE_WITH_YOUR_APP_ID";

    MediaRouter mediaRouter;
    MediaRouteSelector mediaRouteSelector;
    MediaRouter.Callback mMediaRouterCallback;
    CastDevice mCastDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaRouter = MediaRouter.getInstance(this);
        mediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(APP_ID))
                .build();

        mMediaRouterCallback = new MediaRouter.Callback() {
            @Override
            public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
                super.onRouteSelected(router, route);
                Log.d(TAG, "Connected to "+ route.getName());
                mCastDevice = CastDevice.getFromBundle(route.getExtras());
                Toast.makeText(getApplicationContext(), "Connected to "+route.getName(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
                super.onRouteUnselected(router, route);
                mCastDevice = null;
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mediaRouter.addCallback(mediaRouteSelector,mMediaRouterCallback,MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if(isFinishing())
        {
            mediaRouter.removeCallback(mMediaRouterCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem media_route_menu_item = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider provider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(media_route_menu_item);
        provider.setRouteSelector(mediaRouteSelector);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
}
