
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

=================================================================================
# A suggestion

PK: We do not need this whole section, these information go into our NLP base classes.

We can declare the xml schema by two classes: `RelationAnnotation` and `NodeAnnotation`

something like this:

```java
class NodeAnnotation{
    String IdentifierName= "Id";
    String TagName;
    String startPropertyName = "start";
    String endPropertyName = "end";
    List<AnnotationProperty> properties;
    List<NodeAnnotaion> children;
}
class RelationAnnotation{
    String IdentifierName= "Id";
    string TagName;
    List<AnnotationProperty> properties;
    List<AnnotationIdentifier> Identifiers;
}
class AnnotationProperty{
    String Name;
    SupportedTypes Type;
}
class AnnotationIdentifier{
    String TagName;
    String IdentifierName;
}
```

PK: The reader should have a node reader specific for each NLP base class.
Then we can define a reader to read the specified schema from the xml files:

```java
class XmlDataReader{
    XmlDataReader(List<NodeAnnotation> nodesSchema, List<RelationAnnotation> relations);
    List<NlpNode> getNodes(String tagName);
    List<NlpNode> getNodes(String tagName, String parentId);
    List<NlpJoinNode> getJoinNodes(String tagName);
    List<NlpJoinNode> getJoinNodes(String tagName, String parentId);
    NlpNode getAncestor(NlpNode node, String ancestorTagName);
}
```
PK: We do not need this we already have nodes and join nodes in Saul data model, we can extend their fields if needed
Finally we can have two type of nodes in our model:

```java
class NlpNode{
    String Id;
    String Name;
    Integer start;
    Integer end;
    String Text;
    Map<String, object> properties;
}
class  NlpJoinNode{
    String Name;
    List<NlpNode> Nodes;
}
```
How we associate those to the `TextAnnotation`? well using a provider class:

```java
class NlpAnnotationProvider{
    private Map<NlpNode, TextAnnotation> map;

    List<NlpNode> getTokens(NlpNode sentence);
    String getPos(NlpNode sentence, NlpNode token);
    String getLemma(NlpNode sentence, NlpNode token);
    // and more property functions here
}
```

# an example
suppose we have this schema :
```xml
<scene id="..." url="...">
    <sentence id="..." start="" end="">
        <text>...</text>
        <tr id="..." start="..." end="..." />
        <lm id="..." start="..." end="..." />
        <sp id="..." start="..." end="..." />
        <relation id="..." trId="..." lmId="..." spId="..."/>
        <relation id="..." trId="..." lmId="..." spId="..."/>
    </sentence>
</scene>
```
we must declare the schema:
```java
NodeAnnotation scene = new NodeAnnotation("scene");
scene.properties.add(new AnnotationProperty("url", SupportedTypes.String));

NodeAnnotation sentence = new NodeAnnotation("sentence", "scene");//parent name is scene
scene.children.add(sentence);

NodeAnnotation tr = new NodeAnnotation("tr", "sentence");
sentence.children.add(tr);
// I think you get the idea
```

now we can have a reader:

```java
XmlDataReader reader = new  XmlDataReader(List<NodeAnnotation> nodesSchema, List<RelationAnnotation> relations);
DataModel.Populate(reader.getNodes("scene"));
```

and the data model can be something like this:

```scala
object DataModel(xmlReader: XmlDataReader){
   private val provider = new NlpAnnotationProvider()
   \\PK:
   val scenes = node[NlpNode]
   val sentences = node[NlpNode]
   val tokens = node[NlpNodes]
   val trajectors = node[NlpNode]
   val lanmarks = node[NlpNode]
   val indicators = node[NlpNode]
   val relations = node[NlpJoinNode]
   
   val sceneToSentence = edge(scenes, sentences)
   sceneToSentence.addSensor(sceneToSentences _)
   
   val sentenceToToken = edge(sentences, tokens)
   sentencesToToken.addSensor(sentenceToTokens _)
   
   val sentenceToTrajector = edge(sentences, trajectors)
   sentenceToTrajector.addSensor(sentenceToTrajectors _)
   
   val sentenceToRelation = edge(sentences, relations)
   sentenceToRelation.addSensor(sentenceToRelations _)
   
   //sensors
   def sceneToSentences(scene: NlpNode): List[NlpNode] = xmlReader.getNodes("sentence", scene.Id)
   
   def sentenceToTokens(s: NlpNode): List[NlpNode] = provider.getTokens(s)
   
   def sentenceToTrajectors(s: NlpNode): List[NlpNode] = xmlReader.getNodes("tr", s.Id)
   
   def sentenceToRelations(s: NlpNode): List[NlpJoinNode] = xmlReader.getJoinNodes("relarion", s.Id)
   
   // properties
   val posTag = property(tokens) {
     x: NlpNode => provider.getPos(xmlReader.getAncestor(x, "sentence"), x)
   }
   val url = property(scenes){
     x: NlpNode => x.properties("url") 
   }
   
   
   

}
```

# Another suggestion

Let's start from the end data model --a reverse process:

```scala
object myDataModel extends DataModel with NLP {

// all the types (Document, Sentence, Token) are predefined in Saul as NLP base types.

   val scenes = node[Document]
   val sentences = node[Sentence]
   val tokens = node[Token]
   val trajectors = node[Token]
   val landmarks = node[Token]
   val indicators = node[Token]
   val relations = JoinNode[Token,Token]

  //We should remove the edges between the base types, they can be just defaults, no?

   //val sceneToSentence = edge(scenes, sentences)
   //sceneToSentence.addSensor(sceneToSentences _)

   //val sentenceToToken = edge(sentences, tokens)
   //sentencesToToken.addSensor(sentenceToTokens _)

   //val sentenceToTrajector = edge(sentences, trajectors)
   //sentenceToTrajector.addSensor(sentenceToTrajectors _)

   val sentenceToRelation = edge(sentences, relations)
   sentenceToRelation.addSensor(sentenceToRelations _)

   //sensors
   def sceneToSentences(scene: Document): List[Sentence] = xmlReader.getNodes("sentence", scene.Id)

   def sentenceToTokens(s: Sentence): List[Token] = provider.getTokens(s)

   def sentenceToTrajectors(s: Sentence): List[Token] = xmlReader.getNodes("tr", s.Id)

   def sentenceToRelations(s: Sentence): List[JoinNode(Token,Token)] = xmlReader.getJoinNodes("relation", s.Id)

   // properties
   val posTag = property(tokens) {
     x: Token => provider.getPos(xmlReader.getAncestor(x, "sentence"), x)
   }
   val url = property(scenes){
     x: Document => x.properties("url")
   }

}```

// PK: something like above though there are still missing information and it is not well connected; to be modified

I think we need to have a design for three aspects of each NLP base class.
 - Base class itself
 - Data model node specification
 - Related annotations

## Document
 For SpRL example we start from document.
 - The base class we call `Document` class.
  It will have an address `id` and a textual content `text`.
  It can have a start (~0) and end(~document length)

 - For the data model node, we should declare it as :

 ```
 val nodeName= node[Document]
 ```
 and call the reader as

 ``` SaulXMLReader.getDocumentTag("TagNameUsedInXML")```

 this should return a list of `Document` objects.

 - For the annotation side, and for the body of the SaulXMLReader we will have something like this:
 brows the child tags and find `text`, assign it to `Document.text`
 brows the attributes find `id` assign it to `Document.id`


##Sentence
The second base and the most important one is the sentence.
- The base class we call `Sentence` class.
  text
  start
  end
  id
List of properties

   - For the data model node, we should declare it as :

   ```
   val nodeName= node[Sentence]
   ```
   and call the reader as

   ``` SaulXMLReader.getSentenceTag("TagNameUsedInXML")```

   this should return a list of `Sentence` objects.

   - For the annotation side, and for the body of the SaulXMLReader we will have something like this:
   brows the child tags and find `text`, assign it to `Sentence.text`
   brows the attributes find `id` assign it to `Sentence.id`
   if this is a child node of a document keep the `document id`.

##Token

The third base is token.
- The base class we call `Token` class.
  text
  start
  end
  id

   - For the data model node, we should declare it as :

   ```
   val nodeName= node[Token]
   ```
   and call the reader as

   ``` SaulXMLReader.getTokenTag("TagNameUsedInXML")```

   this should return a list of `Token` objects.

   - For the annotation side, and for the body of the SaulXMLReader we will have something like this:
   brows the child tags and find `text`, assign it to `Token.text`
   brows the attributes find `id` assign it to `Token.id`
   if this is a child node of a sentence keep the `sentence id` as well as the `document id` of the sentence.


##Document-Sentence connection

###case1
We already have sentence annotations, one easy way to read this which is not efficient is the following:
We read the documents separately.
We read the sentences separately.
We should have a matching sensor established, when needed this sensor compares ids of the documents
and the `document-id` of sentences and establishes an edge between each document and the contained sentences.

###case2
We do not have sentence annotations:

   We should have a sentence splitter that receives a Document and generates a number of Sentences, we use this as a generating sensor.

   So, we have a Sensor and when we populate the data model that contains document and sentence, each document node
   is automatically connected to the generated sentence nodes.
   (We can ask the user to write the edge also and add the sensor, or we can do this by default so user even does not
   need to declare the edge in the data model.)

##Sentence-Token connection

Again can be done using the generating sensors or matching sensors as we had in document-sentence connection.

#Properies and annotations beyond base classes
The assumption is that all annotations are applied on the base classes. But they can define new nodes of the same types.
For example the trajector annotations can be defined as trajector node in the data model:
```scala
val trajector= node[Token]
```

We can read those from the XML file by,
```scala
SaulXMLReader.getTokenTag("NameUsedInXML")
```













