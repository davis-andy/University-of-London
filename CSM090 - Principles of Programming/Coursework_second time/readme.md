[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/Hv__-qug)
# Queen and King Chess Puzzle 

> **Do not start development before you have read the Development process and Marking sections below. If your development process does not comply with our policies your mark will be reduced.** 
## Intro

This project takes inspiration from chess, although it has significant differences with the standard [chess game](https://en.wikipedia.org/wiki/Chess). To reduce your workload, only the **queen and king** pieces will be involved in play.  The game will be played between **Black** and **White**, as usual. However, the board size will be `S x S`, where `S`  is a number between `3` and `26`, instead of the usual `8 x 8`. (We only set the limit `26`  to avoid visualisation/printing problems for large boards and denotation.) Also, rather unusually, each side (Black or White) may play with any number of queens in any positions, as long as they fit the board. E.g., there can be 5 black queens on the board in a play. But, **each side has exactly one king**, as usual in chess. 

White starts the game, after which Black and White alternate by moving one of own pieces according to the usual chess rules, that are:

- *[Rule1]* A queen can move any number of squares horizontally, vertically or diagonally (along both diagonals), but cannot leap over other pieces.
- *[Rule2]* The king moves one square in any direction, including diagonal, on the board. 

As usual, as a result of any move, the piece that is moved either occupies a previously empty board location, or captures the other side's piece. In that case, the former piece occupies the latter's position, while the latter piece is removed from the board. Clearly, we have the following:

- *[Rule3]* A piece of side X (Black or White) cannot move to a location occupied by a piece of side X.

**Check** for side X is a configuration of the board when X's king can be captured by a piece of the other side Y (in one move). Another chess rule we obey is:

- *[Rule4]* A piece of side X cannot make a move, if the configuration resulting from this move is a check for X. 

**Checkmate** for side X is a configuration of the board when the king of a side X (Black or White)  is in *check* and there is no move available for X to eliminate the *check* situation.

**Stalemate** for side X is a configuration of the board when the side X is *not in check* and there is no move available for X.

Every game results in a win of side X, or a stalemate for side X, or it runs infinitely. Side X wins if the game reaches a configuration which is a checkmate for the opposite side. 



## Notation and Symbols

The columns will be designated by small letter characters from `a` to `z` and the rows by numbers from `1` to `26`. The leftmost column is `a` and the bottom row is `1`.

### Board configurations

We will need *plain board configurations* that are stored in files (on the PC) and *unicode board configurations* that are printed on the screen.

#### Plain board configurations

Each plain board configuration is determined by a sequence of *piece locations*, where a piece location is a string of the form `Xcr` and  `X` is either equal 

- to `K`, to indicate  King, or 
- to `Q` to indicate Queen

and `cr` indicates the column and row of a location of the piece. E.g., `Qe14` says that there is a queen in column `e` and row `14`.

Now, to store a configuration of the whole board in a file, we will use the following format:

- the **first line** of the file contains a single number representing the size of the board `S`
- the **second line** of the file contains piece locations of **White** pieces separated by `,`
- the **third line** of the file contains piece locations of **Black** pieces separated by `,`
- the last `,` on either line can be omitted and there may be arbitrarily many spaces before and after any `,`

See the file `board_examp.txt` for an example. A file is *valid* if it is syntactically correct as specified above, the configuration encoded in it has exactly one king for White and exactly one king for Black, there are no different pieces in the same location, and each location is within the `S x S` square.

#### Unicode board configurations


To designate the pieces, we will use the [chess unicode characters](https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode):
 
| piece |  character | escape sequence   |
|-------|------------|--------------|
|white king | ♔ | \u2654 |
|white queen | ♕ | \u2655 |
|black king | ♚ | \u265A|
|black queen | ♛ | \u265B|
|space of matching width | |\u2001|

> **Note** In Python code, you can use the characters "directly" by copy/pasting from the table above (except the space), or by the escape sequence. E.g., 
>```
>print("♔")
>```
>or 
>```
>print("\u2654")
>```
>will print  `♔` and 
>```
>"♔"=="\u2654"
>```
>will print `True`.

When outputting the configuration of an `S x S` board on the screen, we will use the format, where the output has `S` lines and each line is a string of the form `ln[0] ln[1] ... ln[S-1]` representing the corresponding row. So, each `ln[i]` is either one of ♔, ♖, ♚, ♜, or the space of the matching width (\u2001). For example, the plain board configuration stored in the file `board_examp.txt` corresponds to the following unicode board configuration:

~~~
  ♔  
   ♕ 
 ♚  ♛
     
  ♕  
~~~

### Moves

It will be needed to specify moves of pieces.  To indicate the moves, we will use the strings of the form `crCR`,  where `cr` indicates the column and row of the origin of the move, and `CR` indicates the row and column of the destination of the move. For example, `a1b2` says that the piece located in the column `a` and row `1` moves to the column  `b` and row `2`. Note that the string `crCR` can have length between 4 and 6.

## Requirements

>Note: the requirements below are mandatory to follow. You will lose marks if your implementation does not meet these requirements

In this coursework, you will implement a Python program, in which a human user will play the specific version of chess described above against the computer. The human always plays with White and computer always plays with Black.    

### Initiation

When the program is executed, it first prompts the user to provide a file name that stores a plain board configuration:

```
File name for initial configuration: 
```
The user inputs the file name or types `QUIT` to terminate the program. If this file is not valid (see Plain board configurations), the program states that and prompts to provide a file name again:

```
This is not a valid file. File name for initial configuration: 
```
The user inputs the file name or types `QUIT` to terminate the program. This continues until the user provides a valid file or terminates the program. If a valid file is provided, the plain board configuration it contains becomes the initial configuration of the play. This configuration is printed on the screen in unicode format. For example, 

```
The initial configuration is:
  ♔  
   ♕ 
 ♚  ♛
     
  ♕  
```

### Play rounds

Each round is a move of White followed by a move of Black. In each round, the program prints:

```
Next move of White:
```

The user can indicate a move in the format described above (see Moves), e.g.

```
Next move of White: c3c4
```

Instead of making a move, White can print `QUIT` to indicate that they want to stop the game and save the current configuration in a file. If the user prints `QUIT`, the program prompts the user to provide a name of the file to store the current configuration:

```
File name to store the configuration:
```

After specifying the file name, the program saves the current configuration in the plain format. The program prints the confirmation:

```
The game configuration saved.
```
and terminates.

If the user inputed a move, the program checks if this move is valid chess move (see Intro). If this is not the case, the program prompts the user to provide another input:

```
This is not a valid move. Next move of White:
```

This continues until the user inputs a valid chess move or `QUIT` to save the configuration and terminate the program. When a valid chess move is inputed, the program prints the next configuration of the game (after White's move), e.g.,

```
The configuration after White's move is:
   ♔ 
   ♕ 
 ♚  ♛
     
  ♕  
```
Now, the current configuration may be a checkmate or stalemate for Black (or none of those). If it is a checkmate the program prints:
```
Game over. White wins.
```
and terminates. If it is a stalemate, the program prints:
```
Game over. Stalemate.
```
and terminates. Otherwise, the program computes the next valid move for Black, using any method you like, prints the move and the configuration after this move, e.g.:
```
Next move of Black is e3c1. The configuration after Black's move is:
   ♔ 
   ♕ 
 ♚   
     
  ♛  
```
The current configuration may be a checkmate or stalemate for White (or none of those). If it is a checkmate the program prints:
```
Game over. Black wins.
```
and terminates. If it is a stalemate, the program prints:
```
Game over. Stalemate.
```
and terminates. Otherwise, a new round of the game occurs. 

## Software Specification

>**Important notes:** 
>
> - The specification below, including class/function/method names and data types of arguments/return values, is mandatory to follow. **You will lose marks** if your implementation does not adhere to the specification, **even in the case your program runs with no errors**.
> - All the classes/functions/methods in the file `chess_puzzle.py` must be implemented according to the specification. You can use additional classes/functions/methods.
> - The tests initially present in the file `test_chess_puzzle.py` must pass, when `pytest test_chess_puzzle.py` is executed. Your additional tests (see section Validation) must pass as well.
> - You can use additional files for some parts of your code.
> - Instructions marked with Hint or Hints are not a part of the specification and may be ignored without effect on your mark.
> - Your implementation must be in Python 3.10.
> - Your code must be well-structured in terms of visual readability (use indentation, spacing, etc.) and non-redundancy (instead of duplicating long pieces of code, introduce your own functions). We do not require to follow any variable naming conventions. 
> - All the required functions and methods in the file `chess_puzzle.py` must pass any correct unit tests on them, particularly, unit tests that do not set up any global (or class-wide) variables. Thus, if you are using any global (or class-wide) variables in the implementation of the required functions and methods, make sure that this requirement is satisfied.   

It will be convenient, instead of using chess locations given by columns and rows, such as `e2`, to use the horizontal (i.e., x) and vertical (i.e., y) coordinates of a location ranging from `1` to `26`, such as, respectively, `5` and `2`. The column `a` corresponds to the horizontal coordinate `1`, the column `b` corresponds to the horizontal coordiate `2`, etc., while row `1` corresponds to the vertical coordinate `1`, row `2` corresponds to the vertical coordinate `2`, etc. We need a function that converts chess locations to coordinates, and another function that converts vice versa:
```
def location2index(loc: str) -> tuple[int, int]:
    '''converts chess location to corresponding x and y coordinates'''
    
	
def index2location(x: int, y: int) -> str:
    '''converts  pair of coordinates to corresponding location'''
```

To represent chess pieces, we will make use of the following class:
```
class Piece:
    pos_x : int	
    pos_y : int
    side : bool # True for White and False for Black
    def __init__(self, pos_X : int, pos_Y : int, side_ : bool):
        '''sets initial values'''
```
and to represent a board configuration, we will use a pair (tuple) of an integer representing the size of the board `S` and a list of pieces, i.e.,
```
Board = tuple[int, list[Piece]]
```
The list of pieces contains all the pieces present on the board and the locations on the board with the coordinates not occupied by any piece in the list are considered empty. The following two functions are required:
```
def is_piece_at(pos_X : int, pos_Y : int, B: Board) -> bool:
    '''checks if there is piece at coordinates pox_X, pos_Y of board B''' 
	
def piece_at(pos_X : int, pos_Y : int, B: Board) -> Piece:
    '''
    returns the piece at coordinates pox_X, pos_Y of board B 
    assumes some piece at coordinates pox_X, pos_Y of board B is present
    '''
```

We introduce the two subclasses of `Piece`, one for each piece type involved in the play:
```
class Queen(Piece):
    def __init__(self, pos_X : int, pos_Y : int, side_ : bool):
        '''sets initial values by calling the constructor of Piece'''
	
    def can_reach(self, pos_X : int, pos_Y : int, B: Board) -> bool:
        '''
        checks if this queen can move to coordinates pos_X, pos_Y
        on board B according to rule [Rule1] and [Rule3] (see section Intro)
        Hint: use is_piece_at
        '''
    def can_move_to(self, pos_X : int, pos_Y : int, B: Board) -> bool:
        '''
        checks if this queen can move to coordinates pos_X, pos_Y
        on board B according to all chess rules
        
        Hints:
        - firstly, check [Rule1] and [Rule3] using can_reach
        - secondly, check if result of move is capture using is_piece_at
        - if yes, find the piece captured using piece_at
        - thirdly, construct new board resulting from move
        - finally, to check [Rule4], use is_check on new board
        '''
    def move_to(self, pos_X : int, pos_Y : int, B: Board) -> Board:
        '''
        returns new board resulting from move of this queen to coordinates pos_X, pos_Y on board B 
        assumes this move is valid according to chess rules
        '''


class King(Piece):
    def __init__(self, pos_X : int, pos_Y : int, side_ : bool):
        '''sets initial values by calling the constructor of Piece'''
    def can_reach(self, pos_X : int, pos_Y : int, B: Board) -> bool:
        '''checks if this king can move to coordinates pos_X, pos_Y on board B according to rule [Rule2] and [Rule3]'''
    def can_move_to(self, pos_X : int, pos_Y : int, B: Board) -> bool:
        '''checks if this king can move to coordinates pos_X, pos_Y on board B according to all chess rules'''
    def move_to(self, pos_X : int, pos_Y : int, B: Board) -> Board:
        '''
        returns new board resulting from move of this king to coordinates pos_X, pos_Y on board B 
        assumes this move is valid according to chess rules
        '''
```
To check for *checks* and *checkmates*, we require the functions:
```
def is_check(side: bool, B: Board) -> bool:
    '''
    checks if configuration of B is check for side
    Hint: use can_reach
    '''

def is_checkmate(side: bool, B: Board) -> bool:
    '''
    checks if configuration of B is checkmate for side

    Hints: 
    - use is_check
    - use can_move_to 
    ''' 
    
def is_stalemate(side: bool, B: Board) -> bool:
    '''
    checks if configuration of B is stalemate for side

    Hints: 
    - use is_check
    - use can_move_to 
    '''
``` 

To read the configuration from files (on PC) and save it, we will need the functions:
```
def read_board(filename: str) -> Board:
    '''
    reads board configuration from file in current directory in plain format
    raises IOError exception if file is not valid (see section Plain board configurations)
    '''

def save_board(filename: str, B: Board) -> None:
    '''saves board configuration into file in current directory in plain format'''
```
To generate Black's moves by the computer player, we need:
```
def find_black_move(B: Board) -> tuple[Piece, int, int]:
    '''
    returns (P, x, y) where a Black piece P can move on B to coordinates x,y according to chess rules 
    assuming there is at least one black piece that can move somewhere

    Hints: 
    - use can_move_to
    - possibly, use methods of random library
    '''
```
We suggest the following simplest approach to implement `find_black_move`.  For every Black piece on the board and piece coordinates `x,y`, where `x` and `y` are in the range `1..S`, check if the piece can move there. If so, return this piece and the coordinates. Further, to make the behaviour of the computer player less "predictable", you can pick the pieces on the board in a random order. Also, you can pick the coordinates randomly. This function will not be verified by unit tests (see next section) and you can use any approach to implement it that returns valid results.

For the screen output, we need:
```
def conf2unicode(B: Board) -> str: 
    '''converts board cofiguration B to unicode format string (see section Unicode board configurations)'''
```

Finally, we require the implementation of the play to be enclosed in the function:
```
def main() -> None:
    '''
    runs the play

    Hint: implementation of this could start as follows:
    filename = input("File name for initial configuration: ")
    ...
    '''    
```

## Validation

In the file `test_chess_puzzle.py`, you will find initial unit tests in pytest format for some of the functions/methods provided in the specification. You must provide sufficient tests for all functions and methods. There must be **at least five** test cases in pytest format for each such function/method. We do not mandate how the test cases are organised, i.e., implementation of the test cases related to a function/method can be placed in different test functions, or grouped together. Your tests must cover as many scenarios as possible. *The quality of your testing contributes to the mark.* You will lose marks if you have too few tests, your tests fail, or you did not provide tests for some important scenarios.

Those following functions/methods are exempt from testing due to their particular nature and you don't need to provide tests for them:

- constructors of classes
- `save_board`
- `find_black_move`
- `main`



## Development process

You are expected to work on the project in your assigned *GitHub* repository (which contains this file). You must create frequent commits, with messages, that describe the history of the development of your project. **It is required to create a new commit before the number of lines affected by your changes since your last commit exceeds 20 for every file in your repo.** The number of affected lines in a file is calculated as the *maximum between the number of insertions and the number of deletions* required to obtain the new version of the file from the last committed version of it. One modification of a line counts as one deletion and one insertion.

> For example, if you modified lines `2` and `3` in the file `file1`, deleted lines `4` and `5` and then added a new line before line `2`, the number of insertions is 3, the number of deletions is 4 and the number of lines in `file1` that we consider affected by your changes is 4. If you commit now, the development process requirement would be satisfied because `4 <= 20` (provided there no other files in the repo). The requirement would be also satisfied if along with 4 affected lines in `file1` you had, say, 18 affected lines in another file `file2` in the repo.

> You can check how many insertions and deletions you have in a given FILE_NAME since the previous commit by running a command `git diff --stat --ignore-cr-at-eol FILE_NAME` in your repo directory.  

You will lose marks if you do not meet this requirement (see Marking). Your mark will not be reduced if any intermediate commit, i.e., a commit before the last on `main`, contains incorrect code. The use of branches in git is optional and does not contribute to your mark. If you create branches in your project, then the requirement of having no more than 20 lines affected by changes between commits applies to every commit in every branch, but does not apply to commits that merge branches. You do not have to *push* your repo to GitHub as frequently as you commit; file differences between pushes do not affect your mark. 

## Submission

**The submission date/time of your project is the date/time of your latest push of the `main` branch of your allocated repository to GitHub.** To mark your submission, the contents of the files
- `chess_puzzle.py`
- `test_chess_puzzle.py`
  
in the last commit on the GitHub version of this branch will be reviewed and executed along with any other files imported in these files. The commit history of your allocated GitHub repository will be reviewed as well, but intermediate versions of your files will not be executed.

When marking, your files will be executed inside your project Codio assignment. Therefore, we strongly recommend you to check that the files above run as expected in your project Codio assignment.

**You must confirm that your project is ready for marking by "marking completed" your project assignment on Codio.** Your project will be reviewed/marked at arbitrary time after you marked it completed on Codio but not before the submission deadline has passed. If you marked your project assignment on Codio completed, but want to update it, please contact your module leader. 

## Additional Libraries

You can use *any standard Python libraries available via pip for Python 3.10* in your implementation. If you do, you must make sure they are installed in your Codio project assignment and your implementation runs on Codio as expected. (You can install them using `pip install ...` command in Terminal. You can request admin permissions if necessary using `sudo` command; see Linux documentation.)

## Marking

Your project mark will be calculated by taking the difference between the​ original mark and penalty. The **original mark** is based on your demonstrated capacity of applying software development skills taught in this module. It is determined according to the following rubric:

|Category| Weight | full | 3/4 | 1/2 | 1/4 | 0 |
|--|--|--|--|--|--|--|
| **Requirements** (system responds to all use scenarios and the responses are as expected) | 40% | Excellent | Good | Satisfactory | Minimally acceptable | Unacceptable |  
| **Specification** (all required parts (functions, classes, etc.) are present and their implementation is correct (including data types)) | 35% |Excellent | Good | Satisfactory | Minimally acceptable | Unacceptable |
| **Validation** (appropriate variety of scenarios are verified and tests for them are correctly written) | 25% | Excellent | Good | Satisfactory | Minimally acceptable | Unacceptable |

> Reuse of publicly available or generated from natural language code is not normally taught in this module, so their excessive use in your solution can negatively affect your mark in any of the categories.

The **penalty** is given for violation of the principles stated in the Development process section as well as for minor violations of the plagiarism policy of this module or institution (e.g., a small proportion of code copied without a citation). **It may be 15%, 30%, 45% or 60% depending on a degree of the violation.** 

> For major violations of the plagiarism policy, the suspected cases will be handled according to the student discipline process of the institution.
