# JavaChessEngine
In-Progress Chess Engine Developed With The MiniMax Algorithm Built From Scratch.



# The main branch is under construction! The 2D piece representation branch is working. This current branch is being refactored into using bitboards!

### Todo:
- Refactoring of Piece[][] into long[] bitboard representation


## EVALUATION Method  
The evaluation method is between -1 and 0.

1 is checkmate for white

-1 is checkmate for black

0 is equal evaluation or stalemate

The evaluation takes into account two factors:
Position difference and material difference.

The positional difference is calculated with piece square tables where each piece has assigned values on every square, determining the strength of the particular position. The material difference is calculated with a sigmoid function:  

$y = \frac{1}{1.1 + e^{(-x+4)}} \quad \text{for} \quad 1 < x < 39$

where x is the numerial material difference and y is the material evaluation.
These two factors are weighted dynamically, where very similiar material will weigh position more, whilst bigger material difference will weigh position less.  
[Link to PeSTO's Evaluation Function](https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function)

[Link to the TalkChess forum discussion](http://www.talkchess.com/forum3/viewtopic.php?f=2&t=68311&start=19#)



<div align="center">
  <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/f254d4b6-aa5b-4a99-8ab3-1cf218cc59eb" alt="Image Alt Text">
</div>



<div style="text-align:center;">
    <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/d857dc26-99a5-4608-a0f1-c937d830824d" alt="EnginePanel">
</div>


[Link to RyiSnow's Chess Pieces](https://ryisnow.itch.io/pixel-art-chess-piece-images)
