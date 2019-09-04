/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_jme_bullet_collision_shapes_MeshCollisionShape */

#ifndef _Included_com_jme_bullet_collision_shapes_MeshCollisionShape
#define _Included_com_jme_bullet_collision_shapes_MeshCollisionShape
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jme_bullet_collision_shapes_MeshCollisionShape
 * Method:    setBVH
 * Signature: ([BJ)J
 */
JNIEXPORT jlong JNICALL Java_com_jme_bullet_collision_shapes_MeshCollisionShape_setBVH
  (JNIEnv *, jobject, jbyteArray, jlong);

/*
 * Class:     com_jme_bullet_collision_shapes_MeshCollisionShape
 * Method:    saveBVH
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_jme_bullet_collision_shapes_MeshCollisionShape_saveBVH
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme_bullet_collision_shapes_MeshCollisionShape
 * Method:    createShape
 * Signature: (ZZJ)J
 */
JNIEXPORT jlong JNICALL Java_com_jme_bullet_collision_shapes_MeshCollisionShape_createShape
  (JNIEnv *, jobject, jboolean, jboolean, jlong);

/*
 * Class:     com_jme_bullet_collision_shapes_MeshCollisionShape
 * Method:    finalizeNative
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_jme_bullet_collision_shapes_MeshCollisionShape_finalizeNative
  (JNIEnv *, jobject, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
