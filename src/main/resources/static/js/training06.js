
//CPS
function reverse0(word, ret) {
    ret(word.split('').reverse().join(''));
}

function reverse1(word) {
    return new Promise(function(resolve){
        resolve(word.split('').reverse().join(''));
    });
}

//reverse0("racecar", console.log);

var promise = reverse1("racecar");
promise.then(reversed => console.log(reversed));
console.log("End of Program");


const fs = require('fs');

fs.readFile('fruits.txt', function(error, data) {
    console.log('The file contains: ' + data);
});
