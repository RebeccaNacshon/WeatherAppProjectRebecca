package com.mycompany.rebecca_n.weatherapprebeccaproject.networking;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rebecca_N on 02/20/2017.

 */
public class VolleyManager {

    private static final int BYTES_PER_PIXEL = 4;
    private static final int SCREENS_TO_CACHE = 3;
    private static VolleyManager sharedInstance;

    /**
     * Generate Singleton
     *
     * @param context the calling context.
     * @return the single shared instance of volley manager.
     */
    public synchronized static VolleyManager getInstance(Context context) {

        if (sharedInstance == null)
            sharedInstance = new VolleyManager(context);
        return sharedInstance;
    }


    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleyManager(Context context) {

        requestQueue = getRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache(context));
    }

    /**
     * Ensure that the queue itself is a singleton
     *
     * @param context the calling context
     * @return the single RequestQueue
     */
    public RequestQueue getRequestQueue(Context context) {

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    // Write to an interface not to a class.
    public <T> void addRequest(Context context, Request<T> request) {

        getRequestQueue(context).add(request);
    }

    public void cancelAll(Context context, Object tag) {

        getRequestQueue(context).cancelAll(tag);
    }

    /**
     * Our custom implementation of the bitmap Lru (Least recently used) cache.
     * We will allocate a cache size as big as 32 bpp 3 screens. feel free to play and modify :)
     * Stores the cache as URL-BITMAP KV to efficiency fetch from either the cache or the network the image.
     */
    private class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        /**
         * Call this constructor as the size logic sits in the class and all we need is the calling context.
         *
         * @param context the calling context.
         */
        public BitmapLruCache(Context context) {

            this(getRequiredCacheSize(context));
        }

        /**
         * must be overridden from LruCache Class. won't be called by us as the size logic sits in the class.
         *
         * @param maxSize you already know...
         */
        public BitmapLruCache(int maxSize) {

            super(maxSize);
        }

        @Override
        public Bitmap getBitmap(String url) {

            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {

            put(url, bitmap);
        }
    }

    /**
     * Changes from one device to another (bigger and denser devices usually have bigger storage)
     *
     * @param context
     * @return
     */
    private static int getRequiredCacheSize(Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height * width * BYTES_PER_PIXEL * SCREENS_TO_CACHE;
    }
}
