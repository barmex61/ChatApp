package com.fatih.simplechat.Model

import com.google.firebase.Timestamp

class Message {
    var message:String?=null
    var senderId:String?=null
    var date:Timestamp?=null

    constructor()

    constructor(message: String?, senderId: String?, date: Timestamp) {
        this.message = message
        this.senderId = senderId
        this.date=date
    }


}