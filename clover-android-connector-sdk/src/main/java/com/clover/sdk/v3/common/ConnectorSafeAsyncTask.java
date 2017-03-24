package com.clover.sdk.v3.common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

public abstract class ConnectorSafeAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
  private static final String TAG = ConnectorSafeAsyncTask.class.getSimpleName();
  private WeakReference<Context> mContextWeakReference;
  private WeakReference<Fragment> mFragmentWeakReference;

  public ConnectorSafeAsyncTask(Context context) {
    this.mContextWeakReference = new WeakReference(context);
  }

  public ConnectorSafeAsyncTask(Fragment fragment) {
    this.mFragmentWeakReference = new WeakReference(fragment);
  }

  protected final void onPostExecute(Result result) {
    super.onPostExecute(result);
    if(this.mContextWeakReference != null) {
      this.onPostExecuteContext(result);
    }

    if(this.mFragmentWeakReference != null) {
      this.onPostExecuteFragment(result);
    }

  }

  private void onPostExecuteContext(Result result) {
    Context context = (Context)this.mContextWeakReference.get();
    Log.i(TAG, "onPostExecuteContext context: " + context.getClass().getSimpleName());
    if(context instanceof Activity) {
      Activity activity1 = (Activity)context;
      Log.i(TAG, "onPostExecuteContext isFinishing: " + activity1.isFinishing());
      if(!activity1.isFinishing()) {
        this.onSafePostExecute(result);
      }
    } else {
      this.onSafePostExecute(result);
    }
  }

  private void onPostExecuteFragment(Result result) {
    Fragment fragment = (Fragment)this.mFragmentWeakReference.get();
    String fragmentName = fragment.getClass().getSimpleName();
    Log.i(TAG, "onPostExecuteFragment fragment: " + fragmentName);
    Log.i(TAG, "onPostExecuteFragment calling onSafePostExecute  isDetached: " + fragment.isDetached());
    if(!fragment.isDetached()) {
      this.onSafePostExecute(result);
    }
  }

  protected void onSafePostExecute(Result result) {
  }

  public Context getContext() {
    if(this.mContextWeakReference != null) {
      return (Context)this.mContextWeakReference.get();
    } else {
      if(this.mFragmentWeakReference != null) {
        Fragment fragment = (Fragment)this.mFragmentWeakReference.get();
        if(fragment != null) {
          return fragment.getActivity();
        }
      }
      return null;
    }
  }

  public Fragment getFragment() {
    return this.mFragmentWeakReference.get();
  }
}

