# RaketaFramework

## Содержание

1. [Hello World!](#hello)
2. [Чтение параметров URL'ов](#params)
3. [Гибкие URL'ы](#urls)
4. [Получение форм](#forms)
5. [Загрузки](#downloads)
6. [Лицензия](#license)

<h2 id="hello">Hello World!</h2>

Каждый знает, что туториалы обычно начинаются с "Hello World!". Я сделаю исключение. Наша первая программа на
RaketaFramework'е будет показывать "Hello World!" дважды :). Итак, давайте напишем немного кода:

##### Main.java:
```java
import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.server.*;

public class Main {
    public static void main(String[] args) {
        HandlersList handlersList = new HandlersList(new HelloHandler());
        Server server = new Server(handlersList);
        server.start();
    }

    @RequestURLPattern("/hello")
    private static class HelloHandler implements RequestHandler {
        @RequestMethod("GET")
        public String GET(RequestData requestData) {
            return "Hello World!\r\n" + 
                    "Hello World!";
        }
    }
}
```

Мои поздравления! Это твоё первое приложение! Чтобы запустить его в терминале/консоли, ты можешь написать
что-нибудь вроде этого:

```bash
javac Main.java
java -cp libs/RaketaFramework-1.0.jar Main
```

И чтоб использовать приложение, ты можешь зайти на `http://localhost:8080/hello`.

Итак, теперь я опишу пару вещей в коде.

### Code parts

1. Все обработчики запросов (например, `HelloHandler`) должны реализовать пустой интрефейс-маркер
[`github.dmitmel.raketaframework.web.handle.RequestHandler`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestHandler.java)
и быть аннотированы аннотацией
[`github.dmitmel.raketaframework.web.handle.RequestURLPattern`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestURLPattern.java).
Эта аннотация может установить Regexp для URL'ов, которые могут быть обработаными этим обработчиком. Поддерживаются
группы, ты можешь почитать про них в следуйших частях.

2. Все методы-обработчики должны иметь 1 параметер и возвращать значение. Ты можешь почитать более детализированую
информацию о сигнатурах
[здесь](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestMethod.java).
Также, методы-обработчики должны быть аннотированы аннотацией
[`github.dmitmel.raketaframework.web.handle.RequestMethod`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestMethod.java).
Эта аннотация описывает на каком веб-методе этот Java-метод будет использован. Но, ты не можешь установить
обработчик для `OPTIONS` - 
этот метод должен вернуть поддерживаемые методы.

3. Класс [`github.dmitmel.raketaframework.web.handle.RequestData`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestData.java)
хранит информацию о запросе.

<h2 id="params">Чтение параметров URL'ов</h2>

Ну, это хорошо что мы можем отправлять информацию пользователю, но что если пользователь хочет поговорить с нами? 
Давайте сделаем обработчик запросов который может возвращать несколько приветствий.

```java
// Это внутренний класс класса Main
@RequestURLPattern("/greet")
private static class Greeter implements RequestHandler {
    @RequestMethod("GET")
    public Document GET(RequestData requestData) {
        Document document = new Document();
        
        String who = requestData.getUrlParamOrElse("who", "World");
        int howMany = Integer.parseInt(requestData.getUrlParamOrElse("how-many", "1"));

        for (int i = 0; i < howMany; i++) {
            document.writeF("Hello, %s!\r\n", who);
        }
        
        return document;
    }
}
```

Не забудь добавить новый обработчик в список обработчиков!

```java
// Первый способ (добавить обработчик в конструкторе):
HandlersList handlersList = new HandlersList(new Greeter(), ...);
// Второй способ (использовать метод "add"):
handlersList.add(new Greeter());
```

Что нового?

1. Класс [`github.dmitmel.raketaframework.web.handle.Document`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/Document.java).
Он работает на подобии потока или `java.lang.StringBuilder` - ты можешь добавлять в него данные. Он хранит 
бинарные данные, так что ты можешь положить туда картинку.
2. Методы [`RequestData#getUrlParamOrElse`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestData.java#L27)
и [`RequestData#getUrlParam`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestData.java#L24).
Эти методы просто возвращают один из параметров URL'а.

Теперь, давайте поиспользуем класс `Greeter`!

```bash
# Представь, что сервер уже запущен
curl -i 'http://localhost:8080/greet?who=Dmitriy&how-many=5'
```

Вывод будет наподобии этого:

```http
HTTP/1.1 200 OK
Connection: close
Server: RaketaFramework/1.0 on Mac OS X 10.11.5
Content-Length: 85
Date: Thu, Jul 7, 2016 07:50:18 AM GMT
Content-Type: text/plain

Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
```

<h2 id="urls">Гибкие URL'ы</h2>

Как я сказал в первой части,

> Эта аннотация может установить Regexp для URL'ов, которые могут быть обработаными этим обработчиком. Поддерживаются
> группы, ты можешь почитать про них в следуйших частях.

Да, я говорю о Regexp'ах, URL'ах, и аннотации
[`github.dmitmel.raketaframework.web.handle.RequestURLPattern`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestURLPattern.java).
Время поговорить о них!

Давайте представим, что мы делаем обработчик для раздачи файлов. Клиент должен отправить путь к файлу. Например, 
он/она/оно может отправить путь с помощью параметра URL'а.

```java
// Использование: /load-file?path=...
@RequestURLPattern("/load-file")
private static class FileLoader implements RequestHandler {
    @RequestMethod("GET")
    public byte[] GET(RequestData requestData) {
        String fileName = requestData.getUrlParam("path");
        byte[] bytes = /* Немного кода для загрузки файлов... */;
        return bytes;
    }
}
```

Но, мы можем использовать Regexp'ы!

```java
// Использование: /load-file/...
@RequestURLPattern("/load-file/(.+)")
private static class FileLoader implements RequestHandler {
    @RequestMethod("GET")
    public byte[] GET(RequestData requestData) {
        String fileName = requestData.getMatcherGroup(0);
        byte[] bytes = /* Немного кода для загрузки файлов... */;
        return bytes;
    }
}
```

Методы [`RequestData#getMatcherGroup`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestData.java#L31)
и [`RequestData#getMatcherGroupOrElse`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RequestData.java#L34)
возвращают группы совпадения из Regexp'а URL'а.

<h2 id="forms">Получение форм</h2>

Много веб-приложений используют формы. Но как их можно получать используя RaketaFramework?

Давайте сделаем простое приветственное приложение.

##### В Main.java:
```java
@RequestURLPattern("/form-greeter")
private static class FormGreeter implements RequestHandler {
    @RequestMethod("GET")
    public String GET(RequestData requestData) {
        throw new RedirectionThrowable("/load-file/index.html");
    }
    
    @RequestMethod("POST")
    public String POST(WebFormData form) {
        if (form.getFormParam("name") == null) throw new Error404();
        if (form.getFormParam("greet") == null) throw new Error404();
        
        return String.format("%s %s!", form.getFormParam("greet"), form.getFormParam("name"));
    }
}
```

##### index.html:
```html
<!DOCTYPE html>

<html>
    <head>
        <title>Greet Form</title>
    </head>

    <body>
        <h2>Hello, User!<br></h2>
        
        <form action="/form-greeter" method="POST">
            <p>What's your name?</p>
            <p><input type="text" name="name" value="World" autocomplete="off"></p>
            <p>What greeting would you like<br>to have? (e.g. "Hi", "Hello")</p>
            <p><input type="text" name="greet" value="Hello" autocomplete="off"></p>
            <p><input type="submit" value="Submit"></p>
        </form>
    </body>
</html>
```

Это всё! Но, немного заметок о коде:

1. Ты можешь отсылать клиенту ошибки HTTP. Чтоб использовать их, ты должен просто вызвать исключение с именем 
[`Error${STATE}`](https://github.com/dmitmel/RaketaFramework/tree/master/src/gen/java/github/dmitmel/raketaframework/web/errors).
2. Чтобы перенаправить пользователя на другую страницу, ты должен вызвать исключение
[`github.dmitmel.raketaframework.web.handle.RedirectionThrowable`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/web/handle/RedirectionThrowable.java)

<h2 id="downloads">Загрузки</h2>

Бинарники - [`RaketaFramework-last.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last.jar)

Бинарники тестов - [`RaketaFramework-last-test.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last-test.jar)

Исходники - [`RaketaFramework-last-src.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last-src.zip)

<h2 id="license">Лицензия</h2>

Copyright (c) 2016 Meleshko Dmitriy

Лицензировано под Лицензией Apache, Версии 2.0. Ты можешь получить копию по ссылке

<pre><code><a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a></code></pre>
