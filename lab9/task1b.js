let async = require("async");

function printAsync(s, cb) {
    var delay = Math.floor((Math.random() * 1000) + 500);
    setTimeout(function () {
        console.log(s);
        if (cb) cb();
    }, delay);
}

function task(n) {
    return new Promise((resolve, reject) => {
        printAsync(n, function () {
            resolve(n);
        });
    });
}

function task_sequence(cb) {
    task(1).then((n) => {
        console.log('task', n, 'done');
        return task(2);
    }).then((n) => {
        console.log('task', n, 'done');
        return task(3);
    }).then((n) => {
        console.log('task', n, 'done');
        console.log('done');
        console.log("next sequence")
        cb()
    });
}

function loop(m) {
    let task_list = Array.from({length: m}, () => task_sequence);
    async.waterfall(task_list);
}

loop(4);
