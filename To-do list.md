# To-do list

- **Misc**:
  - In-game status bar

- **In-game calculator**:
  - Basic scientific calculator operations
  - Graphing calculator
  - Unit conversion
  - Expression history

- **Alternative textures**
  - Disable Water Stream particles

- **Overlays**:
  - Highlight the blocks moved by a piston
  - Highlight unstable blockstates //TODO idk if this is needed or even possible to implement. If it is, add this to the readme
  - Display scheduled tick timers
  - Display Hopper cooldown timers
  - Show distance value of Leaves
  - Show distance value of Scaffoldings
  - Show level value of Composters
  - Show level value of Cauldrons
  - Show current fuel and item of Furnaces, Smokers and Blast Furnaces
  - Show marker entities
  - Highlight the power source of Rail blocks
  - Highlight the power source of Redstone Wires
  - Visualize Comparator output logic
  - Visualize the order in which pistons move blocks
  - Visualize the neighbor updates redstone components send when their state changes
  - Visualize Quasi-connectivity interactions

- **Block groups**:
  - Group blocks together to recognize circuits at a glance
  - Name and color groups
  - Copy and move groups around or across worlds

- **Gameplay tweaks**:
  - Disable fluid flushing
  - Disable block gravity
  - Disable explosion block and entity damage

- **Creative mode tweaks**
  - Control block placement delay
  - add in-game UI for these. display their current values with icons and such

  - Autoclicker with configurable delay
  - Toggle clicks
  - Disable Sign GUI
  - Disable picking up items
  - Open obstructed containers
  - Snap view to specific angles //TODO in-game indicators for multiples of 22 and 45 ° on both axes. use a keybind to snap to them
  - Place blocks facing away from the player
  - Place blocks with an offset
  - Insta kill mobs
  - add in-game UI for fast clicking. display its current values with icons and such

  - Scroll block delays and container levels with your mouse wheel >:3
  - Teleport to the block you are looking at with a keybind
  - Zoom view with a keybind
  - Toggle full brightness with a keybind
  - Disable punching End Crystals

  - Disable dimension change loading screens
  - Disable reduced FOV when inside a body of Water
  - Disable overlay effect of Lava
  - Disable overlay effect of Nether Portals

- **Property lookup**:
  - Quickly find blocks based on their properties
  - Find alternatives to existing blocks and replace them in-game

- **Macros**:
  - Save lists of commands to execute when needed
  - Save stacks of items and create copies at any time (bypasses the 100 stacks limit!)
  - Bind keys to macros

- **Sound muffler**:
  - Control the volume level of redstone components and minecarts
  - Fully mute individual components

- **Container tools**:
  - Count and find items within containers in a selected area
  - Replace item stacks remotely
  - Easily fill open containers with specific items

- **Custom blocks and items**
  - Item Source
  - Item Sink (storage block with infinite capacity)
  - Item Pipe



















# Not features:

- render stats //TODO minecraft.getGpuUtilization()


- Fix changes caused by the custom interaction radius not sending block updates to the client //TODO /fill command also does this?????


- maybe for future versions: a "profiler" menu that tracks CPU time usage of each mob category / block type / stuff


- add a "about" screen that talks about the mod, basically just copy the readme.
  - display my skin/drawing in there too and name
  - say that no part of the mod was AI-generated.
    - sounds, textures, code, are all made or designed by humans.
  - credit kai for the sounds
  - 2.5d style with the mod's name sliding in the background at different angles above background colored bars at different z heights


- add images to the readme?
  - show a screenshot of a big block of block entities and like 2fps/300fps difference when static models are on


- ~~add a "keybinds" section~~
  - this is where you can set the keybinds for scrolling containers, teleporting, hiding the screen and everything
    - Activating the scroll key makes the status bar change color or something like that
  - add a "panic button" keybind that hides all entities and block entities, hides all particles, runs /tick freeze and lowers render distance to 2 chunks


- add a tool that connects 2 positions using an algorithm-generated redstone circuit of a specified tick delay. can be 0 too


- add scrolling items in inventories with the scroll wheel
- add moving all the items into and out of your inventory with ctrl+shift+click
  - this goes in the keybinds screen too


- add world stats
  - number of regions
  - number of generated chunks
  - size of the world directory


- 3am water thats normal water but has shaders

- Add a status bar at the bottom of the screen
  - display player position here? maybe? and tps and fps and ping
  - display tab/+/- default keybinds in UIs
    - add these to the pause menu too somehow
  - display game pause status: > if running, || if paused.


- maybe add a "center crosshair" feature in alt textures? Vanilla's crosshair isnt centered



- USE SLIDER/TOGGLE FOR ALT TEXTURES FEATURES. overlay still images

- maybe add chat timestamps?


- add static bed models for 26.1
- remove static bed models in 26.2+
- remove static sign models in 26.2+




- add "entity outline" in rendering screen


- make the item sink store items in a custom file / directory.
  - this offloads the data from the chunk, preventing size limit crashes
  - add this to the description of the item too





- hoppers:
  - for item pipes, add a small graphic with
    - a hopper with a chest on top?                  | items pullsed
    - a hopper pointing right into a chest           | items pushed
    - a hopper a hopper on the left pointing into it | items received
    - a hopper with a hopper below it                | items taken from it
    - a hopper with an item entity on top            | item entities pulled in

  - display a timeline on which each event is shown.
  - clicking on an event shows info about it

  - create time-dependant graphs for:
  - (graphs can toggle betweek stacks and items. 1 snowball is 1/16th of a stack but 1 item)
    - one input(or output) rate for each category of event
    - the amount of total items in the hopper (maxed out at 5 stacks)
    - total item input rate
    - total item output rate
- similar thing for the other trackers



- Prevent the server from sending custom item & block packets to players that don't have the mod installed



- make texture loader try again after a delay when the JVM runs out of memory (it prints an error, prob can be catched)



- add a "Large Black Hole" model that shares the custom rendering of item sinks
  - maybe add customizable size? it's a block entity anyway it can do that stuff.
  - much bigger than the standard item sink model. bigger lensing effect too
  - write in the item that this is only aesthetic and doesn't do anything. only creative mode ofc.
  - maybe add the same thing for item sources, aesthetic version.




- add a "regenerate" command/tool/something
  - This lets you regenerate the selected area to the default world generation,
  - The process removes all block and entities in the selection
  - Async with progress bar




- fix overlays not working properly in multiplayer??? idk





ADD ALL OF THIS TO THE README
ADD ALL OF THIS TO THE README
ADD ALL OF THIS TO THE README

- maybe add an "area block distribution" tool?
  - MAYBE MERGE IT WITH THE CONTAINER TOOLS TOOL?
    - call them "Area analysis" or something

- SELECTION TOOL
  - left/right click to select an area, infinite reach with preview
  - right click a selection to open an in-game GUI menu (minimal, unobstructive, that doesn't stop the game). EACH ACTION HAS A KEYBIND
    - discard selection
    - grow selection (maxes out to connected blocks)
    - shrink selection (makes it as small as possible)
    - regenerate
    - "view slice" - view a 1-block slice of the selection on a specific axis
      - use the scroll wheel to cycle the active slice
      - invisible slices are fully intangible for the player, but they still exist in the world and interact with everything else. 
        - (use the same block rendering filter system but add support for slices)
    - [open replace block menu, affects the whole selection]
    - [open area analysis menu]
      - Count and find items within containers in a selected area
      - Replace item stacks remotely
      - Easily fill open containers with specific items

- GIT INTEGRATION
  - add a UI for it with the branch tree and all
  - ONE REPO PER WORLD OR SERVER
    - changing commit changes the entire world data
    - this includes mobs, blocks, world state.
      - player state, position, and data are NOT included. Players need to change version without getting teleported around or anything of that kind.
      - obviously, mod jars, config files, and other such things aren't included
    - multiplayer users share the same repo and are all on the same commit, as the versioning system is global.
    - all of the dimensions are committed together. This avoids breaking multi-dimensional machines

- Improve readability of glsl shaders

- Fix static black hole and white hole block models
- Fix item models of Item Sink, Item Source, Cosmetic Black Hole, Cosmetic White Hole

- Fix models of Item Pipe

- Add a despawn cooldown option for invisible block overlays

- Improve sprite of Player Scale feature

- fix pause screen not resizing visually, even when the setting is different from default
  - The text does scale with the gui scale, but the actual visual size doesn't





- FEATURE VIEWER
  - view all possible shapes of a renewable feature
  - trees, idk what else

- IMPORTANT but idk if it should be in 1.0.0
  - add a creative tweaks feature that lets the player move at normal speed even when the TPS is slower than 20
  - RELEASE A SEPARATE MOD CONTAINING ONLY THIS FEATURE



- disable sound <sound type> for each sound type




- screen background
  - slowly fade out the colors using a dark fill layer.
  - can be disabled from settings
  - can disable blur from settings
































# After v1.0.0 release

- Make text translatable
- Add GB versions of text
- Add more languages

- add togglable procedural rain sounds
  - mix sounds randomly
  - a few long background rain recordings
  - long wind background
  - a few shorter sounds of larger, more sparse rain drops
  - sounds of individual water droplets falling on metal surfaces, plastic buckets, that kind of stuff
