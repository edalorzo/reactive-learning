
function multiply(x, y) {
    return new Promise(resolve =>  {
        console.log("Multiplying " + x + " * " + y);
        resolve(x * y);
    });
}

function add(x, y) {
    return new Promise(resolve => resolve(x + y));
}

function square(x) {
    return multiply(x, x);
}

function pythagoras(x, y) {
    return square(x).then(x_squared => square(y).then(y_squared => add(x_squared, y_squared)));
}

//pythagoras(3,4).then(console.log);

    
function reverse(word) {
    return new Promise(
        resolve => setTimeout(
            () => resolve(word.split('').reverse().join('')), 2000
        )
    );
}

function isPalindrome(word, ret) {
    return reverse(word)
        .then(reversed => word == reversed ? word : null);
}


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

//reverse("hello").then(console.log);
//isPalindrome("rotor").then(console.log);

readFile('fruits.txt')
    .then(text => text.split('\n'), error => [])
    .then(fruits => fruits.map(reverse))
    .then(promises => Promise.all(promises))
    .then(reversed  => reversed.forEach(word => console.log(word)));

console.log("End of Program");




