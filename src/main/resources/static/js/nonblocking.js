
const http = require('http');

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
    
    res.on('data', (data) => {
        payload += data.toString('utf-8');
    });

    res.on('end', () => {
        console.log(`The payload is ${payload}`);
    });
  
});

req.on('error', (error) => {
    console.log(error);
});
