requirement-notes
--------------------
* Matrix consists of rows (Taxa), columns (Characters) and cells (character-values for taxon)
* Matrix reading from text capture output XML schema: https://github.com/biosemantics/schemas/blob/master/semanticMarkupOutput.xsd
* Matrix output format: The serialized matrix model (can be deserialized by ETC and fed to matrix review)

* Characters 
** types of characters:
*** Attribute characters
**** <attribute> of <structure>
**** e.g. color of leaf
**** Question: is this sufficient information to tell *which* structure is described?
*** Absent/Present characters
**** <absent/present> of <beared structure> at <bearer structure>
**** e.g. color of leaf (at whole_organism *ommitted in ouput*) if no <bearer structure> is available.
**** Question: is this sufficient information to tell *which* structure is described?

* Taxon 
** consists of structures / (organs)
** always consists at least of structure "whole_organism"
** Identification of equal structures for the purpose of charater alignment of two or more taxa
*** e.g. does the "basal leaf" described in description of taxon a describe the same "leaf" in the description of taxon b and 
should the columns describing the "color" of both these be in fact the same single column?
*** Structure equality is determined by structure constraint + namee + ontology id; If they all match they are considered equal

** Taxonomy hierarchy has to be modeled to facilitate character inheritance

* Values 
** can be {Not applicable (taxon does not have the described structure), 
Information missing (empty string, e.g. taxon has structure but it is not described), Single or Multiple values}
** Can be simple: "red"
** Can be a range e.g. "3cm - 5cm"
** include modifier into the cell value; with exceptions given per a fixed list (e.g. frequency modifiers or comparison modifiers such as "than").
** Ability to deal with negation modifiers e.g. "not"

** Matrix transformation:
*** frequency modifiers from XML input can enable/disable inheritance e.g. rarely yellow; often red 
*** position modifiers can ...? e.g. laterally, ...?
*** Unify measurment units e.g. everything in cm
*** 


* Provenance tracking: Cell values and characters

* CLI to (de-)activate options
* Memory efficiency: Can we partition the processing on full blown XML input data?
** e.g. only one root node at a time (should be sufficient if only upward/downward taxonomical inheritances)
** Final full unpartitioned raw ouput to use some libraries designed for sparse matrices? e.g. https://java-matrix.org/


