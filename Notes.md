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
  
  

Validation: Are we building the right product?

Verification: Are we building the product right?

Essential Difficulties - essential part of an object/problem

Accidental Difficulties - Parts or properties of an object/problem that can be removed
  
## Use Cases
## User Stories
## Ethics
## Software Engineering Processes
* roles and workflows
* milestones
* guidelines
* incremental and agile development processes are better than waterfall approach

## Lightwieght Processes
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




