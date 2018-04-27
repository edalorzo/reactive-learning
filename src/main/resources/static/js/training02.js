



//continuation passing style - CPS
function reverse(word, ret) {
    setTimeout(() => ret(word.split('').reverse().join('')));
}


//direct style
function isPalindrome(word, ret) {
    reverse(word, reversed => ret(reversed == word ? word : null));
}


//reverse("hello", console.log); //hello
//reverse("world", console.log); //world

isPalindrome("racecar", console.log);
isPalindrome("tomorrow", console.log);

console.log("End of Program");
