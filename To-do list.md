# To-do list

- **Rendering**:
  - ~~Selectively hide blocks by type or rendering method~~
  - ~~Click through hidden blocks~~
  - ~~Custom block outlines~~

- **Alternative textures**
  - ~~Minimal Redstone Wire~~
  - Disable redstone wire particles
  - ~~Transparent Slime Block~~
  - ~~Transparent Honey Block~~
  - ~~Unobstructive Mangrove Roots~~
  - ~~Unobstructive Scaffolding~~
  
  - ~~Consistent sloped rails~~
  - ~~Alternative Barrier rendering~~
  - ~~Alternative Structure Void rendering~~
  - ~~Alternative Light Block rendering~~

  - ~~3D Redstone Wire~~
  - ~~3D Rails~~
  - ~~3D Ladders~~
  - 3D Iron Chains & Copper Chains
  - 3D Iron Bars & Copper Bars
  - ~~3D Vines~~
  - ~~3D Glow Lichen~~
  - Player model rendering in the pause menu //TODO add this to the readme if possible
    - Display player name above model
    - Display customizable title under the name
  - Replace block entity rendering with static models //TODO idk if this is needed or even possible to implement. If it is, add this to the readme

- **Overlays**:
  - Highlight the blocks moved by a piston
  - Highlight unstable blockstates //TODO idk if this is needed or even possible to implement. If it is, add this to the readme
  - Display scheduled tick timers
  - Display Hopper cooldown timers
  - ~~Show Redstone Wire power level~~
  - ~~Show Powered Rail and Activator Rail power levels~~
  - ~~Show Comparator power levels~~
  - Show current fuel and item of Furnaces, Smokers and Blast Furnaces
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
  - ~~Control flying speed~~
  - ~~Control reach distance~~
  - ~~Control interaction radius (click multiple blocks at once)~~
  - add in-game UI for these. display their current values with icons and such

  - Configurable fast clicking
  - Snap view to specific angles //TODO in-game indicators for multiples of 22 and 45 ° on both axes. use a keybind to snap to them
  - Place blocks facing away from the player
  - Place blocks with an offset
  - Insta kill mobs
  - add in-game UI for fast clicking. display its current values with icons and such

  - Scroll block delays and container levels with your mouse wheel >:3
  - Teleport to the block you are looking at with a keybind
  - Zoom view with a keybind
  - Toggle full brightness with a keybind

  - ~~Phase through blocks while flying~~
  - ~~Phase through entities~~
  - ~~Disable bouncing on Slime Blocks~~
  - Disable not being able to jump from Honey Blocks
  - Disable sliding on the sides of Honey Blocks
  - ~~Disable being slowed down by Slime Blocks~~
  - ~~Disable being slowed down by Honey Blocks~~
  - ~~Disable being slowed down by Soul Sand~~
  - ~~Disable being slowed down by powder snow~~
  - ~~Disable sliding on Ice, Packed Ice and Blue Ice~~
  - ~~Disable being moved by Water and Lava currents~~
  - Disable being dragged by Bubble Columns

  - Disable item change animation
  - Disable hand swing animation
  - ~~Disable being on fire~~
  - ~~Disable freezing effect~~
  - Disable dimension change loading screens
  - Disable reduced FOV when inside a body of Water
  - ~~Disable overlay effect of Water~~
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
  - Infinite item sources
  - Rate counter hoppers
  - Item Sink (storage block with infinite capacity)
  - 0 Friction blocks
  - ∞ Friction blocks
  - [Vanilla Item] Stick item with Custom Name //TODO meant for filters. add to item lore
  - [Vanilla Item] Cobblestone item with Custom Name //TODO meant for fuel filters (sticks can hopper into furnaces and such). add to item lore
  - [Vanilla Item] Name Tag item with Custom Name //TODO meant for name tagging mobs (makes them persistent). add to item lore
  - [Vanilla block] Fire item //TODO same placement requirements as fire
  - [Vanilla block] Soul Fire item //TODO same placement requirements as soul fire
  - [Vanilla block] Nether Portal item
  - [Vanilla block] End Portal item
  - [Vanilla block] End Gateway item
  - [Vanilla block] Headless Piston item   //TODO add this to item lore -> //FIXME Updates make the head appear
  - [Vanilla block] Headless Sticky Piston item   //TODO add this to item lore -> //FIXME Updates make the head appear
  - [Vanilla block] Sticky Piston Head item   //TODO add this to item lore -> //FIXME Updates make the block break unless properly connected to a headless sticky piston
  - [Vanilla block] Piston Head item   //TODO add this to item lore -> //FIXME Updates make the block break unless properly connected to a headless piston
  - [Vanilla block] Short Sticky Piston Head item   //TODO add this to item lore -> //FIXME Updates make the block break unless properly connected to a headless sticky piston
  - [Vanilla block] Short Piston Head item   //TODO add this to item lore -> //FIXME Updates make the block break unless properly connected to a headless piston
  - [Vanilla block] Frosted Ice item       //TODO add this to item lore -> //FIXME Updates make it melt
  - [Vanilla block] Water Cauldron item
  - [Vanilla block] Lava Cauldron item
  - [Vanilla block] Powder Snow Cauldron item
  - [Vanilla block] Unlit Campfire item
  - [Vanilla block] Unlit Soul Campfire item
  - [Vanilla block] Kelp Plant item
  - [Vanilla block] Cave Vines Plant item
  - [Vanilla block] Twisting Vines Plant item
  - [Vanilla block] Weeping Vines Plant item
  - [Vanilla block] Frogspawn item
  - [Vanilla blocks] Cake with Candle items //TODO same placement requirements as cake //FIXME maybe generate them automatically? idk what to do about the render though
  - [Vanilla blocks] Potted plant items //TODO same placement requirements as flower pot
  - [Vanilla blocks] Sloped Rail items //TODO same placement requirements as rails
  - [Vanilla block & item] Your Player Head as an item
  - [Vanilla block & item] Bee Nest with Bees and Honey
  - [Vanilla block & item] Beehive with Bees and Honey
  - [Vanilla block & entity] Armor Stand with arms



- disable punching end crystals? //TODO maybe? idk, very niche









# Not features:

- Update UI, draw one with a more modern style
- render stats //TODO minecraft.getGpuUtilization()



- Fix changes caused by the custom interaction radius not sending block updates to the client //TODO /fill command also does this?????

- Add small pretty custom icons for all the settings

- add cool redstone mechanism overlay in front of the main menu screen
- add cool small redstone mechanism overlay in the pause menu



- maybe for future versions: a "profiler" menu that tracks CPU time usage of each mob category / block type / stuff


- add a "about" screen that talks abotu the mod, basically just copy the readme.
  - display my skin/drawing in there too and name


- add images to the readme?
  - show a screenshot of a big block of block entities and like 2fps/300fps difference when static models are on

- add a "suppress sign GUI" option in creative tweaks to stop sign UIS from opening when placing any kind of sign or hanging sign