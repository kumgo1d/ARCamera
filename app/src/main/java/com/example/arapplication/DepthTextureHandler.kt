package com.example.arapplication

import android.opengl.GLES20
import android.opengl.GLES30
import com.google.ar.core.Frame
import com.google.ar.core.exceptions.NotYetAvailableException


class DepthTextureHandler {
    var depthTexture = -1
        private set
    var depthWidth = -1
        private set
    var depthHeight = -1
        private set

    fun createOnGlThread() {
        val textureId = IntArray(1)
        GLES20.glGenTextures(1, textureId, 0)
        depthTexture = textureId[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTexture)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    }

    fun update(frame: Frame) {
        try {
            val depthImage = frame.acquireDepthImage()
            depthWidth = depthImage.width
            depthHeight = depthImage.height
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTexture)
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES30.GL_RG8,
                depthWidth,
                depthHeight,
                0,
                GLES30.GL_RG,
                GLES20.GL_UNSIGNED_BYTE,
                depthImage.planes[0].buffer
            )
            depthImage.close()
        } catch (e: NotYetAvailableException) {
            // This normally means that depth data is not available yet.
        }
    }
}