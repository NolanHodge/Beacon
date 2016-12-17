#COMP3004 FINAL OVERVIEW

#9 – Questions /100 marks  

#Fill in gaps.

Topic | What is covered.
------------ | -------------
True/False Answers | Domain models, use cases, development processes, design patterns, testing#  
               |
Short Answers. | “Give an example of X” and “What is the difference between X and Y?” 
Short Answers. | Domain model: classes (different kind of domain objects) association attributes
Short Answers. | Functional vs non Functional requirements (WILL BE ASKED MULTIPLE TIMES)
Short Answers. | Incidental vs essential difficulties
               |           
Domain Modeling | Draw a domain model (classes, association, multiplicity)  
Design patterns | Patterns that support encapsulation, wrappers (decorator, adaptor) observer, MVC, Key aspects, 
Design patterns | class diagram, how each promotes changeability , 
Design patterns | given code snippet, identify a pattern used and explain benefits and downside of using patterns 
                |
Architecture | Architectural design, Drift erosion, conceptual vs concrete
             |
Multiple Choice | Krutchens 4+1 views , Architectural design  
            |
Refactoring | Code smell and their impact on system quality 
Refactoring | Identify code smells and apply to refactoring code#
            | 
Professional ethics | Given a scenario, what ethical principles are being violated

## Domain Model
A domain model is a representation of a real world entity, and related abstract constructs from a chosen domain plus their relationship. (basically an abstract UML diagram)

[!Domain Model]
(http://cakebaker.42dh.com/wp-content/uploads/2007/02/domain_model.png
Consists of: 

- Conceptual classes (kinds of objects in the problem domain)
  - Real world concepts, information that the system stores etc.
  - Kinds of classes
    - Actors
    - Boundary objects
    - Domain objects
  - Actors
    - Entities external to the system. (Users, External hardware, etc.)
  - Boundary Objects
    - Model aspects of real world entities that the system interacts with. (User input, Mouse clicks, any interface object, etc.)
  - Domain Objects
    - Pure abstraction of the domain we are able to model manage and store. (Bank accounts, Overdraft Policy, etc).

  - How to find Domain Entities 
    - Larmans 3 approaches
      - Refine an existing Domain Model (Use a previously created one, or a well understood one)
      - Use a conceptual class category list (Brainstorm about all entities relevent to the system)
      - Identify noun phrases (i.e Business Transactions -> FlightReservation, FlightPurchase)
    
- Associations between conceptual classes
  - Relationship between two classes 
  - Typically a short verb
  - Represents an "important" relatonship between two conceptual classes
 
- Attributes of conceptual classes
  - Simple subpart of a project  
  - Store would have an address, or phone number attribute
  
- Inheritance in Domain Models
  - Works the same as UML with skinny and thick arrow
  

## Incidental vs essential difficulties
  - Incidental (Easy to Overcome)
    - Problems which engineers create and can fix.
  - Essential  (Difficult to Overcome)
    - Caused by the problem to be solved, and nothing can remove it. (Users want it? Have to have it!)
    
## Functional vs non Functional requirements 
  - Requirement: A statement about the proposed system that all stakeholders agree must be made true in order for the customer’s problem to be adequately solved
  <br/>  
  - Short and concise piece of information
  - Says something about the system
  - All the stakeholders have agreed that it is valid
  - It helps solve the customer’s problem 
  <br/>
    - Functional
      - What is the system supposed to do?
      - Mapping from input to output
    - Non Functional
      - Process: standards, delivery, etc.
      - Product: usability, efficiency, reliability, etc.
      - External: cost
      

Validation: Are we building the right product?

Verification: Are we building the product right?

Essential Difficulties - essential part of an object/problem

Accidental Difficulties - Parts or properties of an object/problem that can be removed

## Use Cases

- Use cases are informal descriptions of the system’s basic functionality and its interactions with the environment
  - System-level functionality
  - User’s point of view
  - Textual, not formal
  - Low tech, low detail
  
## User Stories

A User Story is a unit of work to develop functionality that:
  - Very Specific 
  - Provides Value to Customer
  - Can be tested independently 
  - Can be finished ina  sing iteration
  
- Examples of scenarios considered a "User Story"

  - Assume you are a developer for a billing company
    - Create ways for customers to
    - Generate an invoice for a subscription charge.
    - Generate an invoice that includes usage-based charges.
    - Finalize a customer’s invoice and send it to them.
    - Generate invoices for all customers.
    - Generate invoices for all customers in a single billing cycle.
    - Maintain customer data
    - Select customers for invoice finalization using drag & drop.

(Each one of these

## Refactoring

  - Code Smell? What is it?
    - double potentialEnergy(double mass, double height) 
    - {
    -   return mass * height * 9.81;
    - }
      - 9.81 should be declared as a static variable.
      - int MAGIC_NUMBER = 9.81;
    <br/>
    - if (date.before(SUMMER_START) || date.after(SUMMER_END)) 
    - {
    -   charge = quantity * winterRate + winterServiceCharge;
    - }
    - else 
    - {
    -   charge = quantity * summerRate;
    - } 
       - Bad if-statement, should be easy to understand
       - if (date.equals(SUMMER)){charge = this else charge == that}
       - or 
       - charge = date.equals(SUMMER) ? this :  that;
    <br/>
  
 - Large Class
    - Does the work of two classes
    - Example: Person.class also returns Office phone number of person
      - Should be Person.class has-a Office.class
      
- How to refactor
  - Make sure all your tests pass
  - Identify the code smell
  - Determine how to refactor this code
  - Apply the refactoring
  - Run tests to make sure you didn’t break anything
  - Repeat until the smell is gone
  
## Ethics
## Software Engineering Processes
* roles and workflows
* milestones
* guidelines
* incremental and agile development processes are better than waterfall approach

## Lightweight Processes
* focus on working code
* communication between developers and customer

## Heavyweight Processes
* document driven
* many roles, checkpoitns
* bureaucratic, high management overhead

## Incremental Processes

### Spiral Model
* risk driven
* guides a team to adopt elements of one or more process models
* reqs, design, construction, testing/debugging, deplyoment, maintenance
* choices based on a project's risks generate an appropriate process model for the project

## Waterfall Model
* non-iterative
* bad because software is too complex to be specified in advance and too complicated to build without errors
* after the fact changes are costly

### Stages
* requirements
* analysis
* design
* coding
* testing/verification
* deployment/maintenance
* move to next phase only when preceding phase is reviewed and verified
* places emphasis on documentation - new team members can get familiar using documents

## Agile Processes

### RUP (Rational Unified Process)
* iterative, incremental
* use-case driven
* architecture centric
* uses uml as modelling notation

#### 4 steps
* inception - define scope of project
* elaboration - planning
* construction - building
* transition - release

### XP (Extreme Programming)

### Scrum

## Architectural Styles
* collection of architectural design decisions
* creates beneficial qualities in system

| Type | Style |
|------|-------|
|Language-based|Main program and subroutines|
|Language-based|Object oriented|
|Layered|Client-Server|
|Dataflow|Batch sequential|
|Dataflow|Pipe and filter|
|Shared State|Blackboard|
|Interpreter|Mobile code|
|Implicit Invocation|Publish-Subscribe|
|Implicit Invocation|Event-based|
|Peer to Peer|P2P|

## Architechural Patterns
* collection of design decisions that are applicable to recurring problem and parameterized based on contexts in which problem appears

|Type|Pattern|
|----|-------|
|Creational|Singleton|
|Behavoural|Iterator|
|Behavioural|Observer|
|Behavioural|Strategy|
|Behavioural|Template(Abstract)|
|Structural|Adapter|
|Structural|Composite|
|Structural|Facade|

## Architectural Design in Absence of Experience
* Analogy searching
* Brainstorming
* Literature searching
* Morphological charts
* Remove mental blocks

## Referential architecture
#### defines fundamental components of the domain and relations between them

### properties
### domain
### fundamental abstractions - applicable across the domain 
### template

benefits - captures main ideas and components across domain, provides abstraction for arch, assists in comparing archs in the same domain

### deriving ref arch
* step 1: derive conceptual arch
* step 2: derive ref arch sing conceptual arch

* ref arch must be flexible enough to encompass many product archs
* does not determine implementation
* does not depend on dev methodology, platform or implementation concerns

### conceptual arch
#### in mind of devs, decompose system without going into details of interface spec
* includes - what a system does, responsibilites, ineractions, control and data flow
* examine existing documentation to obtain

### concrete arch
#### actual relationships, real arch based on code, harder to understand, created manually
* includes - what a system does and how it will do it
* inspect code to obtain

concrete arch should be used to refine conceptual arch


## Testing
* finding differences between expected and observed behaviour, systematic attempt to find faults in a planned manner
* goal is to show program meets its spec

failure - when expected behaviour deviates from specified behaviour

error state - system in a state where furthor processing will lead to failure

fault - defect/bug, the mechanical/algorithmic cause of an error

defect vs failure - defects may lead to failures but failures may show up elsewhere

defects - missing reqs, wrong spec, infeasible req, faulty system design, wrong algorithms, syntax, precision faults, bounds checking, timing faults

reliability - measure of success with which observed behaviour conforms to spec, the probability that system will not fail

fault avoidance - prevent faults before system is released
fault detection - identify error states and faults before release

## Krutchents 4+1 Views
|View | Style | 
|------|------|
|Logical View | Analysts & Designers produce "Structure"| 
|Implementation View | Programmers produce "Software Management" |
|Process View | System Integrators produce "Performance, Scalability, and Throughput"|
|Deployment View| System Engineers produce "System topology, Delivery, Installation, and Communication"| 

