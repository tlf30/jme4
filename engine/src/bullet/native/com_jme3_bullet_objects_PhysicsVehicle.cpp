/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Author: Normen Hansen
 */

#include "com_jme3_bullet_objects_PhysicsVehicle.h"
#include "jmeBulletUtil.h"
#include "BulletDynamics/Vehicle/btRaycastVehicle.h"

#ifdef __cplusplus
extern "C" {
#endif

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    updateWheelTransform
     * Signature: (JIZ)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_updateWheelTransform
    (JNIEnv *env, jobject object, jlong vehicleId, jint wheel, jboolean interpolated) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->updateWheelTransform(wheel, interpolated);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    createVehicleRaycaster
     * Signature: (JJ)J
     */
    JNIEXPORT jlong JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_createVehicleRaycaster
    (JNIEnv *env, jobject object, jlong bodyId, jlong spaceId) {
        //btRigidBody* body = (btRigidBody*) bodyId;
        btDiscreteDynamicsWorld* spsace = (btDiscreteDynamicsWorld*) spaceId;
        if (spsace == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return 0;
        }
        btDefaultVehicleRaycaster* caster = new btDefaultVehicleRaycaster(spsace);
        return (long) caster;
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    createRaycastVehicle
     * Signature: (JJ)J
     */
    JNIEXPORT jlong JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_createRaycastVehicle
    (JNIEnv *env, jobject object, jlong objectId, jlong casterId) {
        btRigidBody* body = (btRigidBody*) objectId;
        if (body == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return 0;
        }
        body->setActivationState(DISABLE_DEACTIVATION);
        btVehicleRaycaster* caster = (btDefaultVehicleRaycaster*) casterId;
        btRaycastVehicle::btVehicleTuning tuning;
        btRaycastVehicle* vehicle = new btRaycastVehicle(tuning, body, caster);
        return (long) vehicle;

    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    setCoordinateSystem
     * Signature: (JIII)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_setCoordinateSystem
    (JNIEnv *env, jobject object, jlong vehicleId, jint right, jint up, jint forward) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->setCoordinateSystem(right, up, forward);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    addWheel
     * Signature: (JLcom/jme3/math/Vector3f;Lcom/jme3/math/Vector3f;Lcom/jme3/math/Vector3f;FFLcom/jme3/bullet/objects/infos/VehicleTuning;Z)J
     */
    JNIEXPORT jlong JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_addWheel
    (JNIEnv *env, jobject object, jlong vehicleId, jobject location, jobject direction, jobject axle, jfloat restLength, jfloat radius, jobject tuning, jboolean frontWheel) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return 0;
        }
        btVector3 vec1 = btVector3();
        btVector3 vec2 = btVector3();
        btVector3 vec3 = btVector3();
        jmeBulletUtil::convert(env, location, &vec1);
        jmeBulletUtil::convert(env, direction, &vec2);
        jmeBulletUtil::convert(env, axle, &vec3);
        btRaycastVehicle::btVehicleTuning tune;
        btWheelInfo* info = &vehicle->addWheel(vec1, vec2, vec3, restLength, radius, tune, frontWheel);
        return (long) info;
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    resetSuspension
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_resetSuspension
    (JNIEnv *env, jobject object, jlong vehicleId) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->resetSuspension();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    applyEngineForce
     * Signature: (JIF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_applyEngineForce
    (JNIEnv *env, jobject object, jlong vehicleId, jint wheel, jfloat force) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->applyEngineForce(force, wheel);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    steer
     * Signature: (JIF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_steer
    (JNIEnv *env, jobject object, jlong vehicleId, jint wheel, jfloat value) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->setSteeringValue(value, wheel);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    brake
     * Signature: (JIF)F
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_brake
    (JNIEnv *env, jobject object, jlong vehicleId, jint wheel, jfloat value) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        vehicle->setBrake(value, wheel);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    getCurrentVehicleSpeedKmHour
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_getCurrentVehicleSpeedKmHour
    (JNIEnv *env, jobject object, jlong vehicleId) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return 0;
        }
        return vehicle->getCurrentSpeedKmHour();
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    getForwardVector
     * Signature: (JLcom/jme3/math/Vector3f;)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_getForwardVector
    (JNIEnv *env, jobject object, jlong vehicleId, jobject out) {
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        jmeBulletUtil::convert(env, &vehicle->getForwardVector(), out);
    }

    /*
     * Class:     com_jme3_bullet_objects_PhysicsVehicle
     * Method:    finalizeNative
     * Signature: (JJ)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_PhysicsVehicle_finalizeNative
    (JNIEnv *env, jobject object, jlong casterId, jlong vehicleId) {
        btVehicleRaycaster* rayCaster = (btVehicleRaycaster*) casterId;
        btRaycastVehicle* vehicle = (btRaycastVehicle*) vehicleId;
        if (vehicle == NULL) {
            jclass newExc = env->FindClass("java/lang/NullPointerException");
            env->ThrowNew(newExc, "The native object does not exist.");
            return;
        }
        delete(vehicle);
        delete(rayCaster);
    }

#ifdef __cplusplus
}
#endif

