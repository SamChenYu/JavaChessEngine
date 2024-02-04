# JavaChessEngine
In-Progress Chess Engine Developed With The MiniMax Algorithm.

Current Estimated Elo (Depth 4) at 1150
 
## Match History
### Martin 250 Elo (www.chess.com)
- 31/02/2024 Depth 1 Engine : LOSS ( Estimated Rating 100 )
- 04/02/2024 Depth 4 Engine + Updated Evaluation: WIN (Estimated Rating 1150)

## EVALUATION Method  
The evaluation method is between -1 and 0.

1 is checkmate for white

-1 is checkmate for black

0 is equal evaluation or stalemate

The evaluation takes into account two factors:  Position difference and material difference.

The positional difference is calculated with piece square tables where each piece has assigned values on every square, determining the strength of the particular position. The material difference is calculated with a sigmoid function:  

$y = \frac{1}{1.1 + e^{(-x+4)}} \quad \text{for} \quad 0 < x < 39$

where x is the numerial material difference and y is the material evaluation.
These two factors are weighted dynamically, where very similiar material will weigh position more, whilst bigger material difference will weigh position less.  
[Link to PeSTO's Evaluation Function](https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function)

[Link to the TalkChess forum discussion](http://www.talkchess.com/forum3/viewtopic.php?f=2&t=68311&start=19#)



## ROADMAP:
- GUI ✅ (10/01/24) 
- Input with FEN Notation✅ (07/01/24)
- Basic Evaluate Function ✅ (08/01/24)
  
Engine ✅ (04/02/24)
- Calculate Possible Moves Function ✅ (14/01/24) ✅  Overhauled (28/01/24)
- isCheckmate Function ✅ (18/01/24)
- MiniMax Algorithm ✅ (31/01/24)
- Alpha-Beta Pruning ✅ (03/02/24)


- Make Move Function: ✅ (18/01/24)
  Managing En Passant ✅
  Managing King Castling Rights ✅
  Checks / Checkmate ✅
- Proper Evaluate Function✅ Introduced Piece Square Tables(31/01/24)
- OUPUT FEN NOTATION
- Ability to Play Moves Directly On Panel

- Introduce multi threading and transposition tables

<div align="center">
  <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/f254d4b6-aa5b-4a99-8ab3-1cf218cc59eb" alt="Image Alt Text">
</div>


<div style="text-align:center;">
    <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/3d2b4d18-f49d-42a7-9845-dc88e29c3bca" alt="EnginePanel">
</div>


