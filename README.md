# Reverse Polish Notation
Reverse Polish notation (RPN) is a mathematical notation in which every 
operator follows all of its operands, in contrast to Polish notation 
(PN), which puts the operator before its operands. It is also known as 
postfix notation. It does not need any parentheses as long as each 
operator has a fixed number of operands.


# Problem Statement
* Create a Web application with two routes /rpn and 
reverse_polish_notation. 

* Both routes should perform the same action, given Reverse Polish 
Notation argument(s) op the route should respond with the correct value.

* If the combination of operands or operators are invalid the calculator
 should respond with a 400. You should implement at least 
 (+, -, *, and /) and allow x to stand-in for *.

* One additional constraint, it's nice to use the previous calculation 
in follow on calculations. Please reserve _ as a placeholder for the 
previous HTTP result.

* We do prefer Python, Java, or Scala, but use any technologies to 
create the service, so long as it runs on a modern 64bit Mac, Linux, or 
Windows. Optimize for code clarity.

* Please deliver the source in a zip or tar.gz. Include clear 
instruction on how to compile (if necessary) and run the application.


# Features
* RPN Algorithm implmentation
* Supports 2 routes /rpn & /reverse_polish_notation
* Support for '_' to be used to retrieve previous calculated result 
* Session based tracking using cookies
* Swagger Integration for API documentation
* Dashboard view for analytics
* No manual installation needed. Self contained source and package 
    via SBT.    

# Routes
* /rpn - RPN API
* /reverse_polish_notation - RPN API
* /testrpn - Test RPN API - Runs a set of test cases
* /api - Swagger API docs
* /dashboard - RPN Dashboard - Shows RPN API Requests analytics.


# Test Cases
* /testrpn run a pre defined list of test cases.

# System Requirements
* Java 1.8 or above
* sbt - 0.13.13

# Specifications
* Play 2.15 - Streamy - Dynamically resolved using sbt.
* Uses H2 in memory db for calculations and storage.
* CanvasJS, JQuery, Moment JS for Charts View in /dashboard
* Bootstrap for stylesheets

# How to run
* cd to the extracted folder.
* chmod +x sbt && chmod +x sbt-dist/bin/sbt 
* execute sbt run: ./sbt run
* access http://127.0.0.1:9000/api
* first time access will ask you to run SQL script. Click on Apply Script button.
* Explore the /api for details on various available apis. You can use the swagger interface to test endpoints as well.
