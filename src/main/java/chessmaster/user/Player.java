package chessmaster.user;

import chessmaster.exceptions.InvalidMoveException;
import chessmaster.exceptions.NullPieceException;
import chessmaster.exceptions.ParseCoordinateException;
import chessmaster.game.ChessBoard;
import chessmaster.game.Coordinate;
import chessmaster.game.Move;
import chessmaster.parser.Parser;
import chessmaster.pieces.ChessPiece;
import chessmaster.ui.TextUI;

import java.util.ArrayList;

public abstract class Player {

    protected ArrayList<Move> moves;
    protected ArrayList<ChessPiece> pieces;
    protected int colour;

    public Player(int colour) {
        this.moves = new ArrayList<>();
        this.pieces = new ArrayList<>();
        this.colour = colour;
    }

    public void addMove(Move move) {
        this.moves.add(move);
    }

    public void initialisePieces(ChessBoard board) {
        int row, col;
        if (this.colour == ChessPiece.BLACK) {
            row = 6;
        } else {
            row = 0;
        }

        for (int row_temp = row; row < row_temp + 2; row++) {
            for (col = 0; col < ChessBoard.SIZE; col++) {
                try {
                    ChessPiece piece = board.getPieceAtCoor(new Coordinate(col, row));
                    this.pieces.add(piece);
                } catch (NullPieceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printAllPieces() {
        for (ChessPiece p : pieces) {
            System.out.println("Piece: " + p);
            System.out.println("Colour: " + p.getColour());
            System.out.println("Is captured: " + p.getCaptured());
        }
    }

    public int getColour() {
    return this.colour;
    }

    public Move getNextMove(ChessBoard board) {
        // Get user input
        String input = TextUI.getUserInput();
        if (Parser.isUserInputAbort(input)) {
            return null;
        }

        // Parse input into a Move object
        try {
            return Parser.parseMove(input, board);
        } catch (ParseCoordinateException | NullPieceException e) {
            TextUI.printErrorMessage(e);
        }

        return new Move();
    }

    public boolean move(Move move, ChessBoard board) {
        try {
            board.executeMove(move);
            this.addMove(move);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}