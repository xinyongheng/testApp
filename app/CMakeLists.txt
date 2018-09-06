# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             ffmpeg-inf

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/jni/hellowJni.c )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

#增加头文件搜索目录
include_directories(src/main/jni/include/)

target_link_libraries( # Specifies the target library.
                        ffmpeg-inf

#增加ffmpeg库，注意顺序（具体看lib/pkgconfig的依赖关系）
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavfilter-6.so
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavformat-57.so
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavcodec-57.so
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libswresample-2.so
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libswscale-4.so
${CMAKE_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavutil-55.so

#增加ffmpeg所需要的依赖库
-lm -lz
# Links the target library to the log library
# included in the NDK.
${log-lib}

)