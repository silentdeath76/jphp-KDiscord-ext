<?php

namespace discord\rpc;

class UserObject
{
    private $username = "";
    private $avatar = "";
    private $id = "";
    private $discriminator = "";
    private $bot = false;

    private $flags = 0;

    private $premiumType = 0;
    private $premiumTier = 0;
    private $premiumName = "";

    /**
     *
     * @return UserObject
     */
    public static function ofArray (array $arr)
    {
        return new self($arr);
    }

    private function __construct ($dataArray)
    {
        $this->username = $dataArray[0];
        $this->id = $dataArray[1];
        $this->avatar = $dataArray[2];
        $this->flags = $dataArray[3];
        $this->discriminator = $dataArray[4];
        $this->bot = $dataArray[5];
        $this->premiumName = $dataArray[6];
        $this->premiumTier = $dataArray[7];
        $this->premiumType = $dataArray[8];
    }

    public function getUsername ()
    {
        return $this->username;
    }

    public function getAvatar ()
    {
        return $this->avatar;
    }

    public function getId ()
    {
        return $this->id;
    }

    public function getDiscriminator ()
    {
        return $this->discriminator;
    }

    public function isBot ()
    {
        return $this->bot;
    }

    public function getFlags ()
    {
        return $this->flags;
    }

    public function getPremiumType ()
    {
        return $this->premiumType;
    }

    public function getPremiumTier ()
    {
        return $this->premiumTier;
    }

    public function getPremiumName ()
    {
        return $this->premiumName;
    }
}