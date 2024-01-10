<?php

namespace discord\rpc;

class KDiscord {
    public function __construct (String $clientID)
    {
    }

    public function setDetails (String $val): void
    {
    }

    public function setState (String $val): void
    {
    }

    public function setStartTimestamp (int $val): void
    {
    }

    public function setEndTimestamp (int $val): void
    {
    }

    public function updateActivity (): void
    {
    }

    public function addButton (String $text, String $url): void
    {
    }

    public function changeButton (int $index, String $text, String $url): void
    {
    }

    public function removeButton ($index): void
    {
    }

    public function setLargeImage (String $key, String $hint = null): void
    {
    }

    public function setSmallImage (String $key, String $hint = null): void
    {
    }

    public function on ($event, $callback): void
    {
    }
}