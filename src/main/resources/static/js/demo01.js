
// direct style
function id0(id) {
    retun id;
}

//continuation passing style
function id1(id, ret) {
    ret(id);
}

// 5! = 5 * 4 * 3 * 2 * = 120
function fact0(n) {
    if(n==0) {
        return 1;
    }
    return n * fact0(n - 1);
}


function fact1(n, ret) {
    if(n==0) {
        ret (1);
    } else {
        fact1(n - 1, n0 => ret(n *  n0));
    }
}


console.log(id0("hello"));
id1("hello", console.log);

console.log(fact0(5)); //120
fact1(5, console.log);


