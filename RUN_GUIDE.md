#Instruction for building and deploying application locally on unix-based OS

##Pre Requirements:

1) You need to have installed locally [Maven](https://maven.apache.org/)
**version 3.5.2** or higher and it must be setted to environment variable.
It means that when you calling a command "mvn --version" you
should see something like this:

    **Apache Maven 3.5.2**  
    **Maven home: /usr/share/maven**  
    **Java version: 1.8.0_191, vendor: Oracle Corporation**  
    **Java home: /usr/lib/jvm/java-8-openjdk-amd64/jre**  
    **Default locale: ru_RU, platform encoding: UTF-8**  
    **OS name: "linux", version: "4.15.0-23-generic", arch: "amd64", family: "unix"**
    
2) You need to have installed locally [MongoDB](https://docs.mongodb.com/manual/) **version 4.0.4** or higher and
it should be started as a service with the following parameters:

    * host = localhost (127.0.0.1)
    * port = 27017
    
    It means that when you calling a command "mongo --version" you
    should see something like this:
        
    **MongoDB shell version v4.0.4**  
    **git version: f288a3bdf201007f3693c58e140056adf8b04839**  
    **OpenSSL version: OpenSSL 1.1.0g  2 Nov 2017**  
    **allocator: tcmalloc**  
    **modules: none**  
    **build environment:**  
        **distmod: ubuntu1804**  
        **distarch: x86_64**  
        **target_arch: x86_64**
        
    and when you make HTTP GET request to url "localhost:27017" you
    should get something like this:
    
    **It looks like you are trying to access MongoDB over HTTP on the native driver port.**
    
    ####Note:
    If you want to set your own parameters, you need to change file
    ***src/main/resources/application.properties*** and specify
    * spring.data.mongodb.host={your_own_host_value}
    * spring.data.mongodb.port={your_own_port_value}
    
    Also you can specify database access credentials specifying next parameters:
    * spring.data.mongodb.username={your_own_username}
    * spring.data.mongodb.password={your_own_password}
    
##Installation and deploying

1. git clone https://github.com/moevm/nosql2018-github_stats && cd ./nosql2018-github_stats
2. mvn spring-boot:run

After that application will start and deploy on localhost on port 8090

####Note:
If you want to set your own port, you need to change file
***src/main/resources/application.properties*** and specify
* server.port = {your_own_port}