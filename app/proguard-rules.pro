# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations

#---------------------------------
# Retrofit
#---------------------------------
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and challenging, so keep them.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#---------------------------------
# Gson
#---------------------------------
# Keep data classes used for JSON serialization
-keep class com.vourourou.forklife.data.remote.** { *; }
-keep class com.vourourou.forklife.data.remote.responses.** { *; }
-keep class com.vourourou.forklife.data.local.entity.** { *; }

# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from removing the generic type from TypeToken
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

#---------------------------------
# OkHttp
#---------------------------------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#---------------------------------
# Hilt / Dagger
#---------------------------------
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-dontwarn dagger.hilt.android.internal.**

#---------------------------------
# Firebase
#---------------------------------
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

#---------------------------------
# Play Games Services
#---------------------------------
-keep class com.google.android.gms.games.** { *; }
-keep class com.google.android.gms.auth.** { *; }

#---------------------------------
# Room Database
#---------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

#---------------------------------
# Compose
#---------------------------------
-dontwarn androidx.compose.**

#---------------------------------
# Coil (Image Loading)
#---------------------------------
-dontwarn coil.**

#---------------------------------
# CameraX
#---------------------------------
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

#---------------------------------
# ML Kit Barcode Scanning
#---------------------------------
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.vision.** { *; }
-dontwarn com.google.mlkit.**

#---------------------------------
# AndroidX / AppCompat
#---------------------------------
-keep class androidx.appcompat.widget.** { *; }
-keep class androidx.core.** { *; }

#---------------------------------
# Kotlin
#---------------------------------
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

#---------------------------------
# Coroutines
#---------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

#---------------------------------
# DataStore
#---------------------------------
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}

#---------------------------------
# Parcelable
#---------------------------------
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#---------------------------------
# Serializable
#---------------------------------
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#---------------------------------
# Enums
#---------------------------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
