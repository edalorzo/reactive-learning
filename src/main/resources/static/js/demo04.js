

function multiply(x, y, ret) {
    ret (x * y);
}

function add(x, y, ret) {
    ret (x + y);
}

function square(x, ret) {
    multiply(x, x, ret);
}


//pythagoras a2 +  b2 = c2

function pythagoras(x, y, ret) {
    square(x, function(x_squared) {
        square(y, function(y_squared) {
            add(x_squared, y_squared, ret);
        });
    });
}


pythagoras(3, 4, console.log);

