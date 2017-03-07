# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/malin/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript inter
# class:
#-keepclassmembers class fqcn.of.javascript.inter.for.webview {
#   public *;
#}

#-------------------------------压缩-------------------------------
# 代码压缩级别
-optimizationpasses 5
# 使用大小写混合
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
# 混淆时预校验
-dontpreverify
# 记录日志
-verbose
# 混淆时所用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-------------------------------压缩-------------------------------

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-ignorewarnings

#保护本地代码
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

#--------------------org.apache.http---------------------------------------------------
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontnote org.apache.http.**

-keep class android.net.http.** { *; }
-dontwarn android.net.http.**
-dontnote android.net.http.**
#--------------------org.apache.http---------------------------------------------------


-keep class com.google.**
-dontwarn com.google.**

-keep class org.robovm.apple.**
-dontwarn org.robovm.apple.**
-dontnote org.robovm.apple.**


-keep class sun.security.ssl.SSLContextImpl
-dontwarn sun.security.ssl.SSLContextImpl
-dontnote sun.security.ssl.SSLContextImpl

-keep class com.android.org.conscrypt.SSLParametersImpl
-dontwarn com.android.org.conscrypt.SSLParametersImpl
-dontnote com.android.org.conscrypt.SSLParametersImpl

-keep class org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontwarn org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontnote org.apache.harmony.xnet.provider.jsse.SSLParametersImpl


-keep class java.util.Optional
-dontwarn java.util.Optional
-dontnote java.util.Optional

-keep class sun.misc.Unsafe
-dontwarn sun.misc.Unsafe


-dontwarn sun.**
-keep class sun.**
-dontnote sun.**

-keep class sun.misc.Unsafe
-dontwarn sun.misc.Unsafe
-dontnote sun.misc.Unsafe



#-------------------------------RxJava-------------------------------
#http://www.on1024.com/2016/01/13/%E5%8D%87%E7%BA%A7%E5%88%B0%20RxAndroid%201.x.x%20%E4%B9%8B%E5%90%8E%E6%B7%B7%E6%B7%86%E8%A6%81%E6%B3%A8%E6%84%8F%E7%9A%84/
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontnote rx.**
#-------------------------------RxJava-------------------------------


#-------------------------------retrofit2-------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn okio.**
#-------------------------------retrofit2-------------------------------


#-------------------------------okhttp3-------------------------------
 -keep class okhttp3.** {*; }
 -keep interface okhttp3.** {*; }
 -dontwarn okhttp3.**
#-------------------------------okhttp3-------------------------------


#R
-keep public class meizhi.meizhi.malin.R$*{
		public static final int *;
}

#BuildConfig
-keep class meizhi.meizhi.malin.BuildConfig { *; }
#Binder
-keep public class * extends android.os.Binder

##实体类不参与混淆
-keep class meizhi.meizhi.malin.network.bean.** { *; }

##自定义控件不参与混淆
-keep class meizhi.meizhi.malin.view.** { *; }


# Keep the support library
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


# Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class **.R$* {*;}

-verbose
-keepclasseswithmembernames class * {
    native <methods>;
}


-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-dontnote okio.**

-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-keep class com.squareup.okhttp.OkHttpClient
-dontwarn com.squareup.okhttp.OkHttpClient
-dontnote com.squareup.okhttp.OkHttpClient


-dontwarn java.lang.invoke.*


# 保持混淆时类的实名及行号(——————— 调试时打开 ———————)
#-keepattributes SourceFile,LineNumberTable

# Remove logging calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 枚举需要keep see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}




-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment




#####################记录生成的日志数据,gradle build时在本项目根目录输出################

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

#http://my.oschina.net/aibenben/blog/371889
#http://treesouth.github.io/2015/04/05/Android%E4%B8%ADProGuard%E6%B7%B7%E6%B7%86%E9%85%8D%E7%BD%AE%E5%92%8C%E6%80%BB%E7%BB%93/


###Umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}


#-------------------------------tencent.bugly-------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#-------------------------------tencent.bugly-------------------------------


#-------------------------------Freco混淆-------------------------------
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
#-------------------------------Freco混淆-------------------------------