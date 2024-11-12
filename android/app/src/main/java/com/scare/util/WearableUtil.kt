package com.scare.util

import android.content.Context
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

object WearableUtils {

    suspend fun isWatchConnected(context: Context): Boolean {
        return try {
            val nodeClient = Wearable.getNodeClient(context)
            val connectedNodes: List<Node> = nodeClient.connectedNodes.await()
            connectedNodes.isNotEmpty() // 연결된 노드가 있으면 true 반환
        } catch (e: Exception) {
            e.printStackTrace()
            false // 에러 발생 시 false 반환
        }
    }

}