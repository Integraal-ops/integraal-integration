# Global info 
All services (as in Spring Service) should expose public methods 
that are called either by other services or by controllers.

To simplify the management of the code, and the AOP features of 
spring all public methods should follow the following contract : 

```java 
public OutBean doStuff(InBean inBean) {}
```
All methods should take at maximum 1 parameter called InBean 
and return an object called OutBean

- All InBean should Extends the GenericInBean class
- All OutBean should extends the GenericOutBean class

The purpose of those superclass (GenericInBean/GenericOutBean) 
is to manage AOP Globally easily such as :
* AOP method for handling Standardized Logging/ Traces / metrics)
* Security checks for Facade Services that are exposed via Controllers or CRONs methods
* Debug Supplementary Information


## List of InBean/OutBean Generic Type
* **ControllerInBean/ControllerOutBean** : Beans used as input/output API exposed via Controllers
* **CronInBean/CronOutBean** : Beans used as Input/Output of CRON methods (Output is used mainly as logging information source)
* **ApiInBean/ApiOutBean** : Beans used as body for Requests/Responses of API calls made by the application to external services