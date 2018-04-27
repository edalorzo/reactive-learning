
async function multiply(x, y) {
    return x * y;
}

async function add(x, y) {
    return x + y;
}

async function square(x) {
    return multiply(x, x);
}

async function pythagoras(x, y) {
    var x_squared = await square(x);
    var y_squared = await square(y);
    return add(x_squared, y_squared);
}

function reverse(word) {
    return new Promise(
        resolve => setTimeout(
            () => resolve(word.split('').reverse().join('')), 2000
        )
    );
}

var fs = require('fs');

async function readFile(file) {
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

async function getFruits() {
    try {
        var fruits = await readFile('fruits.txt');
        return fruits.split('\n');
    } catch(error) {
        console.log(error);
        return [];
    }
}


getFruits().then(fruits => fruits.map(reverse))
    .then(promises => Promise.all(promises))
    .then(reversed  => reversed.forEach(word => console.log(word)));


pythagoras(3,4).then(console.log);
console.log("End of Program");
