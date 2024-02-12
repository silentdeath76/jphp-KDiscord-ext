package org.develnext.jphp.ext.kdiscordextension.classes

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.ConnectionError.NotConnected
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.*
import dev.cbyrne.kdiscordipc.data.activity.Activity
import kotlinx.coroutines.*
import org.develnext.jphp.ext.kdiscordextension.KDiscordExtension
import php.runtime.annotation.Reflection.*
import php.runtime.env.Environment
import php.runtime.invoke.Invoker
import php.runtime.lang.BaseObject
import php.runtime.reflection.ClassEntity

@Suppress("FunctionName")
@Name("__KDiscord")
@Namespace(KDiscordExtension.NS)
class KDiscordObject(env: Environment?, clazz: ClassEntity?) : BaseObject(env, clazz) {
    private var isManuallyDisconnect: Boolean = false
    private var ipc: KDiscordIPC = KDiscordIPC("0")

    var details: String? = null
        @Signature @Name("setDetails") set(value) {
            activity.details = if (value!!.length < 2) null else value
            field = activity.details
        }
    var state: String? = null
        @Signature @Name("setState") set(value) {
            activity.state = if (value!!.length < 2) null else value
            field = activity.state
        }
    var startTimestamp: Long? = System.currentTimeMillis()
        @Signature @Name("setStartTimestamp") set(value) {
            field = if (value!! <= 0) null else value
        }
    var endTimestamp: Long? = 0L
        @Signature @Name("setEndTimestamp") set(value) {
            field = value
        }

    private val largeImage = mutableListOf("", null)
    private val smallImage = mutableListOf("", null)
    private val eventMap = mutableMapOf<String, Invoker>()

    private val activity: Activity = Activity();

    @DelicateCoroutinesApi
    @Signature
    fun __construct(clientID: String) {

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

                ipc.subscribe(DiscordEvent.CurrentUserUpdate)
                ipc.subscribe(DiscordEvent.ActivityJoinRequest)
                ipc.subscribe(DiscordEvent.ActivityJoin)
                ipc.subscribe(DiscordEvent.ActivityInvite)
                ipc.subscribe(DiscordEvent.ActivitySpectate)
            }

            ipc.on<DisconnectedEvent> {
                try {
                    coroutineScope {
                        if (!isManuallyDisconnect) {
                            Thread.sleep(1000L)
                            connect()
                        }
                    }
                } catch (e: Exception) {
                    println("ERROR: ${e.message}")
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

            connect()
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
        val handler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }

        try {
            GlobalScope.launch (handler) {
                try {
                    ipc.activityManager.clearActivity()
                    updateActivity()
                } catch (e: Exception) {
                    println(e.message)
                } catch (e: Error) {
                    println(e.message)
                } catch (e: NotConnected) {
                    println(e.message)
                } finally {
                }
                ipc.activityManager.setActivity(activity)
            }
        } catch (e: NotConnected) {
            println(e.message)
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

    @DelicateCoroutinesApi
    @Signature
    fun connect() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }

        GlobalScope.launch(handler) {
            try {
                ipc.connect()
            } catch (e: Exception) {
                println(e.message)
            } catch (e: Error) {
                println(e.message)
            } catch (e: NotConnected) {
                println(e.message)
            } finally {
            }
        }
    }

    @Signature
    fun disconnect() {
        isManuallyDisconnect = true
        if (ipc.connected) ipc.disconnect();
    }


    @Signature
    fun setParty(id: String, size: Int, max: Int) {
        activity.party = Activity.Party(id, Activity.Party.PartySize(size, max))
    }

    @DelicateCoroutinesApi
    @Signature
    fun setSecrets(join: String, match: String, spectate: String) {
        removeButton(1);
        removeButton(0);
        activity.secrets = Activity.Secrets(join, match, spectate)

        GlobalScope.launch {
            ipc.on<ActivityJoinEvent> {
                //println("The user has joined someone else's party! ${data.secret}")

                if (eventMap[Events.ACTIVITY_JOIN.toString().lowercase()] is Invoker) {
                    eventMap[Events.ACTIVITY_JOIN.toString().lowercase()]!!.callAny(data.secret)
                }
            }

            ipc.on<ActivityInviteEvent> {
                //println("We have been invited to join ${data.user.username}'s party! (${data.activity.party.id})")

                if (eventMap[Events.ACTIVITY_INVITE.toString().lowercase()] is Invoker) {
                    if (eventMap[Events.ACTIVITY_INVITE.toString().lowercase()]!!.callAny(data.activity.party.id).toBoolean()) {
                        ipc.activityManager.acceptInvite(data)
                    }
                }
            }
        }
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
        DISCONNECTED, ERROR, CURRENT_USER_UPDATE, ACTIVITY_JOIN, ACTIVITY_INVITE, READY;
    }
}