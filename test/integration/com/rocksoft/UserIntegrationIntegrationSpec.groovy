package com.rocksoft

import org.apache.catalina.startup.HomesUserDatabase;
import org.junit.internal.runners.statements.FailOnTimeout;

import grails.test.spock.IntegrationSpec

class UserIntegrationIntegrationSpec extends IntegrationSpec {

	def "Saving our first user to the database"() {
		given: "A brand new user"
		def joe = new User(loginId: "joe", password: "secret", homepage: "http://www.grailsinaction.com")
		
		when: "the user is saved"
		joe.save()
		
		then: "it saved successfully and can be found in the database"
		joe.errors.errorCount == 0
		joe.id != null
		User.get(joe.id).loginId == joe.loginId
	}
	
	def "Updating a saved user changes its properties"() {
		given: "An existing user"
		def joe = new User(loginId: "joe", password: "secret", homepage: "http://www.grailsinaction.com")
		joe.save(failOnError:true)
				
		when: "a property is changed"
		def foundUser = User.get(joe.id)
		foundUser.password = 'new[password'
		foundUser.save(FailOnError:true)
		
		then: "the change is reflected in the database"
		User.get(joe.id).password == "new[password"
	}
	
	def "Deleting a user removes it from the db" () {
		given: "An existing user"
		def joe = new User(loginId: "joe", password: "secret", homepage: "http://www.grailsinaction.com")
		joe.save(failOnError:true)
				
		when: "the user is deleted"
		def foundUser = User.get(joe.id)
		foundUser.delete()
		
		then: "the user is no longer found in the database"
			User.get(joe.id) == null
			!User.exists(joe.id)
	}
	
	def "Saving a user with validation problems causes an error" () {
		given: "A user that fails some validations"
		User user = new User(loginId: "b", password: "c", homepage: "asdasd")  
		
		when: "The validate method is invoked"
		user.validate()
		
		then: "Errors exist"
		user.hasErrors()
		
	}

	def "Saving a user with a login id that matches password causes an error" () {
		def rejectedValue = "abcdefg"
		
		given: "A user that fails some validations"
		User user = new User(loginId: rejectedValue, password: rejectedValue)
		
		when: "The validate method is invoked"
		user.validate()
		
		then: "Errors exist"
		user.hasErrors()
		
		user.errors.getFieldError("password").code == "validator.invalid"
		user.errors.getFieldError("password").rejectedValue == rejectedValue
		user.loginId == rejectedValue
	}

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }
}
