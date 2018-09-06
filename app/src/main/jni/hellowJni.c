//
// Created by Administrator on 2017/12/4.
//
#include <stdio.h>
#include "com_example_test_test_myffmpeg_JniUtils.h"
#include "include/libavcodec/avcodec.h"

/*
 * Class:     com_example_test_test_myffmpeg_HellowJni
 * Method:    configurationinfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_test_test_myffmpeg_HellowJni_configurationinfo
        (JNIEnv *env, jobject obj) {
    char info[10000] = {0};
    sprintf(info, "%s\n", avcodec_configuration());
    return (*env)->NewStringUTF(env, info);
}

JNIEXPORT jstring JNICALL
Java_com_example_test_test_myffmpeg_JniUtils_getAndroidCpuType(JNIEnv *env, jclass type) {
    // TODO
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#define ABI "armeabi-v7a/NEON"
#else
#define ABI "armeabi-v7a"
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__mips__)
#define ABI "mips"
#else
#define ABI "unknown"
#endif
    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}

JNIEXPORT jstring JNICALL
Java_com_example_test_test_myffmpeg_HellowJni_getAndroidCpuType(JNIEnv *env, jobject instance) {

    // TODO
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
#define ABI "armeabi-v7a/NEON"
#else
#define ABI "armeabi-v7a"
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__mips__)
#define ABI "mips"
#else
#define ABI "unknown"
#endif
    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}

JNIEXPORT jstring JNICALL
Java_com_example_test_test_myffmpeg_JniUtils_jiaMi(JNIEnv *env, jclass type, jstring s_) {
    const char *s = (*env)->GetStringUTFChars(env, s_, 0);
    (*env)->ReleaseStringUTFChars(env, s_, s);

    return (*env)->NewStringUTF(env, "xxxxx");
}