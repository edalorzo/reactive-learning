
/**
 * Identity function - Direct style.
 */ 
function id0(id) {
    return id;
}

console.log(id0(12345)); //yields 12345

/**
 * Identity function - CPS
 */
function id1(id, ret) {
    ret (id);
}

id1(12345, console.log); //yields 12345

/**
 * Recursive Factorial - Direct style  
 */
function fact0(n) {
    if(n==0) {
        return 1;
    }
    return n * fact0(n - 1);
}

console.log(fact0(5)); //yields 120

/**
 * Recursive Factorial - CPS
 */
function fact1(n, ret) {
    if(n == 0) {
        ret (1);
    } else {
        fact1(n - 1, n0 => ret (n * n0));
    }
}

fact1(5, console.log); //yields 120


/**
 * Tail Recursive Factorial - Direct Style
 */
function fact2(n) {
    function fact2_tail_rec(n, a) {
        if(n == 0) {
            return a;
        }
        return fact2_tail_rec(n-1, n * a);
    }
    return fact2_tail_rec(n, 1);
}

console.log(fact2(5)); //yields 120

/**
 * Tail Recursive Factorial - CPS
 */
function fact3(n, ret) {
    function fact3_tail_rec(n, a, ret) {
        if(n == 0) {
            ret (a);
        } else {
            fact3_tail_rec(n-1, n * a, ret);
        }
    }
    fact3_tail_rec(n, 1, ret);
}

/**
 * Iterative Factorial - Direct Style
 */
fact3(5, console.log); //yields 120


function fact4(n) {
    let f = 1;
    for(let i = n; i > 0; i--) {
        f *= i;
    }
    return f;
}

console.log(fact4(5)); //yields 120

/**
 * Iterative Factorial - CPS
 */
function fact5(n, ret) {
    let f = 1;
    for(let i = n; i > 0; i--) {
        f *= i;
    }
    ret (f);
}


fact5(5, console.log); //yields 120

