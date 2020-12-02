const walk = require('walkdir');
const fs = require('fs');
const async = require('async');
const performance = require('perf_hooks').performance;


function countLines(path) {
    return new Promise(((resolve, reject) => {
        let cnt = 0;
        fs.createReadStream(path).on('data', function (chunk) {
            cnt += chunk.toString('utf8')
                .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
                .length - 1;
        }).on('end', function () {
            // console.log(path, cnt);
            resolve(cnt);
        }).on('error', function (err) {
            // console.error(err);
            reject(err);
        });
    }));
}


function syncCount(paths) {
    let totalLines = 0;

    const tasks_arr = paths.map((p) => (cb) => {
        countLines(p).then(l => {
            totalLines += l;
            cb();
        });
    });

    return new Promise(((resolve) => {
        async.waterfall(tasks_arr)
            .then(() => {
                resolve(totalLines)
            })
    }));
}

const PATH = './PAM08'
const paths = walk.sync(PATH).filter(p => {
    return fs.lstatSync(p).isFile()
})

function measureSync(){
    const start = performance.now()
    syncCount(paths).then((totalLines) => {
        const timeElapsed = performance.now() - start;
        console.log("Synchronously: " + Math.round(timeElapsed) + "ms")
        console.log(totalLines + " lines")

        //now call async version
        measureAsync();
    })
}

function measureAsync(){
    const start = performance.now()
    Promise.all(
        paths.map(p => countLines(p))
    ).then((lines) => {
        const totalLines = lines.reduce((acc, val) => acc + val)

        const timeElapsed = performance.now() - start;
        console.log("Asynchronously: " + Math.round(timeElapsed) + "ms")
        console.log(totalLines + " lines")
    })
}

measureSync()
// measureAsync();
