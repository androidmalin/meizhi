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

#参考文章
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources

#-------------------------------optimize-------------------------------
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources
#/sdk/tools/proguard/proguard-android-optimize.txt
#-------------------------------common-------------------------------
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources
# 混淆时所用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#代码压缩级别
-optimizationpasses 7
-dontshrink
-flattenpackagehierarchy
-verbose
-dontskipnonpubliclibraryclassmembers
-ignorewarnings


#混淆时预校验
-dontpreverify

#使用大小写混合
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclasses

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#记录日志
-verbose

#忽略警告，避免打包时某些警告出现
-ignorewarnings

# 保护给定的可选属性
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,LocalVariableTable,*JavascriptInterface*


# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#-------------------------------common-------------------------------


#-------------------------------通用混淆-------------------------------
-renamesourcefileattribute SourceFile

-keep class **.R
-keepclassmembers class **.R$* {
    public static <fields>;
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#-------------------------------通用混淆-------------------------------



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


#-------------------------------okhttp3-------------------------------
#https://github.com/square/okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#-------------------------------okhttp3-------------------------------


#-------------------------------retrofit2-------------------------------
#https://github.com/square/retrofit
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-------------------------------retrofit2-------------------------------

#---------------Begin: proguard configuration for Gson  ----------
#https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------


# Remove logging calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}



#http://my.oschina.net/aibenben/blog/371889
#http://treesouth.github.io/2015/04/05/Android%E4%B8%ADProGuard%E6%B7%B7%E6%B7%86%E9%85%8D%E7%BD%AE%E5%92%8C%E6%80%BB%E7%BB%93/