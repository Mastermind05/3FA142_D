const userService = require("../service/test");


class User {
    constructor(userid) {
        this.userid = String(userid); 
        this.username = null;
        this.name = null;
        this.password = null;
            // this.search_ = null;
    }


    setUsername(username) {
        if (x) 
            return 
        this.username = username;
    }

    setBirthday(birthdate) {
        this.birthdate = birthdate;
        // this.age = ;
    }
    getBirthday() {
        return this.birthdate;
    }


    async login (identifier, password) {
        userService.login(identifier, password);
    } 

    async register (username, password) {
        userService.register(username, password);
    }

}

module.exports = User;


