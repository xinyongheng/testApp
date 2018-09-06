//
// Created by Administrator on 2017/12/1.
//
#include "com_example_test_test_myffmpeg_JniUtils.h"
/* Class:     Java_com_example_test_test_myffmpeg_JniUtils_getStringFormC
* Method:    getStringFormC
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_example_test_test_myffmpeg_JniUtils_getStringFormC
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "这里是来自c的string");
}
