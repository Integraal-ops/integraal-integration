# General Info
A flow is a suite of logical steps concatenated together
to perform a complex set of operations.
- It starts with an entryPoint 
- It ends with an EndPoint
- If any issue is detected, the IssueHandler tries to manage it

Let's take 3 flows as an Example to better understand: 
#### **Flow 1**
- Name: Daily Statistics for Sales
- Description : This flow takes all data regarding sales,
extract some information from it 
format it to a specific format in a mail 
and sends it to the Lead Sales. the flow runs every Day at 11:00
- Schema : 
// TODO :: Add Mermaid schema here

Trigger CRON --> EntryPointFlow1 --> S11 --> S12 --> S13 --> S0M --> EndpointFlow1

* **S11** : Step read Sales information from database
* **S12** : Step Extract useful information from data of the previous Step
* **S13** : Step formating the useful information into a pretty mail for the sales Lead
* **S0M** : Step send Mail with content from previous Step to the Sales Lead

#### **Flow 2**
- Name: Reminder to close or by the cart in an ecommerce shop
- Description : In a Daily cron, checks all users with a cart not entirely finished and send them a reminder email
- Schema : 
// TODO :: Add Mermaid schema here

Trigger CRON2 --> EntryPointFlow2 --> S21 -<sends multiple messages>-> S22 --> S23 --> S0M --> EndpointFlow2

* **S21** : Step Find all users with a non empty cart
* **S22** : Step get the information of the cart and previous purchases of the user
* **S23** : Step formating the useful information into a pretty reminder mail for the user
* **S0M** : Step send Mail with content from previous Step to the user

(Purpose of example is to show that 1 entryPoint can produce many endpoints)

#### **Flow 3**
- Name: A Sale want to send a specific important message to a specific user
- Description : In a backoffice interface, an employee wants to send a message about special offer to a specific client (ponctuel)
- Schema :
  // TODO :: Add Mermaid schema here
Web API 3 --> EntryPointFlow3 --> S31 --> S32 --> S0M --> EndpointFlow3

* **S31** : Step get information for the client from database and the message from the web API Call
* **S32** : Step Formating to a pretty mail the content from the message and user data
* **S0M** : Step send Mail with content from previous Step to the user

(Purpose is to show that flows are not only for async tasks from CRONs but also from Web/Rest calls)

From these information we can see that the step **S0M** is used in all 3 flows

Based on that, we have 6 important concepts : 
- **FlowKeyID** : is the id of the flow definition (Flow 1, Flow 2, Flow 3) (describe the overall steps/flow in general)
- **FlowID** : is the id of one iteration of a flowID, exemple for the flow 1, every day a new iteration is run by the CRON trigger, 
each type a new flow is run with a new flowID for the same FlowKeyID
- **StepKeyID** : is the id of the step Definition (S11, S12, ..., S32, S0M)
- **StepID** : is the id of the iteration of the stepKeyID (for ex, in the flow 2, for each user found in S21, a new Iteration of S22 is run and that for each iteration of the flow2)
- **Data** : the business data of the flow, used by steps to execute the logic (ex : the comment from Flow 3, or the list of user with non empty cart or user information)
This Data is not structured or strictly typed, as the shape of the data depends on each steps
- **MetaData** : data like Duration, Input Size, Output Size, Number of message sent as Output used for integrity checks, and stats

