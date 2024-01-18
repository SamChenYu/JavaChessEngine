# JavaChessEngine
In-Progress Chess Engine Developed With The MiniMax Algorithm

EVALUATION Method:  
Evaluation is between a number between -1 and 1.   
Any number above 0 means white is winning. 1 is checkmate for white  
Any number below 0 means black is winning. -1 is checkmate for black  
0 is an even game  


ROADMAP:
- GUI ✅ (10/01/24)
- Input with FEN Notation✅ (07/01/24)
- Basic Evaluate Function ✅ (08/01/24)
  
Engine (IN PROGRESS)
- Calculate Possible Moves Function ✅ (14/01/24)
- isCheckmate Function ✅ (18/01/24)
- MiniMax Algorithm
- Alpha-Beta Pruning


- Make Move Function: ✅ (18/01/24)
  Things to take note:
  Managing En Passant
  Managing King Castling Rights [ if a rook / king moves, then privileges are revoked ]
  Checks / Checkmate

  
- Proper Evaluate Function
(The basic evaluate function is solely based on material, and nothing else. This is to faciliate the development of the minimax algorithm, then a proper evaluate function will be built.)

- OUPUT FEN NOTATION
- Ability to Play Moves Directly On Panel

<div align="center">
  <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/f254d4b6-aa5b-4a99-8ab3-1cf218cc59eb" alt="Image Alt Text">
</div>


<div style="text-align:center;">
    <img src="https://github.com/SamChenYu/JavaChessEngine/assets/150127006/3d2b4d18-f49d-42a7-9845-dc88e29c3bca" alt="EnginePanel">
</div>


