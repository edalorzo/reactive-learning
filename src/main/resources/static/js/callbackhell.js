
function multiply(x, y, ret) {
    //setTimeout(() => ret (x * y));
    ret (x * y);
}

function add(x, y, ret) {
    ret (x + y);
}

function square(x, ret) {
    multiply(x, x, ret);
}

function pythagoras(x, y, ret) {
    square(x, function(x_squared) {
        square(y, function(y_squared) {
            add(x_squared, y_squared, ret);
        });
    });
}


pythagoras(3,4, console.log); //yields 25


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

//fact(15000, console.log); //stackoverflow
fact(5, console.log); //120

