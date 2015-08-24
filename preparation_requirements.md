requirement-notes
--------------------
###### Blackbox
* Input: 
 * text capture output, see https://github.com/biosemantics/schemas/blob/master/semanticMarkupOutput.xsd
 * ontologies that can be used to map structure (or possibly also quality terms against?)

* Output: 
 * text capture output
 
###### Preparations
* Resolve character splitting (utilize ontologies) due to
   * quality term synonym
   * organ term synonym
* Resolve lumping of characters (utilize ontologies) due to 
   * organ identification problem
* Character constraint is put as
  * part of character value
  * new relation
 
