# PawnChess
Simple variation of chess, with pawns as the only available pieces

Pawns can make the same moves as in the original version (excluding promotion):
- moving forward (1 square, or 2 squares if it's pawn's first move)
- capturing opposite pawns diagonally
- en passant (special pawn-only move type (see: https://en.wikipedia.org/wiki/Chess#En_passant)

Game terminal accepts only particular input type: 
(without any spaces and uppercase letters) starting square coordinates (column from a to h and row from 1 to 8), the destination coordinates (columns rows)
for example "a2a4" means "pawn at a2 has to move to a4"

The game is over when:
- player types in "exit"
- player fullfills winning conditions (his pawn goes to last row or captures the last opponent's pawn)
- there is a stalemate (no possible moves)
