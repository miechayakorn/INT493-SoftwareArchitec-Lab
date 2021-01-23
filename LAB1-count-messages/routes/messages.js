var express = require('express');
var router = express.Router();

const messages = require('../public/temp/massage.json')

router.get('/', function (req, res, next) {
    res.json(messages)
});

router.post('/', function (req, res, next) {
    if (req.body.text) {
        let found = false
        messages.data.forEach(data => {
            if (data.text == req.body.text) {
                data.count = data.count + 1
                found = true
                res.status(200).json(messages)
            }
        });
        if (found == false) {
            const add = {
                "text": req.body.text,
                "count": 1
            }
            messages.data.push(add)
            res.status(201).json(messages)
        }
    } else {
        console.log("ldkfjlk")
        res.status(400).json("โปรดใส่ข้อมูลที่ถูกต้อง!!")
    }
});

module.exports = router;
