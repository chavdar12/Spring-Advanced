rootProject.name = "spring-advanced"

include("aop-demo", "events-demo", "hateoas-demo", "trade-zone")

project(":aop-demo").projectDir = file("AOP-Demo")
project(":events-demo").projectDir = file("Events-Demo")
project(":hateoas-demo").projectDir = file("Hateoas-Demo")
project(":trade-zone").projectDir = file("Spring-Advanced-Final-Project/Back-End")
