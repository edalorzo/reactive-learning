# Table of Contents

- [Learning Reactive Programming](#learning-reactive-programming)
  * [Asynchronous Programming](#asynchronous-programming)
    + [Continuation Passing Style (CPS)](#continuation-passing-style-cps)
    + [Synchronous vs Asynchronous Code](#synchronous-vs-asynchronous-code)
    + [Error Handling in CPS](#error-handling-in-cps)
    + [Callbacks Problems](#callbacks-problems)
    + [Promises](#promises)
    + [Promisify a CPS Function](#promisify-a-cps-function)
    + [Error Handling with Promises](#error-handling-with-promises)
    + [Promises Beget Promises/Promise Chaining](#promises-beget-promisespromise-chaining)
    + [Async/Await vs Promises](#asyncawait-vs-promises)
    + [Error Handling with Async/Await](#error-handling-with-async-await)
    + [Non-Blocking I/O](#non-blocking-io)
    + [Java Reactive Types](#java-reactive-types)
    + [Other Reactive Types](#other-reactive-types)
    + [Using WireMock to Simulate HTTP Services](#using-wiremock-to-simulate-http-services)
    + [Using `curl` to Test Web Services](#using-curl-to-test-web-services)
    + [Other Useful Command Line Tools and Shortcuts](#other-useful-command-line-tools-and-shortcuts)
    + [Useful Tools](#useful-tools)
    + [Recommended Books](#recommended-books)
    + [Recommended Videos](#recommended-videos)
    + [Further Reference](#further-reference)

# Learning Reactive Programming

This project is a sandbox that I use to learn and practice reactive programming concepts.

## Asynchronous Programming

I will start by some introduction to asynchronous programming concepts that I found really useful as I gradually built my understanding of programming in a reactive way.

### Continuation Passing Style (CPS)

Perhaps the best explanation I have found so far on this topic is from [@mattmight's](http://matt.might.net/articles/by-example-continuation-passing-style/) blog.

I'm going to reproduce some of his examples here to reiterate my understanding while I try to build my own knowledge on top of his ideas and examples:

Continuation Passing Style is a way of writing code using only void functions. Instead of using a `return` statement we use a callback to provide the results back to the caller.

Consider the following identity function, where we replace the `return` statement for a callback which for convenience we call `ret`.

```javascript
function id(id, ret) {
    ret (id);
}
```

And we can very easily invoke this function by doing:

```javascript
id(12345, console.log); //prints 12345 to the main output
```

We can take any function written in "direct style" and replace it by a function written in CPS. All we have to do is replace any return statements with a callback call.

For example, if we had the following factorial function:

```javascript
function fact0(n) {
    if(n==0) {
        return 1;
    }
    return n * fact0(n - 1);
}
```

It could be written in continuation passing style as:

```javascript
function fact1(n, ret) {
    if(n == 0) {
        ret(1);
    } else {
        fact1(n - 1, n0 => ret (n * n0));
    }
}
```

And a client can easily invoke it by doing:

```javascript
fact(5, console.log); //prints 120 to main output
```

### Synchronous vs Asynchronous Code

Just by writing a function in CPS it does not become asynchronous. A function written in CPS is as synchronous as thier direct-style counterparts. Asynchrony is an entirely separate attribute of our programs.

To demonstrate that consider the following `reverse` function:

```javascript
function reverse(word, ret) {
    ret(word.split('').reverse().join(''));
}

reverse('hello', console.log);
console.log('end of program');
```

The code above yields the output:

```
olleh
end of program
```

Which clearly demonstrates that the code is synchronous, one statement follows the other and they are executed in the same order they were declared.

Consider now that we add a second function that determines if a given word is a palindrome:

```javascript
function isPalindrome(word, ret) {
    reverse(word, reversed => ret(word == reversed ? word : null));
}

isPalindrome("racecar", console.log); //yields "racecar"
isPalindrome("tomorrow", console.log); //yields null
```

This second function must use CPS too, since the `reverse` function uses CPS. It looks like once a function uses CPS, all of its users are forced to deal with the callback and turn themselves into CPS functions as well.

Now, what will happen if we made the `reverse` function truly asynchronous? Consider the following change:

```javascript
function reverse(word, ret) {
    setTimeout(() => ret(word.split('').reverse().join('')));
}

reverse('hello', console.log);
console.log('end of program');
```

Our program above now yields the following output:

```
end of program
olleh
```

Clearly, the order of the operations is no longer synchronous. The `setTimeout` function has made our `reverse` function asynchronous.

Notice that this function is asynchronous, but Node.js is still single-threaded. Which demonstrates that asynchrony does not need multiple threads to be implemented. An event-loop (where we can schedule work for later execution) should suffice as in this case.

Now the fundamental question here is whether `isPalindrome` need suffer any modifications after we changed `reverse` to be asynchronous, or whether it will continue to work after we did such fundamental change in the nature of our `reverse` function. 

Our `isPalindrome` function will continue to work just fine without any modifications, it is just that now, it is also asynchronous.

```javascript
isPalindrome("racecar", console.log);
console.log("end of program");
```

Now yields the output:

```
end of program
racecar
```

This is, perhaps, one of the key characteristics of CPS, the power to go from synchronous to asynchronous without having to make any changes in the code.

So, the key tenets for me, so far, are:

* I can write any function in CPS.
* A function written in CPS is not by default asynchronous.
* A function written in CPS is compatible with syncrhonous and asynchronous functions.
* A function written in CPS causes all its callers to be written in CPS.
* A function written in CPS is compatible with asynchronous execution.

### Error Handling in CPS

In direct style we are used to using try-catch-finally blocks to control the flow of the program in the case of errors or exceptional conditions.

Consider the following example of a function that gives us the name of the day of the week:

```javascript
function getDayOfWeek0(day) {
    
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];

    if(day >= 1 && day <= 7) {
        return days[day-1];
    }

    throw new Error("Invalid day of the week: " + day);
}
```

In direct style, a client of this function could do something as follows:

```javascript
try {
    const dayName = getDayOfWeek0(myDay);
    console.log("The day of the week is " + dayName);
}
catch(e) {
    console.log("The day of the week is unknown");
}
```

In CPS, we replace any `catch` and `finally` clauses with corresponding callbacks. So our function could be expressed as:

```javascript
function getDayOfWeek1(day, ret, thro) {
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];

    if(day >= 1 && day <= 7) {
        ret (days[day-1]);
    } else {
        thro (new Error("Invalid day of the week: " + day));
    }
}
```

And a client of this function would do something as follows:

```javascript
getDayOfWeek1(myDay,
              dayName => console.log("The day of the week is " + dayName),
              err => console.log('The day of the week is unknown'));
```

Something worth mentioning is that once written in CPS, the clients of the function are not expecting to handle any exceptions in a direct style anymore. Therefore, if we make a mistake while writing our `getDayOfWeek1` function such that it might still throw an exception, then the clients of the function are (most likely) going to be unprepared to deal with it, since they are expecting any errors to arrive through the CPS callback and not in a direct style exception handling way.

This raises an interesting question: how to deal with input validation, e.g. in the case of e.g. `ret` or `thro` are null or invalid arguments?

Of course passing that passing invalid callback arguments could be considered a programming mistake, a bug in our code. There is no escape but to throw those back in direct style. But it is still worth considering how our programs will behave if we mistakenly introduce direct style exception propagation in our CPS written programs.


### Callbacks Problems

Callbacks have two major problems:

**Nested call stack**: if the code is synchronous, and the language does not support tail call optimizations, then every continuation adds to the call stack leading to a potential stack overflow error.

For example, Node.js v9.11.1 (the version I'm using for these examples) does not support tail call optimizations. So even the following function written in tail recursive way causes a stack overflow error:

```javascript
function fact(n, ret) {
    function fact_tail_rec(n, a, ret) {
        if(n == 0) {
            ret (a);
        } else {
            multiply(n, a, m => fact_tail_rec(n-1, m, ret));
        }
    }
    fact_tail_rec(n, 1, ret);
}

fact(15000, console.log); //stackoverflow
```

A typically solution to this problem would be to actually make the function work asynchronously, that way the it would complete immediately and relinquish the call stack. For example, we could make the function above become asynchronous my making the `multiply` function asynchronous:

```javascript
function multiply(x, y, ret) {
    setTimeout(() => ret (x * y));
}
```

The invocation to `fact(15000, console.log)` would take a while to complete, and would yield an `Infinity` result since the result is beyond the supported arithmetic boundaries of JavaScript, but it would not cause a stackoverflow.


**Callback hell**: it is a real pain to write nested functions. The code becomes harder to read, to follow, to reason about and to maintain.

```javascript
function multiply(x, y, ret) {
    ret (x * y);
}

function add(x, y, ret) {
    ret (x + y);
}

function square(x, ret) {
    multiply(x, x, ret);
}

function pythagoras(x, y, ret) {
    square(x, function (x_squared) {
        square(y, function (y_squared) {
            add(x_squared, y_squared, ret);
        });
    });
}

pythagoras(3, 4, console.log);
```

There are a [few ways to organize](http://callbackhell.com) our code when using CPS such that we keep callback hell under control and there are interesting libraries (like [async.js](https://caolan.github.io/async/)) that can help us write better code, but in general there's not easy way to dodge this bullet. 

Alternatively, these days it is customary to replace callbacks with promises.

### Promises

An alternative to deal with callback hell is to replace CPS with a direct style that returns a promise. 

A promise is just an object that is returned by function instead of the actual result. The promise has three possible states:

* Before the result is ready, the Promise is _pending_.
* If a result is available, the Promise is _fulfilled_.
* If an error happened, the Promise is _rejected_.

We can register callbacks to react to promise state changes. The callback can provide the result of the function when the promise is resolved or provides an error when the promise has been rejected.

```javascript
function reverse(word) {
    return new Promise(
        resolve => resolve(word.split('').reverse().join(''))
    );
}
```

The promise state callbacks are asynchronous by default. The following program yields "End of Program" first and then "olleh".

```javascript
reverse("hello").then(console.log);
console.log("End of Program");
```

### Promisify a CPS Function

The promise does not get fulfilled until either it is resolved or rejected and this could happen asynchronously. For example, consider the following code that solves the promise after 2 seconds have passed:

```javascript
function reverse(word) {
    return new Promise(
        resolve => setTimeout(
            () => resolve(word.split('').reverse().join('')), 2000
        )
    );
}
```

We also can take existing code using callbacks and convert it to use promises. We call this "promisifying" a function.

Consider the following example where the Node.js, asynchronous `readFile` function is converted from a callback/CPS style to a direct style with promises.

```javascript
var fs = require('fs');

function readFile(file) {
    return new Promise((resolve, reject) => {
        fs.readFile(file, function(err, buffer) {
            if(err) {
                reject(err);
            } else {
                resolve(buffer.toString('utf-8'));
            }
        });
    });
}
```

### Error Handling with Promises

Promises still use callbacks to handle state change events inside the promise and as such it follows the CPS style to deal with errors, i.e. there is a second callback to deal with error case.

The following promise either receives the `text` of the file successfully read or an `error` for a file that could not be read for any reason, e.g. file not found.

```javascript
readFile('fruits.txt')
    .then(text => text.split('\n'),
          error => []);
```

### Promises Beget Promises/Promise Chaining

Every callback of a promise produces a new promise on which we can register other callbacks. This allows us to create a pipeline.

```javascript
readFile('fruits.txt')
    .then(text => text.split('\n'), error => [])
    .then(fruits => fruits.map(reverse))
    .then(promises => Promise.all(promises))
    .then(reversed  => reversed.forEach(word => console.log(word)));
```

### Async/Await vs Promises

Although promises are a very elegant solution that reduce some of complexity of writing code in CPS, the truth is that they still make use of callbacks to handle state changes. In certain cases, even with promises, the code looks a bit cumbersome, like our Pythagorean function below, implemented using promises:

```javascript
function pythagoras(x, y) {
    return square(x).then(x_squared => square(y).then(y_squared => add(x_squared, y_squared)));
}
```

More recently, a number of programming languages have introduced support for two new keywords known as `async` and `await`. In JavaScript the `async` keyword allows us to create a promise out some apparently direct style code:

```javascript
async function multiply(x, y) {
    return x * y;
}

async function add(x, y) {
    return x + y;
}
```

So, the functions `multiply` and `add` above, actually return promises.

We can also use `await` with functions that already return promises:

```javascript
async function square(x) {
    return multiply(x, x);
}
```

Another characteristics of an `async` function is that inside its body we can call any other async functions and subscribe to their callbacks in what appears to be direct style programming using the `await` keyword:

```javascript
async function pythagoras(x, y) {
    var x_squared = await square(x);
    var y_squared = await square(y);
    return add(x_squared, y_squared);
}
```

The code above is equivalent to our previous example in which we used promises, but the `await` keyword seems to imply this code is in direct style, when in fact it is just syntactic sugar for asynchronous, promised-based code. The `await` keyword is just syntactic sugar for subscribing to the `then` callback in the promise.

### Error Handling with Async/Await

Likewise, async/await can help us simulate a direct style of error handling, using try/catch statements.

```javascript
async function getFruits() {
    try {
        var fruits = await readFile('fruits.txt');
        return fruits.split('\n');
    } catch(error) {
        console.log(error);
        return [];
    }
}
```

The catch is just syntactic sugar for subscribing to the error callback in the promise, but under the hood this is still the same program as before. The whole thing still uses promises.

```javascript
getFruits().then(fruits => fruits.map(reverse))
    .then(promises => Promise.all(promises))
    .then(reversed  => reversed.forEach(word => console.log(word)));
```

### Non-Blocking I/O

All this knowledge becomes relevant when we realize that in the moment that we need to do some I/O operation, like reading file or making an HTTP request, we want to avoid blocking our current thread of execution and in Node.js and JavaScript this is of paramount importance since the entire environment is single-threaded.

It is interesting because the operating system already works in a non-blocking way. It is our programming languages that were modeled in a blocking manner.

As an example, imagine that you had a computer with a single CPU. Any I/O operation that you do will be orders of magnitude slower than the CPU, right?. Say you want to read a file or do an HTTP request. Do you think the CPU will stay there, idle, doing nothing while the disk head goes and fetches a few bytes and puts them in the disk buffer or while the some arrive through the network interface? Obviously not. The operating system will register an interruption (i.e. a callback) and will use its valuable, single CPU for something else in the mean time. When the disk head has managed to read a few bytes and made them available to be consumed, an interruption will be triggered and the OS will then give attention to it, restore the previous process block and allocate some CPU time to handle the available data.

So, in this case, the CPU is like a thread in your application. It is never blocked. It is always doing some CPU-bound stuff.

The idea behind NIO programming is the same. In the example below, our Node.js application runs in a single thread, and it makes an HTTP request to get some fruits data. Immediately after we place the request our valuable single thread is released to attend other tasks, while we wait for some callback to be resolved.  In our example below, while we're waiting for the data to arrive, Node.js uses its single thread to do some other work, represented by the interval function that we schedule.

When the callback resolves, it will be automatically scheduled to be processed by your single thread.

As such, that thread works as an event loop, one in which we're supposed to schedule only CPU bound stuff. Every time we need to do I/O, that's done in a non-blocking way and when that I/O is complete, some CPU-bound callback is put into the event loop to deal with the response.

**The Callback Way**

```javascript
const http = require('http');

function getFruits(ret, thro) {

    const options = {
        hostname: '127.0.0.1',
        port: 4040,
        path: '/fruits'
    };

    // Make a request
    const req = http.request(options);
    req.setHeader('Accept','application/json');
    req.end(); //flushes the request

    req.on('response', (res) => {

        let payload = "";
       
        res.on('data', data => payload += data.toString('utf-8'));
        res.on('end', () => ret (JSON.parse(payload)));
        
    });

    req.on('error', error => thro (error));
}

getFruits(console.log, console.log);

let count = 0;
const interval = setInterval(() => {
    count++;
    if(count > 5) {
        return clearInterval(interval);
    }
    console.log("I'm still alive and kicking!!!");
}, 1000);

console.log("End of Program");
```

**The Promise Way**

```javascript
const http = require('http');

function getFruits() {

    return new Promise((resolve,reject) => {

        const options = {
            hostname: '127.0.0.1',
            port: 4040,
            path: '/fruits'
        };

        // Make a request
        const req = http.request(options);
        req.setHeader('Accept','application/json');
        req.end(); //flushes the request

        req.on('response', res => {

            let payload = "";
            
            res.on('data', data => payload += data.toString('utf-8'));
            res.on('end', () => resolve(JSON.parse(payload)));
            
        });

        req.on('error', error => reject(error));
        
    });
}

async function reverse(word) {
    return word.split('').reverse().join('');
}


async function printFruitsReversed() {
    const fruits = await getFruits();
    fruits.forEach(async (fruit) => {
        const reversed = await reverse(fruit);
        console.log(reversed);
    });
    
}

// alternatively:
// getFruits().then(fruits => fruits.map(reverse))
//     .then(promises => Promise.all(promises))
//     .then(reversed  => reversed.forEach(word => console.log(word)));

printFruitsReversed();

let count = 0;
const interval = setInterval(() => {
    count++;
    if(count > 5) {
        clearInterval(interval);
        return;
    }
    console.log("I'm still alive and kicking!!!");
}, 1000);

console.log("End of Program");
```

This is a powerful concept, because with a very small amount threads we can process thousands of requests and therefore we can scale more easily. Do more with less.

This feature is one of the major selling points of [Node.js](https://nodejs.org/en/) and the reason why even using a single thread it can be used to develop backend applications.

Likewise this is the reason for the proliferation of frameworks like:

* [Netty](http://netty.io)
* [Reactive Streams Initiative](http://www.reactive-streams.org) 
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Project Reactor](https://projectreactor.io) 

They all are seeking to promote this type of optimization and programming model.

There is also an interesting movement of new frameworks that leverage this powerful features and are trying to compete or complement one another. I'm talking of interesting projects like:

* [Vert.x](http://vertx.io)
* [Ratpack](https://ratpack.io) 

I'm pretty sure there must be many more out there for other languages.

### Java Reactive Types

In Java, the closes type we have to a promise is `CompletableFuture`, with it we can implement a similar behavior to what we have done so far:

```java
//direct style
String reverse0(String word) {
    return new StringBuilder(word).reverse().toString();
}

//continuation passing style
void reverse1(String word, Consumer<String> ret) {
    ret.accept(new StringBuilder(word).reverse().toString());
}

//promise style
CompletableFuture<String> reverse2(String word) {
    return CompletableFuture.completedFuture(new StringBuilder(word).reverse().toString());
}
```

All these examples are completly synchronous and the steps below run in the exact order they are programmed.

```java
System.out.println(reverse0("hello"));
reverse1("hello", System.out::println);
reverse2("hello").thenAccept(System.out::println);
```

We could also implement our Pythagorean example from before:

```java
CompletableFuture<Integer> add(int x, int y) {
    return CompletableFuture.completedFuture(x + y);
}

CompletableFuture<Integer> multiply(int x, int y) {
    return CompletableFuture.completedFuture(x * y);
}

CompletableFuture<Integer> square(int x) {
    return multiply(x, x);
}

CompletableFuture<Integer> pythagoras(int x, int y) {
    return square(x).thenCompose(squareOfx -> 
        square(y).thenCompose(squareOfy -> add(squareOfx, squareOfy))
    );
}
```

And our client would simply do:

```java
pythagoras(3,4).thenAccept(System.out::println); //yields 25
```

Our code could become asynchronous if we run any of the tasks in a separete thread:

```java
CompletableFuture<Integer> multiply(int x, int y) {
    return CompletableFuture.supplyAsync(() -> x * y);
}
```

Then our client would do:

```java
pythagoras(3,4).thenAccept(System.out::println); //yields 25
System.out.println("End of Program");
Thread.sleep(1000);
```

The `Thread.sleep(1000)` at the end is just to avoid that our program ends before completing the promise, since if a Java program ends it does not wait by default for any pending futures to complete.

And of course, we also have a way to make the promise report any errors occurred during the operation of the function:

```java
private static CompletableFuture<String> getDayOfWeek1(int day) {
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};

    CompletableFuture<String> promise = new CompletableFuture<>();
    if (day >= 1 && day <= 7) {
        promise.complete(days[day - 1]);
    } else {
        promise.completeExceptionally(new IllegalArgumentException("Invalid day of the week: " + day));
    }

    return promise;
}
```

And our client could simply do:

```java
getDayOfWeek1(0).handle((day, error) -> {
    if(error != null) {
        return "The day is unknown";
    }
    return day;
}).thenAccept(System.out::println); //yiels "The days is unknown"
```

### Other Reactive Types

TBD

* Netty uses promises.
* Vert.x uses callbacks.
* Ratpack uses promises.
* Spring uses reactive streams.

### Using WireMock to Simulate HTTP Services

TBD

### Using `curl` to Test Web Services

The following are a few examples of how to use `curl` to test Web Services:

A `GET` request

```text
curl -s -H "Accept:application/json" -X GET http://localhost:8080/order/12345
```

A `POST` request

```text
curl -i -H "Content-Type:application/json" -X POST https://localhost:8080/customer -d '{"email":"edwin@dalorzo.com","name":"Edwin Dalorzo"}'
```

A `POST` request with a file as payload:

```text
curl -i -X POST http://localhost:8080/customer -H "Content-Type:application/xml" -d @customer-request.xml
```

**Where**:

* `-k`  `--insecure` allows curl to make insecure https calls.
* `-i`  `--include` includes the http headers in the output.
* `-X`  `--request`  specifies the request method (i.e. GET, PUT, POST, DELETE, etc).
* `-H`  `--header`   provides a header key value pair.
* `-d`  `--data`     specifies the body of the request, if preceded with @ represents a payload file.
* `-s`  `--silent`   don't show progress meter or error message. Just the output of the service.

If we're getting JSON back and it is not pretty-printed, we can format it by using a command-line program like [Jq](https://stedolan.github.io/jq/), which is a command-line tool to manipulate and format JSON.

```text
curl -s -H "Accept:application/json" -X GET http://localhost:8080/order/12345 | jq
```

If the response is in XML, then you may consider using [Xmllint](http://xmlsoft.org/xmllint.html) for the same purpose:

```text
curl -s -H "Accept:application/xml -X GET http://localhost:8080/customer/12345 | xmllint --format -
```

We can also obtain response times statistics by adding the `-w` flag to the command line parameters and we can provide a file detailing the variables we want to get back:

```text
curl -s -H "Accept:application/json" -w @curl-format.txt -X GET http://localhost:8080/order/12345
```

Where `curl-format.txt` contains the following:

```text
    time_namelookup:  %{time_namelookup}\n
       time_connect:  %{time_connect}\n
    time_appconnect:  %{time_appconnect}\n
   time_pretransfer:  %{time_pretransfer}\n
      time_redirect:  %{time_redirect}\n
 time_starttransfer:  %{time_starttransfer}\n
                    ----------\n
         time_total:  %{time_total}\n
```

### Other Useful Command Line Tools and Shortcuts

You can discover the number of cores you have in your MacOs by doing

```text
sysctl -n hw.ncpu
```

In Java `jshell` we can also do a:

```text
jshell> Runtime.getRuntime().availableProcessors();
``` 

### Useful Tools

* [JMeter](https://jmeter.apache.org/)
* [WireMock](http://wiremock.org/docs/running-standalone/)
* [Jq](https://stedolan.github.io/jq/)
* [Xmllint](http://xmlsoft.org/xmllint.html)

### Recommended Books

* [JavaScript with Promises](http://my.safaribooksonline.com/book/programming/javascript/9781491930779)
* [Exploring ES6](https://leanpub.com/exploring-es6)

### Recommended Videos

* [Help, I'm Stuck in the Event Loop](https://vimeo.com/96425312)

### Further Reference

* [Continuation Passing Style in JavaScript](http://matt.might.net/articles/by-example-continuation-passing-style/)
* [Async/Await in JavaScript](http://matt.might.net/articles/by-example-continuation-passing-style/)
* [Understanding Reactive Types](https://spring.io/blog/2016/04/19/understanding-reactive-types)
* [Reactive Programming vs Reactive Systems](https://www.oreilly.com/ideas/reactive-programming-vs-reactive-systems)
* [Parallel vs concurrent in Node.js](https://bytearcher.com/articles/parallel-vs-concurrent/)
* [What's the difference between a continuation and a callback?](https://stackoverflow.com/q/14019341/697630)
* [Callback Hell](http://callbackhell.com)
