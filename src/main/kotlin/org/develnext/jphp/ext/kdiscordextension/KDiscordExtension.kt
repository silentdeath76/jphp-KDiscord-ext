package org.develnext.jphp.ext.kdiscordextension

import org.develnext.jphp.ext.kdiscordextension.classes.KDiscordObject
import org.develnext.jphp.ext.kdiscordextension.classes.UserObject
import php.runtime.env.CompileScope
import php.runtime.ext.support.Extension

class KDiscordExtension : Extension() {
    companion object {
        const val NS: String = "discord\\rpc"
    }

    override fun getStatus(): Status {
        return Status.EXPERIMENTAL;
    }

    override fun getPackageNames(): Array<String> {
        return arrayOf("discord")
    }

    override fun onRegister(p0: CompileScope?) {
        registerClass(p0, KDiscordObject::class.java)
        registerClass(p0, UserObject::class.java)
    }

}