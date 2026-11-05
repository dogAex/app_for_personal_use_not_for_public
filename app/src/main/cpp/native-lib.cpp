#include <jni.h>
#include <string>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#define LOG_TAG "QG_ReShade_Core"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_qg_reshade_MainActivity_probeKernel(JNIEnv* env, jobject) {
    // ???? kgsl-3d0 ???????????
    int fd = open("/dev/kgsl-3d0", O_RDWR);
    if (fd >= 0) {
        LOGI("???? kgsl-3d0 ???? (fd: %d)", fd);
        close(fd);
        return env->NewStringUTF("QG ReShade ????? (fd: OK)");
    } else {
        LOGI("?????????? Shizuku ??? Root");
        return env->NewStringUTF("QG ReShade ??????? Shizuku ??");
    }
}
