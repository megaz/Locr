Locr
=========

Experimental hack project playing with Play Framework, ReactiveMongo and Scala and creating a user following system similar to Twitter. 

Usage

### 1' : Create User 

```scala
 curl -X POST -d '{ "username":"test", "password":"sdg435fdsgdfgs", "email" : "test@gmail.com" }' --header "Content-Type:application/json" http://localhost:9000/registration/createUser --include
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Content-Length: 17

User test created
```

TODO

1. Refactor Followers, Following and Authentication Controllers and decouple persistence layer into separate DAO classes.
2. Extra persistence tests similar to TestUserDAO
3. Tests for controllers. 
4. Provide secured actions. 







