
//direct style
function id0(id) {
    return id;
}

//continuation passing style
function id1(id, ret) {
    ret (id);
}


// 5! = 5 * 4 * 3 * 2 * 1 = 120
function fact0(n) {
    if(n == 0) {
        return 1;
    }
    return n * fact0(n - 1);
}

function fact1(n, ret) {
    if(n == 0) {
        ret(1);
    } else {
        fact1(n-1, n0 => ret(n * n0));
    }
}

console.log(id0("hello")); //direct style
id1("hello", console.log); //CPS

console.log(fact0(5)); //direct style -> 120
fact1(5, console.log); //CPS -> 120


// fact(5)
// 5 * fact(4)
// (5 * (4 * fact(3)))
// (5 * (4 * (3 * fact(2))))
// (5 * (4 * (3 * (2 * fact(1)))))
// (5 * (4 * (3 * (2 * (1 * fact(0))))))
// (5 * (4 * (3 * (2 * (1 * (1))))))
