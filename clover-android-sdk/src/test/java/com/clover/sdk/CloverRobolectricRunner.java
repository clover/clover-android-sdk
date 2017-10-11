package com.clover.sdk;

import android.os.Build;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.ReflectionHelpers;

import java.io.File;

/**
 * RobolectricRunner override for apps and lib combined so that robolectric finds the manifest file and uses the sdk version
 * for all clover apps and libraries.
 */

public class CloverRobolectricRunner extends RobolectricTestRunner {

  // Build output location for Android Studio 2.2 and older
  private static final String BUILD_OUTPUT = "build/intermediates";

  // Build output location for Android Studio 2.3 and newer
  private static final String BUILD_OUTPUT_APP_LEVEL = "app/build/intermediates";

  public CloverRobolectricRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
  }

  @Override
  protected AndroidManifest getAppManifest(Config config) {
    super.getAppManifest(config);

    final String buildType = getType(config);
    final String buildFlavor = getFlavor(config);

    FileFsFile res = FileFsFile.from(BUILD_OUTPUT, "/res/merged", buildFlavor, buildType);
    FileFsFile assets = FileFsFile.from(BUILD_OUTPUT, "assets", buildFlavor, buildType);
    FileFsFile manifest = FileFsFile.from(BUILD_OUTPUT, "manifests", "full", buildFlavor, buildType, "AndroidManifest.xml");

    if (!manifest.exists()) {
      manifest = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "manifests", "full", buildFlavor, buildType, "AndroidManifest.xml");
      if (!manifest.exists()) {
        //This is where module manifest will be found.
        manifest = FileFsFile.from(BUILD_OUTPUT, "manifests", "aapt", buildFlavor, buildType, "AndroidManifest.xml");
      }
    }

    if (!res.exists()) {
      res = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "/res/merged", buildFlavor, buildType);
    }

    if (!assets.exists()) {
      assets = FileFsFile.from(BUILD_OUTPUT_APP_LEVEL, "assets", buildFlavor, buildType);
    }

    config.constants();

    AndroidManifest androidManifest = new AndroidManifest(manifest, res, assets) {

      /*
      @Override
      public Class getRClass() {
        return R.class;
      } */

      public int getTargetSdkVersion() {
        return Build.VERSION_CODES.KITKAT;
      }

    };

    return androidManifest;
  }

  private String getBuildOutputDir(Config config) {
    return config.buildDir() + File.separator + "intermediates";
  }

  private String getType(Config config) {
    try {
      return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
    } catch (Throwable e) {
      return null;
    }
  }

  private String getFlavor(Config config) {
    try {
      return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
    } catch (Throwable e) {
      return null;
    }
  }

  private String getAbiSplit(Config config) {
    try {
      return config.abiSplit();
    } catch (Throwable e) {
      return null;
    }
  }

  private String getPackageName(Config config) {
    try {
      final String packageName = config.packageName();
      if (packageName != null && !packageName.isEmpty()) {
        return packageName;
      } else {
        return ReflectionHelpers.getStaticField(config.constants(), "APPLICATION_ID");
      }
    } catch (Throwable e) {
      return null;
    }
  }

}
