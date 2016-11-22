
In Saul every data is represented by nodes, edges and properties and join nodes.
Properties are related to only nodes. If a set of nodes are related to each other and make a new concept together then we need to be able to build a join node based on
those and assign them properties.
The nodes and edges and properties are abstract for each domain.
Domain specific data structures for a specific domain will be built based on this abstraction and by making domain specific types of nodes.
Each type has a way to get an address.
Each type is associated with a number of sensors that provide the properties about that type.
Each type has a way to establish edges between other types in that domain.
We need to have a way to build join nodes which can abstract and domain independent.
We start with having an NLP domain with two types of nodes:
Sentences
Tokens
We just indicate the edge and there shouldn't be a need to specify a sensor on the edge.
We should be able to have a list of black box sensors that give us properties of Sentence nodes and Token nodes.
That is all!



