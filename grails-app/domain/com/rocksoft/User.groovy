package com.rocksoft

class User {
	String loginId
	String password
	String homepage
	Date dateCreated

    static constraints = {
		loginId size: 3..20, unique: true, nullable:false
		password size: 6..8, nullable:false, validator: { password, user -> password != user.loginId }
		homepage url:true, nullable:true 
		
    }
}
