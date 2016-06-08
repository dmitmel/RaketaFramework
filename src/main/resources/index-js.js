function startDialog() {
    alert("Yeah! This is JavaScript!");
    setTimeout(function () {
        var name = prompt("What's your name, by the way?");
        if (name === "") {
            name = "World";
        }
        alert("Hello " + name + "!");
    }, 1000);
}

function startTimer() {
    var timerVar = setInterval(myTimer, 1000);
    var counter = 10;

    function myTimer() {
        $("p#timer").html(counter);
        counter--;
    }

    setTimeout(function () {
        clearInterval(timerVar);
        $("p#timer").html("BOOM! :)");
        setTimeout(function() {
            $("p#timer").html("Here will be timer :)");
        }, 2000);
    }, counter * 1000 + 2000);
}