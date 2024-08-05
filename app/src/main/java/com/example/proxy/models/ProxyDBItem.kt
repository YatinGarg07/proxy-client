package com.example.proxy.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
class ProxyDBItem: RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var profileName: String = ""
    var protocol: String = ""
    var server: String = ""
    var port: String = ""
    var authMethod: String = ""
    var username: String? = null
    var password: String? = null

    fun toProxyItem(): ProxyItem{
        return ProxyItem(
            _id = this@ProxyDBItem._id,
            profileName = this@ProxyDBItem.profileName,
            protocol = this@ProxyDBItem.protocol,
            server = this@ProxyDBItem.server,
            port = this@ProxyDBItem.port,
            authMethod = this@ProxyDBItem.authMethod,
            username = this@ProxyDBItem.username,
            password = this@ProxyDBItem.password,
        )
    }

}

