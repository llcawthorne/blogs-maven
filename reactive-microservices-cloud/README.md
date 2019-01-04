Following along with this blog to make a set of Mongo backed reactive
microservices that leverage the features of Spring Cloud:

<https://piotrminkowski.wordpress.com/2018/05/04/reactive-microservices-with-spring-webflux-and-spring-cloud/>

To try it out, first start the discovery service, then the account and customer services,
then you might want to start a second account or customer service to load balance, 
using -Dserver.port=XXXX where XXXX is something other than what is in the
application.yml for the respective app.  Then finally start the gateway service.

You can use the create-data.sh script in /scripts to load a little bit of data to interact with.  I had to run it through git bash on a windows box, since Windows wouldn't let me do the curl commands directly.