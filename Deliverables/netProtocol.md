Spaces is replaced with hash (`#`).

# Extended commands structure

The words written in capital refers to Java enumeration, the actual string sent throw net could be different.

## Connection and lobby commands

### From client to server
- `LOGIN username` Client request to login with username.
- `LOGOUT` Client request to logout (leave game).

### From server to client

- `LOGIN_RESPONSE (success username sessionID)|(fail code)` Server response to client login request. If successful a session id is sent to the user, otherwise one of the following codes are sent:
  - `0` There's already a logged user with the same username.
  - `1` The lobby is full or a game already started.
  - `NOT_LOGGED_YET`
- `NOTIFY_NEW_USER username` Inform client that a new user has logged in the room.
- `NOTIFY_SUSPENDED_USER username` Inform client that another user has been suspended.
- `SEND_PLAYERS_LIST username [username] [username] [username]` Send the complete players list.

## Game and players preparation commands

### From server to client

- `START_GAME` Inform client that the game in started.
- `SEND_PRIVATE_OBJECTIVE_CARD color name description` Send private objective card to user.
- `SEND_WINDOW_PATTERNS_TO_CHOOSE WindowPattern WindowPattern WindowPattern` Send window patterns to player.
- `SEND_TOOL_CARDS ToolCard ToolCard ToolCard` Send public objective cards to player.
- `SEND_PUBLIC_OBJECTIVE_CARDS PublicObjectiveCard PublicObjectiveCard PublicObjectiveCard` Send public objective cards to player.

### From client to server
- `SELECT_WINDOW_PATTERN index` Send the index of the window pattern selected (index of thw window pattern sent with `SEND_WINDOW_PATTERNS_TO_CHOOSE`).

## Game command

### From client to server

- `PLACE_DICE_FROM_DRAFT row col Dice` Place dice in player's window pattern at the specified row and col.

- `USE_TOOL_CARD index` Send the intent to use the tool card at the specified index.
- `END_TURN` End the player's turn.

- `SELECT_DICE_FROM_DRAFT_EFFECT index` Send the index of the selected dice in draft.
- `INCREMENT_OR_DECREMENT_DICE_EFFECT value` S
- `SELECT_DICE_FROM_WINDOW_PATTERN row col` Send the index of the cell of wich select the dice. 
- `MOVE_DICE_IN_WINDOW_PATTERN row col` Send the index of a cell in wich move a previously selected dice.
- `PLACE_DICE row col` Send the index of a cell in wich move a previously selected dice.
- `SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH round diceInRoid` Send the index of the selected dice in round track.

### From server to client

- `ROUND_ORDER index index [index] [index]` Send players order in current round.
- `NEW_TURN index TIME` Notify new turn, send the current player index and the maximum time to perform the moves.

- `UPDATE_DRAFT Dice ... [Dice]` Send the current dice in draft.
- `UPDATE_WINDOW_PATTERNS WindowPattern [WindowPattern] [WindowPattern] [WindowPattern]` Send all the window patterns.
- `UPDATE_PLAYERS_TOKENS` Send the favor tokens of every player.
- `UPDATE_TOOL_CARDS_TOKENS token1 token2 token3` Send the favor tokens currently placed over the tool cards.
- `UPDATE_ROUND_TRACK DiceList ... [DiceList]` Update the round track.

- `SELECT_DICE_FROM_DRAFT` Ask the client to select a dice from the draft.
- `SELECT_INCREMENT_OR_DECREMENT` Ask the client to increment or decrement a previous selected dice.
- `SELECT_DICE_FROM_WINDOW_PATTERN` Ask the client to select a dice from his window pattern.
- `MOVE_WINDOW_PATTERN_DICE` Ask the client to select a cell in which move a previous selected dice.
- `SELECT_DICE_FROM_ROUND_TRACK_AND_SWITCH` Ask the client to select a dice from the round track.
- `PLACE_DICE` Ask the client to select a cell in wich place a previous selected dice.

- `DICE_PLACEMENT_RESTRICTION_BROKEN` Notify that the attempt to place a dice has failed.
- `CELL_ALREADY_OCCUPIED` Notify that the attempt to place a dice has failed because the cell is already occupied.

- `END_OF_TOOL_CARD_USE` Notify the client that the current tool card's effects are finished.

## End of game command

- `SEND_SCORES score score [score] [score]` Send all players' score.
- `END_GAME_FOR_ABANDONEMENT` Notify the (only remaining) client that all the others players have abandoned and him win.

# Object definition

- `WindowPattern := name difficulty Cell[0][0] Cell[0][1] ... Cell[4][4] cell[4][5]`
- `Cell := restriction Dice`
- `Dice := value color`
- `ToolCard := id name description`
- `PublicObjectiveCard := id name description points`

# Protocol structure examples
- `login#Picasso` Login from a user called Picasso
- `login_response#failed#1` Login failed because the game is starting or is already started
- `roundOrder#4#0#1#2#3#3#2#1#0` In the 4th round, the order of players that will play is Players[0] -> Player[1] -> Player[2] -> Player[3] -> Player[3] > Player[2] -> Player[1] -> Player[0]

## Connection sequence diagram example
![](connSeq.png)