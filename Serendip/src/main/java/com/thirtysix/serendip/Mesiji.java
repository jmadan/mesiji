package com.thirtysix.serendip;

import android.app.Application;
import android.content.res.Configuration;

public class Mesiji extends Application {
	private static Mesiji singleton;

	public static Mesiji getinstance() {
		return singleton;
	}

	@Override
	public final void onCreate() {
		super.onCreate();
		singleton = this;
	}

	@Override
	public final void onTerminate() {
		super.onTerminate();
	}

	@Override
	public final void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public final void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
