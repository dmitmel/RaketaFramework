# RaketaFramework

#### This tutorial is too old. It'll be updated soon.

## Contents

1. [Hello World!](#hello)
2. [Reading URL parameters](#params)
3. [Flexible URLs](#urls)
4. [Receiving forms](#forms)
5. [Downloads](#downloads)
6. [License](#license)

<h2 id="hello">Hello World!</h2>

Everyone knows, that tutorials usually start from "Hello World!". I'll make exception. Our first program using
RaketaFramework will show "Hello World!" twice :). Well, let's write some code:

##### Main.java:
```java
import github.dmitmel.raketaframework.handle.*;
import github.dmitmel.raketaframework.server.*;

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

My congratulations! It's your first app! To run it from terminal, you can type something like this:

```bash
javac Main.java
java -cp libs/RaketaFramework-1.0.jar Main
```

And to use app, you can open page `http://localhost:8080/hello`.

Well, now I'll describe some things in code.

### Code parts

1. All request handlers (for example, `HelloHandler`) must implement empty marker-interface 
[`github.dmitmel.raketaframework.handle.RequestHandler`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestHandler.java)
and be annotated by
[`github.dmitmel.raketaframework.handle.RequestURLPattern`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestURLPattern.java).
This annotation can set Regexp for URLs, which can be handled by this handler. Groups are supported, you can read about them in
next parts.

2. All handler methods must have 1 parameter and return value. You can read more detailed info about signatures
[here](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestMethod.java).
Also, they must be annotated using annotation
[`github.dmitmel.raketaframework.handle.RequestMethod`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestMethod.java).
This annotation describes on which-method this Java-method will be used. But, you can't set handler for `OPTIONS` - 
this method must return supported-methods.

3. Class [`github.dmitmel.raketaframework.handle.RequestData`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestData.java)
stores data about request.

<h2 id="params">Reading URL parameters</h2>

Well, this's nice that we can send data to user, but what if user wants to talk with us? Let's make request handler
which will return some greetings.

```java
// This class is inner class of Main
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

Don't forget to add new handler to handlers list!

```java
// First way (add handler in constructor):
HandlersList handlersList = new HandlersList(new Greeter(), ...);
// Second way (use method "add"):
handlersList.add(new Greeter());
```

What's new?

1. Class [`github.dmitmel.raketaframework.handle.Document`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/Document.java).
It works like stream or `java.lang.StringBuilder` - you can append it's data. It stores binary data, so you can put there
image.
2. Methods [`RequestData#getUrlParamOrElse`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestData.java#L27)
and [`RequestData#getUrlParam`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestData.java#L24).
These methods simply return one of params in URL.

Now, let's use class `Greeter`!

```bash
# Imagine, that server has been already started
curl -i 'http://localhost:8080/greet?who=Dmitriy&how-many=5'
```

Output will be like this:

```http
HTTP/1.1 200 OK
Connection: close
Server: RaketaFramework/1.0.0 on Mac OS X 10.11.5
Content-Length: 85
Date: 8:14:04 PM
Content-Type: text/plain

Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
Hello, Dmitriy!
```

<h2 id="urls">Flexible URLs</h2>

As I said in the 1^st^ part,

> This annotation can set URL regexp, which can be handled by this handler. Groups are supported, you can read about them in
> next parts.

Yeah, I'm talking about Regexps, URLs, and annotation
[`github.dmitmel.raketaframework.handle.RequestURLPattern`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestURLPattern.java).
Time to talk about them!

Let's imagine, that we're making handler for serving files. Client must send path of file. For example, he/she/it can
send path using URL param.

```java
// Usage: /load-file?path=...
@RequestURLPattern("/load-file")
private static class FileLoader implements RequestHandler {
    @RequestMethod("GET")
    public byte[] GET(RequestData requestData) {
        String fileName = requestData.getUrlParam("path");
        byte[] bytes = /* Some code for loading files... */;
        return bytes;
    }
}
```

But, you can use Regexps!

```java
// Usage: /load-file/...
@RequestURLPattern("/load-file/(.+)")
private static class FileLoader implements RequestHandler {
    @RequestMethod("GET")
    public byte[] GET(RequestData requestData) {
        String fileName = requestData.getMatcherGroup(0);
        byte[] bytes = /* Some code for loading files... */;
        return bytes;
    }
}
```

Methods [`RequestData#getMatcherGroup`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestData.java#L31)
and [`RequestData#getMatcherGroupOrElse`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RequestData.java#L34)
return match groups from URL Regexp.

<h2 id="forms">Receiving forms</h2>

Many-applications use forms. But how to receive them using RaketaFramework?

Let's create simple greeting-app.

##### In the Main.java:
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

That's all! But, some notes about code:

1. You can send to client HTTP errors. To use them, you must just throw exception with name 
[`Error${STATE}`](https://github.com/dmitmel/RaketaFramework/tree/master/src/gen/java/github/dmitmel/raketaframework/errors).
2. To redirect user to other page, you must throw 
[`github.dmitmel.raketaframework.handle.RedirectionThrowable`](https://github.com/dmitmel/RaketaFramework/blob/master/src/main/java/github/dmitmel/raketaframework/handle/RedirectionThrowable.java)

<h2 id="downloads">Downloads</h2>

Binaries - [`RaketaFramework-last.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last.jar)

Test binaries - [`RaketaFramework-last-test.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last-test.jar)

Sources - [`RaketaFramework-last-src.jar`](https://raw.githubusercontent.com/dmitmel/RaketaFramework/master/build/RaketaFramework-last-src.zip)

<h2 id="license">License</h2>
Copyright (c) 2016 Meleshko Dmitriy

Licensed under the Apache License, Version 2.0. You can get a copy at

<pre><code><a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a></code></pre>
