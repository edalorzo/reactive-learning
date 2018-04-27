
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
