# Javascript

Currently it is being generated but we need to 'manage it'. 
* Short term just aggregate and store
* Long term we need the 'correct javascript'for this request.
* We would like strategies that allow the javascript to be included (piecemeal) or sent with the data

# Accept Header / Content negotiation

* Find the recursive list of lens for a view
* Use the list to make an accept header which is sent
* Use the list to minimise the JSON sent back - work out how to make this optional

# JSON

* Parse JSON for puts/posts - done by second point
* Nice library instead of quick JSON - done
* Lists  - in progress

#Optionals
Of primitives and views

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

* LensValidations: add 'exact' as an option, so that we can say 'we do this and only this'
* Make sure the annotations are set up right (only interface/only clsas etc)
* Check that if deprecated javascript must be set
* check name structures
* A field in a view must be in the entity

# Ideas
 
Don't forget we can use exactly the same idea for Cassandra/Doc model in SQL. We can store the javascript
in the database (indexed by hashcode)