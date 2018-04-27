

//setTimeout(callback)

//CPS
function reverse(word, ret) {
    setTimeout(() => ret(word.split('').reverse().join('')), 2000);
}

//CPS
function isPalindrome(word, ret) {
    reverse(word, reversed =>  ret(reversed == word ? word : null));
}

isPalindrome("racecar", console.log); //yields "racecar"
isPalindrome("tomorrow", console.log); //yields null

//reverse('hello', console.log);
console.log('end of program');
