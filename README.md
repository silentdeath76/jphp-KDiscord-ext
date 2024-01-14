# jphp-KDiscord-ext

##### Как скачать для DevelNext?
Жми **[сюда](https://github.com/silentdeath76/jphp-KDiscord-ext/releases/latest)**
<br>
<br>
##### Подключение пакета в JPHP
```
 jppm add jphp-KDiscord-ext@git+https://github.com/silentdeath76/jphp-KDiscord-ext
```
<br>

##### Как пользоваться:
1. Чтобы использовать пакет вам надо создать приложение в [discord](https://discord.com/developers/applications) и получить Application ID.
2. Чтобы ваше приложение отображало иконку, надо загрузить изображения в разделе "Rich Presence". Загружаете и даете ID вашей картинке (может не сразу работать, у меня только через пару минут начало подгружать ее)

<br>
<br>

##### Простой пример использования
```php
$ipc = new KDiscord("817484458025418764"); // 817484458025418764 - это APPLICATION ID
$ipc->setDetails("Заголовок");
$ipc->setState("Подзаголовок");
$ipc->setLargeImage("big", "amazing"); // big - ID вашей картинки, amazing - подсказка при наведении на нее
$ipc->setSmallImage("small");

$ipc->addButton("Best video on planet", "https://youtu.be/dQw4w9WgXcQ");
```
<br>

##### Как менять уже заданные парраметры?
Все просто, меняете и вызваете метод `updateActivity()`

```php
$ipc->setState("Hello world!");
$ipc->updateActivity();
```