const express = require("express")
const router = express.Router()

const AppController = require("../../controllers/AppController")

router.get("/get-all-songs", AppController.getAllSongs)

module.exports = router
