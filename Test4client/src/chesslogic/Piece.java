/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chesslogic;

import java.util.LinkedList;

public class Piece {
    int xp;
    int yp;
    int x;
    int y;
    boolean isWhite;
    LinkedList<Piece> ps;
    String name;
    boolean hasMoved;
    boolean canBeEnPassantCaptured; // New field for en passant


    public Piece(int xp, int yp, boolean isWhite, String n, LinkedList<Piece> ps) {
        this.xp = xp;
        this.yp = yp;
        this.x = xp * 64;
        this.y = yp * 64;
        this.isWhite = isWhite;
        this.ps = ps;
        this.name = n;
        this.hasMoved = false;
        this.canBeEnPassantCaptured = false;
        ps.add(this);
    }

    public LinkedList<int[]> getValidMoves(boolean isSimulating) {
        LinkedList<int[]> moves = new LinkedList<>();
        switch (name.toLowerCase()) {
            case "pawn":
                if (isWhite) {
                    if (getPiece(xp, yp - 1) == null) {
                        moves.add(new int[]{xp, yp - 1}); // Move forward
                    }
                    if (yp == 6 && getPiece(xp, yp - 2) == null && getPiece(xp, yp - 1) == null) {
                        moves.add(new int[]{xp, yp - 2}); // Initial double move
                    }
                    // Capture diagonally
                    if (xp > 0 && getPiece(xp - 1, yp - 1) != null && !getPiece(xp - 1, yp - 1).isWhite) {
                        moves.add(new int[]{xp - 1, yp - 1});
                    }
                    if (xp < 7 && getPiece(xp + 1, yp - 1) != null && !getPiece(xp + 1, yp - 1).isWhite) {
                        moves.add(new int[]{xp + 1, yp - 1});
                    }
                 // En passant
                    if (yp == 3) {
                        Piece leftPiece = getPiece(xp - 1, yp);
                        Piece rightPiece = getPiece(xp + 1, yp);
                        if (leftPiece != null && !leftPiece.isWhite && leftPiece.canBeEnPassantCaptured) {
                            moves.add(new int[]{xp - 1, yp - 1});
                        }
                        if (rightPiece != null && !rightPiece.isWhite && rightPiece.canBeEnPassantCaptured) {
                            moves.add(new int[]{xp + 1, yp - 1});
                        }
                    }
                } else { // Black pawns
                    if (getPiece(xp, yp + 1) == null) {
                        moves.add(new int[]{xp, yp + 1}); // Move forward
                    }
                    if (yp == 1 && getPiece(xp, yp + 2) == null && getPiece(xp, yp + 1) == null) {
                        moves.add(new int[]{xp, yp + 2}); // Initial double move
                    }
                    // Capture diagonally
                    if (xp > 0 && getPiece(xp - 1, yp + 1) != null && getPiece(xp - 1, yp + 1).isWhite) {
                        moves.add(new int[]{xp - 1, yp + 1});
                    }
                    if (xp < 7 && getPiece(xp + 1, yp + 1) != null && getPiece(xp + 1, yp + 1).isWhite) {
                        moves.add(new int[]{xp + 1, yp + 1});
                    }
                    // En passant
                    if (yp == 4) {
                        Piece leftPiece = getPiece(xp - 1, yp);
                        Piece rightPiece = getPiece(xp + 1, yp);
                        if (leftPiece != null && leftPiece.isWhite && leftPiece.canBeEnPassantCaptured) {
                            moves.add(new int[]{xp - 1, yp + 1});
                        }
                        if (rightPiece != null && rightPiece.isWhite && rightPiece.canBeEnPassantCaptured) {
                            moves.add(new int[]{xp + 1, yp + 1});
                        }
                    }
                }
                break;

            case "rook":
                addLinearMoves(moves, 0, 1); // Down
                addLinearMoves(moves, 0, -1); // Up
                addLinearMoves(moves, 1, 0); // Right
                addLinearMoves(moves, -1, 0); // Left
                break;

            case "knight":
                addMove(moves, xp + 1, yp + 2);
                addMove(moves, xp - 1, yp + 2);
                addMove(moves, xp + 1, yp - 2);
                addMove(moves, xp - 1, yp - 2);
                addMove(moves, xp + 2, yp + 1);
                addMove(moves, xp - 2, yp + 1);
                addMove(moves, xp + 2, yp - 1);
                addMove(moves, xp - 2, yp - 1);
                break;

            case "bishop":
                addDiagonalMoves(moves, 1, 1); // Down-right
                addDiagonalMoves(moves, 1, -1); // Up-right
                addDiagonalMoves(moves, -1, 1); // Down-left
                addDiagonalMoves(moves, -1, -1); // Up-left
                break;

            case "queen":
                // Queen combines rook (linear) and bishop (diagonal) moves
                addLinearMoves(moves, 0, 1); // Down
                addLinearMoves(moves, 0, -1); // Up
                addLinearMoves(moves, 1, 0); // Right
                addLinearMoves(moves, -1, 0); // Left
                addDiagonalMoves(moves, 1, 1); // Down-right
                addDiagonalMoves(moves, 1, -1); // Up-right
                addDiagonalMoves(moves, -1, 1); // Down-left
                addDiagonalMoves(moves, -1, -1); // Up-left
                break;

            case "king":
                // King's moves: 1 square in any direction
                addMove(moves, xp, yp - 1);   // Up
                addMove(moves, xp, yp + 1);   // Down
                addMove(moves, xp - 1, yp);   // Left
                addMove(moves, xp + 1, yp);   // Right
                addMove(moves, xp - 1, yp - 1); // Up-left
                addMove(moves, xp + 1, yp - 1); // Up-right
                addMove(moves, xp - 1, yp + 1); // Down-left
                addMove(moves, xp + 1, yp + 1); // Down-right
             // Skip filtering if we are simulating opponent moves (to avoid recursion)
                
                // Add castling moves
                if (!hasMoved && !isSimulating) {
                    addCastlingMoves(moves);
                }
                
                
                
                break;
        }

        // If it's not the king and we are not simulating, filter out moves that could expose the king to check
    if (!isSimulating && !name.equalsIgnoreCase("king")) {
        PinnedCase(moves);
    }

    // If it's the king, filter out moves that put it in check
    if (!isSimulating && name.equalsIgnoreCase("king")) {
        filterOutMovesInCheck(moves);
    }
        
        return moves;
    }

    private void PinnedCase(LinkedList<int[]> moves) {
        // Backup the original position of this piece
        int originalXp = this.xp;
        int originalYp = this.yp;
    
        // Find the king of the same color
        Piece king = null;
        if (this.isWhite) {
            king = ChessGame.wking;
        }else
            king = ChessGame.bking;
    
        if (king == null) return; // No king found, shouldn't happen in a valid game
    
        // Create a list to hold the valid moves that don't expose the king to check
        LinkedList<int[]> validMoves = new LinkedList<>();
    
        // For each move in the moves list, simulate the move
        for (int[] move : moves) {
            int targetXp = move[0];
            int targetYp = move[1];
            Piece tmp=getPiece(targetXp, targetYp);
            // Temporarily move the piece to the target position
            if(tmp!=null)
            {
            	if(tmp.isWhite!=this.isWhite)
            		tmp.kill();
            }
            
            this.xp = targetXp;
            this.yp = targetYp;
    
            // Check if after this move the king is in check
            if (!isKingInCheckAfterMove(king)) {
                validMoves.add(move); // This move is valid, doesn't expose the king
            }
    
            // Restore the original position of the piece
            this.xp = originalXp;
            this.yp = originalYp;
            if(tmp!=null)
            	tmp.revive();
        }
    
        // Replace the original moves list with only valid moves
        moves.clear();
        moves.addAll(validMoves);
    }
    
    // Helper method to check if the king would be in check after a simulated move
    private boolean isKingInCheckAfterMove(Piece king) {
        // Loop through all the enemy pieces and check if any of them can attack the king
        for (Piece p : ps) {
            if (p.isWhite != king.isWhite) {
                LinkedList<int[]> enemyMoves = p.getValidMoves(true); // Get moves for the enemy piece
                for (int[] enemyMove : enemyMoves) {
                    if (enemyMove[0] == king.xp && enemyMove[1] == king.yp) {
                        return true; // The king is under attack after this move
                    }
                }
            }
        }
        return false;
    }
    
    private void addCastlingMoves(LinkedList<int[]> moves) {
        int rookFile;
        int kingFile = 4; // Assuming the king starts at file 4 (e-file)

        // Kingside castling
        rookFile = 7;
        if (canCastle(kingFile, rookFile)) {
            moves.add(new int[]{kingFile + 2, yp});
        }

        // Queenside castling
        rookFile = 0;
        if (canCastle(kingFile, rookFile)) {
            moves.add(new int[]{kingFile - 2, yp});
        }
    }

    private boolean canCastle(int kingFile, int rookFile) {
        Piece rook = getPiece(rookFile, yp);
        if (rook == null || rook.hasMoved || !(rook.name.equalsIgnoreCase("rook"))) {
            return false;
        }

        // Check if the path between king and rook is clear
        int step = (rookFile > kingFile) ? 1 : -1;
        for (int file = kingFile + step; file != rookFile; file += step) {
            if (getPiece(file, yp) != null) {
                return false;
            }
        }

        // Check if the king is not in check and doesn't pass through check
        for (int file = kingFile; file != kingFile + 3 * step; file += step) {
            if (isSquareUnderAttack(file, yp)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSquareUnderAttack(int x, int y) {
        for (Piece p : ps) {
            if (p.isWhite != isWhite) {
                LinkedList<int[]> opponentMoves = p.getValidMoves(true);
                for (int[] move : opponentMoves) {
                    if (move[0] == x && move[1] == y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void filterOutMovesInCheck(LinkedList<int[]> moves) {
        LinkedList<int[]> validMoves = new LinkedList<>();

        for (int[] move : moves) {
            int newX = move[0];
            int newY = move[1];

            // Temporarily move the king to this new position
            int oldXp = this.xp;
            int oldYp = this.yp;

            this.xp = newX;
            this.yp = newY;

            // Check if this move puts the king in check (without recursion)
            if (!ChessGame.isKingInCheck(this)) {
                validMoves.add(new int[]{newX, newY});
            }

            // Restore the king's original position
            this.xp = oldXp;
            this.yp = oldYp;
        }

        // Update the original moves list with only valid moves
        moves.clear();
        moves.addAll(validMoves);
    }
    
    


    private void addLinearMoves(LinkedList<int[]> moves, int dx, int dy) {
        for (int i = 1; i < 8; i++) {
            int newX = xp + i * dx;
            int newY = yp + i * dy;
            if (newX < 0 || newX > 7 || newY < 0 || newY > 7) {
                break; // Out of bounds
            }
            if (getPiece(newX, newY) != null) {
                if (getPiece(newX, newY).isWhite != isWhite) {
                    moves.add(new int[]{newX, newY}); // Can capture
                }
                break; // Stop at the first piece (can't jump over pieces)
            }
            moves.add(new int[]{newX, newY});
        }
    }
    
    private void addDiagonalMoves(LinkedList<int[]> moves, int dx, int dy) {
        for (int i = 1; i < 8; i++) {
            int newX = xp + i * dx;
            int newY = yp + i * dy;
            if (newX < 0 || newX > 7 || newY < 0 || newY > 7) {
                break; // Out of bounds
            }
            Piece targetPiece = getPiece(newX, newY);
            if (targetPiece != null) {
                if (targetPiece.isWhite != isWhite) {
                    moves.add(new int[]{newX, newY}); // Can capture opponent piece
                }
                break; // Stop at the first piece (cannot jump over pieces)
            }
            moves.add(new int[]{newX, newY}); // Empty square
        }
    }


    private void addMove(LinkedList<int[]> moves, int newX, int newY) {
        if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
            if (getPiece(newX, newY) == null || getPiece(newX, newY).isWhite != isWhite) {
                moves.add(new int[]{newX, newY});
            }
        }
    }

    public boolean move(int xp, int yp) {
        //if (ChessGame.isMyTurn != isWhite) return false; // Only move on the correct turn
        LinkedList<int[]> moves = getValidMoves(false);
        for (int[] move : moves) {
            if (move[0] == xp && move[1] == yp) {
                // Reset en passant flag for all pieces of the same color
            	 for (Piece p : ps) {
                     if (p.isWhite == this.isWhite) {
                         p.canBeEnPassantCaptured = false;
                     }
                 }
            	// Handle en passant capture
                 if (name.equalsIgnoreCase("pawn") && xp != this.xp && getPiece(xp, yp) == null) {
                     Piece capturedPawn = getPiece(xp, this.yp);
                     if (capturedPawn != null && capturedPawn.canBeEnPassantCaptured) {
                         capturedPawn.kill();
                     }
                 }

                 // Set en passant flag for double pawn move
                 if (name.equalsIgnoreCase("pawn") && Math.abs(yp - this.yp) == 2) {
                     this.canBeEnPassantCaptured = true;
                 }
                // Handle castling
                if (name.equalsIgnoreCase("king") && Math.abs(xp - this.xp) == 2) {
                    performCastling(xp);
                } else {
                    Piece targetPiece = ChessGame.getPiece(xp * 64, yp * 64);
                    if (targetPiece != null) {
                        if (targetPiece.name.equalsIgnoreCase("king")) {
                            // Don't allow capturing the king
                            this.x = this.xp * 64;
                            this.y = this.yp * 64;
                            return false;
                        }
                        targetPiece.kill();
                    }
                }
                this.xp = xp;
                this.yp = yp;
                this.x = xp * 64;
                this.y = yp * 64;
                this.hasMoved = true;
                return true;
            }
        }

        // If no valid move, reset piece position
        this.x = this.xp * 64;
        this.y = this.yp * 64;
        return false;
    }

    private void performCastling(int newKingX) {
        int rookOldX = (newKingX > this.xp) ? 7 : 0;
        int rookNewX = (newKingX > this.xp) ? newKingX - 1 : newKingX + 1;
        Piece rook = getPiece(rookOldX, this.yp);
        if (rook != null) {
            rook.xp = rookNewX;
            rook.x = rookNewX * 64;
            rook.hasMoved = true;
        }
    }

    public void kill() {
        ps.remove(this);
    }
    public void revive() {
    	ps.add(this);
    }

    public static Piece getPiece(int x, int y) {
        for (Piece p : ChessGame.ps) {
            if (p.xp == x && p.yp == y) {
                return p;
            }
        }
        return null;
    }
}