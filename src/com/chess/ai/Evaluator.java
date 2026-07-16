package com.chess.ai;

import com.chess.ai.evaluation.*;
import com.chess.game.Game;


public class Evaluator {

    private final MaterialEvaluator materialEvaluator;
    private final MobilityEvaluator mobilityEvaluator;
    private final PawnStructureEvaluator pawnEvaluator;
    private final KingSafetyEvaluator kingSafetyEvaluator;
    private final BishopPairEvaluator bishopEvaluator;
    private final RookFileEvaluator rookEvaluator;
    private final EndgameEvaluator endgameEvaluator;

    public Evaluator(){
        pawnEvaluator = new PawnStructureEvaluator();
        materialEvaluator = new MaterialEvaluator();
        mobilityEvaluator = new MobilityEvaluator();
        kingSafetyEvaluator = new KingSafetyEvaluator();
        bishopEvaluator = new BishopPairEvaluator();
        rookEvaluator = new RookFileEvaluator();
        endgameEvaluator = new EndgameEvaluator();

    }


    public int evaluate(Game game){

        int score = 0;


        // Material + Piece Square Tables
        score += materialEvaluator.evaluate(game);

        // Piece mobility
        score += mobilityEvaluator.evaluate(game);

        //pawn structures: isolated and double pawns
        score += pawnEvaluator.evaluate(game);

        //king safety, pawn shield, open files near king
        score += kingSafetyEvaluator.evaluate(game);

        score += bishopEvaluator.evaluate(game);
        score += rookEvaluator.evaluate(game);
        score += endgameEvaluator.evaluate(game);

        return score;
    }
}