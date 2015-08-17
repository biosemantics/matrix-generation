requirement-notes
--------------------
###### Blackbox
* Input: 
 * text capture output, see https://github.com/biosemantics/schemas/blob/master/semanticMarkupOutput.xsd
 * ontologies that can be used to map structure (or possibly also quality terms against?)

* Output: 
 * Serialized model (for now)

* CLI to (de-)activate options (e.g. transformation steps, see below)

###### Domain 
 * Matrix consists of rows (Taxa), columns (Characters) and cells (character-values for taxon)
 * Characters 
   * Types
      * Attribute characters
        * `<attribute> of <structure>`
          * e.g. color of leaf
        * Question: is this sufficient information to tell *which* structure is described?
      * Absent/Present characters
        * `<absent/present> of <beared structure> at <bearer structure>`
           * e.g. color of leaf (at whole_organism *ommitted in ouput*) if no <bearer structure> is available.
        * Question: is this sufficient information to tell *which* structure is described
           * e.g. see discussion/open question: https://github.com/biosemantics/matrix-generation/issues/1
 * Taxon 
    * consists of structures / (organs)
    * always consists at least of structure "whole_organism"
    * Identification of equal structures for the purpose of charater alignment of two or more taxa
       * e.g. does the "basal leaf" described in description of taxon a describe the same "leaf" in the description of taxon b and should the columns describing the "color" of both these be in fact the same single column?
    * Structure equality is determined by structure constraint + namee + ontology id; If they all match they are considered equal
    * Taxonomy hierarchy has to be modeled to facilitate character inheritance
 * Values 
    * can be {Not applicable (taxon does not have the described structure), 
Information missing (empty string, e.g. taxon has structure but it is not described), Single or Multiple values}
     * Can be simple: "red"
     * Can be a range e.g. "3cm - 5cm"
     * include modifier into the cell value; with exceptions given per a fixed list (e.g. frequency modifiers or comparison modifiers such as "than").
     * Ability to deal with negation modifiers e.g. "not"

###### Matrix transformation
 * Generate absent present characters and values from existing structures
 * Generate absent present characters from relations e.g. "stem with leafs" with relation "with" leads to "presence of leaf at stem"
 * Ontology-implying inheritance: Implication can be determined by ontology relations between individuals. E.g. The Is-a relation can determine If flower is absent, petal must be absent too / If petal is present, flower must be present too. Similarly for the "sub/superclass relation". Other relation types?
 * Removal of attribute characters where structure absent e.g."flower without large petal", usually in markup looks as follows
  `<structure name="flower"> .. </structure>
  <structure name="petal">
     <character type="size" value="large"/>
  </structure>
  <relation from.. to.. "without/>`
  In the matrix, we usually don't want to see attribute characters such as `"size of petal" = large` if the text actually expressed that it is absent
  We usually only want to know `"presence of large petal of/at flower" = absent`.
 * Filter character types (e.g. show only absent/present characters or only attribute characters)
 * Split range values: Instead of a single character `"length.." "3-5cm"` have two characters `"length_max" "5cm"` / `"length_min" "3cm"`
 * Taxonomy descendant inheritance: inherit values from parent to child if the child does not have a value for the character but a value can be applicable
 * (Taxonomy ancestor inheritance: if all children of parent contain the structure and have the character set the same parent could inherit: if contains structure and value can be applicable) This has been discarded.
 * Fix constrainted structures
  * Prepend structure by is_modifier character values and search ontologies for matches: If match found adapt structure accordingly and remove these is_modifier characters/values. Longer constrainted structure matches are preferred.
 * Remove characters with only a single value over all taxa
 * Frequency modifiers from XML input can enable/disable inheritance e.g. rarely yellow; often red 
 * Position modifiers can ...? e.g. laterally, ...?
 * Unify measurment units e.g. everything in cm
 * Provenance tracking: Cell values and characters
 * Detect value conflicts by looking at provenance
 * Sort taxa by name; Sort characters by structure name and then by name

###### Considerations
* Memory efficiency: Can we partition the processing on full blown XML input data?
 * e.g. only one root node at a time (should be sufficient if only upward/downward taxonomical inheritances)
 * Final full unpartitioned raw ouput to use some libraries designed for sparse matrices? e.g. https://java-matrix.org/
