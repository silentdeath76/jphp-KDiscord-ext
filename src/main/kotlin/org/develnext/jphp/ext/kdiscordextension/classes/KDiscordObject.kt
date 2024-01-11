package org.develnext.jphp.ext.kdiscordextension.classes

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.*
import dev.cbyrne.kdiscordipc.data.activity.Activity
import kotlinx.coroutines.*
import org.develnext.jphp.ext.kdiscordextension.KDiscordExtension
import php.runtime.annotation.Reflection.*
import php.runtime.env.Environment
import php.runtime.invoke.Invoker
import php.runtime.lang.BaseObject
import php.runtime.reflection.ClassEntity
import java.util.logging.Level
import java.util.logging.Logger

@Name("__KDiscord")
@Namespace(KDiscordExtension.NS)
class KDiscordObject(env: Environment?, clazz: ClassEntity?) : BaseObject(env, clazz) {
    private var isManuallyDisconnect: Boolean = false
    private var ipc: KDiscordIPC = KDiscordIPC("0")

    var details: String? = null
        @Signature
        @Name("setDetails")
        set(value) {
            activity.details = if (value!!.length < 2) null else value
            field = activity.details
        }
    var state: String? = null
        @Signature
        @Name("setState")
        set(value) {
            activity.state = if (value!!.length < 2) null else value
            field = activity.state
        }
    var startTimestamp: Long? = System.currentTimeMillis()
        @Signature
        @Name("setStartTimestamp")
        set(value) {
            field = if (value!! <= 0) null else value
        }
    var endTimestamp: Long? = 0L
        @Signature
        @Name("setEndTimestamp")
        set(value) {
            field = if (value!! > startTimestamp!!) value else null
        }

    private val largeImage = mutableListOf("", null)
    private val smallImage = mutableListOf("", null)
    private val eventMap = mutableMapOf<String, Invoker>()

    private val activity: Activity = Activity();

    @DelicateCoroutinesApi
    @Signature
    fun __construct(clientID: String) {
        Logger.getLogger("KDiscordIPC").level = Level.OFF;

        ipc = KDiscordIPC(clientID)

        GlobalScope.launch {
            val user = UserObject(__env__);

            ipc.on<ReadyEvent> {
                updateActivity()
                ipc.activityManager.setActivity(activity)

                if (eventMap[Events.READY.toString().lowercase()] is Invoker) {
                    user.init(data.user)
                    eventMap[Events.READY.toString().lowercase()]!!.callAny(user.toArray())
                }
            }

            ipc.on<DisconnectedEvent> {
                GlobalScope.launch {
                    if (!isManuallyDisconnect) {
                        Thread.sleep(1000L)
                        ipc.connect();
                    }
                }

                if (eventMap[Events.DISCONNECTED.toString().lowercase()] is Invoker) {
                    eventMap[Events.DISCONNECTED.toString().lowercase()]!!.callAny()
                }
            }

            ipc.on<ErrorEvent> {
                if (eventMap[Events.ERROR.toString().lowercase()] is Invoker) {
                    eventMap[Events.ERROR.toString()
                        .lowercase()]!!.callAny("ERROR IPC communication error (${data.code}): ${data.message}")
                }
            }

            ipc.on<CurrentUserUpdateEvent> {
                if (eventMap[Events.CURRENT_USER_UPDATE.toString().lowercase()] is Invoker) {
                    user.init(data)
                    eventMap[Events.CURRENT_USER_UPDATE.toString().lowercase()]!!.callAny(user.toArray())
                }
            }

            GlobalScope.launch {
                ipc.connect()
            }
        }
    }


    @DelicateCoroutinesApi
    private fun updateActivity() {
        activity.details = details
        activity.state = state
        activity.timestamps = Activity.Timestamps(startTimestamp!!, null)

        if (endTimestamp!! > System.currentTimeMillis()) {
            activity.timestamps = Activity.Timestamps(System.currentTimeMillis(), endTimestamp)
        }

        activity.assets = Activity.Assets()

        if (largeImage.get(0)?.isNotEmpty() == true) {
            activity.assets!!.largeImage = largeImage[0]

            if (largeImage[1] == null || largeImage[1]?.length!! > 2) {
                activity.assets!!.largeText = largeImage[1]
            } else {
                activity.assets!!.largeText = null
            }
        }

        if (smallImage.get(0)?.isNotEmpty() == true) {
            activity.assets!!.smallImage = smallImage[0]

            if (smallImage[1] == null || smallImage[1]!!.length > 2) {
                activity.assets!!.smallText = smallImage[1]
            } else {
                activity.assets!!.smallText = null
            }
        }

        GlobalScope.launch {
            ipc.activityManager.setActivity(activity)
        }
    }

    @DelicateCoroutinesApi
    @Signature
    @Name("updateActivity")
    fun updateState() {
        GlobalScope.launch {
            updateActivity() // all broken
            ipc.activityManager.clearActivity()
            ipc.activityManager.setActivity(activity)
        }
    }


    @Signature
    fun addButton(text: String, url: String) {
        if (activity.buttons == null) {
            activity.buttons = mutableListOf(Activity.Button(text, url));
            return;
        }

        if (activity.buttons!!.size >= 2) {
            removeButton(0)
        }

        activity.buttons!!.add(activity.buttons!!.size, Activity.Button(text, url))
    }

    @Signature
    fun changeButton(index: Int, text: String, url: String) {
        if (activity.buttons == null || index > activity.buttons!!.size - 1) return;

        val button = activity.buttons!![index]
        button.label = text
        button.url = url
    }

    @Signature
    fun removeButton(index: Int = 0) {
        if (activity.buttons == null || index > activity.buttons!!.size - 1) return;

        activity.buttons!!.removeAt(index)

        if (activity.buttons!!.size == 0) activity.buttons = null
    }


    @Signature
    fun setLargeImage(key: String) {
        largeImage[0] = key
        largeImage[1] = null
    }

    @Signature
    fun setLargeImage(key: String, hint: String?) {
        largeImage[0] = key
        largeImage[1] = hint
    }

    @Signature
    fun setSmallImage(key: String) {
        smallImage[0] = key
        smallImage[1] = null
    }

    @Signature
    fun setSmallImage(key: String, hint: String?) {
        smallImage[0] = key
        smallImage[1] = hint
    }


    @Signature
    fun disconnect() {
        isManuallyDisconnect = true
        if (ipc.connected) ipc.disconnect();
    }


    @Signature
    fun on(event: String, callback: Invoker) {
        eventMap[event.lowercase()] = callback
    }


    @Signature
    fun __destruct() {
        disconnect()
    }

    private enum class Events {
        DISCONNECTED,
        ERROR,
        CURRENT_USER_UPDATE,
        ACTIVITY_JOIN,
        ACTIVITY_INVITE,
        READY;
    }
}