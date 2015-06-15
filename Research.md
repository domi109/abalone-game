# Table of Contents #


# Introduction #
[Abalone](http://en.wikipedia.org/wiki/Abalone_(board_game)) is a two-person [zero-sum](http://en.wikipedia.org/wiki/Zero-sum) board game, with [perfect information](http://en.wikipedia.org/wiki/Perfect_information). This is a quick overview of different approaches to [artificial intelligence](http://en.wikipedia.org/wiki/Artificial_intelligence) which could be interesting to implement into the [abalone-game](http://code.google.com/p/abalone-game/).

## Game Complexity ##
Game complexity deals with how complex

### Space-State Complexity ###


### Game-Tree Complexity ###

### Complexity of Abalone ###
The average game-length of Abalone, between human players, is around 87 [ply](http://en.wikipedia.org/wiki/Ply_(game_theory)). By contrast the average game-length of Abalone, between computer players, is around 130 ply, as computer players play more conservative/defensive. The average [branching factor](http://en.wikipedia.org/wiki/Branching_factor) is around 60 - 65.

The space-state complexity of Abalone is similar to Checkers, and since checkers has been solved, it could be solved for Abalone. So space-state complexity for Abalone is low. Having said that, the game-tree complexity of Abalone is complex, due to the high branching factor (As a comparison the branching factor for Chess is around 35, half that of Abalone). Therefore the game-tree complexity of Abalone is high. Overall this leads to a relatively complex game, which is not easy to solve.

## Game Tree Search Algorithms ##

### Minimax Algorithm ###
[resource](https://chessprogramming.wikispaces.com/Minimax).
[Minimax](http://en.wikipedia.org/wiki/Minimax) is algorithm.

_Pseudo Code_:
```
public int Max(int depth) {
    if (depth == 0) return evaluate();
    int max = -infinity;
    for (all moves) {
    	score = Min(depth - 1);
        if(score > max)
            max = score;
    }
    return max;
}
 
public int Min(int depth) {
    if (depth == 0) return -evaluate();
    int min = +infinity;
    for (all moves) {
        score = Max(depth - 1);
        if(score < min)
            min = score;
    }
    return min;
}
```

_Advantages_:
  1. Easy to implement.
  1. Benchmark algorithm (ie. defacto algorithm).
_Disadvantages_:
  1. Does not prune branches.
  1. Needs two separate methods (Max and Min).

### Negamax Algorithm ###
[resource](https://chessprogramming.wikispaces.com/Negamax).
[Negamax](http://en.wikipedia.org/wiki/Negamax) is a simplified variant of Minimax.

_Pseudo Code_:
```
public int Negamax(int depth) {
    	if (depth == 0) return evaluate();
    	int max = -infinity;
    	for (all moves)  {
        	score = -Negamax(depth - 1);
        	if(score > max)
            		max = score;
    	}
    	return max;
}
```

_Advantages_:
  1. Easy to implement.
  1. Only one method, instead of two like Minimax.
_Disadvantages_:
  1. Does not prune branches.

### Alpha-Beta Pruning ###
[resource](https://chessprogramming.wikispaces.com/Alpha-Beta).
[Alpha-Beta](http://en.wikipedia.org/wiki/Alpha-beta_pruning) is

### Negascout Algorithm ###
[resource](https://chessprogramming.wikispaces.com/NegaScout).
[Negascout](http://en.wikipedia.org/wiki/Negascout) is

### SSS Star Algorithm ###
[resource](https://chessprogramming.wikispaces.com/SSS*+and+Dual*).
[SSS Star](http://en.wikipedia.org/wiki/SSS*) is

### MTD(f) Algorithm ###
[resource](https://chessprogramming.wikispaces.com/MTD%28f%29).
[MTD(f)](http://en.wikipedia.org/wiki/MTD(f)) is

### Iterative Deepening ###
[resource](https://chessprogramming.wikispaces.com/Iterative+Deepening).
[Iterative Deepening](http://en.wikipedia.org/wiki/Iterative_deepening_depth-first_search) is

### Transposition Table ###
A [transposition table](http://en.wikipedia.org/wiki/Transposition_table) is a table which monitors all possible moves analyzed, and makes sure no double moves are analyzed. It is often done through the use of a [hash table](http://en.wikipedia.org/wiki/Hash_table).

[Zorbrit's hashing](https://chessprogramming.wikispaces.com/Zobrist+Hashing).
[resource](https://chessprogramming.wikispaces.com/Transposition+Table).

## Evaluation Functions ##
A [evaluation function](http://en.wikipedia.org/wiki/Evaluation_function) is a function which estimates how good

### ABLA Evaluation Function ###
Is a weighted linear evaluation function is from the paper

eval(_s_) = w<sub>1</sub>f<sub>1</sub>(_s_) + w<sub>2</sub>f<sub>2</sub>(_s_)

_Where_:
  * f<sub>1</sub>(_s_): The difference between the sum of the Manhattan distances of marbles for each players, for a given board state _s_.
  * w<sub>1</sub>: Is the weight for f<sub>1</sub>(_s_).
  * f<sub>2</sub>(_s_): The difference between the sum of the number of neighboring teammates for marbles for each players, for a given board state _s_.
  * w<sub>2</sub>: Is the weight for f<sub>2</sub>(_s_).

### Lemmens Evaluation Function ###
This evaluation function is from the paper

eval(_s_) = w<sub>1</sub>f<sub>1</sub>(_s_) + w<sub>2</sub>f<sub>2</sub>(_s_) + w<sub>3</sub>f<sub>3</sub>(_s_) + w<sub>4</sub>f<sub>4</sub>(_s_) + w<sub>5</sub>f<sub>5</sub>(_s_) - w<sub>6</sub>f<sub>6</sub>(_s_)

_Where_:
  * f<sub>1</sub>(_s_): The difference between the sum of the Manhattan distances of marbles for each players, for a given board state s.
  * w<sub>1</sub>: Is the weight for f<sub>1</sub>(_s_).
  * f<sub>2</sub>(_s_):The difference between the sum of the number of neighboring teammates for marbles for each players, for a given board state _s_.
  * w<sub>2</sub>: Is the weight for f<sub>2</sub>(_s_).
  * f<sub>3</sub>(_s_):
  * w<sub>3</sub>: Is the weight for f<sub>3</sub>(_s_).
  * f<sub>4</sub>(_s_):
  * w<sub>4</sub>: Is the weight for f<sub>4</sub>(_s_).
  * f<sub>5</sub>(_s_):
  * w<sub>5</sub>: Is the weight for f<sub>5</sub>(_s_).
  * f<sub>6</sub>(_s_):
  * w<sub>6</sub>: Is the weight for f<sub>6</sub>(_s_).

## Machine Learning ##
Machine learning could be applied to the evaluation functions, by for example, optimizing the weights in the evaluation functions.

One possibility is to use a genetic algorithm to learn the optimal weights.

## Monte Carlo Methods ##
Normal Monte Carlo Search seems poor. But might have a look into Monte Carlo Tree Search methods, as they seem to be promising.

# References #
[Abalearn: Efficient Self-Play Learning of the game Abalone](http://dme.uma.pt/people/faculty/pedro.campos/papers/Abalearn.pdf) by Pedro Campos and Thibault Langlois.

[Abalearn: A Risk-Sensitive Approach to Self-play Learning in Abalone](http://books.google.nl/books?id=L4h2A2vF2pUC&pg=PA35&lpg=PA35&dq=%22machine+learning%22%2Babalone&source=bl&ots=3KOwjAK1zv&sig=kTf2yF8uy4t8g8Jx8r09T9jvd_M&hl=nl&ei=sfqxSvOwN8bi-QaZvsXcCQ&sa=X&oi=book_result&ct=result&resnum=5#v=onepage&q=%22machine%20learning%22%2Babalone&f=false) by Pedro Campos and Thibault Langlois.

[A Simple Intelligent Agent for Playing Abalone Game: ABLA](http://cse.yeditepe.edu.tr/~eozcan/research/papers/ABLA_id136final.pdf) by Ender Ozcan and Berk Hulagu.

[Constructing an Abalone Game-Playing Agent](http://www.cs.unimaas.nl/~uiterwyk/Theses/BSc/Lemmens_BSc-paper.pdf) by N.P.P.M. Lemmens.

[Implementing a Computer Player for Abalone using Alpha-Beta and Monte-Carlo Search](http://www.unimaas.nl/games/files/msc/pcreport.pdf) by Pascal Chorus.

[Algorithmic Fun - Abalone](http://www.ist.tugraz.at/staff/aichholzer/research/rp/abalone/tele1-02_aich-abalone.pdf) by Oswin Aichholzer, Franz Aurenhammer, and Tino Werner.

[Abalone – Final Project Report](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.114.6831&rep=rep1&type=pdf) by Benson Lee, Hyun Joo Noh.

[Monte-Carlo Tree Search: A New Framework for Game AI](http://sander.landofsand.com/publications/Monte-Carlo_Tree_Search_-_A_New_Framework_for_Game_AI.pdf) by Guillaume Chaslot, Sander Bakkes, Istvan Szita, and Pieter Spronck.

[Monte-Carlo Tree Search Solver](http://www.ru.is/faculty/yngvi/pdf/WinandsBS08.pdf) by Mark H.M. Winands , Yngvi Bjornsson, and Jahn-Takeshi Saito.

[Heuristic evaluation functions in artificial intelligence search algorithms](http://www.springerlink.com/content/q0v51v7j311um169/) by Richard E. Korf (Need SpringerLink access, can be accessed for free at uni).

[Forum discussion](http://stackoverflow.com/questions/1291377/how-do-i-create-a-good-evaluation-function-for-a-new-board-game) about evaluation functions.

[An Introduction to Game Tree Algorithms](http://www.hamedahmadi.com/gametree/) by Hamed Ahmadi.


[Theory of Computer Games: Fall 2009](http://www.iis.sinica.edu.tw/~tshsu/tcg2009/index.html) by Tsan-sheng Hsu.

link to [ABA-PRO](http://www.ist.tugraz.at/staff/aichholzer/research/rp/abalone/) game.

### Opponent Modeling ###
[Opponent modeling by analyzing play](http://www.cs.kuleuven.be/~dtai/publications/files/38667.ps.gz)

[Opponent Models and Knowledge Symmetry in Game-Tree Search](http://www.fdg.unimaas.nl/educ/donkers/pdf/kag2004.pdf) by Jeroen Donkers.

[Incorporating Opponent Models into Adversary Search](http://www.cs.technion.ac.il/~shaulm/papers/pdf/Carmel-Markovitch-aaai1996.pdf) by David Carmel and Shaul Markovitch.

[USER MODELING IN GAMES](http://fccs.wshe.lodz.pl/fccs2007/artykuly/kosciuk.pdf) by Katarzyna KO´SCIUK.

[Probabilistic opponent-model search](http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6V0C-43B8CXY-1&_user=499911&_rdoc=1&_fmt=&_orig=search&_sort=d&_docanchor=&view=c&_acct=C000024558&_version=1&_urlVersion=0&_userid=499911&md5=c3632130b9789fbfa21c796713116eea) by H. H. L. M. Donkers, J. W. H. M. Uiterwijk and H. J. van den Herik.

[Learning Opponent-Type Probabilities from PrOM Search](http://www.fdg.unimaas.nl/educ/donkers/pubs/..%5Cpdf%5Cbnaic02.pdf) by H. H. L. M. Donkers, J. W. H. M. Uiterwijk and H. J. van den Herik.