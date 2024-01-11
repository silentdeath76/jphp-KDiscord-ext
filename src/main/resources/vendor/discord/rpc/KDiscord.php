<?php
namespace discord\rpc;

use discord;

class KDiscord {

    private $instance;

    public function __construct (String $clientID)
    {
        $this->instance = new __KDiscord($clientID);
    }

    public function setDetails (String $val): void
    {
        $this->instance->setDetails($val);
    }

    public function setState (String $val): void
    {
        $this->instance->setState($val);
    }

    public function setStartTimestamp (int $val): void
    {
        $this->instance->setStartTimestamp($val);
    }

    public function setEndTimestamp (int $val): void
    {
        $this->instance->setEndTimestamp($val);
    }

    public function updateActivity (): void
    {
        $this->instance->updateActivity();
    }

    public function addButton (String $text, String $url): void
    {
        $this->instance->addButton($text, $url);
    }

    public function changeButton (int $index, String $text, String $url): void
    {
        $this->instance->changeButton($index, $text, $url);
    }

    public function removeButton ($index): void
    {
        $this->instance->removeButton($index);
    }

    public function setLargeImage (String $key, String $hint = null): void
    {
        $this->instance->setLargeImage($key, $hint);
    }

    public function setSmallImage (String $key, String $hint = null): void
    {
        $this->instance->setSmallImage($key, $hint);
    }

    public function disconnect (): void
    {
        $this->instance->disconnect();
    }

    public function on ($event, $callback): void
    {
        $this->instance->on($event, function () use ($callback, $event) {
            $args = func_get_args();

            if (is_array($args[0]) && ($event == KDiscordTypes::EVENT_READY || $event == KDiscordTypes::EVENT_CURRENT_USER_UPDATE)) {
                $args[1] = $args[0]; // todo remove it
                $args[0] = UserObject::ofArray($args[0]);
            }

            uiLater(function () use ($callback, $args) {
                call_user_func_array($callback, $args);
            });
        });
    }
}