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
      
## Use Cases
## User Stories
## Ethics
## Software Engineering Processes
## Lightwieght Processes
## Heavyweight Processes

## Incremental Processes
### Spiral Model

Validation: Are we building the right product?

Verification: Are we building the product right?

Essential Difficulties - essential part of an object/problem

Accidental Difficulties - Parts or properties of an object/problem that can be removed

## Waterfall Model
## Agile Processes
### RUP (Rational Unified Process)
### XP (Extreme Programming)
### Scrum
## Architectural Styles
| Type | Style | Pros | Cons | Components | Connexions | Data Elements | Topology (Config) |
|------|-------|------|------|------------|------------|---------------|-------------------|
|Language-based|Main program and subroutines|
|Language-based|Object oriented|
|Layered|Client-Server|
|Dataflow|Batch sequential|
|Dataflow|Pipe and filter|
|Shared State|Blackboard|
|Interpreter|

## Architechural Patterns

## Krutchents 4+1 Views
|View | Style | 
|------|------|
|Logical View | Analysts & Designers produce "Structure"| 
|Implementation View | Programmers produce "Software Management" |
|Process View | System Integrators produce "Performance, Scalability, and Throughput"|
|Deployment View| System Engineers produce "System topology, Delivery, Installation, and Communication"| 

