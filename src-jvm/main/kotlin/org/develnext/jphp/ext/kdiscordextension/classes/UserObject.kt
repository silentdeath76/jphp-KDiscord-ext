package org.develnext.jphp.ext.kdiscordextension.classes

import dev.cbyrne.kdiscordipc.data.user.User
import org.develnext.jphp.ext.kdiscordextension.KDiscordExtension
import php.runtime.annotation.Reflection
import php.runtime.annotation.Reflection.Property
import php.runtime.env.Environment
import php.runtime.lang.BaseObject
import php.runtime.reflection.ClassEntity

@Reflection.Name("User")
@Reflection.Namespace(KDiscordExtension.NS)
class UserObject(env: Environment?, clazz: ClassEntity? = null) : BaseObject(env, clazz) {
    var flags: Int = 0
    var premiumType: Int = 0
    var premiumTier: Int = 0
    var premiumName: String = ""
    var bot: Boolean = false
    var discriminator: String = ""
    var avatar: String = ""
    var id: String = ""
    var username: String = "";

    fun init(user: User) {
        this.username = user.username;
        this.id = user.id;
        this.avatar = user.avatar!!
        this.flags = user.flags!!
        this.discriminator = user.discriminator;
        this.bot = user.bot == true
        if (user.premiumType != null) {
            this.premiumName = user.premiumType!!.name;
            this.premiumTier = user.premiumType!!.index;
            this.premiumType = user.premiumType!!.ordinal;
        }
    }

    fun toArray(): Array<Any> {
        return arrayOf(
            this.username,
            this.id,
            this.avatar,
            this.flags,
            this.discriminator,
            this.bot,
            this.premiumName,
            this.premiumTier,
            this.premiumType
        )
    }
}