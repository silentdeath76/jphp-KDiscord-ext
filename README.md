# jphp-KDiscord-ext

### code example:
```php
$ipc = new KDiscord("817484458025418764");
$ipc->setState("_____ඞ");
$ipc->setDetails("Test discord package");
$ipc->setLargeImage("big", "amazing");
$ipc->setSmallImage("small");

$ipc->addButton("Best video on planet", "https://youtu.be/dQw4w9WgXcQ");

$ipc->updateActivity();

waitAsync(5000, function () use ($ipc) {
    $ipc->setState("___ඞ__");
    $ipc->updateActivity();
});

waitAsync(10000, function () use ($ipc) {
    $ipc->setState("ඞ_____");
    $ipc->updateActivity();
});

waitAsync(15000, function () use ($ipc) {
    $ipc->disconnect();
});
```