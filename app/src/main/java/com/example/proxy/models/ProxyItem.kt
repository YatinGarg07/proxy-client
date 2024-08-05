package com.example.proxy.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
@Parcelize
data class ProxyItem(
    val _id: @RawValue BsonObjectId = ObjectId(),
    val profileName: String = "",
    val protocol: String = "",
    val server: String = "",
    val port: String = "",
    val authMethod: String = "",
    val username: String? = null,
    val password: String? = null,
): Parcelable {


    fun toProxyDBItem(): ProxyDBItem{
        return ProxyDBItem().apply {
            this._id = this@ProxyItem._id
            this.profileName = this@ProxyItem.profileName
            this.protocol = this@ProxyItem.protocol
            this.server = this@ProxyItem.server
            this.port = this@ProxyItem.port
            this.authMethod = this@ProxyItem.authMethod
            this.username = this@ProxyItem.username
            this.password = this@ProxyItem.password
        }
    }
}