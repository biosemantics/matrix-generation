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
        * include negation modifier into value e.g. "not red", "rarely red", "seldom red". 
         Negation modifiers: never|seldom|infrequently|rarely|unusually|uncommonly|occasionally
        * frequency modifiers not included: frequency/degree modifiers:sometimes|always|often|normally|generally|typically|usually|in general|mainly|mostly|frequently|commonly|regularly|largely|mostly|primarily|essentially|chiefly|predominantly|ofttimes|oftentimes|conspicuously|rather|completely
     * Modifier "not". Add to value 
        * E.g. leave length character value could be "long" with modifier "not". Value would be "not long"
     * Modifier starting with "than" is added to value. 
        * E.g. a leave length character value could be "longer", character modifier could be " than width", so the value is "longer than width"
     * Ability to deal with multiple values for a character
        * `<structure name=plant>
	<character name=count from=1 to=few char_type=range_value/>
</structure>
<structure name=plant>
	<character name=count value=”numerous” constraint=in carpel/>
</structure>`
        This should result in something like `count of plant, value = “1 to few, or numerous in carpel"`

###### Matrix transformation
 * Generate absent present characters and values from existing structures
 * Generate absent present characters from relations e.g. "stem with leafs" with relation "with" leads to "presence of leaf at stem"
 * Ontology-implying inheritance: Implication can be determined by ontology relations between individuals. E.g. The Is-a relation can determine If flower is absent, petal must be absent too / If petal is present, flower must be present too. Similarly for the "sub/superclass relation". Other relation types?
    * Dont infer anything if values indicate both absence and presence, that would not make any sense
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
 * (Taxonomy ancestor inheritance: if all children of parent contain the structure and have the character set the same parent could inherit: if contains structure and value can be applicable)
   *  This has been discarded, see https://github.com/biosemantics/matrix-generation/issues/4
 * Fix constrainted structures
  * Prepend structure by is_modifier character values and search ontologies for matches: If match found adapt structure accordingly and remove these is_modifier characters/values. Longer constrainted structure matches are preferred.
 * Remove characters with only a single value over all taxa
 * Frequency modifiers from XML input can enable/disable inheritance e.g. rarely yellow; often red 
 * Position modifiers can ...? e.g. laterally, ...?
 * Unify measurment units e.g. everything in cm
 * Provenance tracking: Cell values and characters
 * Detect value conflicts by looking at provenance
 * Sort taxa by name; Sort characters by structure name and then by name
 * Use synonymy information between structure names to merge/collapse structures and thus characters together
 * Quantity = 0 characters -> absence
 * modified by “usually” present => absent or present; similarly the other way around and there are also with similar modifiers.

###### Considerations
* Memory efficiency: Can we partition the processing on full blown XML input data?
 * e.g. only one root node at a time (should be sufficient if only upward/downward taxonomical inheritances)
 * Final full unpartitioned raw ouput to use some libraries designed for sparse matrices? e.g. https://java-matrix.org/
* Common model needs and sharing of model between matrix-generation, matrix-review and taxon comparison? 
  * see [1]
  * Matrix generation result has to be manually transformed into matrix-review model inside etc-site project
* Order of transformation steps (impacts outcome)
* "Structures in markup that are shown as “missing” in relation are parsed as if they were present." [1]
   * This needs more thought: What is absence is determined later from ontology. Remove all characters from XML?
* Partition processing phases (1) Determination of what is absent and present (2) Determin the attributes of what is present
* How to deal with "or reduced"? If absent attribute characters would be removed
`<statement id="d0_s0">
        <text>Astrophorida with long-rhabdome triaenes, which may reduced or even absent, and oxeas. </text>
        <biological_entity id="o23" name="whole_organism" name_original="" ontologyid="http://purl.obolibrary.org/obo/UBERON_0000468" type="structure" />
        <biological_entity constraint="long-rhabdome" id="o24" name="triaene" name_original="triaenes" type="structure">
           <character is_modifier="false" name="size" value="reduced" />
           <character is_modifier="false" modifier="even" name="quantity" value="0" />
        </biological_entity>
        <biological_entity id="o25" name="oxea" name_original="oxeas" ontologyid="http://purl.obolibrary.org/obo/PORO_0000431" type="structure" />
        <relation from="o23" id="r14" name="with" negation="false" to="o24" />
     </statement>`
     * Hong answer: Keep both “presence of long-rhabdome triaene” = absent and also “size of ..” = reduced
* AbsentPresentFromRelation can create "absent" while AbsentPresentFromBiologicalEntity creates "present", where the latter one is not correct
    * e.g. where it's not ok: “without oxea” relation while oxea is described as structure in XML, relation: whole_organism without oxea
    * e.g. wherre it's ok: "stem without oxea" relation while oxea is described as structure in XML, relation: stem without oxea -> presence of oxea at stem "absent" while AbsentPresentFromBiologicalEntity can at the same time say it's "present"

###### Problems
present/absent treatment depends on whether we have a type description or character state description:
character state description
(1) red leaf present -> presence of leaf = present | color of leaf = red   NOT  presence of red leaf = present
(2) red leaf absent -> presence of red leaf = absent    NOT    presence of leaf = absent | color of leaf = red
type description
(1) lateral leaf present -> presence of lateral leaf = present
(2) lateral leaf absent -> presence of lateral leaf = absent

We have to differentaitate between character state and type. Consult ontologies to see if whole phrase exists -> type if not consider as character state.
is_modifier attribute on character also gives additional clue that could be used. It indicates whether the modifier appeared directly before the structure in the original text.

We can differentiate two structure types: 
- main structures (leaf, stem, fruit) -> utilize part-of knowledge only from ontology not from XML. Most reliable from ontology not from XML.
- common substructures (apex, base, margin, tip), which can apply to pretty much any main structure (you can assume a list of these terms is known) -> utilize part-of knowledge from XML (not all ontologies manage these, 
some do, others only contain main structures)

Identity puzzle:
same structure or different structure?
e.g. 
- female leg, male leg (both show up as structure name leg, only difference is the description type was female/male)
- leaflet can be from first, second, third arch of the leaf. 
Charaparser may not currently be able to differentiate these sufficiently. Consider a module to enhance output before matrix generation can work on an input that can be assumed to be detailed enough.

Code flow: 
(1) determine presence first
(2) for present structures, determine their character values
(3) filter what columns user wants to see (presence vs. attribute)
-> hong wants to do analysis first to see if we run into problems where we are not able to identify the identity of structures uniquely enough 
(which brings us back to structure hierarchy (depths))

###### Analysis Results of Example datasets
* Relation Types
   * Part-of 54%
   * Appearance 16%
   * Location 18%
   * Derivation .5%
   * No Relation 11%
  

Collect and then analyze datasets of joel (plants) and lorena (birds) 
* Regarding the problems above.
* Analze the categories of relations we have. Currently we know: part-of ("of"), location ("above, beyond")

###### References
* [1] https://docs.google.com/document/d/1D-VwgFY7qGTpPFPxLHe88X7wXeJX2RTaFHYGrIfAlbM/edit
   * This list is a complete version that has been merged from a first version available 
