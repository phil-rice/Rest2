# Operations you should be able to do always

Ask 'what lights are there'
Search for all the lights that are in a particular state
Create a new light (starts in the red state) .. should not be able to create an existing one
Delete a light

# States

## No light
If you do a get you receive 404

## Red

operations:
Change to 'Red + Orange'

## Red + Orange

operations:
Change to 'green'

## Green

operations:
Change to 'Flashing Orange'

## Flashing Orange

operations
Change to 'red'