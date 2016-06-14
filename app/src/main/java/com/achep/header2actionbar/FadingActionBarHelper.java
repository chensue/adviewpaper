/*
 * Copyright (C) 2013 AChep@xda <artemchep@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.achep.header2actionbar;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import org.jan.adviewpaper.demo.R;

/**
 * Created by AChep@xda <artemchep@gmail.com>
 */
public class FadingActionBarHelper {

    private static final String TAG = "FadingActionBarHelper";
    //默认的透明度最大值（255）
    private static final int MAX_ALPHA = 220;
    private int mAlpha = 255;
    private Drawable mDrawable;
    private boolean isAlphaLocked;

    private final ActionBar mActionBar;

    public FadingActionBarHelper(final ActionBar actionBar) {
        mActionBar = actionBar;
    }

    public FadingActionBarHelper(final ActionBar actionBar, final Drawable drawable) {
        mActionBar = actionBar;
        setActionBarBackgroundDrawable(drawable);
    }

    public void setActionBarBackgroundDrawable(Drawable drawable) {
        setActionBarBackgroundDrawable(drawable, true);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setActionBarBackgroundDrawable(Drawable drawable, boolean mutate) {
        mDrawable = mutate ? drawable.mutate() : drawable;
        mActionBar.setBackgroundDrawable(mDrawable);

        if (mAlpha == MAX_ALPHA) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                mAlpha = mDrawable.getAlpha();
        } else {
            setActionBarAlpha(mAlpha);
        }
    }

    /**
     * An {@link android.app.ActionBar} background drawable.
     *
     * @see #setActionBarBackgroundDrawable(android.graphics.drawable.Drawable)
     * @see #setActionBarAlpha(int)
     */
    public Drawable getActionBarBackgroundDrawable() {
        return mDrawable;
    }

    /**
     * Please use this method for global changes only!
     * This is helpful when you need to provide something like
     * Navigation drawer: lock ActionBar and set
     * {@link android.graphics.drawable.Drawable#setAlpha(int)}
     * to {@link #getActionBarBackgroundDrawable()} directly.
     *
     * @param alpha a value from 0 to 255
     * @see #getActionBarBackgroundDrawable()
     * @see #getActionBarAlpha()
     */
    public void setActionBarAlpha(int alpha) {
        if (mDrawable == null) {
            Log.w(TAG, "Set action bar background before setting the alpha level!");
            return;
        }
        if (!isAlphaLocked){
            mDrawable.setAlpha(alpha);
            View view = mActionBar.getCustomView();
            if(view!=null){
                //这里是对自定义actionbar的其他控件背景的处理，我这边就草草了事了
                if(alpha>=55){
                    view.findViewById(R.id.search_button).setBackgroundResource(R.drawable.search);
                    view.findViewById(R.id.refresh_button).setBackgroundResource(R.drawable.refresh);
                }else{
                    view.findViewById(R.id.search_button).setBackgroundResource(R.mipmap.skin_nav_icon_l_search_rev);
                    view.findViewById(R.id.refresh_button).setBackgroundResource(R.mipmap.skin_nav_icon_r_refresh_rev);
                }
                //注意！这边是真正设置背景透明度的地方
                if(view.getBackground()!=null&&alpha<=MAX_ALPHA){
                    view.getBackground().setAlpha(alpha);
                }
            }
        }
        mAlpha = alpha;
    }

    public int getActionBarAlpha() {
        return mAlpha;
    }

    /**
     * When ActionBar's alpha is locked {@link #setActionBarAlpha(int)}
     * won't change drawable\'s alpha (but will change {@link #getActionBarAlpha()} level)
     *
     * @param lock
     */
    public void setActionBarAlphaLocked(boolean lock) {

        // Update alpha level on unlock
        if (isAlphaLocked != (isAlphaLocked = lock) && !isAlphaLocked) {
            setActionBarAlpha(mAlpha);
        }
    }

    public boolean isActionBarAlphaLocked() {
        return isAlphaLocked;
    }
}
