

//direct style
function reverse(word, ret) {
    return new Promise(function(resolve){
        resolve(word.split('').reverse().join(''));
    });
}


const promise = reverse("hello");
promise.then(console.log);
console.log("End of Program");


var fs = require('fs');

//direct style
function readFile(file) {
    return new Promise((resolve, reject) => {
        //CPS
        fs.readFile(file, function(err, buffer) {
            if(err) {
                reject(err);
            } else {
                resolve(buffer.toString('utf-8'));
            }
        });
    });
}

readFile('fruits.txt')
    .then(text => text.split('\n'), error => [])
    .then(fruits => fruits.map(reverse))
    .then(promises => Promise.all(promises))
    .then(reversed  => reversed.forEach(word => console.log(word)));



function multiply(x, y) {
    return new Promise(resolve => resolve(x * y));;
}

function add(x, y) {
    return new Promise(resolve => resolve(x + y));
}

function square(x ) {
    multiply(x, x);
}

function pythagoras(x, y) {
    return square(x).then(x_squared => square(y).then(y_squared => add(x_squared, y_squared)));
}
