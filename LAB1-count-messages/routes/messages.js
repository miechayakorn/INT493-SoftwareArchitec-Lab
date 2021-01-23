var express = require('express');
var router = express.Router();

const messages = require('../public/temp/massage.json')


router.get('/', function (req, res, next) {
    res.json(messages)
});

router.post('/', function (req, res, next) {
    let found = false
    messages.data.forEach(data => {
        if (data.text == req.body.text) {
            data.count = data.count + 1
            console.log("============= +1")
            found = true
            res.status(201).json(messages)
        }
    });
    if (found == false) {
        const add = {
            "text": req.body.text,
            "count": 1
        }
        messages.data.push(add)
        console.log("============= create!!")
        console.log("ADDED!!!")
        res.status(201).json(messages)
    }
});

module.exports = router;
