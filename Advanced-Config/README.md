## Options
``` yaml
BungeeMode: false 		# Use BungeeCord / Singel Arena Mode
Metrics: true     		# Use Metrics
UpdateCheck: true 		# Use the Update Checker
GameEnd:          		# Game End Commands
  Arena:          		# For the Arena
  - ''            
  User: []        		# For the User
  Place:          
    '1': []       		# For the 1st Place
    '2': []       		# For the 2nd Place
    '3': []       		# For the 3rd Place
Enchantment:      		# Sword Enchantments
  KNOCKBACK:      		# Knockback
    use: false    		# Use Enchantment
    level: 1      		# Enchantment Level
  DAMAGE_ALL:     		# Sharpness
    use: false    		# Use Enchantment
    level: 4      		# Enchantment Level
  DURABILITY:     		# Unbreaking
    use: false    		# Use Enchantment
    level: 3      		# Enchantment Level
Sword:					      # Sword Options
  Unbreakable: false	# Make Sword Unbreakable
  ItemFlag:				    # Item Flags
    HIDE_ENCHANTS:		# Hide enchants
      use: false		  # Use Flag
    HIDE_ATTRIBUTES:	# Hide Attributes like Damage
      use: false		  # Use Flag
    HIDE_UNBREAKABLE:	# Hide the unbreakable State
      use: false		  # Use Flag
Bow:					        # Bow Options
  Unbreakable: false 	# Make Bow Unbreakable
  ItemFlag:				    # Item Flags
    HIDE_ATTRIBUTES:	# Hide Attributes like Damage
      use: false		  # Use Flag
    HIDE_UNBREAKABLE:	# Hide the unbreakable State
      use: false		  # Use Flag
```

## Config
``` yaml
test:                 	# Arena Name
  Countdown: 15       	# see Configuration Help
  MaxPlayers: 20      	# see Configuration Help
  KillsToWin: 25      	# see Configuration Help
  AutoStartPlayers: 8 	# see Configuration Help
  EndTime: 600        	# see Configuration Help
  GameEnd:            	# Game End Commands
    Arena: []         	# For the Arena
    User: []          	# For the User
    Place:
      '1': []         	# For the 1st Place
      '2': []         	# For the 2nd Place
      '3': []         	# For the 3rd Place
  Enchantment:        	# Sword Enchantments
    KNOCKBACK:        	# Knockback
      use: false      	# Use Enchantment
      level: 1        	# Enchantment Level
    DAMAGE_ALL:       	# Sharpness
      use: false      	# Use Enchantment
      level: 4        	# Enchantment Level
    DURABILITY:       	# Unbreaking
      use: false      	# Use Enchantment
      level: 3        	# Enchantment Level
  Sword:			  	      # Sword Options
    Unbreakable: false 	# Make Sword Unbreakable
    ItemFlag:			      # Item Flags
      HIDE_ENCHANTS:	  # Hide enchants
        use: false		  # Use Flag
      HIDE_ATTRIBUTES:	# Hide Attributes like Damage
        use: false		  # Use Flag
      HIDE_UNBREAKABLE:	# Hide the unbreakable State
        use: false	  	# Use Flag
  Bow:					        # Bow Options
    Unbreakable: false 	# Make Bow Unbreakable
    ItemFlag:			      # Item Flags
      HIDE_ATTRIBUTES:	# Hide Attributes like Damage
        use: false		  # Use Flag
      HIDE_UNBREAKABLE:	# Hide the unbreakable State
        use: false		  # Use Flag
```
