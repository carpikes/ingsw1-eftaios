package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.exception.CLIException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.util.Set;

/** CLI Map Renderer
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class CLIMapRenderer {

    /** Sector pattern */
    private static final String[] mPattern = {
        "  ____  ",
        " /****\\ ",
        "/*yyyy*\\",
        "\\*zzzz*/",
        " \\____/ "
    };

    /** Sector types */
    private static final String[] mTypes = {"????","    ","HTCH","DANG","USED","????","????","????","ALJN","HUMN"};

    /** Private constructor */
    private CLIMapRenderer() {
        /** Not used */
    }
    
    /** Render the map
     * @param map The map
     * @param curPos Current position (to highlight) or null
     * @param enabledPoints Set of enabled sector (or null = all enabled)
     */
    public static void renderMap(GameMap map, Point curPos, Set<Point> enabledPoints) {
        final int py = mPattern.length;
        final int px = mPattern[0].length();
        int upperPoint = -1, lowerPoint = -1;
        int leftMostPoint = -1, rightMostPoint = -1;

        char[][] buffer = new char[py + (py-1) * GameMap.ROWS][];

        for(int i = 0; i < buffer.length;++i) {
            buffer[i] = new char[2 + (px-2) * GameMap.COLUMNS];
            for(int j=0;j<buffer[i].length;j++)
                buffer[i][j] = ' ';
        }

        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for( int j = 0; j < GameMap.COLUMNS; ++j ) {
                Sector sector = map.getSectorAt(j, i);
                if(sector.getId() != SectorBuilder.NOT_VALID) {
                    String str = map.pointToString(j, i);
                    String str2 = mTypes[sector.getId()];
                    int spos = 0, spos2 = 0;

                    /** Highlight: Player is here */
                    boolean highlight = false;

                    /** Disabled: Player can't walk here */
                    boolean disabled = false;

                    if(curPos != null && curPos.x == j && curPos.y == i)
                        highlight = true;

                    if(!highlight && enabledPoints != null && !enabledPoints.contains(new Point(j,i)))
                        disabled = true;

                    for(int y=0;y<py;y++)
                        for(int x=0;x<px;x++) {
                            int bfy = (j%2)*2 + i*(py-1) + y;
                            int bfx = j*(px-2) + x;

                            char t = mPattern[y].charAt(x);

                            /** Replace magic chars */
                            if(t == ' ')
                                continue;
                            if(t == 'y')
                                if(spos < str.length() && !disabled)
                                    t = str.charAt(spos++);
                                else
                                    t = ' ';
                            if(t == 'z')
                                if(spos2 < str2.length())
                                    t = str2.charAt(spos2++);
                                else
                                    t = ' ';
                            if(t == '*')
                                if(!highlight)
                                    t = ' ';

                            if(disabled && (t == '\\' || t == '_' || t == '/') && buffer[bfy][bfx] != t)
                                t = '.';

                            buffer[bfy][bfx] = t;

                            /** Bounds. So we will only draw useful chars  */
                            if(upperPoint == -1 || upperPoint < bfy)
                                upperPoint = bfy;
                            if(lowerPoint == -1 || lowerPoint > bfy)
                                lowerPoint = bfy;
                            if(leftMostPoint == -1 || leftMostPoint > bfx)
                                leftMostPoint = bfx;
                            if(rightMostPoint == -1 || rightMostPoint < bfx)
                                rightMostPoint = bfx;
                        }
                }
            }
        }

        if(lowerPoint < 0 || upperPoint < 0 || upperPoint >= buffer.length || lowerPoint >= upperPoint)
            throw new CLIException("Can't calculate map height. What's happening?");

        if(leftMostPoint < 0 || rightMostPoint < 0 || rightMostPoint >= buffer[0].length || leftMostPoint >= rightMostPoint)
            throw new CLIException("Can't calculate map width. What's happening?");

        for(int i=lowerPoint;i<=upperPoint;i++) {
            for(int j=leftMostPoint;j<=rightMostPoint;j++)
                IO.write(buffer[i][j]);
            IO.write("");
        }
    }

}
