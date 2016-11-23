
##Saul NLP DataModel and Linguistic BaseClasses

In Saul every data is represented by nodes, edges and properties and join nodes.
Properties are related to only nodes. If a set of nodes are related to each other and make a new concept together then
we need to be able to build a join node based on those and assign them properties.
The nodes, edges and properties are abstract for each domain.
Domain specific data structures will be built based on this abstraction and by making domain specific types of nodes.
Each type has a way to get an address.
Each type is associated with a number of sensors that provide the properties about that type.
Each type has a way to establish edges between other types in that domain.
We need to have a way to build join nodes which can abstract and domain independent.
We think we need to implement an NLP specific `type hierarchy`, and we start with having two types in this hierarchy:

- Sentences
- Tokens

We ideally should indicate the edge and there shouldn't be a need to specify a sensor on the edge.
We should be able to have a list of black box sensors that give us properties of Sentence nodes and Token nodes.
That is all!

##XML Reader

Most of the NLP annotations are printed in XML files. we try to start from the Saul DataReader and see if we can have an XML
annotation READER as generic as possible.

Let review it by an example:

we have an xml file, contains sentences, and some of their tokens have R1, R2, R3 roles,
we also have relations R which connects those roles,
the user defines sentence and maps the sentence annotations to its
tokens, which will be populated from the sentence
and defines properties for R1, R2 and R3
and also defines a join node for R.
We need to establish edges and edge sensors between R and Ri s.

The links between the data structures are established outside them by means of Saul edges.

In an XML file we have single annotations and relational annotations and some attributes sometimes are assigned to each tag.
These components are read to saul nodes, properties, join nodes and edges.

We need as in input to the program a mapping between our NLP `type hierarchy` and the XML tags as the initial anchor points.
We might even tell the users `if you have annotations at all at the sentence level put them in SENTENCE tag` or ask them to write a
mapping between their terminology and our type hierarchy.

1) we have a predefined type hierarchy, for now including sentence and token.
2) we define every type in Saul (even if it is just having one textAnnotation variable). For now we need to implement two class or case classes called Senetnece and Token. (edited)
3) For now we use textAnnotation to provide us the property and edge sensors.
4) we try to think of how our join node will look like.
5) Start from single annotations, and reading them in a generic way?
6) Hide all the TextAnnotation foot prints from the user's point of view.
