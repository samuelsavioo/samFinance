# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\samue\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard/index.html

# Keep Retrofit and OkHttp
-keepattributes Signature, InnerClasses, AnnotationDefault
-keep class retrofit2.** { *; }
-keep @retrofit2.http.** interface * { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

# Keep GSON
-keep class com.google.gson.** { *; }
-keep class com.example.samfinance.network.** { *; }
