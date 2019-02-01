# Immediate tasks

* Rename Entity to Resource - done
* Rewrite the horrible bits of code!
* Multiple interfaces on client 
* Validate fields in a views match fields in an entity
* 'thinking' problem: how do we filter down the view on a child object?


# Javascript
* Kill it!
* Currently it is being generated but we need to 'manage it'. 
* Short term just aggregate and store
* We would like strategies that allow the javascript to be included (piecemeal) or sent with the data


# Resource annotation

* Consider a 'make me a view with all fields options'


# View annotation 

* Error reporting when name wrong
* Consider allowing name to be set


# Accept Header / Content negotiation

* Find the recursive list of lens for a view
* Use the list to make an accept header which is sent
* Use the list to minimise the JSON sent back - work out how to make this optional

# server

* Move most of content to the companion object (much more composable)
* Have a method to compose two servers
'

# JSON

* Parse JSON for puts/posts - done by second point
* Nice library instead of quick JSON - done
* Lists  - in progress

# View stuff

* Have  a field that returns the optional json (only relevant if this was the main entity)
* Have a field that returns a map of links (empty unless this is the main entity)

#Optionals
* Of primitives and views

#Lists
* Need to be able to have lists of views -- done
* Need lists of primitives


#Embedded/Link
Pretty cool, but let's do that when everything else is done... place holders are nice

#Links

The state based approach demoed in Trafficlights looks really simple to use and seriously cool

# Demo

* Make the same demo as for Scala rest


# Validations

* What happens if the interfaces aren't defined
* LensValidations: add 'exact' as an option, so that we can say 'we do this and only this'
* Make sure the annotations are set up right (only interface/only clsas etc)
* Check that if deprecated javascript must be set
* check name structures
* A field in a view must be in the entity

# Ideas
 
Don't forget we can use exactly the same idea for Cassandra/Doc model in SQL. We can store the javascript
in the database (indexed by hashcode)