<?php

namespace discord\rpc;

class KDiscordTypes {
    const X_BUTTON_FIRST = 0;
    const X_BUTTON_SECOND = 1;

    const EVENT_READY = 'ready';
    const EVENT_ERROR = 'error';
    const EVENT_DISCONNECTED = 'disconnected';
    const EVENT_ACTIVITY_JOIN = 'activity_join';
    const EVENT_ACTIVITY_INVITE = 'activity_invite';
    const EVENT_CURRENT_USER_UPDATE = 'current_user_update';
}