

async function multiply(x, y) {
    return x * y;
}

async function add(x, y) {
    return x + y;
}

async function square(x ) {
    return multiply(x, x);
}

async function pythagoras(x, y) {
    var x_squared = await square(x);
    var y_squared = await square(y);
    return add(x_squared, y_squared);
}

pythagoras(3,4).then(console.log);
