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

# Configuration process
The configuration of different aspect of the project will be defined in special beans that will be
built at startup of the application.
For now as the Documentation is manually handled, here some set of rules
## Rules :  
* is for all `ConfigurationBean` to have Builders or explicitly manage default values.
* All fields of the `ConfigurationBeans` needs to be well documented with examples.
* If a Filed is an enumeration of some type, in the documentation all values has to be explicitly written.

## Improvements
* Find a tool / way to automatically document a `ConfigurationBean` from the code