
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
