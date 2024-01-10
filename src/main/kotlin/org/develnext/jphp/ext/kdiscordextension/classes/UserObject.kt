package org.develnext.jphp.ext.kdiscordextension.classes

import dev.cbyrne.kdiscordipc.data.user.User
import javafx.util.Pair
import org.develnext.jphp.ext.kdiscordextension.KDiscordExtension
import php.runtime.Memory
import php.runtime.annotation.Reflection
import php.runtime.annotation.Reflection.Property
import php.runtime.env.Environment
import php.runtime.lang.BaseObject
import php.runtime.reflection.ClassEntity

@Reflection.Name("User")
@Reflection.Namespace(KDiscordExtension.NS)
class UserObject(env: Environment?, clazz: ClassEntity? = null) : BaseObject(env, clazz) {
    @Property
    var premiumType: Int = 0

    @Property
    var premiumTier: Int = 0

    @Property
    var premiumName: String = ""

    @Property
    var bot: Boolean = false

    @Property
    var discriminator: String = ""

    @Property
    var avatar: String = ""

    @Property
    var id: String = ""

    @Property
    var username: String = "";

    fun init(user: User) {
        this.username = user.username;
        this.id = user.id;
        this.avatar = user.avatar!!
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
            this.discriminator,
            this.bot,
            this.premiumName,
            this.premiumTier,
            this.premiumType
        )
    }
}