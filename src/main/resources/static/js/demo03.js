
//diret style
function getDayOfWeek0(day) {
    
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];

    if(day >= 1 && day <= 7) {
        return days[day-1];
    }

    throw new Error("Invalid day of the week: " + day);
}

const myDay = 0;
try {
    const dayName = getDayOfWeek0(myDay);
    console.log("The day of the week is " + dayName);
}
catch(e) {
    console.log("The day of the week is unknown");
}


//CPS
function getDayOfWeek1(day, ret, thro) {
    
    const days = ['Monday','Tuesday','Wednesday', 'Thursday',
                  'Friday', 'Saturday','Sunday'];

    if(day >= 1 && day <= 7) {
        ret (days[day-1]);
    } else {
        thro (new Error("Invalid day of the week: " + day));
    }
}

getDayOfWeek1(myDay,
              dayName => console.log("The day of the week is " + dayName),
              err => console.log('The day of the week is unknown'));



