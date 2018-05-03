# Dungeon Crawler

## About
During my Sophomore year in college, I was tasked to create a basic Android video game with a group. The part I worked on to make it unique from the other projects was to implement procedural generation. Afterwards, I decided to rebuild the game with the portion of the code I wrote. I took the level generation and the collision code and set off to create a game of my own.

Because of my love of Starcraft, I’ve always wanted to build a game just like it, with every type of interaction one would find in such a game. So, I decided to create the underlying RTS engine I want as a proof-of-concept while building something relatively quick and playable on top. Because of that, there are actually way more features not explicitly used in the game that are hidden. I encourage you to look through the Classes and their Methods to see all they do.

***
## Controls

WASD to move

Space to dash (2 second cooldown)

Left Mouse Button to fire a projectile (4 hits to kill)
 
E to open a chest or use stairs.

***
## Engine Features

### Command Pattern

Entities (my name for things that do things and can have things done to them) don’t directly do things. They have action queues that tell the Actions within them to do things. The queues aren’t really used here; they’re for the RTS emulation.

### Actions

Actions can have pre-actions depending on prerequisites (For example, chargers use the ActionAttack action, but resort to ActionMoving towards the player if they’re not within attack range.)

Actions can have default actions. (Nothing in the game uses it yet, though) If there were no other action after this action, it would be replaced with the default action. This is to emulate the behavior of workers in Starcraft; when a worker is done mining a mineral, it defaults to returning it to base then defaults to mining again.

Actions can do perpetual things (with the act() method) or one time actions (with onStart() and onEnd() methods)

### Resources

All entities have resources that are handed to whatever entity killed it. Experience is written this way. All Chargers are simply spawned with 10 exp each, which the player collects on death.

### Cooldowns

Entities have Timings (implemented as cooldowns in EntityPC) that can prevent the Entity from doing the action again (from the period of “start” to “end”) or prevent all action (from the period of “inoperableStart” to “inoperableEnd”). The cooldowns are on BasicAttack (cooldown of ½ second) Dash (cooldown of 2 seconds) and Mine ( just half of a second so the user can’t doubletap the mine button and restart the process by accident).

### Tickrate

The entire engine works on an executorService that calls the gameTick() method every 60th of a second. The tickrate can be changed (in the final variables in Main) to operate at any speed. The actions are written to adapt to the change. (They operate based on how much time has passed, so an Entity with a speed of 5, like the player, will go 5 blocks/second regardless of the tickrate. There will likely be slight differences in the collision simulations, however)
Input is taken in and written to a hashMap, (and double[] for mouse location), so new inputs, keys, and even keybinding is possible. It’s taken in and sent to EntityPC.receiveInput() for interpreting.

### World Seeds



 
I wanted to write the A* search algorithm for the AI, but I didn’t have time. The surrounding code for it is written in: path following, periodic rechecking of path (for moving goals), etc.

***
## Changelog

### 0.1

Fixed the spawning problems with new levels (new entities weren’t being pulled from level data) and I fixed the input buffer problem that occurred after mining a resource (opening a chest or using stairs)

### 0.2

Added Entity-Entity Collision

Because of fringe decimal math inaccuracies, The amount pushed back during collision is 1% more than calculated to allow for smoother sliding..

Increased Attack Rate to compensate for the increased effective volume of enemies

### 0.2.1

Fixed Chests not correctly removing themselves when obtained.

### 0.3

Corrected seed system.

A seed (a long) is read or generated and used to create an instance of Random

The Random is used to create new Randoms for each Level

Each Level uses their Random to generate doubles for each RNG call.

Implemented arguments

	--testing
Enables info text in console

	-s <seed>
Sets a specific seed

	-d <difficulty>
Sets a specific difficulty (default 2)

Note: Map generation is affected by difficulty (it should probably be tied only to seed)

Enabled putting the seed into clipboard (for testing)

Press Ctrl + S when paused

Added information in pause menu

Note: This doesn’t help during soft crashes (hangups) where you can’t pause the game to get the seed



