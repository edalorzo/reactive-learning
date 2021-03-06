-> Direct Style <-
==================

Consider the following identity function, written in the traditional direct style:

```js
function identity(id) {
	return id;
}
```

As in

```js
console.log(identity("Hello world"));
$> Hello world
```

-----------------------------------------------------------------------------------------------------------------

-> Continuation Passing Style <-
================================

_Continuation Passing Style_ (CPS) is a way of writing code using only **void** functions. 
Instead of using a **return** statement we use a _callback function_ to provide the results back to the caller.
For example, consider the following identity function, where we replace the **return** statement for a callback 
which for convenience we call `ret`.

```js
function identity(id, ret) {
    ret (id);
}
```

As in:

```js
id("Hello World", console.log);
$> Hello world
```

-----------------------------------------------------------------------------------------------------------------

-> From Direct Style to Continuation Passing Style <-
=====================================================

We can take any function written in "direct style" and replace it by a function written in CPS. 
All we have to do is replace any return statements with a callback call.

For example, if we had the following factorial function:

```js
function factorial(n) {
    if(n==0) {
        return 1;
    }
    return n * factorial(n - 1);
}
```

It could be written in continuation passing style as:

```js
function factorial(n, ret) {
    if(n == 0) {
        ret(1);
    } else {
        factorial(n - 1, n0 => ret (n * n0));
    }
}
```

-----------------------------------------------------------------------------------------------------------------
-> CPS Call Stack Example <-
=====================================================

Consider how the call stack folds and then unfolds in the invocation of a CPS factorial function.

```js
factorial(5, console.log);
factorial(4, n0 => console.log(5 * n0));
factorial(3, n1 => (n0 => console.log(n * n0))(4 * n1));
factorial(2, n2 => (n1 => (n0 => console.log(n * n0))(4 * n1))(3 * n2));
factorial(1, n3 => (n2 => (n1 => (n0 => console.log(n * n0))(4 * n1))(3 * n2))(2 * n3));
factorial(0, (n3 => (n2 => (n1 => (n0 => console.log(n * n0))(4 * n1))(3 * n2))(2 * n3))(1));

factorial(1, n3 => (n2 => (n1 => (n0 => console.log(n * n0))(4 * n1))(3 * n2))(2 * 1));
factorial(2, n2 => (n1 => (n0 => console.log(n * n0))(4 * n1))(3 * 2 * 1));
factorial(3, n1 => (n0 => console.log(n * n0))(4 * 3 * 2 * 1));
factorial(4, n0 => console.log(5 * 4 * 3 * 2 * 1));
console.log(120)
$> 120
```

-----------------------------------------------------------------------------------------------------------------
-> Error Handling in Direct Style <-
=====================================================

In direct style we use a try-catch-finally block to control the flow of the program in the case of errors or exceptional conditions.

Consider the following example of a function that gives us the name of the day of the week:

```js
function getDayOfWeek0(day) {
    
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];
    
    if(day >= 1 && day <= 7) {
        return days[day-1];
    }
    
    throw new Error("Invalid day of the week: " + day);
}
```
`
In direct style, a client of this function could do something as follows:

```js
try {
    const dayName = getDayOfWeek0(myDay);
    console.log("The day of the week is " + dayName);
}
catch(e) {
    console.log("The day of the week is unknown");
}
```

-----------------------------------------------------------------------------------------------------------------
-> Error Handling in CPS <-
=====================================================

In CPS, we replace any `catch` and `finally` clauses with corresponding callbacks. So our function could be expressed as:

```js
function getDayOfWeek1(day, ret, thro, fin) {
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];
    
    try{
        if(day >= 1 && day <= 7) {
            ret (days[day-1]);
        } else {
            thro (new Error("Invalid day of the week: " + day));
        }
    } finally {
        fin();
    }
}
```

And a client of this function would do something as follows:

```js
getDayOfWeek1(myDay,
              dayName => console.log("The day of the week is " + dayName),
              err => console.log('The day of the week is unknown'));
```

-----------------------------------------------------------------------------------------------------------------
-> Food for Thought <-
=====================================================

What could happen if for any reason the invocation of any of the callbackes `ret`, `thro` of `fin` 
threw an exception?

This raises an interesting question: how to deal with input validation, e.g. in the case of e.g. ret or thro are null or invalid arguments?

```js
getDayOfWeek1(null,
              dayName => console.log("The day of the week is " + dayName),
              err => console.log('The day of the week is unknown'));
```              

Of course passing that passing invalid callback arguments could be considered a programming mistake, a bug in our code. There is no escape,
but to throw those back in direct style. But it is still worth considering how our programs will behave if we mistakenly introduce 
direct style exception propagation in our CPS written programs.

-----------------------------------------------------------------------------------------------------------------
-> Sound Familiar? <-
=====================================================

Which of the following alternatives is the best way to write reactive code?

```java
Single<Foo> getReactiveFoo(Bar bar, Baz baz, int price) {
     Objects.requireNonNull(bar, "bar should not be null");
     Objects.requireNonNull(baz, "baz should not be null");
     Preconditions.checkArgument(price > 0, "price should be > 0");
     return Single.fromCallable(() -> {
           //...
     });
}
```

```java
Single<Foo> getReactiveFoo(Bar bar, Baz baz, int price) {
     return Single.fromCallable(() -> {
           Objects.requireNonNull(bar, "bar should not be null");
           Objects.requireNonNull(baz, "baz should not be null");
           Preconditions.checkArgument(price > 0, "price should be > 0");
           //...
     });
}
```

-----------------------------------------------------------------------------------------------------------------
-> Callbacks Are Still Synchronous <-
=====================================================

Just by writing a function in CPS it does not become asynchronous. A function written in CPS is as synchronous 
as thier direct-style counterparts. Asynchrony is an entirely separate attribute of our programs.

To demonstrate that consider the following reverse function:

```js
function reverse(word, ret) {
    ret(word.split('').reverse().join(''));
}
    
reverse('hello', console.log);
console.log('end of program');
````

Which yields:

```
$> end of program
$> olleh
```

Which clearly demonstrates that the code is synchronous, one statement follows the other and they are executed 
in the same order they were declared.

-----------------------------------------------------------------------------------------------------------------
-> CPS is Infectious <-
=====================================================

Consider now that we add a second function that determines if a given word is a palindrome:

```
function isPalindrome(word, ret) {
    reverse(word, reversed => ret(word == reversed ? true : false));
}
      
isPalindrome("racecar", console.log); //yields true
isPalindrome("tomorrow", console.log); //yields false
```

Can you find a way to write the `isPalindrome` function in direct style, but using the CPS `reverse` function?
<br>

It looks like once a function uses CPS, all of its users are forced to deal with the callback and turn themselves 
into CPS functions as well.

-----------------------------------------------------------------------------------------------------------------
-> CPS with Asynchronous Functions <-
=====================================================

Now, what will happen if we made the `reverse` function truly asynchronous?

```js
function reverse(word, ret) {
    setTimeout(() => ret(word.split('').reverse().join('')));
}

reverse('hello', console.log);
console.log('end of program');
```
`
Our program above now yields the following output:

```
$> end of program
$> olleh
```

Clearly, the order of the operations is no longer synchronous. The `setTimeout` function has made 
our `reverse` function asynchronous.

JavaScript is still single-threaded, which demonstrates that asynchrony does not need multiple threads
to be implemented. An event-loop (where we can schedule work for later execution) should suffice as in this case.

-----------------------------------------------------------------------------------------------------------------
-> How Does Asynchrony Affects Execytion of isPalindrome? <-
============================================================

Now the fundamental question here is whether `isPalindrome` needs to suffer any modifications after we changed 
the `reverse` to be to be asynchronous, or whether it will continue to work after such fundamental change 
in the nature of function execution.

```js
isPalindrome("racecar", console.log);
console.log("end of program");
```

It yields:

```
$> end of program
$> true
```

It seems that `isPalindrome` function will continue to work just fine without any modifications, it is just that now, 
it is also asynchronous.

This is, perhaps, one of the key characteristics of CPS, the power to go from synchronous to asynchronous 
without having to make any changes in the code.

-----------------------------------------------------------------------------------------------------------------
-> Tenets of Continuation Passing Style <-
=====================================================

So, the key tenets for me, so far, are:

* I can write any function in CPS.
* A function written in CPS is not by default asynchronous.
* A function written in CPS is compatible with syncrhonous and asynchronous functions.
* A function written in CPS causes all its callers to be written in CPS.

-----------------------------------------------------------------------------------------------------------------
-> Callbacks Problems: Nested Call Stack <-
=====================================================

**Nested call stack**: if the code is synchronous, and the language does not support 
tail call optimizations, then every continuation adds to the call stack leading to 
a potential stack overflow error.

For example, Node.js v9.11.1 (the version I'm using for these examples) does not 
support tail call optimizations. So even the following function written in 
tail recursive way causes a stack overflow error:

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

-----------------------------------------------------------------------------------------------------------------
-> How to Fix Stackoverflow Errors? <-
=====================================================

A typically solution to this problem would be to actually make the function work 
asynchronously, that way the it would complete immediately and relinquish the call stack. 
For example, we could make the function above become asynchronous my making the `multiply` 
function asynchronous:

```javascript
function multiply(x, y, ret) {
    setTimeout(() => ret (x * y));
}
```

The invocation to `fact(15000, console.log)` would take a while to complete, 
and would yield an `Infinity` result since the result is beyond the supported 
arithmetic boundaries of JavaScript, but it would not cause a stackoverflow.

-----------------------------------------------------------------------------------------------------------------
-> Callback Hell <-
=====================================================

**Callback hell**: it is a real pain to write nested functions. The code becomes 
harder to read, to follow, to reason about and to maintain.

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

-----------------------------------------------------------------------------------------------------------------
-> How to Fix Callback Hell? <-
=====================================================

There are a [few ways to organize](http://callbackhell.com) our code when using CPS 
such that we keep callback hell under control and there are interesting libraries 
(like [async.js](https://caolan.github.io/async/)) that can help us write better code, 
but in general there's not easy way to dodge this bullet. 

Alternatively, these days it is customary to replace callbacks with **promises**.

-----------------------------------------------------------------------------------------------------------------
-> Promises <-
=====================================================

An alternative to deal with callback hell is to replace CPS with a direct style that 
returns a promise. 

A promise is just an object that is returned by the function instead of the actual result. 
The promise has three possible states:

* Before the result is ready, the Promise is _pending_.
* If a result is available, the Promise is _fulfilled_.
* If an error happened, the Promise is _rejected_.

We can register callbacks to react to promise state changes. The callback can provide 
the result of the function when the promise is resolved or provides an error when 
the promise has been rejected.

```javascript
function reverse(word) {
    return new Promise(
        resolve => resolve(word.split('').reverse().join(''))
    );
}
```
