# General Infos
Services are the main source of Behaviour of applications
Business and Technical logical should Be implemented in Services 
corresponding to each nature of the behaviour

For Now Services are segmented in 4 kinds : 
* **FacadeService** : Service directly called from Controller or Cron
The purpose of these services is to check the security / coherence
of the input Data (ex: check that all required fields are properly filled)
and call one or multiple LogicServices/AdapterServices to perform the behaviour
At the end of the service Checks and recomposition could also be applied to the result
* **LogicService** : These services should contain the main logic of the application
The business Logic should describe simply and could use Any AdapterService/LogicService needed
Typically LogicServices are those who are in charge to store/read data from the persistence Layer
* **AdapterService** : Typically Services that responsible for all technical behaviour
These services should be written the most Abstract/Generic possible to be easily reused
Some Exemples
  * Storage Services responsible for Upload/Download data into storage solutions as S3 (this service is agnostic of the content of the data)
  * Parser Service that will read Flat Files (CSV, TSV, ...) take a validator and a converter to manage the exact content of the file (the service does not explicitly define the treatment of the file)
  * RateLimited and Resilient Rest Client to send large amount of API calls with rateLimit / retry / timeout without carrying about the API by itself
  * Date/Time/TimeZone Service that will make computations / comparison of dateTimes with different timezones

* **StarterService** :  Services that are used to add behaviour at startup of a Spring application : 
Possible usage could be : 
  * Purge Process to clean a database
  * RecoveryProcedure for a scaled application

## Code Structure 
- A service should be stateless 
- No fields are authorized besides the Autowired Services
- For configuration Services, properties could be loaded as Autowired Properties and stored in fields
- All fields should be final as much as possible
- A service should declare its contract with public methods that are defined in a `xxxService` Interface and the implementation is in a `xxxServiceImpl` Class
- A service can Autowire another Service but must depend on the interface as much as possible
- Static final fields are permitted as constants (private or public)
- except for methods defined in the interface, all other methods should be static and follow the concept of pure functions to limit boundary effects
